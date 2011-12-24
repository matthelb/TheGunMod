package com.heuristix;

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
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/20/11
 * Time: 5:56 PM
 */
public class GunCreator extends JFrame {

    public static final String VERSION = "0.7";

    private static final Dimension TEXT_FIELD_SIZE = new Dimension(100, 20);
    private static final NumberFormatter INTEGER_FORMATTER = new NumberFormatter(new DecimalFormat("#"));
    private static final NumberFormatter DECIMAL_FORMATTER = new NumberFormatter(new DecimalFormat("#.#"));

    public static List<Pair<String,String>> obfuscatedClassName = new LinkedList<Pair<String,String>>();
    static {
        //add additional Pairs as obfuscations increase
        obfuscatedClassName.add(new Pair("ry", "nq"));
        obfuscatedClassName.add(new Pair("rv", "wd"));
    }

    private DisplayableImageButton gunImageButton, bulletImageButton;
    private DisplayableBytesButton shootSoundButton;

    private JTextField nameField, bulletNameField;
    private JComboBox fireMode, scope;
    private JFormattedTextField damageField, rangeField, zoomField, shotsPerMinuteField, reloadField, clipSizeField, recoilXField, recoilYField, bulletIdField, gunIdField, roundsPerMinuteField, bulletSpreadField, roundsPerShotField;
    private JPanel[] fireModePanels;

    private final FileChooserCallback openCallback = new FileChooserCallback() {
        public void selectedFile(File file) {
            byte[] bytes = Util.read(file);
            if(file.getName().toLowerCase().endsWith("gun"))
                load(new CustomGun(bytes));
            else {
                try {
                    load(new Gun(bytes));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private final FileChooserCallback saveCallback = new FileChooserCallback() {
        public void selectedFile(File file) {
            String fileName = file.getName();
            int index = fileName.lastIndexOf('.');
            if(index > -1) {
                String extension = fileName.substring(index);
                if(!extension.toLowerCase().equals(".gun2"))
                    fileName = fileName.substring(0, index) + "gun2";
            } else {
                fileName = fileName + ".gun2";
            }
            file = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + fileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                if(out != null)
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
        nameField.setPreferredSize(TEXT_FIELD_SIZE);
        northPanel.add(nameField);

        GridLayout columned = new GridLayout(0, 2, 5, 5);
        westPanel.setLayout(columned);
        eastPanel.setLayout(columned);
        DefaultFormatterFactory integerFormatter = new DefaultFormatterFactory(INTEGER_FORMATTER, INTEGER_FORMATTER, INTEGER_FORMATTER);
        DefaultFormatterFactory decimalFormatter = new DefaultFormatterFactory(DECIMAL_FORMATTER, DECIMAL_FORMATTER, DECIMAL_FORMATTER);

        damageField = new JFormattedTextField(0);
        damageField.setPreferredSize(TEXT_FIELD_SIZE);
        damageField.setFormatterFactory(integerFormatter);

        rangeField = new JFormattedTextField((float) 0);
        rangeField.setPreferredSize(TEXT_FIELD_SIZE);
        rangeField.setFormatterFactory(decimalFormatter);

        shotsPerMinuteField = new JFormattedTextField(0);
        shotsPerMinuteField.setPreferredSize(TEXT_FIELD_SIZE);
        shotsPerMinuteField.setFormatterFactory(integerFormatter);

        reloadField = new JFormattedTextField(0);
        reloadField.setPreferredSize(TEXT_FIELD_SIZE);
        reloadField.setFormatterFactory(integerFormatter);

        clipSizeField = new JFormattedTextField(0);
        clipSizeField.setPreferredSize(TEXT_FIELD_SIZE);
        clipSizeField.setFormatterFactory(integerFormatter);

        recoilXField = new JFormattedTextField(0);
        recoilXField.setPreferredSize(TEXT_FIELD_SIZE);
        recoilXField.setFormatterFactory(integerFormatter);

        recoilYField = new JFormattedTextField(0);
        recoilYField.setPreferredSize(TEXT_FIELD_SIZE);
        recoilYField.setFormatterFactory(integerFormatter);

        bulletIdField = new JFormattedTextField(0);
        bulletIdField.setPreferredSize(TEXT_FIELD_SIZE);
        bulletIdField.setFormatterFactory(integerFormatter);

        gunIdField = new JFormattedTextField(0);
        gunIdField.setPreferredSize(TEXT_FIELD_SIZE);
        gunIdField.setFormatterFactory(integerFormatter);

        zoomField = new JFormattedTextField(1.0f);
        zoomField.setPreferredSize(TEXT_FIELD_SIZE);
        zoomField.setFormatterFactory(decimalFormatter);

        bulletSpreadField = new JFormattedTextField(0.0f);
        bulletSpreadField.setPreferredSize(TEXT_FIELD_SIZE);
        bulletSpreadField.setFormatterFactory(decimalFormatter);

        roundsPerMinuteField = new JFormattedTextField(0);
        roundsPerMinuteField.setPreferredSize(TEXT_FIELD_SIZE);
        roundsPerMinuteField.setFormatterFactory(integerFormatter);

        roundsPerShotField = new JFormattedTextField(1);
        roundsPerShotField.setPreferredSize(TEXT_FIELD_SIZE);
        roundsPerShotField.setFormatterFactory(integerFormatter);
        fireModePanels = new JPanel[FireMode.values().length];
        for(int i = 0; i < fireModePanels.length; i++) {
            fireModePanels[i] = new JPanel();
        }
        fireModePanels[FireMode.BURST.ordinal()].setLayout(new FlowLayout());
        fireModePanels[FireMode.BURST.ordinal()].add(new JLabel("Rounds per minute: "));
        fireModePanels[FireMode.BURST.ordinal()].add(roundsPerMinuteField);

        bulletNameField = new JTextField();
        bulletNameField.setPreferredSize(TEXT_FIELD_SIZE);

        westPanel.add(new JLabel("Gun item ID: "));
        westPanel.add(gunIdField);
        westPanel.add(new JLabel("Bullet item ID: "));
        westPanel.add(bulletIdField);
        westPanel.add(new JLabel("Bullet name: "));
        westPanel.add(bulletNameField);
        westPanel.add(new JLabel("Bullet damage: "));
        westPanel.add(damageField);
        westPanel.add(new JLabel("Bullet range: "));
        westPanel.add(rangeField);
        westPanel.add(new JLabel("Bullet spread: "));
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
        eastPanel.add(new JLabel("Rounds per shot: " ));
        eastPanel.add(roundsPerShotField);
        scope = new JComboBox(Scope.values());
        scope.setPreferredSize(TEXT_FIELD_SIZE);
        eastPanel.add(new JLabel("Scope type: "));
        eastPanel.add(scope);
        eastPanel.add(new JLabel("Scope zoom: "));
        eastPanel.add(zoomField);
        final JPanel southPanel1 = new JPanel();
        southPanel1.setLayout(new FlowLayout());
        fireMode = new JComboBox(FireMode.values());
        fireMode.setPreferredSize(TEXT_FIELD_SIZE);
        fireMode.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for(int i = 0; i < fireModePanels.length; i++)
                    southPanel1.remove(fireModePanels[i]);
                southPanel1.add(fireModePanels[((FireMode)e.getItem()).ordinal()]);
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
        southPanel1.add(fireModePanels[((FireMode)fireMode.getSelectedItem()).ordinal()]);

        southPanel.add(southPanel1);
        JPanel southPanel2 = new JPanel();
        southPanel2.setLayout(new FlowLayout());
        southPanel2.add(new JLabel("Shoot sound: "));
        shootSoundButton = new DisplayableBytesButton();
        shootSoundButton.setPreferredSize(TEXT_FIELD_SIZE);
        southPanel2.add(shootSoundButton);
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
        gunFileChooser.setFileFilter(new FileNameExtensionFilter("GUN/2 file", "gun", "gun2"));
        openMenuItem.addActionListener(new FileChooserActionListener(this, openCallback, true, gunFileChooser));
        saveMenuItem.addActionListener(new FileChooserActionListener(this, saveCallback, false, gunFileChooser));
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    private void load(CustomGun gun) {
        if(gun != null) {
            nameField.setText(gun.name);
            try {
                fireMode.setSelectedItem(FireMode.values()[(gun.itemGunFireMode)]);
            } catch (ArrayIndexOutOfBoundsException ignored) { }
            try {
                scope.setSelectedItem(Scope.values()[gun.itemGunScope]);
            } catch (ArrayIndexOutOfBoundsException ignored) { }
            damageField.setText(String.valueOf(gun.bulletDamage));
            rangeField.setText(String.valueOf(gun.bulletRange));
            shotsPerMinuteField.setText(String.valueOf(gun.itemGunShotsPerMinute));
            roundsPerMinuteField.setText(String.valueOf(gun.itemGunRoundsPerMinute));
            reloadField.setText(String.valueOf(gun.itemGunReloadTime));
            clipSizeField.setText(String.valueOf(gun.itemGunClipSize));
            recoilXField.setText(String.valueOf(gun.itemGunRecoilX));
            recoilYField.setText(String.valueOf(gun.itemGunRecoilY));
            bulletIdField.setText(String.valueOf(gun.itemBulletId));
            gunIdField.setText(String.valueOf(gun.itemGunId));
            zoomField.setText(String.valueOf(gun.itemGunZoom));
            bulletSpreadField.setText(String.valueOf(gun.bulletSpread));
            roundsPerShotField.setText(String.valueOf(gun.itemGunRoundsPerShot));
            shootSoundButton.updateButton(gun.itemGunShootSound, gun.itemGunShootSoundBytes);

            gunImageButton.updateImage(gun.getGunTexture());
            bulletImageButton.updateImage(gun.getBulletTexture());
        }
    }

    private void load(Gun gun) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException {
        if(gun != null) {
            List<Pair<String, byte[]>> gunClasses = gun.getClasses();
            List<byte[]> resources = gun.getResources();

            HashMap<String, Method> methods = new HashMap<String, Method>();
            for(int i = 0; i < obfuscatedClassName.size(); i++) {
                Pair<String, String> obfuscatedNames = obfuscatedClassName.get(i);
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";L" + obfuscatedNames.getSecond() +";)V", new Method("(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V",
                        new InvokeMethod(superWorldEntity, new int[]{Opcodes.RETURN}, "com/heuristix/EntityBulletBase", "<init>", "(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V", false, true, false)));
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";)V", new Method("(Lnet/minecraft/src/World;)V",
                        new InvokeMethod(superWorld, new int[]{Opcodes.RETURN}, "com/heuristix/EntityBulletBase", "<init>", "(Lnet/minecraft/src/World;)V", false, true, false)));
            }
            byte[] entityBulletClassBytes = ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(0).getSecond(), gunClasses.get(0).getFirst(), methods, false);
            Class entityBulletClass = Util.defineClass(entityBulletClassBytes, gunClasses.get(0).getFirst());
            if(entityBulletClass ==null) {
                for(int i = 0; entityBulletClass == null; i++) {
                    entityBulletClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(entityBulletClassBytes, gunClasses.get(0).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(0).getFirst() + i);
                }
            }
            Class itemBulletClass = Util.defineClass(gunClasses.get(1).getSecond(), gunClasses.get(1).getFirst());
            if(itemBulletClass == null ) {
                for(int i = 0; itemBulletClass == null; i++) {
                    itemBulletClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(1).getSecond(), gunClasses.get(1).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(1).getFirst() + i);
                }
            }
            Constructor itemBulletConstructor = itemBulletClass.getDeclaredConstructor(int.class);
            itemBulletConstructor.setAccessible(true);
            ItemProjectile itemBullet = (ItemProjectile) itemBulletConstructor.newInstance(gun.getItemBulletId());
            Constructor entityBulletConstructor = entityBulletClass.getDeclaredConstructor(World.class);
            entityBulletConstructor.setAccessible(true);
            EntityProjectile entityProjectile = (EntityProjectile) entityBulletConstructor.newInstance(new Object[]{null});
            Class itemGunClass = Util.defineClass(gunClasses.get(2).getSecond(), gunClasses.get(2).getFirst());
            if(itemGunClass == null ) {
                for(int i = 0; itemGunClass == null; i++) {
                    itemGunClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(2).getSecond(), gunClasses.get(2).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(2).getFirst() + i);
                }
            }
            Constructor itemGunConstructor = itemGunClass.getDeclaredConstructor(int.class, ItemProjectile.class);
            itemGunConstructor.setAccessible(true);
            ItemGun itemGun = (ItemGun) itemGunConstructor.newInstance(gun.getItemGunId(), itemBullet);

            nameField.setText(itemGun.getName());
            try {
                fireMode.setSelectedItem(FireMode.values()[(itemGun.getFireMode())]);
            } catch (ArrayIndexOutOfBoundsException ignored) { }
            try {
                scope.setSelectedItem(Scope.values()[itemGun.getScope()]);
            } catch (ArrayIndexOutOfBoundsException ignored) { }
            bulletNameField.setText(itemBullet.getName());
            damageField.setText(String.valueOf(entityProjectile.getDamage()));
            rangeField.setText(String.valueOf(entityProjectile.getEffectiveRange()));
            shotsPerMinuteField.setText(String.valueOf(itemGun.getShotsPerMinute()));
            roundsPerMinuteField.setText(String.valueOf(itemGun.getRoundsPerMinute()));
            reloadField.setText(String.valueOf(itemGun.getReloadTime()));
            clipSizeField.setText(String.valueOf(itemGun.getClipSize()));
            recoilXField.setText(String.valueOf(itemGun.getRecoilX()));
            recoilYField.setText(String.valueOf(itemGun.getRecoilY()));
            bulletIdField.setText(String.valueOf(gun.getItemBulletId()));
            gunIdField.setText(String.valueOf(gun.getItemGunId()));
            zoomField.setText(String.valueOf(itemGun.getZoom()));
            bulletSpreadField.setText(String.valueOf(entityProjectile.getSpread()));
            roundsPerShotField.setText(String.valueOf(itemGun.getRoundsPerShot()));

            bulletImageButton.updateImage(ImageIO.read(new ByteArrayInputStream(resources.get(0))));
            gunImageButton.updateImage(ImageIO.read(new ByteArrayInputStream(resources.get(1))));
            shootSoundButton.updateButton(itemGun.getShootSound().substring(itemGun.getShootSound().lastIndexOf('.') + 1) + ".ogg", resources.get(2));
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
    public static final int[] superWorld = { Opcodes.ALOAD_0, Opcodes.ALOAD_1};
    public static final int[] superWorldEntity = { Opcodes.ALOAD_0, Opcodes.ALOAD_1, Opcodes.ALOAD_2 };

    private void write(OutputStream out) throws IOException {
        ByteVector outBytes = new ByteVector();
        outBytes.putInt(Gun.MAGIC);

        outBytes.putInt(3);
        HashMap<String, Method> methods = new HashMap<String, Method>();
        methods.put("getDamage()I", new Method(new BytecodeValue(Integer.parseInt(damageField.getText()))));
        methods.put("getEffectiveRange()F", new Method(new BytecodeValue(Float.parseFloat(rangeField.getText()))));
        methods.put("getSpread()F", new Method(new BytecodeValue(Float.parseFloat(bulletSpreadField.getText()))));


        methods.put("<init>(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V",
                new Method("(L" + obfuscatedClassName.get(0).getFirst() + ";L" +  obfuscatedClassName.get(0).getSecond() + ";)V",
                        new InvokeMethod(superWorldEntity, new int[]{Opcodes.RETURN}, "com/heuristix/EntityBulletBase", "<init>",
                                "(L" + obfuscatedClassName.get(0).getFirst() + ";L" +  obfuscatedClassName.get(0).getSecond() + ";)V", false, true, false)));
        methods.put("<init>(Lnet/minecraft/src/World;)V", new Method("(L" + obfuscatedClassName.get(0).getFirst() + ";)V",
                        new InvokeMethod(superWorld, new int[]{Opcodes.RETURN}, "com/heuristix/EntityBulletBase", "<init>", "(L" + obfuscatedClassName.get(0).getFirst() + ";)V", false, true, false)));
        String name = "Entity" + bulletNameField.getText().replaceAll("[^a-z^A-Z^0-9]", "") + nameField.getText().replaceAll("[^a-z^A-Z^0-9]", "");
        byte[] bytes = ExtensibleClassAdapter.modifyClassBytes(EntityProjectileBase.class, name, (HashMap<String, Method>) methods.clone(), true);
        byte[] stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        methods.clear();
        methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(bulletNameField.getText())));
        methods.put("getCraftAmount()I", new Method(new BytecodeValue(16)));
        name = "Item" + bulletNameField.getText().replaceAll("[^a-z^A-Z^0-9]", "");
        bytes = ExtensibleClassAdapter.modifyClassBytes(ItemProjectileBase.class, name, (HashMap<String, Method>) methods.clone(), true);
        stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        methods.clear();
        methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(nameField.getText())));
        methods.put("getShootSound()Ljava/lang/String;", new Method(new BytecodeValue("guns." + shootSoundButton.getText().substring(0, shootSoundButton.getText().indexOf(".")))));
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
        name = "Item" + nameField.getText().replaceAll("[^a-z^A-Z^0-9]", "");
        bytes = ExtensibleClassAdapter.modifyClassBytes(ItemGunBase.class, name, (HashMap<String, Method>) methods.clone(), true);
        stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        outBytes.putInt(3);
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

        outBytes.putInt(Integer.parseInt(bulletIdField.getText()));
        outBytes.putInt(Integer.parseInt(gunIdField.getText()));

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
