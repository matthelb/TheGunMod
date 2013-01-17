package com.heuristix.guns.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import com.heuristix.guns.FireMode;
import com.heuristix.guns.ProjectileType;
import com.heuristix.guns.Scope;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/13/12
 * Time: 6:03 PM
 */
public class GunPanel extends JPanel {

    private static final Dimension COMPONENT_SIZE = new Dimension(100, 20);
    private static final NumberFormatter INTEGER_FORMATTER = new NumberFormatter(new DecimalFormat("#"));
    private static final NumberFormatter DECIMAL_FORMATTER = new NumberFormatter(new DecimalFormat("#.#"));

    private final JFrame parent;
    final BufferedCanvasGun canvas;

    DisplayableImageButton customScopeButton;
    DisplayableBytesButton shootSoundButton, reloadSoundButton;

    JTextField nameField, projectileNameField;
    JComboBox projectileType, fireMode, scope;
    JFormattedTextField damageField, rangeField, zoomField, shotsPerMinuteField, reloadField, clipSizeField, recoilXField, recoilYField, projectileIdField, gunIdField, roundsPerMinuteField, projectileSpreadField, roundsPerShotField, reloadPartsField;
    JDialog textureDialog, customScopeDialog;
    TexturePanel texturePanel;
    JPanel scopePanel;

    GunPanel(JFrame parent) {
        this.parent = parent;
        this.canvas = new BufferedCanvasGun(360, 240);
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        final JPanel northPanel = new JPanel(), westPanel = new JPanel(), centerPanel = new JPanel(), eastPanel = new JPanel(), southPanel = new JPanel();

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


        damageField = createFormattedTextField(0, COMPONENT_SIZE, integerFormatter, canvas, "damage");
        rangeField = createFormattedTextField(0, COMPONENT_SIZE, decimalFormatter, canvas, "range");
        shotsPerMinuteField = createFormattedTextField(0, COMPONENT_SIZE, integerFormatter, canvas, "shotsPerMinute");
        reloadField = createFormattedTextField(0, COMPONENT_SIZE, integerFormatter, canvas, "reloadTime");
        reloadPartsField = createFormattedTextField(1, COMPONENT_SIZE, integerFormatter, canvas, "reloadParts");
        clipSizeField = createFormattedTextField(0, COMPONENT_SIZE, integerFormatter, canvas, "clipSize");
        recoilXField = createFormattedTextField(0, COMPONENT_SIZE, integerFormatter, canvas, "recoilX");
        recoilYField = createFormattedTextField(0, COMPONENT_SIZE, integerFormatter, canvas, "recoilY");
        projectileIdField = createFormattedTextField(0, COMPONENT_SIZE, integerFormatter, canvas, "projectileId");
        gunIdField = createFormattedTextField(0, COMPONENT_SIZE, integerFormatter, canvas, "gunId");
        zoomField = createFormattedTextField(1.0f, COMPONENT_SIZE, decimalFormatter, canvas, "zoom");
        projectileSpreadField = createFormattedTextField(0.0f, COMPONENT_SIZE, decimalFormatter, canvas, "projectileSpread");
        roundsPerMinuteField = createFormattedTextField(0, COMPONENT_SIZE, integerFormatter, canvas, "roundsPerMinute");
        roundsPerShotField = createFormattedTextField(1, COMPONENT_SIZE, integerFormatter, canvas, "roundsPerShot");
        final JLabel roundsPerMinuteLabel = new JLabel("Rounds per minute: ");


        projectileNameField = new JTextField();
        projectileNameField.setPreferredSize(COMPONENT_SIZE);

        projectileType = new JComboBox(ProjectileType.values());
        projectileType.setPreferredSize(COMPONENT_SIZE);

        scopePanel = new JPanel();
        scope = new JComboBox(Scope.values());
        scope.setPreferredSize(new Dimension(COMPONENT_SIZE.width * 3 / 4, COMPONENT_SIZE.height));
        customScopeButton = new DisplayableImageButton();
        customScopeButton.setPreferredSize(new Dimension(360, 240));
        customScopeDialog = new JDialog(parent, "Custom Scope", true);
        customScopeDialog.setLayout(new BorderLayout());
        customScopeDialog.add(customScopeButton, BorderLayout.CENTER);
        JPanel dialogButtonPanel = new JPanel();
        dialogButtonPanel.setLayout(new FlowLayout());
        JButton dialogOkButton = new JButton("Ok");
        dialogOkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                customScopeDialog.setVisible(false);
            }
        });
        dialogButtonPanel.add(dialogOkButton);
        customScopeDialog.add(dialogButtonPanel, BorderLayout.SOUTH);
        customScopeDialog.add(new JLabel("Select an image to be used as a custom scope"), BorderLayout.NORTH);
        final JButton viewScopeDialogButton = new JButton("Choose Image");
        viewScopeDialogButton.setPreferredSize(new Dimension(COMPONENT_SIZE.width / 3, COMPONENT_SIZE.height));
        viewScopeDialogButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                customScopeDialog.setVisible(true);
            }
        });
        scope.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem().equals(Scope.CUSTOM)) {
                    scopePanel.add(viewScopeDialogButton);

                } else {
                    scopePanel.remove(viewScopeDialogButton);
                }
                parent.pack();
            }
        });
        scopePanel.add(scope);
        customScopeDialog.pack();

        fireMode = new JComboBox(FireMode.values());
        fireMode.setPreferredSize(COMPONENT_SIZE);
        fireMode.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem().equals(FireMode.BURST)) {
                    eastPanel.add(roundsPerMinuteLabel);
                    eastPanel.add(roundsPerMinuteField);
                } else {
                    eastPanel.remove(roundsPerMinuteField);
                    eastPanel.remove(roundsPerMinuteLabel);
                }
                parent.pack();
            }
        });

        texturePanel = new TexturePanel();
        texturePanel.addTextureChangedListener(true, canvas);
        textureDialog = new JDialog(parent, "Advanced Textures", true);
        textureDialog.setLayout(new BorderLayout());
        textureDialog.add(texturePanel, BorderLayout.CENTER);
        dialogButtonPanel = new JPanel();
        dialogButtonPanel.setLayout(new FlowLayout());
        dialogOkButton = new JButton("Ok");
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
        westPanel.add(new JLabel("Reload parts"));
        westPanel.add(reloadPartsField);


        eastPanel.add(new JLabel("Clip size: "));
        eastPanel.add(clipSizeField);
        eastPanel.add(new JLabel("Recoil: "));
        eastPanel.add(recoilYField);
        eastPanel.add(new JLabel("Horizontal recoil: "));
        eastPanel.add(recoilXField);
        eastPanel.add(new JLabel("Projectiles per shot: "));
        eastPanel.add(roundsPerShotField);

        eastPanel.add(new JLabel("Scope type: "));
        eastPanel.add(scopePanel);
        eastPanel.add(new JLabel("Scope zoom: "));
        eastPanel.add(zoomField);


        eastPanel.add(new JLabel("Fire mode: "));
        eastPanel.add(fireMode);

        JButton textureButton = new JButton("Textures");
        ActionListener textureActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textureDialog.setVisible(!textureDialog.isVisible());
            }
        };
        textureButton.addActionListener(textureActionListener);
        southPanel.setLayout(new GridLayout(0, 1, 10, 10));
        JPanel southPanel1 = new JPanel();
        southPanel1.add(textureButton);
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
        southPanel.add(southPanel1);
        southPanel.add(southPanel2);
        centerPanel.add(canvas);
        canvas.repaint();

        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
    }

    private JFormattedTextField createFormattedTextField(Object initialValue, Dimension size, JFormattedTextField.AbstractFormatterFactory factory, DocumentListener listener, String propertyHook) {
        JFormattedTextField field = new JFormattedTextField(initialValue);
        field.setPreferredSize(size);
        field.setFormatterFactory(factory);
        field.getDocument().addDocumentListener(listener);
        field.getDocument().putProperty("guns.property", propertyHook);
        return field;
    }
}
