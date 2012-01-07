package com.heuristix.swing;

import com.heuristix.*;
import com.heuristix.asm.ByteVector;
import com.heuristix.asm.Opcodes;
import com.heuristix.util.*;
import net.minecraft.src.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/20/11
 * Time: 5:56 PM
 */
public class GunCreator extends JFrame {

    public static final String VERSION = "0.9.1";

    private static final Dimension COMPONENT_SIZE = new Dimension(100, 20);
    private static final NumberFormatter INTEGER_FORMATTER = new NumberFormatter(new DecimalFormat("#"));
    private static final NumberFormatter DECIMAL_FORMATTER = new NumberFormatter(new DecimalFormat("#.#"));
    private static final String NON_ALPHA_NUMERICAL_REGEX = "[^a-z^A-Z^0-9]";
    private static final int PROPERTIES = 2;

    public static final List<Pair<String, String>> OBFUSCATED_CLASS_NAMES = new LinkedList<Pair<String, String>>();

    static {
        //add additional Pairs as obfuscations increase
        OBFUSCATED_CLASS_NAMES.add(new Pair("ry", "nq"));
        OBFUSCATED_CLASS_NAMES.add(new Pair("rv", "wd"));
    }

    private DisplayableImageButton gunImageButton, bulletImageButton;
    private DisplayableBytesButton shootSoundButton, reloadSoundButton;

    private JTextField nameField, projectileNameField;
    private JComboBox projectileType, fireMode, scope;
    private JFormattedTextField damageField, rangeField, zoomField, shotsPerMinuteField, reloadField, clipSizeField, recoilXField, recoilYField, projectileIdField, gunIdField, roundsPerMinuteField, bulletSpreadField, roundsPerShotField;
    private JPanel[] fireModePanels;

    private final FileChooserCallback openCallback = new FileChooserCallback() {
        public void selectedFile(File file) {
            byte[] bytes = Util.read(file);
            try {
                load(new Gun(bytes));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private final FileChooserCallback saveCallback = new FileChooserCallback() {
        public void selectedFile(File file) {
            String fileName = file.getName();
            int index = fileName.lastIndexOf('.');
            if (index > -1) {
                String extension = fileName.substring(index);
                if (!extension.toLowerCase().equals(".gun2"))
                    fileName = fileName.substring(0, index) + "gun2";
            } else {
                fileName = fileName + ".gun2";
            }
            file = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + fileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                if (out != null)
                    write(out);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public GunCreator() {
        super("GunCreator v" + VERSION);
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        JPanel westPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        JPanel southPanel = new JPanel();

        northPanel.setLayout(new FlowLayout());
        northPanel.add(new JLabel("Gun Name:"));
        nameField = new JTextField();
        nameField.setPreferredSize(COMPONENT_SIZE);
        northPanel.add(nameField);

        GridLayout columned = new GridLayout(0, 2, 5, 5);
        westPanel.setLayout(columned);
        eastPanel.setLayout(columned);
        DefaultFormatterFactory integerFormatter = new DefaultFormatterFactory(INTEGER_FORMATTER, INTEGER_FORMATTER, INTEGER_FORMATTER);
        DefaultFormatterFactory decimalFormatter = new DefaultFormatterFactory(DECIMAL_FORMATTER, DECIMAL_FORMATTER, DECIMAL_FORMATTER);

        damageField = new JFormattedTextField(0);
        damageField.setPreferredSize(COMPONENT_SIZE);
        damageField.setFormatterFactory(integerFormatter);

        rangeField = new JFormattedTextField((float) 0);
        rangeField.setPreferredSize(COMPONENT_SIZE);
        rangeField.setFormatterFactory(decimalFormatter);

        shotsPerMinuteField = new JFormattedTextField(0);
        shotsPerMinuteField.setPreferredSize(COMPONENT_SIZE);
        shotsPerMinuteField.setFormatterFactory(integerFormatter);

        reloadField = new JFormattedTextField(0);
        reloadField.setPreferredSize(COMPONENT_SIZE);
        reloadField.setFormatterFactory(integerFormatter);

        clipSizeField = new JFormattedTextField(0);
        clipSizeField.setPreferredSize(COMPONENT_SIZE);
        clipSizeField.setFormatterFactory(integerFormatter);

        recoilXField = new JFormattedTextField(0);
        recoilXField.setPreferredSize(COMPONENT_SIZE);
        recoilXField.setFormatterFactory(integerFormatter);

        recoilYField = new JFormattedTextField(0);
        recoilYField.setPreferredSize(COMPONENT_SIZE);
        recoilYField.setFormatterFactory(integerFormatter);

        projectileIdField = new JFormattedTextField(0);
        projectileIdField.setPreferredSize(COMPONENT_SIZE);
        projectileIdField.setFormatterFactory(integerFormatter);

        gunIdField = new JFormattedTextField(0);
        gunIdField.setPreferredSize(COMPONENT_SIZE);
        gunIdField.setFormatterFactory(integerFormatter);

        zoomField = new JFormattedTextField(1.0f);
        zoomField.setPreferredSize(COMPONENT_SIZE);
        zoomField.setFormatterFactory(decimalFormatter);

        bulletSpreadField = new JFormattedTextField(0.0f);
        bulletSpreadField.setPreferredSize(COMPONENT_SIZE);
        bulletSpreadField.setFormatterFactory(decimalFormatter);

        roundsPerMinuteField = new JFormattedTextField(0);
        roundsPerMinuteField.setPreferredSize(COMPONENT_SIZE);
        roundsPerMinuteField.setFormatterFactory(integerFormatter);

        roundsPerShotField = new JFormattedTextField(1);
        roundsPerShotField.setPreferredSize(COMPONENT_SIZE);
        roundsPerShotField.setFormatterFactory(integerFormatter);
        fireModePanels = new JPanel[FireMode.values().length];
        for (int i = 0; i < fireModePanels.length; i++) {
            fireModePanels[i] = new JPanel();
        }
        fireModePanels[FireMode.BURST.ordinal()].setLayout(new FlowLayout());
        fireModePanels[FireMode.BURST.ordinal()].add(new JLabel("Rounds per minute: "));
        fireModePanels[FireMode.BURST.ordinal()].add(roundsPerMinuteField);

        projectileNameField = new JTextField();
        projectileNameField.setPreferredSize(COMPONENT_SIZE);

        projectileType = new JComboBox(ProjectileType.values());
        projectileType.setPreferredSize(COMPONENT_SIZE);

        westPanel.add(new JLabel("Gun item ID: "));
        westPanel.add(gunIdField);
        westPanel.add(new JLabel("Projectile item ID: "));
        westPanel.add(projectileIdField);
        westPanel.add(new JLabel("Projectile name: "));
        westPanel.add(projectileNameField);
        westPanel.add(new JLabel("Projectile type: "));
        westPanel.add(projectileType);
        westPanel.add(new JLabel("Projectile damage: "));
        westPanel.add(damageField);
        westPanel.add(new JLabel("Projectile range: "));
        westPanel.add(rangeField);
        westPanel.add(new JLabel("Projectile spread: "));
        westPanel.add(bulletSpreadField);
        westPanel.add(new JLabel("Shots per minute: "));
        westPanel.add(shotsPerMinuteField);
        westPanel.add(new JLabel("Reload time (ms): "));
        westPanel.add(reloadField);


        eastPanel.add(new JLabel("Clip size: "));
        eastPanel.add(clipSizeField);
        eastPanel.add(new JLabel("Recoil: "));
        eastPanel.add(recoilYField);
        eastPanel.add(new JLabel("Horizontal recoil: "));
        eastPanel.add(recoilXField);
        eastPanel.add(new JLabel("Rounds per shot: "));
        eastPanel.add(roundsPerShotField);
        scope = new JComboBox(Scope.values());
        scope.setPreferredSize(COMPONENT_SIZE);
        eastPanel.add(new JLabel("Scope type: "));
        eastPanel.add(scope);
        eastPanel.add(new JLabel("Scope zoom: "));
        eastPanel.add(zoomField);
        final JPanel southPanel1 = new JPanel();
        southPanel1.setLayout(new FlowLayout());
        fireMode = new JComboBox(FireMode.values());
        fireMode.setPreferredSize(COMPONENT_SIZE);
        fireMode.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < fireModePanels.length; i++)
                    southPanel1.remove(fireModePanels[i]);
                southPanel1.add(fireModePanels[((FireMode) e.getItem()).ordinal()]);
                pack();
            }
        });
        eastPanel.add(new JLabel("Fire mode: "));
        eastPanel.add(fireMode);

        gunImageButton = new DisplayableImageButton();
        gunImageButton.setPreferredSize(new Dimension(128, 128));
        gunImageButton.setToolTipText("Drag custom gun icon here.");
        centerPanel.add(gunImageButton);

        bulletImageButton = new DisplayableImageButton();
        bulletImageButton.setPreferredSize(new Dimension(32, 32));
        bulletImageButton.setToolTipText("Drag custom bullet icon here.");
        southPanel.setLayout(new GridLayout(0, 1, 10, 10));
        southPanel1.add(new JLabel("Bullet icon: "));
        southPanel1.add(bulletImageButton);
        southPanel1.add(fireModePanels[((FireMode) fireMode.getSelectedItem()).ordinal()]);

        southPanel.add(southPanel1);
        JPanel southPanel2 = new JPanel();
        southPanel2.setLayout(new FlowLayout());
        southPanel2.add(new JLabel("Shoot sound: "));
        shootSoundButton = new DisplayableBytesButton();
        shootSoundButton.setPreferredSize(COMPONENT_SIZE);
        southPanel2.add(shootSoundButton);
        southPanel2.add(new JLabel("Reload sound: "));
        reloadSoundButton = new DisplayableBytesButton();
        reloadSoundButton.setPreferredSize(COMPONENT_SIZE);
        southPanel2.add(reloadSoundButton);
        southPanel.add(southPanel2);

        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open"), saveMenuItem = new JMenuItem("Save");
        JFileChooser gunFileChooser = new JFileChooser(Util.getHomeDirectory());
        gunFileChooser.setFileFilter(new FileNameExtensionFilter("GUN2 file", "gun2"));
        openMenuItem.addActionListener(new FileChooserActionListener(this, openCallback, true, gunFileChooser));
        saveMenuItem.addActionListener(new FileChooserActionListener(this, saveCallback, false, gunFileChooser));
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    /*private void load(CustomGun gun) {
        if (gun != null) {
            nameField.setText(gun.name);
            try {
                fireMode.setSelectedItem(FireMode.values()[(gun.itemGunFireMode)]);
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            try {
                scope.setSelectedItem(Scope.values()[gun.itemGunScope]);
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            damageField.setText(String.valueOf(gun.bulletDamage));
            rangeField.setText(String.valueOf(gun.bulletRange));
            shotsPerMinuteField.setText(String.valueOf(gun.itemGunShotsPerMinute));
            roundsPerMinuteField.setText(String.valueOf(gun.itemGunRoundsPerMinute));
            reloadField.setText(String.valueOf(gun.itemGunReloadTime));
            clipSizeField.setText(String.valueOf(gun.itemGunClipSize));
            recoilXField.setText(String.valueOf(gun.itemGunRecoilX));
            recoilYField.setText(String.valueOf(gun.itemGunRecoilY));
            projectileIdField.setText(String.valueOf(gun.itemBulletId));
            gunIdField.setText(String.valueOf(gun.itemGunId));
            zoomField.setText(String.valueOf(gun.itemGunZoom));
            bulletSpreadField.setText(String.valueOf(gun.bulletSpread));
            roundsPerShotField.setText(String.valueOf(gun.itemGunRoundsPerShot));
            shootSoundButton.updateButton(gun.itemGunShootSound, gun.itemGunShootSoundBytes);

            gunImageButton.updateImage(gun.getGunTexture());
            bulletImageButton.updateImage(gun.getBulletTexture());
        }
    }*/

    private void load(Gun gun) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException {
        if (gun != null) {
            List<Pair<String, byte[]>> gunClasses = gun.getClasses();
            List<byte[]> resources = gun.getResources();

            Class entityProjectileClass = Util.defineClass(gunClasses.get(0).getSecond(), gunClasses.get(0).getFirst());
            if (entityProjectileClass == null) {
                for (int i = 0; entityProjectileClass == null; i++) {
                    entityProjectileClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(0).getSecond(), gunClasses.get(0).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(0).getFirst() + i);
                }
            }
            Class projectileType = entityProjectileClass.getSuperclass();
            /*if(projectileType.equals(com.heuristix.EntityBulletBase.class))
                projectileType = com.heuristix.guns.EntityBulletBase.class;*/
            HashMap<String, Method> methods = new HashMap<String, Method>();
            for (int i = 0; i < OBFUSCATED_CLASS_NAMES.size(); i++) {
                Pair<String, String> obfuscatedNames = OBFUSCATED_CLASS_NAMES.get(i);
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";L" + obfuscatedNames.getSecond() + ";)V", new Method("(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V",
                        new InvokeMethod(SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, projectileType.getCanonicalName().replace('.', '/'), "<init>", "(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V", false, true, false)));
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";)V", new Method("(Lnet/minecraft/src/World;)V",
                        new InvokeMethod(SUPER_WORLD, new int[]{Opcodes.RETURN}, projectileType.getCanonicalName().replace('.', '/'), "<init>", "(Lnet/minecraft/src/World;)V", false, true, false)));
            }
            byte[] entityProjectileClassBytes = ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(0).getSecond(), gunClasses.get(0).getFirst(), methods, false);
            entityProjectileClass = Util.defineClass(entityProjectileClassBytes, gunClasses.get(0).getFirst());
            if (entityProjectileClass == null) {
                for (int i = 0; entityProjectileClass == null; i++) {
                    entityProjectileClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(entityProjectileClassBytes, gunClasses.get(0).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(0).getFirst() + i);
                }
            }
            Class itemProjectileClass = Util.defineClass(gunClasses.get(1).getSecond(), gunClasses.get(1).getFirst());
            if (itemProjectileClass == null) {
                for (int i = 0; itemProjectileClass == null; i++) {
                    itemProjectileClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(1).getSecond(), gunClasses.get(1).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(1).getFirst() + i);
                }
            }
            Constructor itemProjectileConstructor = itemProjectileClass.getDeclaredConstructor(int.class);
            itemProjectileConstructor.setAccessible(true);
            ItemProjectile itemBullet = (ItemProjectile) itemProjectileConstructor.newInstance(gun.getItemBulletId());
            Constructor entityBulletConstructor = entityProjectileClass.getDeclaredConstructor(World.class);
            entityBulletConstructor.setAccessible(true);
            EntityProjectile entityProjectile = (EntityProjectile) entityBulletConstructor.newInstance(new Object[]{null});
            Class itemGunClass = Util.defineClass(gunClasses.get(2).getSecond(), gunClasses.get(2).getFirst());
            if (itemGunClass == null) {
                for (int i = 0; itemGunClass == null; i++) {
                    itemGunClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(2).getSecond(), gunClasses.get(2).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(2).getFirst() + i);
                }
            }
            Constructor itemGunConstructor = itemGunClass.getDeclaredConstructor(int.class, ItemProjectile.class);
            itemGunConstructor.setAccessible(true);
            ItemGun itemGun = (ItemGun) itemGunConstructor.newInstance(gun.getItemGunId(), itemBullet);

            this.projectileType.setSelectedItem(ProjectileType.forClass(projectileType));
            nameField.setText(itemGun.getName());
            try {
                fireMode.setSelectedItem(FireMode.values()[(itemGun.getFireMode())]);
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            try {
                scope.setSelectedItem(Scope.values()[itemGun.getScope()]);
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            projectileNameField.setText(itemBullet.getName());
            damageField.setText(String.valueOf(entityProjectile.getDamage()));
            rangeField.setText(String.valueOf(entityProjectile.getEffectiveRange()));
            shotsPerMinuteField.setText(String.valueOf(itemGun.getShotsPerMinute()));
            roundsPerMinuteField.setText(String.valueOf(itemGun.getRoundsPerMinute()));
            reloadField.setText(String.valueOf(itemGun.getReloadTime()));
            clipSizeField.setText(String.valueOf(itemGun.getClipSize()));
            recoilXField.setText(String.valueOf(itemGun.getRecoilX()));
            recoilYField.setText(String.valueOf(itemGun.getRecoilY()));
            projectileIdField.setText(String.valueOf(gun.getItemBulletId()));
            gunIdField.setText(String.valueOf(gun.getItemGunId()));
            zoomField.setText(String.valueOf(itemGun.getZoom()));
            bulletSpreadField.setText(String.valueOf(entityProjectile.getSpread()));
            roundsPerShotField.setText(String.valueOf(itemGun.getRoundsPerShot()));

            bulletImageButton.updateImage(ImageIO.read(new ByteArrayInputStream(resources.get(0))));
            gunImageButton.updateImage(ImageIO.read(new ByteArrayInputStream(resources.get(1))));
            shootSoundButton.updateButton(itemGun.getShootSound().substring(itemGun.getShootSound().lastIndexOf('.') + 1) + ".ogg", resources.get(2));
            if (resources.size() > 3) {
                reloadSoundButton.updateButton(itemGun.getReloadSound().substring(itemGun.getReloadSound().lastIndexOf('.') + 1) + ".ogg", resources.get(3));
            } else {
                reloadSoundButton.updateButton("", null);
            }
        }
    }

    /*private void write(OutputStream out) throws IOException {
        ByteVector outBytes = new ByteVector();
        outBytes.putInt(CustomGun.MAGIC);
        byte[] nameBytes = Utilities.getStringBytes(nameField.getText());
        outBytes.putByteArray(nameBytes, 0, nameBytes.length);
        outBytes.putInt(Integer.parseInt(damageField.getText()));
        outBytes.putInt(Float.floatToIntBits(Float.parseFloat(rangeField.getText())));
        HashMap<String, String> bulletOverrideMethods = methodOverrides.get(EntityBulletBase.class);
        if(bulletOverrideMethods == null)
            bulletOverrideMethods = new HashMap<String, String>();
        bulletOverrideMethods.remove(null);
        outBytes.putShort(bulletOverrideMethods.size());
        for(Map.Entry<String, String> entry : bulletOverrideMethods.entrySet()) {
            byte[] bytes = Utilities.parseByteArray(entry.getValue());
            if(bytes == null)
                bytes = new byte[0];
            outBytes.putInt(bytes.length);
            byte[] stringBytes = Utilities.getStringBytes(entry.getKey());
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }

        outBytes.putInt(Integer.parseInt(bulletIdField.getText()));
        ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
        ImageIO.write(bulletImageButton.getImage(), "png", imageOut);
        byte[] imageBytes = imageOut.toByteArray();
        outBytes.putInt(imageBytes.length);
        outBytes.putByteArray(imageBytes, 0, imageBytes.length);
        HashMap<String, String> itemBulletOverrideMethods = methodOverrides.get(ItemProjectileBase.class);
        if(itemBulletOverrideMethods == null)
            itemBulletOverrideMethods = new HashMap<String, String>();
        itemBulletOverrideMethods.remove(null);
        outBytes.putShort(itemBulletOverrideMethods.size());
        for(Map.Entry<String, String> entry : itemBulletOverrideMethods.entrySet()) {
            byte[] bytes = Utilities.parseByteArray(entry.getValue());
            outBytes.putInt(bytes.length);
            byte[] stringBytes = Utilities.getStringBytes(entry.getKey());
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }

        outBytes.putInt(Integer.parseInt(gunIdField.getText()));
        imageOut.reset();
        ImageIO.write(gunImageButton.getImage(), "png", imageOut);
        imageBytes = imageOut.toByteArray();
        imageOut.close();
        outBytes.putInt(imageBytes.length);
        outBytes.putByteArray(imageBytes, 0, imageBytes.length);
        byte[] shootSoundBytes = shootSoundButton.getBytes();
        outBytes.putInt(shootSoundBytes.length);
        outBytes.putByteArray(shootSoundBytes, 0, shootSoundBytes.length);
        byte[] shootSoundNameBytes = Utilities.getStringBytes(Utilities.numbersToText(shootSoundButton.getText()));
        outBytes.putByteArray(shootSoundNameBytes, 0, shootSoundNameBytes.length);
        outBytes.putInt(Integer.parseInt(shotsPerMinuteField.getText()));
        outBytes.putInt(((FireMode) fireMode.getSelectedItem()).ordinal());
        outBytes.putInt(((Scope) scope.getSelectedItem()).ordinal());
        outBytes.putInt(Float.floatToIntBits(Float.parseFloat(zoomField.getText())));
        outBytes.putInt(Integer.parseInt(clipSizeField.getText()));
        outBytes.putInt(Integer.parseInt(reloadField.getText()));
        outBytes.putInt(Integer.parseInt(recoilXField.getText()));
        outBytes.putInt(Integer.parseInt(recoilYField.getText()));
        HashMap<String, String> itemGunOverrideMethods = methodOverrides.get(ItemGunBase.class);
        if(itemGunOverrideMethods == null)
            itemGunOverrideMethods = new HashMap<String, String>();
        itemGunOverrideMethods.remove(null);
        outBytes.putShort(itemGunOverrideMethods.size());
        for(Map.Entry<String, String> entry : itemGunOverrideMethods.entrySet()) {
            byte[] bytes = Utilities.parseByteArray(entry.getValue());
            outBytes.putInt(bytes.length);
            byte[] stringBytes = Utilities.getStringBytes(entry.getKey());
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }
        outBytes.putInt(Integer.parseInt(roundsPerMinuteField.getText()));
        outBytes.putInt(Float.floatToIntBits(Float.parseFloat(bulletSpreadField.getText())));
        outBytes.putInt(Integer.parseInt(roundsPerShotField.getText()));
        try {
            out.write(outBytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public static final int[] SUPER_WORLD = {Opcodes.ALOAD_0, Opcodes.ALOAD_1};
    public static final int[] SUPER_WORLD_ENTITY = {Opcodes.ALOAD_0, Opcodes.ALOAD_1, Opcodes.ALOAD_2};

    private void write(OutputStream out) throws IOException {
        ByteVector outBytes = new ByteVector();
        outBytes.putInt(Gun.MAGIC);

        outBytes.putInt(3);
        HashMap<String, Method> methods = new HashMap<String, Method>();
        methods.put("getDamage()I", new Method(new BytecodeValue(Integer.parseInt(damageField.getText()))));
        methods.put("getEffectiveRange()F", new Method(new BytecodeValue(Float.parseFloat(rangeField.getText()))));
        methods.put("getSpread()F", new Method(new BytecodeValue(Float.parseFloat(bulletSpreadField.getText()))));

        Class clazz = ((ProjectileType) projectileType.getSelectedItem()).getProjectileType();
        methods.put("<init>(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V",
                new Method("(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";L" + OBFUSCATED_CLASS_NAMES.get(0).getSecond() + ";)V",
                        new InvokeMethod(SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, clazz.getCanonicalName().replace('.', '/'), "<init>",
                                "(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";L" + OBFUSCATED_CLASS_NAMES.get(0).getSecond() + ";)V", false, true, false)));
        methods.put("<init>(Lnet/minecraft/src/World;)V", new Method("(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";)V",
                new InvokeMethod(SUPER_WORLD, new int[]{Opcodes.RETURN}, clazz.getCanonicalName().replace('.', '/'), "<init>", "(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";)V", false, true, false)));
        String name = "Entity" + projectileNameField.getText().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "") + nameField.getText().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "");
        byte[] bytes = ExtensibleClassAdapter.modifyClassBytes(clazz, name, (HashMap<String, Method>) methods.clone(), true);
        byte[] stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        methods.clear();
        methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(projectileNameField.getText())));
        methods.put("getCraftAmount()I", new Method(new BytecodeValue(16)));
        name = "Item" + projectileNameField.getText().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "");
        bytes = ExtensibleClassAdapter.modifyClassBytes(ItemProjectileBase.class, name, (HashMap<String, Method>) methods.clone(), true);
        stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        methods.clear();
        methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(nameField.getText())));
        methods.put("getShootSound()Ljava/lang/String;", new Method(new BytecodeValue("guns." + shootSoundButton.getText().substring(0, shootSoundButton.getText().indexOf(".")))));
        methods.put("getReloadSound()Ljava/lang/String;", new Method(new BytecodeValue("guns." + reloadSoundButton.getText().substring(0, reloadSoundButton.getText().indexOf(".")))));
        methods.put("getShotsPerMinute()I", new Method(new BytecodeValue(Integer.parseInt(shotsPerMinuteField.getText()))));
        methods.put("getFireMode()I", new Method(new BytecodeValue(((FireMode) fireMode.getSelectedItem()).ordinal())));
        methods.put("getReloadTime()I", new Method(new BytecodeValue(Integer.parseInt(reloadField.getText()))));
        methods.put("getClipSize()I", new Method(new BytecodeValue(Integer.parseInt(clipSizeField.getText()))));
        methods.put("getRecoilX()I", new Method(new BytecodeValue(Integer.parseInt(recoilXField.getText()))));
        methods.put("getRecoilY()I", new Method(new BytecodeValue(Integer.parseInt(recoilYField.getText()))));
        methods.put("getZoom()F", new Method(new BytecodeValue(Float.parseFloat(zoomField.getText()))));
        methods.put("getScope()I", new Method(new BytecodeValue(((Scope) scope.getSelectedItem()).ordinal())));
        methods.put("getRoundsPerMinute()I", new Method(new BytecodeValue(Integer.parseInt(roundsPerMinuteField.getText()))));
        methods.put("getRoundsPerShot()I", new Method(new BytecodeValue(Integer.parseInt(roundsPerShotField.getText()))));
        name = "Item" + nameField.getText().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "");
        bytes = ExtensibleClassAdapter.modifyClassBytes(ItemGunBase.class, name, (HashMap<String, Method>) methods.clone(), true);
        stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        outBytes.putInt(4);
        ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
        ImageIO.write(bulletImageButton.getImage(), "png", imageOut);
        bytes = imageOut.toByteArray();
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        imageOut = new ByteArrayOutputStream();
        ImageIO.write(gunImageButton.getImage(), "png", imageOut);
        bytes = imageOut.toByteArray();
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        bytes = shootSoundButton.getBytes();
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        bytes = reloadSoundButton.getBytes();
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        /*outBytes.putInt(Integer.parseInt(projectileIdField.getText()));
        outBytes.putInt(Integer.parseInt(gunIdField.getText()));*/
        outBytes.putInt(PROPERTIES);

        String[] strings = new String[]{"itemGunId", "itemBulletId"};
        int[][] ints = new int[][]{ReverseBuffer.getInt(Integer.parseInt(gunIdField.getText())), ReverseBuffer.getInt(Integer.parseInt(projectileIdField.getText()))};

        for (int i = 0; i < strings.length; i++) {
            stringBytes = Util.getStringBytes(strings[i]);
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            bytes = Util.getByteArray(ints[i]);
            outBytes.putInt(bytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }

        out.write(outBytes.toByteArray());
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                GunCreator gunCreator = new GunCreator();
                gunCreator.setVisible(true);
            }
        });
    }


}
