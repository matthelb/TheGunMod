// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.logging.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.minecraft.server.MinecraftServer;

// Referenced classes of package net.minecraft.src:
//            Achievement, StatBase, StatCollector, BaseMod, 
//            MLProp, CraftingManager, FurnaceRecipes, BiomeGenBase, 
//            SpawnListEntry, EntityLiving, EntityList, BiomeGenHell, 
//            TileEntity, Block, StatList, StatCrafting, 
//            Item, IRecipe, ItemStack, WorldServer, 
//            ChunkProviderGenerate, World, ChunkProviderHell, ItemBlock, 
//            EntityPlayerMP, Packet100OpenWindow, IInventory, NetServerHandler, 
//            Container, EntityPlayer, EnumCreatureType, IChunkProvider

public final class ModLoader
{

    private static File cfgdir;
    private static File cfgfile;
    public static Level cfgLoggingLevel;
    private static long clock = 0L;
    public static final boolean DEBUG = false;
    private static Field field_modifiers = null;
    private static Map classMap = null;
    private static boolean hasInit = false;
    private static int highestEntityId = 3000;
    private static final Map inGameHooks = new HashMap();
    private static MinecraftServer instance = null;
    private static int itemSpriteIndex = 0;
    private static int itemSpritesLeft = 0;
    private static File logfile;
    private static File modDir;
    private static final Logger logger = Logger.getLogger("ModLoader");
    private static FileHandler logHandler = null;
    private static Method method_RegisterEntityID = null;
    private static Method method_RegisterTileEntity = null;
    private static final LinkedList modList = new LinkedList();
    private static int nextBlockModelID = 1000;
    private static final Map overrides = new HashMap();
    public static final Properties props = new Properties();
    private static BiomeGenBase standardBiomes[];
    private static int terrainSpriteIndex = 0;
    private static int terrainSpritesLeft = 0;
    private static final boolean usedItemSprites[] = new boolean[256];
    private static final boolean usedTerrainSprites[] = new boolean[256];
    public static final String VERSION = "ModLoader Server 1.0.0";
    private static Method method_getNextWindowId;
    private static Field field_currentWindowId;

    public static void AddAchievementDesc(Achievement achievement, String s, String s1)
    {
        try
        {
            if(achievement.statName.contains("."))
            {
                String s2 = achievement.statName.split("\\.")[1];
                setPrivateValue(net.minecraft.src.StatBase.class, achievement, 1, StatCollector.translateToLocal((new StringBuilder("achievement.")).append(s2).toString()));
                setPrivateValue(net.minecraft.src.Achievement.class, achievement, 3, StatCollector.translateToLocal((new StringBuilder("achievement.")).append(s2).append(".desc").toString()));
            } else
            {
                setPrivateValue(net.minecraft.src.StatBase.class, achievement, 1, s);
                setPrivateValue(net.minecraft.src.Achievement.class, achievement, 3, s1);
            }
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            logger.throwing("ModLoader", "AddAchievementDesc", illegalargumentexception);
            ThrowException(illegalargumentexception);
        }
        catch(SecurityException securityexception)
        {
            logger.throwing("ModLoader", "AddAchievementDesc", securityexception);
            ThrowException(securityexception);
        }
        catch(NoSuchFieldException nosuchfieldexception)
        {
            logger.throwing("ModLoader", "AddAchievementDesc", nosuchfieldexception);
            ThrowException(nosuchfieldexception);
        }
    }

    public static int AddAllFuel(int i)
    {
        logger.finest((new StringBuilder("Finding fuel for ")).append(i).toString());
        int j = 0;
        for(Iterator iterator = modList.iterator(); iterator.hasNext() && j == 0; j = ((BaseMod)iterator.next()).AddFuel(i)) { }
        if(j != 0)
        {
            logger.finest((new StringBuilder("Returned ")).append(j).toString());
        }
        return j;
    }

    public static int AddArmor(String s)
    {
        return -1;
    }

    private static void addMod(ClassLoader classloader, String s)
    {
        try
        {
            String s1 = s.split("\\.")[0];
            if(s1.contains("$"))
            {
                return;
            }
            if(props.containsKey(s1) && (props.getProperty(s1).equalsIgnoreCase("no") || props.getProperty(s1).equalsIgnoreCase("off")))
            {
                return;
            }
            Package package1 = (net.minecraft.src.ModLoader.class).getPackage();
            if(package1 != null)
            {
                s1 = (new StringBuilder(String.valueOf(package1.getName()))).append(".").append(s1).toString();
            }
            Class class1 = classloader.loadClass(s1);
            if(!(net.minecraft.src.BaseMod.class).isAssignableFrom(class1))
            {
                return;
            }
            setupProperties(class1);
            BaseMod basemod = (BaseMod)class1.newInstance();
            if(basemod != null)
            {
                modList.add(basemod);
                logger.fine((new StringBuilder("Mod Loaded: \"")).append(basemod.toString()).append("\" from ").append(s).toString());
                System.out.println((new StringBuilder("Mod Loaded: ")).append(basemod.toString()).toString());
                MinecraftServer.logger.info((new StringBuilder("Mod Loaded: ")).append(basemod.toString()).toString());
            }
        }
        catch(Throwable throwable)
        {
            logger.fine((new StringBuilder("Failed to load mod from \"")).append(s).append("\"").toString());
            System.out.println((new StringBuilder("Failed to load mod from \"")).append(s).append("\"").toString());
            logger.throwing("ModLoader", "addMod", throwable);
            ThrowException(throwable);
        }
    }

    private static void setupProperties(Class class1)
        throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException, NoSuchFieldException
    {
        Properties properties = new Properties();
        File file = new File(cfgdir, (new StringBuilder(String.valueOf(class1.getName()))).append(".cfg").toString());
        if(file.exists() && file.canRead())
        {
            properties.load(new FileInputStream(file));
        }
        StringBuilder stringbuilder = new StringBuilder();
        Field afield[];
        int i = (afield = class1.getFields()).length;
        for(int j = 0; j < i; j++)
        {
            Field field = afield[j];
            if((field.getModifiers() & 8) == 0 || !field.isAnnotationPresent(net.minecraft.src.MLProp.class))
            {
                continue;
            }
            Class class2 = field.getType();
            MLProp mlprop = (MLProp)field.getAnnotation(net.minecraft.src.MLProp.class);
            String s = mlprop.name().length() == 0 ? field.getName() : mlprop.name();
            Object obj = field.get(null);
            StringBuilder stringbuilder1 = new StringBuilder();
            if(mlprop.min() != (-1.0D / 0.0D))
            {
                stringbuilder1.append(String.format(",>=%.1f", new Object[] {
                    Double.valueOf(mlprop.min())
                }));
            }
            if(mlprop.max() != (1.0D / 0.0D))
            {
                stringbuilder1.append(String.format(",<=%.1f", new Object[] {
                    Double.valueOf(mlprop.max())
                }));
            }
            StringBuilder stringbuilder2 = new StringBuilder();
            if(mlprop.info().length() > 0)
            {
                stringbuilder2.append(" -- ");
                stringbuilder2.append(mlprop.info());
            }
            stringbuilder.append(String.format("%s (%s:%s%s)%s\n", new Object[] {
                s, class2.getName(), obj, stringbuilder1, stringbuilder2
            }));
            if(properties.containsKey(s))
            {
                String s1 = properties.getProperty(s);
                Object obj1 = null;
                if(class2.isAssignableFrom(java.lang.String.class))
                {
                    obj1 = s1;
                } else
                if(class2.isAssignableFrom(Integer.TYPE))
                {
                    obj1 = Integer.valueOf(Integer.parseInt(s1));
                } else
                if(class2.isAssignableFrom(Short.TYPE))
                {
                    obj1 = Short.valueOf(Short.parseShort(s1));
                } else
                if(class2.isAssignableFrom(Byte.TYPE))
                {
                    obj1 = Byte.valueOf(Byte.parseByte(s1));
                } else
                if(class2.isAssignableFrom(Boolean.TYPE))
                {
                    obj1 = Boolean.valueOf(Boolean.parseBoolean(s1));
                } else
                if(class2.isAssignableFrom(Float.TYPE))
                {
                    obj1 = Float.valueOf(Float.parseFloat(s1));
                } else
                if(class2.isAssignableFrom(Double.TYPE))
                {
                    obj1 = Double.valueOf(Double.parseDouble(s1));
                }
                if(obj1 == null)
                {
                    continue;
                }
                if(obj1 instanceof Number)
                {
                    double d = ((Number)obj1).doubleValue();
                    if(mlprop.min() != (-1.0D / 0.0D) && d < mlprop.min() || mlprop.max() != (1.0D / 0.0D) && d > mlprop.max())
                    {
                        continue;
                    }
                }
                logger.finer((new StringBuilder(String.valueOf(s))).append(" set to ").append(obj1).toString());
                if(!obj1.equals(obj))
                {
                    field.set(null, obj1);
                }
            } else
            {
                logger.finer((new StringBuilder(String.valueOf(s))).append(" not in config, using default: ").append(obj).toString());
                properties.setProperty(s, obj.toString());
            }
        }

        if(!properties.isEmpty() && (file.exists() || file.createNewFile()) && file.canWrite())
        {
            properties.store(new FileOutputStream(file), stringbuilder.toString());
        }
    }

    public static int addOverride(String s, String s1)
    {
        return 0;
    }

    public static void addOverride(String s, String s1, int i)
    {
        int j = -1;
        int k = 0;
        if(s.equals("/terrain.png"))
        {
            j = 0;
            k = terrainSpritesLeft;
        } else
        if(s.equals("/gui/items.png"))
        {
            j = 1;
            k = itemSpritesLeft;
        } else
        {
            return;
        }
        System.out.println((new StringBuilder("Overriding ")).append(s).append(" with ").append(s1).append(" @ ").append(i).append(". ").append(k).append(" left.").toString());
        logger.finer((new StringBuilder("addOverride(")).append(s).append(",").append(s1).append(",").append(i).append("). ").append(k).append(" left.").toString());
        Object obj = (Map)overrides.get(Integer.valueOf(j));
        if(obj == null)
        {
            obj = new HashMap();
            overrides.put(Integer.valueOf(j), obj);
        }
        ((Map)obj).put(s1, Integer.valueOf(i));
    }

    public static void AddRecipe(ItemStack itemstack, Object aobj[])
    {
        CraftingManager.getInstance().addRecipe(itemstack, aobj);
    }

    public static void AddShapelessRecipe(ItemStack itemstack, Object aobj[])
    {
        CraftingManager.getInstance().addShapelessRecipe(itemstack, aobj);
    }

    public static void AddSmelting(int i, ItemStack itemstack)
    {
        FurnaceRecipes.smelting().addSmelting(i, itemstack);
    }

    public static void AddSpawn(Class class1, int i, int j, int k, EnumCreatureType enumcreaturetype)
    {
        AddSpawn(class1, i, j, k, enumcreaturetype, null);
    }

    public static void AddSpawn(Class class1, int i, int j, int k, EnumCreatureType enumcreaturetype, BiomeGenBase abiomegenbase[])
    {
        if(class1 == null)
        {
            throw new IllegalArgumentException("entityClass cannot be null");
        }
        if(enumcreaturetype == null)
        {
            throw new IllegalArgumentException("spawnList cannot be null");
        }
        if(abiomegenbase == null)
        {
            abiomegenbase = standardBiomes;
        }
        for(int l = 0; l < abiomegenbase.length; l++)
        {
            List list = abiomegenbase[l].getSpawnableList(enumcreaturetype);
            if(list == null)
            {
                continue;
            }
            boolean flag = false;
            Iterator iterator = list.iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break;
                }
                SpawnListEntry spawnlistentry = (SpawnListEntry)iterator.next();
                if(spawnlistentry.entityClass != class1)
                {
                    continue;
                }
                spawnlistentry.itemWeight = i;
                spawnlistentry.field_35484_b = j;
                spawnlistentry.field_35485_c = k;
                flag = true;
                break;
            } while(true);
            if(!flag)
            {
                list.add(new SpawnListEntry(class1, i, j, k));
            }
        }

    }

    public static void AddSpawn(String s, int i, int j, int k, EnumCreatureType enumcreaturetype)
    {
        AddSpawn(s, i, j, k, enumcreaturetype, null);
    }

    public static void AddSpawn(String s, int i, int j, int k, EnumCreatureType enumcreaturetype, BiomeGenBase abiomegenbase[])
    {
        Class class1 = (Class)classMap.get(s);
        if(class1 != null && (net.minecraft.src.EntityLiving.class).isAssignableFrom(class1))
        {
            AddSpawn(class1, i, j, k, enumcreaturetype, abiomegenbase);
        }
    }

    public static boolean DispenseEntity(World world, double d, double d1, double d2, int i, 
            int j, ItemStack itemstack)
    {
        boolean flag = false;
        for(Iterator iterator = modList.iterator(); iterator.hasNext() && !flag; flag = ((BaseMod)iterator.next()).DispenseEntity(world, d, d1, d2, i, j, itemstack)) { }
        return flag;
    }

    public static List getLoadedMods()
    {
        return Collections.unmodifiableList(modList);
    }

    public static Logger getLogger()
    {
        return logger;
    }

    public static MinecraftServer getMinecraftServerInstance()
    {
        return instance;
    }

    public static Object getPrivateValue(Class class1, Object obj, int i)
        throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field field = class1.getDeclaredFields()[i];
            field.setAccessible(true);
            return field.get(obj);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            logger.throwing("ModLoader", "getPrivateValue", illegalaccessexception);
            ThrowException("An impossible error has occured!", illegalaccessexception);
            return null;
        }
    }

    public static Object getPrivateValue(Class class1, Object obj, String s)
        throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field field = class1.getDeclaredField(s);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            logger.throwing("ModLoader", "getPrivateValue", illegalaccessexception);
            ThrowException("An impossible error has occured!", illegalaccessexception);
            return null;
        }
    }

    public static int getUniqueBlockModelID(BaseMod basemod, boolean flag)
    {
        return nextBlockModelID++;
    }

    public static int getUniqueEntityId()
    {
        return highestEntityId++;
    }

    private static int getUniqueItemSpriteIndex()
    {
        for(; itemSpriteIndex < usedItemSprites.length; itemSpriteIndex++)
        {
            if(!usedItemSprites[itemSpriteIndex])
            {
                usedItemSprites[itemSpriteIndex] = true;
                itemSpritesLeft--;
                return itemSpriteIndex++;
            }
        }

        Exception exception = new Exception("No more empty item sprite indices left!");
        logger.throwing("ModLoader", "getUniqueItemSpriteIndex", exception);
        ThrowException(exception);
        return 0;
    }

    public static int getUniqueSpriteIndex(String s)
    {
        if(s.equals("/gui/items.png"))
        {
            return getUniqueItemSpriteIndex();
        }
        if(s.equals("/terrain.png"))
        {
            return getUniqueTerrainSpriteIndex();
        } else
        {
            Exception exception = new Exception((new StringBuilder("No registry for this texture: ")).append(s).toString());
            logger.throwing("ModLoader", "getUniqueItemSpriteIndex", exception);
            ThrowException(exception);
            return 0;
        }
    }

    private static int getUniqueTerrainSpriteIndex()
    {
        for(; terrainSpriteIndex < usedTerrainSprites.length; terrainSpriteIndex++)
        {
            if(!usedTerrainSprites[terrainSpriteIndex])
            {
                usedTerrainSprites[terrainSpriteIndex] = true;
                terrainSpritesLeft--;
                return terrainSpriteIndex++;
            }
        }

        Exception exception = new Exception("No more empty terrain sprite indices left!");
        logger.throwing("ModLoader", "getUniqueItemSpriteIndex", exception);
        ThrowException(exception);
        return 0;
    }

    private static void init()
    {
        hasInit = true;
        String s = "1111111111111111111111111111111111111101111111011111111111111001111111111111111111111011111010111111100110000011111110000000001111111001100000110000000100000011000000010000001100000000000000110000000000000000000000000000000000000000000000001100000000000000";
        String s1 = "1111111111111111111111111111110111111111111111111111110111111111111111111111000111111011111111111111001111000000111111111111100011111111000010001111011110000000111111000000000011111100000000001111000000000111111000000000001101000000000001111111111111000011";
        for(int i = 0; i < 256; i++)
        {
            usedItemSprites[i] = s.charAt(i) == '1';
            if(!usedItemSprites[i])
            {
                itemSpritesLeft++;
            }
            usedTerrainSprites[i] = s1.charAt(i) == '1';
            if(!usedTerrainSprites[i])
            {
                terrainSpritesLeft++;
            }
        }

        try
        {
            classMap = (Map)getPrivateValue(net.minecraft.src.EntityList.class, null, 0);
            field_modifiers = (java.lang.reflect.Field.class).getDeclaredField("modifiers");
            field_modifiers.setAccessible(true);
            Field afield[] = (net.minecraft.src.BiomeGenBase.class).getDeclaredFields();
            LinkedList linkedlist = new LinkedList();
            for(int j = 0; j < afield.length; j++)
            {
                Class class1 = afield[j].getType();
                if((afield[j].getModifiers() & 8) == 0 || !class1.isAssignableFrom(net.minecraft.src.BiomeGenBase.class))
                {
                    continue;
                }
                BiomeGenBase biomegenbase = (BiomeGenBase)afield[j].get(null);
                if(!(biomegenbase instanceof BiomeGenHell))
                {
                    linkedlist.add(biomegenbase);
                }
            }

            standardBiomes = (BiomeGenBase[])linkedlist.toArray(new BiomeGenBase[0]);
            try
            {
                method_RegisterTileEntity = (net.minecraft.src.TileEntity.class).getDeclaredMethod("a", new Class[] {
                    java.lang.Class.class, java.lang.String.class
                });
            }
            catch(NoSuchMethodException nosuchmethodexception1)
            {
                method_RegisterTileEntity = (net.minecraft.src.TileEntity.class).getDeclaredMethod("addMapping", new Class[] {
                    java.lang.Class.class, java.lang.String.class
                });
            }
            method_RegisterTileEntity.setAccessible(true);
            try
            {
                method_RegisterEntityID = (net.minecraft.src.EntityList.class).getDeclaredMethod("a", new Class[] {
                    java.lang.Class.class, java.lang.String.class, Integer.TYPE
                });
            }
            catch(NoSuchMethodException nosuchmethodexception2)
            {
                method_RegisterEntityID = (net.minecraft.src.EntityList.class).getDeclaredMethod("addMapping", new Class[] {
                    java.lang.Class.class, java.lang.String.class, Integer.TYPE
                });
            }
            method_RegisterEntityID.setAccessible(true);
        }
        catch(SecurityException securityexception)
        {
            logger.throwing("ModLoader", "init", securityexception);
            ThrowException(securityexception);
            throw new RuntimeException(securityexception);
        }
        catch(NoSuchFieldException nosuchfieldexception)
        {
            logger.throwing("ModLoader", "init", nosuchfieldexception);
            ThrowException(nosuchfieldexception);
            throw new RuntimeException(nosuchfieldexception);
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            logger.throwing("ModLoader", "init", nosuchmethodexception);
            ThrowException(nosuchmethodexception);
            throw new RuntimeException(nosuchmethodexception);
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            logger.throwing("ModLoader", "init", illegalargumentexception);
            ThrowException(illegalargumentexception);
            throw new RuntimeException(illegalargumentexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            logger.throwing("ModLoader", "init", illegalaccessexception);
            ThrowException(illegalaccessexception);
            throw new RuntimeException(illegalaccessexception);
        }
        try
        {
            try
            {
                loadConfig();
            }
            catch(IOException ioexception)
            {
                if(ioexception.getMessage().contains("No such file or directory"))
                {
                    String s2 = "Error loading ModLoader config. Check the common problems section in the ModLoaderMP thread.";
                    ThrowException(new RuntimeException(s2, ioexception));
                } else
                {
                    throw ioexception;
                }
            }
            if(props.containsKey("loggingLevel"))
            {
                cfgLoggingLevel = Level.parse(props.getProperty("loggingLevel"));
            }
            logger.setLevel(cfgLoggingLevel);
            if((logfile.exists() || logfile.createNewFile()) && logfile.canWrite() && logHandler == null)
            {
                logHandler = new FileHandler(logfile.getPath());
                logHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(logHandler);
            }
            logger.fine("ModLoader Server 1.0.0 Initializing...");
            System.out.println("ModLoader Server 1.0.0 Initializing...");
            MinecraftServer.logger.info("ModLoader Server 1.0.0 Initializing...");
            File file = new File((net.minecraft.src.ModLoader.class).getProtectionDomain().getCodeSource().getLocation().toURI());
            modDir.mkdirs();
            readFromModFolder(modDir);
            readFromClassPath(file);
            System.out.println("Done.");
            props.setProperty("loggingLevel", cfgLoggingLevel.getName());
            Iterator iterator = modList.iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break;
                }
                BaseMod basemod = (BaseMod)iterator.next();
                basemod.ModsLoaded();
                if(!props.containsKey(basemod.getClass().getName()))
                {
                    props.setProperty(basemod.getClass().getName(), "on");
                }
            } while(true);
            initStats();
            saveConfig();
        }
        catch(Throwable throwable)
        {
            logger.throwing("ModLoader", "init", throwable);
            ThrowException("ModLoader has failed to initialize.", throwable);
            if(logHandler != null)
            {
                logHandler.close();
            }
            throw new RuntimeException(throwable);
        }
    }

    private static void initStats()
    {
        for(int i = 0; i < Block.blocksList.length; i++)
        {
            if(!StatList.oneShotStats.containsKey(Integer.valueOf(0x1000000 + i)) && Block.blocksList[i] != null && Block.blocksList[i].getEnableStats())
            {
                String s = StatCollector.translateToLocalFormatted("stat.mineBlock", new Object[] {
                    Block.blocksList[i].translateBlockName()
                });
                StatList.mineBlockStatArray[i] = (new StatCrafting(0x1000000 + i, s, i)).registerStat();
                StatList.objectMineStats.add(StatList.mineBlockStatArray[i]);
            }
        }

        for(int j = 0; j < Item.itemsList.length; j++)
        {
            if(!StatList.oneShotStats.containsKey(Integer.valueOf(0x1020000 + j)) && Item.itemsList[j] != null)
            {
                String s1 = StatCollector.translateToLocalFormatted("stat.useItem", new Object[] {
                    Item.itemsList[j].getStatName()
                });
                StatList.objectUseStats[j] = (new StatCrafting(0x1020000 + j, s1, j)).registerStat();
                if(j >= Block.blocksList.length)
                {
                    StatList.itemStats.add(StatList.objectUseStats[j]);
                }
            }
            if(!StatList.oneShotStats.containsKey(Integer.valueOf(0x1030000 + j)) && Item.itemsList[j] != null && Item.itemsList[j].isDamageable())
            {
                String s2 = StatCollector.translateToLocalFormatted("stat.breakItem", new Object[] {
                    Item.itemsList[j].getStatName()
                });
                StatList.objectBreakStats[j] = (new StatCrafting(0x1030000 + j, s2, j)).registerStat();
            }
        }

        HashSet hashset = new HashSet();
        Object obj;
        for(Iterator iterator = CraftingManager.getInstance().getRecipeList().iterator(); iterator.hasNext(); hashset.add(Integer.valueOf(((IRecipe)obj).getRecipeOutput().itemID)))
        {
            obj = iterator.next();
        }

        Object obj1;
        for(Iterator iterator1 = FurnaceRecipes.smelting().getSmeltingList().values().iterator(); iterator1.hasNext(); hashset.add(Integer.valueOf(((ItemStack)obj1).itemID)))
        {
            obj1 = iterator1.next();
        }

        Iterator iterator2 = hashset.iterator();
        do
        {
            if(!iterator2.hasNext())
            {
                break;
            }
            int k = ((Integer)iterator2.next()).intValue();
            if(!StatList.oneShotStats.containsKey(Integer.valueOf(0x1010000 + k)) && Item.itemsList[k] != null)
            {
                String s3 = StatCollector.translateToLocalFormatted("stat.craftItem", new Object[] {
                    Item.itemsList[k].getStatName()
                });
                StatList.objectCraftStats[k] = (new StatCrafting(0x1010000 + k, s3, k)).registerStat();
            }
        } while(true);
    }

    public static boolean isModLoaded(String s)
    {
label0:
        {
            Class class1 = null;
            try
            {
                class1 = Class.forName(s, false, (net.minecraft.server.MinecraftServer.class).getClassLoader());
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                return false;
            }
            if(class1 == null)
            {
                break label0;
            }
            Iterator iterator = modList.iterator();
            BaseMod basemod;
            do
            {
                if(!iterator.hasNext())
                {
                    break label0;
                }
                basemod = (BaseMod)iterator.next();
            } while(!class1.isInstance(basemod));
            return true;
        }
        return false;
    }

    public static void loadConfig()
        throws IOException
    {
        cfgdir.mkdir();
        if(!cfgfile.exists() && !cfgfile.createNewFile())
        {
            return;
        }
        if(cfgfile.canRead())
        {
            FileInputStream fileinputstream = new FileInputStream(cfgfile);
            props.load(fileinputstream);
            fileinputstream.close();
        }
    }

    public static void OnTick(MinecraftServer minecraftserver)
    {
        if(!hasInit)
        {
            init();
            logger.fine("Initialized");
        }
        long l = 0L;
        if(minecraftserver.worldMngr != null && minecraftserver.worldMngr[0] != null)
        {
            l = minecraftserver.worldMngr[0].getWorldTime();
            Iterator iterator = inGameHooks.entrySet().iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break;
                }
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                if(clock != l || !((Boolean)entry.getValue()).booleanValue())
                {
                    ((BaseMod)entry.getKey()).OnTickInGame(minecraftserver);
                }
            } while(true);
        }
        clock = l;
    }

    public static void PopulateChunk(IChunkProvider ichunkprovider, int i, int j, World world)
    {
        if(!hasInit)
        {
            init();
            logger.fine("Initialized");
        }
        Iterator iterator = modList.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            BaseMod basemod = (BaseMod)iterator.next();
            if(ichunkprovider instanceof ChunkProviderGenerate)
            {
                basemod.GenerateSurface(world, world.rand, i, j);
            } else
            if(ichunkprovider instanceof ChunkProviderHell)
            {
                basemod.GenerateNether(world, world.rand, i, j);
            }
        } while(true);
    }

    private static void readFromModFolder(File file)
        throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException
    {
        ClassLoader classloader = (net.minecraft.server.MinecraftServer.class).getClassLoader();
        Method method = (java.net.URLClassLoader.class).getDeclaredMethod("addURL", new Class[] {
            java.net.URL.class
        });
        method.setAccessible(true);
        if(!file.isDirectory())
        {
            throw new IllegalArgumentException("folder must be a Directory.");
        }
        File afile[] = file.listFiles();
        for(int i = 0; i < afile.length; i++)
        {
            File file1 = afile[i];
            if(!file1.isDirectory() && (!file1.isFile() || !file1.getName().endsWith(".jar") && !file1.getName().endsWith(".zip")))
            {
                continue;
            }
            if(classloader instanceof URLClassLoader)
            {
                method.invoke(classloader, new Object[] {
                    file1.toURI().toURL()
                });
            }
            logger.finer((new StringBuilder("Adding mods from ")).append(file1.getCanonicalPath()).toString());
            if(file1.isFile())
            {
                logger.finer("Zip found.");
                FileInputStream fileinputstream = new FileInputStream(file1);
                ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
                do
                {
                    ZipEntry zipentry = zipinputstream.getNextEntry();
                    if(zipentry == null)
                    {
                        break;
                    }
                    String s1 = zipentry.getName();
                    if(!zipentry.isDirectory() && s1.startsWith("mod_") && s1.endsWith(".class"))
                    {
                        addMod(classloader, s1);
                    }
                } while(true);
                zipinputstream.close();
                fileinputstream.close();
                continue;
            }
            if(!file1.isDirectory())
            {
                continue;
            }
            Package package1 = (net.minecraft.src.ModLoader.class).getPackage();
            if(package1 != null)
            {
                String s = package1.getName().replace('.', File.separatorChar);
                file1 = new File(file1, s);
            }
            logger.finer("Directory found.");
            File afile1[] = file1.listFiles();
            if(afile1 == null)
            {
                continue;
            }
            for(int j = 0; j < afile1.length; j++)
            {
                String s2 = afile1[j].getName();
                if(afile1[j].isFile() && s2.startsWith("mod_") && s2.endsWith(".class"))
                {
                    addMod(classloader, s2);
                }
            }

        }

    }

    private static void readFromClassPath(File file)
        throws FileNotFoundException, IOException
    {
        logger.finer((new StringBuilder("Adding mods from ")).append(file.getCanonicalPath()).toString());
        ClassLoader classloader = (net.minecraft.src.ModLoader.class).getClassLoader();
        if(file.isFile() && (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")))
        {
            logger.finer("Zip found.");
            FileInputStream fileinputstream = new FileInputStream(file);
            ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
            do
            {
                ZipEntry zipentry = zipinputstream.getNextEntry();
                if(zipentry == null)
                {
                    break;
                }
                String s1 = zipentry.getName();
                if(!zipentry.isDirectory() && s1.startsWith("mod_") && s1.endsWith(".class"))
                {
                    addMod(classloader, s1);
                }
            } while(true);
            fileinputstream.close();
        } else
        if(file.isDirectory())
        {
            Package package1 = (net.minecraft.src.ModLoader.class).getPackage();
            if(package1 != null)
            {
                String s = package1.getName().replace('.', File.separatorChar);
                file = new File(file, s);
            }
            logger.finer("Directory found.");
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int i = 0; i < afile.length; i++)
                {
                    String s2 = afile[i].getName();
                    if(afile[i].isFile() && s2.startsWith("mod_") && s2.endsWith(".class"))
                    {
                        addMod(classloader, s2);
                    }
                }

            }
        }
    }

    public static void RegisterBlock(Block block)
    {
        RegisterBlock(block, null);
    }

    public static void RegisterBlock(Block block, Class class1)
    {
        try
        {
            if(block == null)
            {
                throw new IllegalArgumentException("block parameter cannot be null.");
            }
            int i = block.blockID;
            ItemBlock itemblock = null;
            if(class1 != null)
            {
                itemblock = (ItemBlock)class1.getConstructor(new Class[] {
                    Integer.TYPE
                }).newInstance(new Object[] {
                    Integer.valueOf(i - 256)
                });
            } else
            {
                itemblock = new ItemBlock(i - 256);
            }
            if(Block.blocksList[i] != null && Item.itemsList[i] == null)
            {
                Item.itemsList[i] = itemblock;
            }
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            logger.throwing("ModLoader", "RegisterBlock", illegalargumentexception);
            ThrowException(illegalargumentexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            logger.throwing("ModLoader", "RegisterBlock", illegalaccessexception);
            ThrowException(illegalaccessexception);
        }
        catch(SecurityException securityexception)
        {
            logger.throwing("ModLoader", "RegisterBlock", securityexception);
            ThrowException(securityexception);
        }
        catch(InstantiationException instantiationexception)
        {
            logger.throwing("ModLoader", "RegisterBlock", instantiationexception);
            ThrowException(instantiationexception);
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            logger.throwing("ModLoader", "RegisterBlock", invocationtargetexception);
            ThrowException(invocationtargetexception);
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            logger.throwing("ModLoader", "RegisterBlock", nosuchmethodexception);
            ThrowException(nosuchmethodexception);
        }
    }

    public static void RegisterEntityID(Class class1, String s, int i)
    {
        try
        {
            method_RegisterEntityID.invoke(null, new Object[] {
                class1, s, Integer.valueOf(i)
            });
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            logger.throwing("ModLoader", "RegisterEntityID", illegalargumentexception);
            ThrowException(illegalargumentexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            logger.throwing("ModLoader", "RegisterEntityID", illegalaccessexception);
            ThrowException(illegalaccessexception);
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            logger.throwing("ModLoader", "RegisterEntityID", invocationtargetexception);
            ThrowException(invocationtargetexception);
        }
    }

    public static void RegisterTileEntity(Class class1, String s)
    {
        try
        {
            method_RegisterTileEntity.invoke(null, new Object[] {
                class1, s
            });
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            logger.throwing("ModLoader", "RegisterTileEntity", illegalargumentexception);
            ThrowException(illegalargumentexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            logger.throwing("ModLoader", "RegisterTileEntity", illegalaccessexception);
            ThrowException(illegalaccessexception);
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            logger.throwing("ModLoader", "RegisterTileEntity", invocationtargetexception);
            ThrowException(invocationtargetexception);
        }
    }

    public static void RemoveSpawn(Class class1, EnumCreatureType enumcreaturetype)
    {
        RemoveSpawn(class1, enumcreaturetype, null);
    }

    public static void RemoveSpawn(Class class1, EnumCreatureType enumcreaturetype, BiomeGenBase abiomegenbase[])
    {
        if(class1 == null)
        {
            throw new IllegalArgumentException("entityClass cannot be null");
        }
        if(enumcreaturetype == null)
        {
            throw new IllegalArgumentException("spawnList cannot be null");
        }
        if(abiomegenbase == null)
        {
            abiomegenbase = standardBiomes;
        }
label0:
        for(int i = 0; i < abiomegenbase.length; i++)
        {
            List list = abiomegenbase[i].getSpawnableList(enumcreaturetype);
            if(list == null)
            {
                continue;
            }
            Iterator iterator = list.iterator();
            SpawnListEntry spawnlistentry;
            do
            {
                if(!iterator.hasNext())
                {
                    continue label0;
                }
                spawnlistentry = (SpawnListEntry)iterator.next();
            } while(spawnlistentry.entityClass != class1);
            list.remove(spawnlistentry);
        }

    }

    public static void RemoveSpawn(String s, EnumCreatureType enumcreaturetype)
    {
        RemoveSpawn(s, enumcreaturetype, null);
    }

    public static void RemoveSpawn(String s, EnumCreatureType enumcreaturetype, BiomeGenBase abiomegenbase[])
    {
        Class class1 = (Class)classMap.get(s);
        if(class1 != null && (net.minecraft.src.EntityLiving.class).isAssignableFrom(class1))
        {
            RemoveSpawn(class1, enumcreaturetype, abiomegenbase);
        }
    }

    public static void saveConfig()
        throws IOException
    {
        cfgdir.mkdir();
        if(!cfgfile.exists() && !cfgfile.createNewFile())
        {
            return;
        }
        if(cfgfile.canWrite())
        {
            FileOutputStream fileoutputstream = new FileOutputStream(cfgfile);
            props.store(fileoutputstream, "ModLoader Config");
            fileoutputstream.close();
        }
    }

    public static void SetInGameHook(BaseMod basemod, boolean flag, boolean flag1)
    {
        if(flag)
        {
            inGameHooks.put(basemod, Boolean.valueOf(flag1));
        } else
        {
            inGameHooks.remove(basemod);
        }
    }

    public static void setPrivateValue(Class class1, Object obj, int i, Object obj1)
        throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field field = class1.getDeclaredFields()[i];
            field.setAccessible(true);
            int j = field_modifiers.getInt(field);
            if((j & 0x10) != 0)
            {
                field_modifiers.setInt(field, j & 0xffffffef);
            }
            field.set(obj, obj1);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            logger.throwing("ModLoader", "setPrivateValue", illegalaccessexception);
            ThrowException("An impossible error has occured!", illegalaccessexception);
        }
    }

    public static void setPrivateValue(Class class1, Object obj, String s, Object obj1)
        throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field field = class1.getDeclaredField(s);
            int i = field_modifiers.getInt(field);
            if((i & 0x10) != 0)
            {
                field_modifiers.setInt(field, i & 0xffffffef);
            }
            field.setAccessible(true);
            field.set(obj, obj1);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            logger.throwing("ModLoader", "setPrivateValue", illegalaccessexception);
            ThrowException("An impossible error has occured!", illegalaccessexception);
        }
    }

    public static void TakenFromCrafting(EntityPlayer entityplayer, ItemStack itemstack)
    {
        BaseMod basemod;
        for(Iterator iterator = modList.iterator(); iterator.hasNext(); basemod.TakenFromCrafting(entityplayer, itemstack))
        {
            basemod = (BaseMod)iterator.next();
        }

    }

    public static void TakenFromFurnace(EntityPlayer entityplayer, ItemStack itemstack)
    {
        BaseMod basemod;
        for(Iterator iterator = modList.iterator(); iterator.hasNext(); basemod.TakenFromFurnace(entityplayer, itemstack))
        {
            basemod = (BaseMod)iterator.next();
        }

    }

    public static void OnItemPickup(EntityPlayer entityplayer, ItemStack itemstack)
    {
        BaseMod basemod;
        for(Iterator iterator = modList.iterator(); iterator.hasNext(); basemod.OnItemPickup(entityplayer, itemstack))
        {
            basemod = (BaseMod)iterator.next();
        }

    }

    public static void ThrowException(String s, Throwable throwable)
    {
        throwable.printStackTrace();
        logger.log(Level.SEVERE, "Unexpected exception", throwable);
        MinecraftServer.logger.throwing("ModLoader", s, throwable);
        throw new RuntimeException(s, throwable);
    }

    private static void ThrowException(Throwable throwable)
    {
        ThrowException("Exception occured in ModLoader", throwable);
    }

    private ModLoader()
    {
    }

    public static void Init(MinecraftServer minecraftserver)
    {
        instance = minecraftserver;
        try
        {
            String s = (net.minecraft.src.ModLoader.class).getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            s = s.substring(0, s.lastIndexOf('/'));
            cfgdir = new File(s, "/config/");
            cfgfile = new File(s, "/config/ModLoader.cfg");
            logfile = new File(s, "ModLoader.txt");
            modDir = new File(s, "/mods/");
        }
        catch(URISyntaxException urisyntaxexception)
        {
            getLogger().throwing("ModLoader", "Init", urisyntaxexception);
            ThrowException("ModLoader", urisyntaxexception);
            return;
        }
        try
        {
            try
            {
                method_getNextWindowId = (net.minecraft.src.EntityPlayerMP.class).getDeclaredMethod("aH", (Class[])null);
            }
            catch(NoSuchMethodException nosuchmethodexception)
            {
                method_getNextWindowId = (net.minecraft.src.EntityPlayerMP.class).getDeclaredMethod("getNextWidowId", (Class[])null);
            }
            method_getNextWindowId.setAccessible(true);
            try
            {
                field_currentWindowId = (net.minecraft.src.EntityPlayerMP.class).getDeclaredField("ci");
            }
            catch(NoSuchFieldException nosuchfieldexception)
            {
                field_currentWindowId = (net.minecraft.src.EntityPlayerMP.class).getDeclaredField("currentWindowId");
            }
            field_currentWindowId.setAccessible(true);
        }
        catch(NoSuchFieldException nosuchfieldexception1)
        {
            getLogger().throwing("ModLoader", "Init", nosuchfieldexception1);
            ThrowException("ModLoader", nosuchfieldexception1);
            return;
        }
        catch(NoSuchMethodException nosuchmethodexception1)
        {
            getLogger().throwing("ModLoader", "Init", nosuchmethodexception1);
            ThrowException("ModLoader", nosuchmethodexception1);
            return;
        }
        init();
    }

    public static void OpenGUI(EntityPlayer entityplayer, int i, IInventory iinventory, Container container)
    {
        if(!hasInit)
        {
            init();
        }
        if(entityplayer instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
            try
            {
                method_getNextWindowId.invoke(entityplayermp, new Object[0]);
                int j = field_currentWindowId.getInt(entityplayermp);
                entityplayermp.playerNetServerHandler.sendPacket(new Packet100OpenWindow(j, i, iinventory.getInvName(), iinventory.getSizeInventory()));
                entityplayermp.currentCraftingInventory = container;
                entityplayermp.currentCraftingInventory.windowId = j;
                entityplayermp.currentCraftingInventory.onCraftGuiOpened(entityplayermp);
            }
            catch(InvocationTargetException invocationtargetexception)
            {
                getLogger().throwing("ModLoaderMultiplayer", "OpenModGUI", invocationtargetexception);
                ThrowException("ModLoaderMultiplayer", invocationtargetexception);
            }
            catch(IllegalAccessException illegalaccessexception)
            {
                getLogger().throwing("ModLoaderMultiplayer", "OpenModGUI", illegalaccessexception);
                ThrowException("ModLoaderMultiplayer", illegalaccessexception);
            }
        }
    }

    static 
    {
        cfgLoggingLevel = Level.FINER;
    }
}
