package com.heuristix.guns;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import com.heuristix.EntityProjectile;
import com.heuristix.ItemGun;
import com.heuristix.ItemGunBase;
import com.heuristix.ItemProjectile;
import com.heuristix.ItemProjectileBase;
import com.heuristix.TheGunMod;
import com.heuristix.guns.asm.Opcodes;
import com.heuristix.guns.helper.IOHelper;
import com.heuristix.guns.util.ClassDescriptor;
import com.heuristix.guns.util.ExtensibleClassVisitor;
import com.heuristix.guns.util.InvokeMethod;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.Method;
import com.heuristix.guns.util.Pair;

import cpw.mods.fml.common.registry.EntityRegistry;

public class GunManager {

	private final TheGunMod tgm;
	
	private GunBridge gunBridge;
	private int projectileTrackerId;
	
	private final Map<String, Class<?>> nameToEntityProjectileClassMap;
    private final Map<Integer, Class<? extends EntityProjectile>> trackerIdToEntityProjectileClassMap;
    private final Map<String, ItemProjectile> nameToItemProjectileMap;
    private final Map<String, Object> updatedGunMap;
    
	public GunManager(TheGunMod tgm) {
        this.nameToEntityProjectileClassMap = new HashMap<String, Class<?>>();
        this.nameToItemProjectileMap = new HashMap<String, ItemProjectile>();
        this.trackerIdToEntityProjectileClassMap = new HashMap<Integer, Class<? extends EntityProjectile>>();
        this.updatedGunMap = new HashMap<String, Object>();
        this.tgm = tgm;
	}

	public int initGuns(File directory) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		if (directory == null) {
			throw new IllegalArgumentException("Invalid gun directory");
		}
		int registered = 0;
		if (!directory.exists()) {
			directory.mkdirs();
		} else {
			for (File f : directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".gun2")
							|| name.toLowerCase().endsWith(".zip");
				}
			})) {
				if (f.getName().toLowerCase().endsWith(".gun2")) {
					if (loadGun(f.getName(), new FileInputStream(f))) {
						registered++;
					}
				} else {
					ZipFile zipFile = new ZipFile(f);
					for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();) {
						ZipEntry entry = e.nextElement();
						if (!entry.isDirectory()
								&& entry.getName().toLowerCase()
										.endsWith(".gun2")) {
							if (loadGun(zipFile.getName() + File.separator + entry.getName(), zipFile.getInputStream(entry))) {
								registered++;
							}
						}
					}
					zipFile.close();
				}
			}
			updateGuns();
		}
        return registered;
	}
	
	@SuppressWarnings("unchecked")
	private void updateGuns() throws IOException {
		for (Map.Entry<String, Object> entry : updatedGunMap.entrySet()) {
			if (entry.getKey().endsWith(".zip")) {
				ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(entry.getKey()));
				try {
					for(Map.Entry<String, Pair<String, byte[]>> zipEntry : ((Map<String, Pair<String, byte[]>>) entry.getValue()).entrySet()) {
						zout.putNextEntry(new ZipEntry(zipEntry.getKey()));
						zout.write(zipEntry.getValue().getSecond());
						zout.flush();
						Log.fine(String.format("Updated gun file %s in archive %s from version %s to %s", zipEntry.getKey(),
								entry.getKey().substring(entry.getKey().lastIndexOf(File.separatorChar)),
										zipEntry.getValue().getFirst() , AbstractGunBridge.VERSION), tgm.getClass());
					}
				} catch (IOException e) {
					Log.throwing(getClass(), "updateGuns()", e, tgm.getClass());
				} finally {
					zout.close();
				}
			} else {
				FileOutputStream fout = new FileOutputStream(entry.getKey());
				try {
					fout.write(((Pair<String, byte[]>) entry.getValue()).getSecond());
					fout.flush();
				} catch (IOException e) {
					Log.throwing(getClass(), "updateGuns()", e, tgm.getClass());
				} finally {
					fout.close();
				}
				Log.fine(String.format("Updated gun file %s from version %s to %s", entry.getKey().substring(entry.getKey().lastIndexOf(File.separator)),
						 ((Pair<String, byte[]>) entry.getValue()).getFirst(), AbstractGunBridge.VERSION), tgm.getClass());
			}
		}
		
	}

	private boolean loadGun(String path, InputStream is) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
    	if (gunBridge == null) {
    		gunBridge = new DefaultGunBridge();
    	}
    	Gun gun = null;
        try {
            gun = new Gun(IOHelper.read(is));
        } catch (Exception e) {
            Log.throwing(getClass(), "initItems()", e, tgm.getClass());
        }
        if (gun != null) {
            int[] versionCreated = gun.getProperties().get("versionCreated");
            String version = "";
            if (versionCreated == null || !(version = Util.getStringFromBytes(versionCreated)).equals(AbstractGunBridge.VERSION)) {
                gunBridge.read(gun, TheGunMod.DEBUG);
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                gunBridge.write(byteOut);
                gun = new Gun(byteOut.toByteArray());
                if (path.contains(".zip")) {
                	Map<String, Pair<String, byte[]>> zipGuns = (Map<String, Pair<String, byte[]>>) updatedGunMap.get(path.substring(0, path.lastIndexOf(File.separator)));
                	if (zipGuns == null) {
                		zipGuns = new HashMap<String, Pair<String, byte[]>>();
                	}
                	zipGuns.put(path.substring(path.lastIndexOf(File.separator) + 1), new Pair<String, byte[]>(version, byteOut.toByteArray()));
                	updatedGunMap.put(path.substring(0, path.lastIndexOf(File.separator)), zipGuns);
                } else {
                	updatedGunMap.put(path, new Pair<String, byte[]>(version, byteOut.toByteArray()));
                }
            }
            registerGun(gun, TheGunMod.DEBUG);
            return true;
        } else {
            Log.fine(String.format("Could not load gun %s", path.lastIndexOf(File.separator)), tgm.getClass());
            return false;
        }
    }

    private void registerGun(Gun gun, boolean deobfuscate) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Pair<String, byte[]>> gunClasses = gun.getClasses();
        Map<String, byte[]> resources = gun.getResources();

        Class<? extends EntityProjectile> entityProjectileClass = (Class<? extends EntityProjectile>) nameToEntityProjectileClassMap.get(gunClasses.get(0).getFirst());
        if (entityProjectileClass == null) {
            String projectileType = ClassDescriptor.getClassDescription(Opcodes.ASM4, gunClasses.get(0).getSecond()).getSuperName();
            HashMap<String, Method> methods = new HashMap<String, Method>();
            String worldClass = (deobfuscate) ? "net/minecraft/world/World" : AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(0).getFirst(), entityLivingClass = (deobfuscate) ? "net/minecraft/entity/EntityLiving" : AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(0).getSecond();
            for (int i = 0; i < AbstractGunBridge.OBFUSCATED_CLASS_NAMES.size(); i++) {
                Pair<String, String> obfuscatedNames = AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(i);
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";L" + obfuscatedNames.getSecond() + ";)V",
                        new Method("(L" + worldClass + ";L" + entityLivingClass + ";)V",
                                new InvokeMethod(AbstractGunBridge.SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";L" + entityLivingClass + ";)V", false, true, false)));
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";)V",
                        new Method("(L" + worldClass + ";)V",
                                new InvokeMethod(AbstractGunBridge.SUPER_WORLD, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";)V", false, true, false)));
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";DDD)V",
                        new Method("(L" + worldClass + ";DDD)V",
                                new InvokeMethod(AbstractGunBridge.SUPER_WORLD_COORDS, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";DDD)V", false, true, false)));
            }
            byte[] entityProjectileClassBytes = ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, gunClasses.get(0).getSecond(), gunClasses.get(0).getFirst(), (Map<String, Method>) methods.clone(), false);
            entityProjectileClass = (Class<? extends EntityProjectile>) Util.defineClass(entityProjectileClassBytes, null/*gunClasses.get(0).getFirst()*/, EntityProjectile.class.getClassLoader());
            nameToEntityProjectileClassMap.put(entityProjectileClass.getName(), entityProjectileClass);
            trackerIdToEntityProjectileClassMap.put(projectileTrackerId, entityProjectileClass);
            EntityRegistry.registerModEntity(entityProjectileClass, entityProjectileClass.getName(), projectileTrackerId++, tgm, 100, 1, true);
        }
        ItemProjectile itemProjectile = nameToItemProjectileMap.get(gunClasses.get(1).getFirst());
        if (itemProjectile == null) {
            Class<?> itemProjectileClass = nameToEntityProjectileClassMap.get(gunClasses.get(1).getFirst());
            if (itemProjectileClass == null) {
                itemProjectileClass = Util.defineClass(gunClasses.get(1).getSecond(), null/*gunClasses.get(1).getFirst()*/, ItemProjectileBase.class.getClassLoader());
                nameToEntityProjectileClassMap.put(itemProjectileClass.getName(), itemProjectileClass);
            }
            Constructor<?> itemProjectileConstructor = itemProjectileClass.getConstructors()[0];
            itemProjectileConstructor.setAccessible(true);
            itemProjectile = (ItemProjectile) itemProjectileConstructor.newInstance(gun.getItemProjectileId());
            nameToItemProjectileMap.put(itemProjectileClass.getName(), itemProjectile);
        }
        List<BufferedImage> projectileTextures = new LinkedList<BufferedImage>();
        List<BufferedImage> gunTextures = new LinkedList<BufferedImage>();
        for (Map.Entry<String, byte[]> entry : resources.entrySet()) {
            if (entry.getKey().contains("gun") || entry.getKey().contains("projectile")) {
                List<BufferedImage> textures = (entry.getKey().contains("gun")) ? gunTextures : projectileTextures;
                textures.add(ImageIO.read(new ByteArrayInputStream(entry.getValue())));
            }
        }
        if (itemProjectile != null) {
            itemProjectile.setIconIndex(tgm.textureManager.registerTexture(projectileTextures.toArray(new BufferedImage[projectileTextures.size()])));
            tgm.registerItem(itemProjectile);
        }

        Class<? > itemGunClass = nameToEntityProjectileClassMap.get(gunClasses.get(2).getFirst());
        ItemGun itemGun = null;
        if (itemGunClass == null) {
            itemGunClass = Util.defineClass(gunClasses.get(2).getSecond(), null/*gunClasses.get(2).getFirst()*/, ItemGunBase.class.getClassLoader());
            nameToEntityProjectileClassMap.put(itemGunClass.getName(), itemGunClass);
            Constructor<?> itemGunConstructor = itemGunClass.getDeclaredConstructor(int.class, ItemProjectile.class);
            itemGunConstructor.setAccessible(true);
            itemGun = (ItemGun) itemGunConstructor.newInstance(gun.getItemGunId(), itemProjectile);
        }
        if (itemGun != null) {
            itemGun.setIconIndex(tgm.textureManager.registerTexture(gunTextures.toArray(new BufferedImage[projectileTextures.size()])));
            tgm.registerItem(itemGun);
            TheGunMod.proxy.registerItemRenderer(itemGun);
            tgm.soundManager.registerSound(itemGun.getShootSound().replaceFirst("\\.", "/") + ".ogg", resources.get("shootSound"));
            if (resources.get("reloadSound") != null) {
            	tgm.soundManager.registerStreaming(itemGun.getReloadSound().replaceFirst("\\.", "/") + ".ogg", resources.get("reloadSound"));
            }
            if (itemGun.getScope() == Scope.CUSTOM.ordinal()) {
                itemGun.setCustomScope(ImageIO.read(new ByteArrayInputStream(resources.get("customScope"))));
            }
        }
        itemProjectile.putProjectileClass(itemGun, entityProjectileClass);
        nameToItemProjectileMap.put(gunClasses.get(1).getFirst(), itemProjectile);
        Log.fine("Gun loaded " + itemGun.getName(), tgm.getClass());
    }
}
