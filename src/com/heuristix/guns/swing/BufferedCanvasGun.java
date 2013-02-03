package com.heuristix.guns.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.heuristix.guns.helper.ImageHelper;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 8/27/12
 * Time: 9:04 AM
 */
public class BufferedCanvasGun extends BufferedCanvas implements DocumentListener, TextureChangedListener  {

	private static final long serialVersionUID = 6424666189688380842L;
	
	private BufferedImage gun;

    public BufferedCanvasGun(int width, int height) {
        super(width, height);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (gun != null) {
            g.drawImage(gun, getWidth() / 2 - gun.getWidth() / 2, getHeight() / 2 - gun.getHeight() / 2, null);
        }
    }


    protected Rarity getRarity() {
        return Rarity.ARTIFACT;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        propertyChanged((String) e.getDocument().getProperty("guns.property"), getDocumentText(e.getDocument()));
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        propertyChanged((String) e.getDocument().getProperty("guns.property"), getDocumentText(e.getDocument()));
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        propertyChanged((String) e.getDocument().getProperty("guns.property"), getDocumentText(e.getDocument()));
    }

    private void propertyChanged(String property, String value) {
        switch (property) {
            case "damage":
                break;
            case "range":
                break;
            case "shotsPerMinute":
                break;
            case "reloadTime":
                break;
            case "reloadParts":
                break;
            case "clipSize":
                break;
            case "recoilX":
                break;
            case "recoilY":
                break;
            case "projectileId":
                break;
            case "gunId":
                break;
            case "zoom":
                break;
            case "projectileSpread":
                break;
            case "roundsPerMinute":
                break;
            case "roundsPerShot":
                break;
            default:
                break;
        }
        repaint();
    }

    @Override
    public void textureChanged(DisplayableImageButton b, BufferedImage texture) {
        texture = ImageHelper.resizeImage(texture, 160, 160);
        AffineTransform af = AffineTransform.getTranslateInstance(0.5 * texture.getHeight(), 0.5 * texture.getWidth());
        af.rotate(-Math.PI / 4 * 0.95);
        af.translate(-0.3 * texture.getHeight(), -0.3 * texture.getWidth());
        AffineTransformOp rotate = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
        gun = new BufferedImage((int) (texture.getWidth() * 1.5), texture.getHeight(), texture.getType());
        rotate.filter(texture, gun);
        repaint();
    }

    private static String getDocumentText(Document d) {
        try {
            return d.getText(0, d.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
