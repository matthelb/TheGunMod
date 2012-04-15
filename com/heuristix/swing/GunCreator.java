package com.heuristix.swing;

import com.heuristix.*;
import com.heuristix.util.Log;
import com.heuristix.util.ReverseBuffer;
import net.minecraft.src.mod_Guns;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/20/11
 * Time: 5:56 PM
 */
public class GunCreator extends AbstractGunBridge {

    public static final boolean MC_SRC_MOD = false;

    private static final Dimension COMPONENT_SIZE = new Dimension(100, 20);
    private static final NumberFormatter INTEGER_FORMATTER = new NumberFormatter(new DecimalFormat("#"));
    private static final NumberFormatter DECIMAL_FORMATTER = new NumberFormatter(new DecimalFormat("#.#"));

    private final JFrame frame;

    private DisplayableBytesButton shootSoundButton, reloadSoundButton;

    private JTextField nameField, projectileNameField;
    private JComboBox projectileType, fireMode, scope;
    private JFormattedTextField damageField, rangeField, zoomField, shotsPerMinuteField, reloadField, clipSizeField, recoilXField, recoilYField, projectileIdField, gunIdField, roundsPerMinuteField, projectileSpreadField, roundsPerShotField;
    private JPanel[] fireModePanels;
    private JDialog textureDialog;
    private TexturePanel texturePanel;

    private final FileChooserCallback openCallback = new FileChooserCallback() {
        public void selectedFile(File file) {
            byte[] bytes = Util.read(file);
            try {
                read(new Gun(bytes), true);
            } catch (Exception e) {
                Log.throwing(getClass(), "selectedFile(File file)", e, mod_Guns.class);
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
                Log.throwing(getClass(), "selectedFile(File file)", e, mod_Guns.class);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        Log.throwing(getClass(), "selectedFile(File file)", e, mod_Guns.class);
                    }
                }
            }
        }
    };

    public GunCreator() {
        this.frame = new JFrame("GunCreator v" + VERSION);
        init();
    }

    private void init() {
        frame.setLayout(new BorderLayout());

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

        projectileSpreadField = new JFormattedTextField(0.0f);
        projectileSpreadField.setPreferredSize(COMPONENT_SIZE);
        projectileSpreadField.setFormatterFactory(decimalFormatter);

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

        texturePanel = new TexturePanel();
        textureDialog = new JDialog(frame, "Advanced Textures", true);
        textureDialog.setLayout(new BorderLayout());
        textureDialog.add(texturePanel, BorderLayout.CENTER);
        JPanel dialogButtonPanel = new JPanel();
        dialogButtonPanel.setLayout(new FlowLayout());
        JButton dialogOkButton = new JButton("Ok");
        dialogOkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textureDialog.setVisible(false);
            }
        });
        dialogButtonPanel.add(dialogOkButton);
        textureDialog.add(dialogButtonPanel, BorderLayout.SOUTH);
        textureDialog.pack();

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
        westPanel.add(projectileSpreadField);
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
                frame.pack();
            }
        });
        eastPanel.add(new JLabel("Fire mode: "));
        eastPanel.add(fireMode);

        southPanel.setLayout(new GridLayout(0, 1, 10, 10));

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

        JButton textureButton = new JButton("Textures");
        ActionListener textureActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textureDialog.setVisible(!textureDialog.isVisible());
            }
        };
        textureButton.addActionListener(textureActionListener);
        centerPanel.add(textureButton);

        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(westPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(eastPanel, BorderLayout.EAST);
        frame.add(southPanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open"), saveMenuItem = new JMenuItem("Save");
        JFileChooser gunFileChooser = new JFileChooser(Util.getHomeDirectory());
        gunFileChooser.setFileFilter(new FileNameExtensionFilter("GUN2 file", "gun2"));
        openMenuItem.addActionListener(new FileChooserActionListener(frame, openCallback, true, gunFileChooser));
        saveMenuItem.addActionListener(new FileChooserActionListener(frame, saveCallback, false, gunFileChooser));
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        JMenu advancedMenu = new JMenu("Advanced");
        JMenuItem hdTextureItem = new JMenuItem("HD Textures");
        hdTextureItem.addActionListener(textureActionListener);
        advancedMenu.add(hdTextureItem);
        menuBar.add(advancedMenu);

        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    public void setFrameVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    Log.throwing(getClass(), "main(String[] args)", e, mod_Guns.class);
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception e1) {
                        Log.throwing(getClass(), "main(String[] args)", e1, mod_Guns.class);
                    }
                }
                GunCreator gunCreator = new GunCreator();
                gunCreator.setFrameVisible(true);
            }
        });
    }

    @Override
    protected int getGunId() {
        return Integer.parseInt(gunIdField.getText());
    }

    @Override
    protected int getProjectileId() {
        return Integer.parseInt(projectileIdField.getText());
    }

    @Override
    protected String getShootSound() {
        return shootSoundButton.getText();
    }

    @Override
    protected String getReloadSound() {
        return reloadSoundButton.getText();
    }

    @Override
    protected int getRoundsPerShot() {
        return Integer.parseInt(roundsPerShotField.getText());
    }

    @Override
    protected int getRoundsPerMinute() {
        return Integer.parseInt(roundsPerShotField.getText());
    }

    @Override
    protected Scope getScope() {
        return (Scope) scope.getSelectedItem();
    }

    @Override
    protected float getZoom() {
        return Float.parseFloat(zoomField.getText());
    }

    @Override
    protected int getRecoilY() {
        return Integer.parseInt(recoilYField.getText());
    }

    @Override
    protected int getRecoilX() {
        return Integer.parseInt(recoilXField.getText());
    }

    @Override
    protected int getClipSize() {
        return Integer.parseInt(clipSizeField.getText());
    }

    @Override
    protected int getReloadTime() {
        return Integer.parseInt(reloadField.getText());
    }

    @Override
    protected FireMode getFireMode() {
        return (FireMode) fireMode.getSelectedItem();
    }

    @Override
    protected int getShotsPerMinute() {
        return Integer.parseInt(shotsPerMinuteField.getText());
    }

    @Override
    protected String getGunName() {
        return nameField.getText();
    }

    @Override
    protected String getProjectileName() {
        return projectileNameField.getText();
    }

    @Override
    protected ProjectileType getProjectileType() {
        return (ProjectileType) projectileType.getSelectedItem();
    }

    @Override
    protected float getSpread() {
        return Float.parseFloat(projectileSpreadField.getText());
    }

    @Override
    protected float getRange() {
        return Float.parseFloat(rangeField.getText());
    }

    @Override
    protected int getDamage() {
        return Integer.parseInt(damageField.getText());
    }

    @Override
    protected Map<String, byte[]> getResources() {
        Map<String, byte[]> resources = new HashMap<String, byte[]>();
        for(int i = 0; i < 2; i++) {
            Map<Integer, BufferedImage> textures = texturePanel.getTextures(i == 0);
            for(Map.Entry<Integer, BufferedImage> entry : textures.entrySet()) {
                String key = entry.getKey() + "x" + entry.getKey() + ((i == 0) ? "gun" : "projectile");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    ImageIO.write(entry.getValue(), "PNG", out);
                    resources.put(key, out.toByteArray());
                } catch (IOException e) {
                    Log.throwing(getClass(), "getResources()", e, mod_Guns.class);
                }
            }
        }
        resources.put("shootSound", shootSoundButton.getBytes());
        resources.put("reloadSound", reloadSoundButton.getBytes());
        return resources;
    }

    @Override
    protected Map<String, int[]> getProperties() {
        HashMap<String, int[]> properties = new HashMap<String, int[]>();
        properties.put("itemGunId", ReverseBuffer.getInt(getGunId()));
        properties.put("itemProjectileId", ReverseBuffer.getInt(getProjectileId()));
        properties.put("versionCreated", ReverseBuffer.getString(VERSION));
        return properties;
    }

    @Override
    protected void setProjectileSpread(float spread) {
        projectileSpreadField.setText(String.valueOf(spread));
    }

    @Override
    protected void setZoom(float zoom) {
        zoomField.setText(String.valueOf(zoom));
    }

    @Override
    protected void setRange(float effectiveRange) {
        rangeField.setText(String.valueOf(effectiveRange));
    }

    @Override
    protected void setRoundsPerShot(int roundsPerShot) {
        roundsPerShotField.setText(String.valueOf(roundsPerShot));
    }

    @Override
    protected void setRoundsPerMinute(int roundsPerMinute) {
        roundsPerMinuteField.setText(String.valueOf(roundsPerMinute));
    }

    @Override
    protected void setGunId(int itemGunId) {
        gunIdField.setText(String.valueOf(itemGunId));
    }

    @Override
    protected void setProjectileId(int itemProjectileId) {
        projectileIdField.setText(String.valueOf(itemProjectileId));
    }

    @Override
    protected void setRecoilY(int recoilY) {
        recoilYField.setText(String.valueOf(recoilY));
    }

    @Override
    protected void setRecoilX(int recoilX) {
        recoilXField.setText(String.valueOf(recoilX));
    }

    @Override
    protected void setClipSize(int clipSize) {
        clipSizeField.setText(String.valueOf(clipSize));
    }

    @Override
    protected void setReloadTime(int reloadTime) {
        reloadField.setText(String.valueOf(reloadTime));
    }

    @Override
    protected void setShotsPerMinute(int shotsPerMinute) {
        shotsPerMinuteField.setText(String.valueOf(shotsPerMinute));
    }

    @Override
    protected void setDamage(int damage) {
        damageField.setText(String.valueOf(damage));
    }

    @Override
    protected void setScope(Scope scope) {
        this.scope.setSelectedItem(scope);
    }

    @Override
    protected void setFireMode(FireMode fireMode) {
        this.fireMode.setSelectedItem(fireMode);
    }

    @Override
    protected void setProjectileType(ProjectileType projectileType) {
        this.projectileType.setSelectedItem(projectileType);
    }

    @Override
    protected void setProjectileName(String name) {
        projectileNameField.setText(name);
    }

    @Override
    protected void setGunName(String name) {
        nameField.setText(name);
    }

    @Override
    protected void setShootSound(String shootSound) {
        shootSoundButton.setText(shootSound);
    }

    @Override
    protected void setReloadSound(String reloadSound) {
        reloadSoundButton.setText(reloadSound);
    }

    @Override
    protected void addProperty(String property, int[] value) {
        if(property.equals("itemGunId")) {
            setGunId(((value[0] & 0xFF) << 24) + ((value[1] & 0xFF) << 16) + ((value[2] & 0xFF) << 8) + (value[3] & 0xFF));
        } else if(property.equals("itemProjectileId") || property.equals("itemBulletId")) {
            setProjectileId(((value[0] & 0xFF) << 24) + ((value[1] & 0xFF) << 16) + ((value[2] & 0xFF) << 8) + (value[3] & 0xFF));
        }
    }

    @Override
    protected void addResource(String resource, byte[] value) {
        if(resource.contains("gun") || resource.contains("projectile")) {
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(value));
                texturePanel.setTexture(image, resource.contains("gun"));
            } catch (IOException e) {
                Log.throwing(getClass(), "addResource(String resource, byte[] value)", e, mod_Guns.class);
            }
        } else if(resource.contains("shootSound")) {
            shootSoundButton.updateButton(getShootSound(), value);
        } else if(resource.contains("reloadSound")) {
            reloadSoundButton.updateButton(getReloadSound(), value);
        }
    }
}
