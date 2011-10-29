package com.heuristix;

import com.heuristix.asm.ByteVector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/20/11
 * Time: 5:56 PM
 */
public class GunCreator extends JFrame {

    public static final String VERSION = "0.5.1";

    private static final Dimension TEXT_FIELD_SIZE = new Dimension(100, 20);
    private static final NumberFormatter INTEGER_FORMATTER = new NumberFormatter(new DecimalFormat("#"));
    private static final NumberFormatter DECIMAL_FORMATTER = new NumberFormatter(new DecimalFormat("#.#"));

    private DisplayableImageButton gunImageButton, bulletImageButton;
    private DisplayableBytesButton shootSoundButton;

    private JTextField nameField;
    private JComboBox fireMode, scope;
    private JFormattedTextField damageField, rangeField, zoomField, shotsPerMinuteField, reloadField, clipSizeField, recoilXField, recoilYField, bulletIdField, gunIdField, roundsPerMinuteField, bulletSpreadField, roundsPerShotField;
    private JPanel[] fireModePanels;
    private HashMap<Class, HashMap<String, String>> methodOverrides;

    private final FileChooserCallback openCallback = new FileChooserCallback() {
        public void selectedFile(File file) {
            load(new CustomGun(Utilities.read(file)));
        }
    };

    private final FileChooserCallback saveCallback = new FileChooserCallback() {
        public void selectedFile(File file) {
            String fileName = file.getName();
            int index = fileName.lastIndexOf('.');
            if(index > -1) {
                String extension = fileName.substring(index);
                if(!extension.toLowerCase().equals(".gun"))
                    fileName = fileName.substring(0, index) + "GUN";
            } else {
                fileName = fileName + ".GUN";
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
        this.methodOverrides = new HashMap<Class, HashMap<String, String>>();
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

        westPanel.add(new JLabel("Gun item ID: "));
        westPanel.add(gunIdField);
        westPanel.add(new JLabel("Bullet item ID: "));
        westPanel.add(bulletIdField);
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
        JFileChooser gunFileChooser = new JFileChooser(Utilities.getHomeDirectory());
        gunFileChooser.setFileFilter(new FileNameExtensionFilter("GUN file", "gun"));
        openMenuItem.addActionListener(new FileChooserActionListener(this, openCallback, true, gunFileChooser));
        saveMenuItem.addActionListener(new FileChooserActionListener(this, saveCallback, false, gunFileChooser));
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        JMenu advancedMenu = new JMenu("Advanced");
        JMenuItem overrideMethodsItem = new JMenuItem("Override Methods");
        overrideMethodsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                methodOverrides = showOverrideDialog(GunCreator.this, methodOverrides);
            }
        });
        advancedMenu.add(overrideMethodsItem);
        menuBar.add(advancedMenu);

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
            methodOverrides.put(EntityBulletBase.class, transform(gun.bulletOverrideMethods));
            methodOverrides.put(ItemProjectileBase.class, transform(gun.itemBulletOverrideMethods));
            methodOverrides.put(ItemGunBase.class, transform(gun.itemGunOverrideMethods));

            gunImageButton.updateImage(gun.getGunTexture());
            bulletImageButton.updateImage(gun.getBulletTexture());
        }
    }

    private void write(OutputStream out) throws IOException {
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
        byte[] shootSoundNameBytes = Utilities.getStringBytes(Utilities.replaceNumbers(shootSoundButton.getText()));
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
    }

    private static HashMap<Class, HashMap<String, String>> showOverrideDialog(Window parent, HashMap<Class, HashMap<String, String>> methods) {
        OverrideMethodsDialog dialog = new OverrideMethodsDialog(parent, methods);
        dialog.setVisible(true);
        dialog.dispose();
        return dialog.getOverrideMethods();
    }

    private static class OverrideMethodsDialog extends JDialog {

        private static final Class[] classes = new Class[]{EntityBulletBase.class, ItemGunBase.class, ItemProjectileBase.class};
        private JTable[] methodTables;

        public OverrideMethodsDialog(Window parent, HashMap<Class, HashMap<String, String>> methods) {
            super(parent, "Override Methods", ModalityType.APPLICATION_MODAL);
            this.methodTables = new JTable[classes.length];
            init(methods);
        }

        private void init(HashMap<Class, HashMap<String, String>> methods) {
            this.setLayout(new GridLayout(0, 1, 5, 5));
            for(int i = 0; i < 3; i++) {
                methodTables[i] = new JTable(4, 2);
                int j = 0;
                HashMap<String, String> classMethods = methods.get(classes[i]);
                if(classMethods != null) {
                    for(Map.Entry<String, String> entry : classMethods.entrySet()) {
                        methodTables[i].setValueAt(entry.getKey(), j, 0);
                        methodTables[i].setValueAt(entry.getValue(), j++, 1);
                    }
                }
                this.add(methodTables[i]);
            }
            pack();
        }

        public HashMap<Class, HashMap<String, String>> getOverrideMethods() {
            HashMap<Class, HashMap<String, String>> methods = new HashMap<Class, HashMap<String, String>>();
            for(int i = 0; i < 3; i++) {
                TableModel model = methodTables[i].getModel();
                HashMap classMethods = new HashMap();
                for(int j = 0; j < model.getRowCount(); j++) {
                    classMethods.put(model.getValueAt(j, 0), model.getValueAt(j, 1));
                }
                methods.put(classes[i], classMethods);
            }
            return methods;
        }
    }

    private HashMap<String, String> transform(HashMap<String, byte[]> map) {
        HashMap<String, String> newMap = new HashMap<String, String>();
        for(Map.Entry<String, byte[]> entry : map.entrySet())
            newMap.put(entry.getKey(), Arrays.toString(entry.getValue()).replaceFirst("\\[", "").replace("]", "").replace(" ", ""));
        return newMap;
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
