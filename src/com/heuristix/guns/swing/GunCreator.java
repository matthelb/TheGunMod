package com.heuristix.guns.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.minecraftforge.common.MinecraftForge;


import com.heuristix.TheGunMod;
import com.heuristix.guns.AbstractGunBridge;
import com.heuristix.guns.FireMode;
import com.heuristix.guns.Gun;
import com.heuristix.guns.ProjectileType;
import com.heuristix.guns.Scope;
import com.heuristix.guns.helper.IOHelper;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.ReverseBuffer;


/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/20/11
 * Time: 5:56 PM
 */
public class GunCreator extends AbstractGunBridge {

    public static final boolean MC_SRC_MOD = true;

    private final JFrame frame;

    private JTabbedPane tabbedPane;

    private final FileChooserCallback openCallback = new FileChooserCallback() {
        public void selectedFile(File file) {
            byte[] bytes = IOHelper.read(file);
            try {
                int newTab = JOptionPane.showConfirmDialog(frame, "Open gun in new tab?", "New tab?", JOptionPane.YES_NO_OPTION);
                if (newTab == JOptionPane.YES_OPTION) {
                    addNewTab(file.getName());
                } else {
                	tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), file.getName());
                }
                read(new Gun(bytes), true);
            } catch (Exception e) {
                Log.throwing(getClass(), "selectedFile(File file)", e, TheGunMod.class);
            }
        }
    };

    private final FileChooserCallback saveCallback = new FileChooserCallback() {
        public void selectedFile(File file) {
            String fileName = file.getName();
            int index = fileName.lastIndexOf('.');
            if (index > -1) {
                String extension = fileName.substring(index);
                if (!extension.toLowerCase().equals(".gun2")) {
                    fileName = fileName.substring(0, index) + "gun2";
                }
            } else {
                fileName = fileName + ".gun2";
            }
            file = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + fileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                if (out != null) {
                    write(out);
                }
            } catch (IOException e) {
                Log.throwing(getClass(), "selectedFile(File file)", e, TheGunMod.class);
            } finally {
            	tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), file.getName());
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        Log.throwing(getClass(), "selectedFile(File file)", e, TheGunMod.class);
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
        tabbedPane = new JTabbedPane();
        addNewTab("New gun");
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        final JMenuItem newMenuItem = new JMenuItem("New"), openMenuItem = new JMenuItem("Open"), saveMenuItem = new JMenuItem("Save"), closeMenuItem = new JMenuItem("Close");
        JFileChooser gunFileChooser = new JFileChooser(IOHelper.getHomeDirectory());
        gunFileChooser.setFileFilter(new FileNameExtensionFilter("GUN2 file", "gun2"));
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewTab("New gun");
            }
        });
        openMenuItem.addActionListener(new FileChooserActionListener(frame, openCallback, true, gunFileChooser));
        saveMenuItem.addActionListener(new FileChooserActionListener(frame, saveCallback, false, gunFileChooser));
        closeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int save = JOptionPane.showConfirmDialog(frame, "Save gun before closing tab?", "Save gun?", JOptionPane.YES_NO_OPTION);
                if (save == JOptionPane.YES_OPTION) {
                    for (ActionListener al : saveMenuItem.getActionListeners()) {
                        al.actionPerformed(e);
                    }
                }
                tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
            }
        });
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(closeMenuItem);
        menuBar.add(fileMenu);

        JMenu advancedMenu = new JMenu("Advanced");
        JMenuItem hdTextureItem = new JMenuItem("HD Textures");
        hdTextureItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((GunPanel) tabbedPane.getSelectedComponent()).textureDialog.setVisible(!((GunPanel) tabbedPane.getSelectedComponent()).textureDialog.isVisible());
            }
        });
        advancedMenu.add(hdTextureItem);
        menuBar.add(advancedMenu);

        frame.add(tabbedPane);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    private void addNewTab(String title) {
        GunPanel p = new GunPanel(frame);
        tabbedPane.addTab(title, p);
        tabbedPane.setSelectedComponent(p);
    }

    public void setFrameVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
    	MinecraftForge.GUN_CREATOR_SRC_MOD = true;
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    Log.throwing(getClass(), "main(String[] args)", e, TheGunMod.class);
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception e1) {
                        Log.throwing(getClass(), "main(String[] args)", e1, TheGunMod.class);
                    }
                }
                GunCreator gunCreator = new GunCreator();
                gunCreator.setFrameVisible(true);
            }
        });
    }

    @Override
    protected int getGunId() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).gunIdField.getText());
    }

    @Override
    protected int getProjectileId() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).projectileIdField.getText());
    }

    @Override
    protected String getShootSound() {
        return ((GunPanel) tabbedPane.getSelectedComponent()).shootSoundButton.getText();
    }

    @Override
    protected String getReloadSound() {
        return ((GunPanel) tabbedPane.getSelectedComponent()).reloadSoundButton.getText();
    }

    @Override
    protected int getRoundsPerShot() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).roundsPerShotField.getText());
    }

    @Override
    protected int getRoundsPerMinute() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).roundsPerMinuteField.getText());
    }

    @Override
    protected Scope getScope() {
        return (Scope) ((GunPanel) tabbedPane.getSelectedComponent()).scope.getSelectedItem();
    }

    @Override
    protected float getZoom() {
        return Float.parseFloat(((GunPanel) tabbedPane.getSelectedComponent()).zoomField.getText());
    }

    @Override
    protected int getRecoilY() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).recoilYField.getText());
    }

    @Override
    protected int getRecoilX() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).recoilXField.getText());
    }

    @Override
    protected int getClipSize() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).clipSizeField.getText());
    }

    @Override
    protected int getReloadTime() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).reloadField.getText());
    }

    @Override
    protected FireMode getFireMode() {
        return (FireMode) ((GunPanel) tabbedPane.getSelectedComponent()).fireMode.getSelectedItem();
    }

    @Override
    protected int getShotsPerMinute() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).shotsPerMinuteField.getText());
    }

    @Override
    protected String getGunName() {
        return ((GunPanel) tabbedPane.getSelectedComponent()).nameField.getText();
    }

    @Override
    protected String getProjectileName() {
        return ((GunPanel) tabbedPane.getSelectedComponent()).projectileNameField.getText();
    }

    @Override
    protected ProjectileType getProjectileType() {
        return (ProjectileType) ((GunPanel) tabbedPane.getSelectedComponent()).projectileType.getSelectedItem();
    }

    @Override
    protected float getSpread() {
        return Float.parseFloat(((GunPanel) tabbedPane.getSelectedComponent()).projectileSpreadField.getText());
    }

    @Override
    protected float getRange() {
        return Float.parseFloat(((GunPanel) tabbedPane.getSelectedComponent()).rangeField.getText());
    }

    @Override
    protected int getDamage() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).damageField.getText());
    }

    @Override
    protected int getReloadParts() {
        return Integer.parseInt(((GunPanel) tabbedPane.getSelectedComponent()).reloadPartsField.getText());
    }

    @Override
    protected Map<String, byte[]> getResources() {
        Map<String, byte[]> resources = new HashMap<String, byte[]>();
        for(int i = 0; i < 2; i++) {
            Map<Integer, BufferedImage> textures = ((GunPanel) tabbedPane.getSelectedComponent()).texturePanel.getTextures(i == 0);
            for(Map.Entry<Integer, BufferedImage> entry : textures.entrySet()) {
                String key = entry.getKey() + "x" + entry.getKey() + ((i == 0) ? "gun" : "projectile");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    ImageIO.write(entry.getValue(), "PNG", out);
                    resources.put(key, out.toByteArray());
                } catch (IOException e) {
                    Log.throwing(getClass(), "getResources()", e, TheGunMod.class);
                }
            }
        }
        resources.put("shootSound", ((GunPanel) tabbedPane.getSelectedComponent()).shootSoundButton.getBytes());
        resources.put("reloadSound", ((GunPanel) tabbedPane.getSelectedComponent()).reloadSoundButton.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            if (getScope().equals(Scope.CUSTOM)) {
                ImageIO.write(((GunPanel) tabbedPane.getSelectedComponent()).customScopeButton.getImage(), "PNG", out);
            }
            resources.put("customScope", out.toByteArray());
        } catch (IOException e) {
            Log.throwing(getClass(), "getResources()", e, TheGunMod.class);
        }
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
        ((GunPanel) tabbedPane.getSelectedComponent()).projectileSpreadField.setText(String.valueOf(spread));
    }

    @Override
    protected void setZoom(float zoom) {
        ((GunPanel) tabbedPane.getSelectedComponent()).zoomField.setText(String.valueOf(zoom));
    }

    @Override
    protected void setRange(float effectiveRange) {
        ((GunPanel) tabbedPane.getSelectedComponent()).rangeField.setText(String.valueOf(effectiveRange));
    }

    @Override
    protected void setRoundsPerShot(int roundsPerShot) {
        ((GunPanel) tabbedPane.getSelectedComponent()).roundsPerShotField.setText(String.valueOf(roundsPerShot));
    }

    @Override
    protected void setRoundsPerMinute(int roundsPerMinute) {
        ((GunPanel) tabbedPane.getSelectedComponent()).roundsPerMinuteField.setText(String.valueOf(roundsPerMinute));
    }

    @Override
    protected void setGunId(int itemGunId) {
        ((GunPanel) tabbedPane.getSelectedComponent()).gunIdField.setText(String.valueOf(itemGunId));
    }

    @Override
    protected void setProjectileId(int itemProjectileId) {
        ((GunPanel) tabbedPane.getSelectedComponent()).projectileIdField.setText(String.valueOf(itemProjectileId));
    }

    @Override
    protected void setRecoilY(int recoilY) {
        ((GunPanel) tabbedPane.getSelectedComponent()).recoilYField.setText(String.valueOf(recoilY));
    }

    @Override
    protected void setRecoilX(int recoilX) {
        ((GunPanel) tabbedPane.getSelectedComponent()).recoilXField.setText(String.valueOf(recoilX));
    }

    @Override
    protected void setClipSize(int clipSize) {
        ((GunPanel) tabbedPane.getSelectedComponent()).clipSizeField.setText(String.valueOf(clipSize));
    }

    @Override
    protected void setReloadTime(int reloadTime) {
        ((GunPanel) tabbedPane.getSelectedComponent()).reloadField.setText(String.valueOf(reloadTime));
    }

    @Override
    protected void setShotsPerMinute(int shotsPerMinute) {
        ((GunPanel) tabbedPane.getSelectedComponent()).shotsPerMinuteField.setText(String.valueOf(shotsPerMinute));
    }

    @Override
    protected void setDamage(int damage) {
        ((GunPanel) tabbedPane.getSelectedComponent()).damageField.setText(String.valueOf(damage));
    }

    @Override
    protected void setScope(Scope scope) {
        ((GunPanel) tabbedPane.getSelectedComponent()).scope.setSelectedItem(scope);
    }

    @Override
    protected void setFireMode(FireMode fireMode) {
        ((GunPanel) tabbedPane.getSelectedComponent()).fireMode.setSelectedItem(fireMode);
    }

    @Override
    protected void setProjectileType(ProjectileType projectileType) {
        ((GunPanel) tabbedPane.getSelectedComponent()).projectileType.setSelectedItem(projectileType);
    }

    @Override
    protected void setProjectileName(String name) {
        ((GunPanel) tabbedPane.getSelectedComponent()).projectileNameField.setText(name);
    }

    @Override
    protected void setGunName(String name) {
        ((GunPanel) tabbedPane.getSelectedComponent()).nameField.setText(name);
    }

    @Override
    protected void setShootSound(String shootSound) {
        ((GunPanel) tabbedPane.getSelectedComponent()).shootSoundButton.setText(shootSound);
    }

    @Override
    protected void setReloadSound(String reloadSound) {
        ((GunPanel) tabbedPane.getSelectedComponent()).reloadSoundButton.setText(reloadSound);
    }

    @Override
    protected void setReloadParts(int parts) {
        ((GunPanel) tabbedPane.getSelectedComponent()).reloadPartsField.setText(String.valueOf(parts));
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
                ((GunPanel) tabbedPane.getSelectedComponent()).texturePanel.setTexture(image, resource.contains("gun"));
            } catch (IOException e) {
                Log.throwing(getClass(), "addResource(String resource, byte[] value)", e, TheGunMod.class);
            }
        } else if(resource.contains("shootSound")) {
            ((GunPanel) tabbedPane.getSelectedComponent()).shootSoundButton.updateButton(getShootSound(), value);
        } else if(resource.contains("reloadSound")) {
            ((GunPanel) tabbedPane.getSelectedComponent()).reloadSoundButton.updateButton(getReloadSound(), value);
        } else if(resource.contains("customScope")) {
            try {
                ((GunPanel) tabbedPane.getSelectedComponent()).customScopeButton.updateImage(ImageIO.read(new ByteArrayInputStream(value)));
            } catch (IOException e) {
                Log.throwing(getClass(), "addResource(String resource, byte[] value)", e, TheGunMod.class);
            }
        }
    }
}
