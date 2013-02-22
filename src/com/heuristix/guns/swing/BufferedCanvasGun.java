package com.heuristix.guns.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.heuristix.guns.client.Resources;
import com.heuristix.guns.helper.ImageHelper;
import com.heuristix.guns.helper.MathHelper;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 8/27/12
 * Time: 9:04 AM
 */
public class BufferedCanvasGun extends BufferedCanvas implements DocumentListener, TextureChangedListener  {

	private static final long serialVersionUID = 6424666189688380842L;
	
	private static final Font SANS_SERIF = new Font("SansSerif", Font.PLAIN, 12);
	private static final DecimalFormat ROUNDER = new DecimalFormat("#.##");
	
	private BufferedImage[] background;
	private BufferedImage gun;

	private String name;
	private int damage, shotsPerMinute, reloadTime, reloadParts, clipSize, recoilX, recoilY, projectileId, gunId, roundsPerMinute, roundsPerShot;
	private float range, zoom, projectileSpread;
	
    public BufferedCanvasGun(int width, int height) {
        super(width, height);
        this.background = loadBackground();
    }
    
	@Override
    public void paint(Graphics g) {
		if (background == null) {
	        g.setColor(Color.GRAY);
	        g.fillRect(0, 0, getWidth(), getHeight());
		} else {
			for (int x = 0; x < getWidth(); x += 32) {
				for (int y = 0; y < getHeight(); y += 32) {
					BufferedImage image = background[y == 0 ? 0 : 1];
					g.drawImage(image, x, y, 32, 32, null);
				}
			}
		}
        if (gun != null) {
            g.drawImage(gun, getWidth() / 2 - gun.getWidth() / 2, getHeight() / 2 - gun.getHeight() / 2, null);
            g.setColor(Color.WHITE);
            g.setFont(SANS_SERIF.deriveFont(Font.BOLD, 32));
            int nameWidth = g.getFontMetrics().stringWidth(name);
            g.drawString(name, getWidth() / 2 - nameWidth / 2, g.getFontMetrics().getHeight());
            g.setFont(SANS_SERIF);
            g.drawString("Max DPS: " + ROUNDER.format(getMaxDPS()), 5, getHeight() - g.getFontMetrics().getHeight() - 5);
            g.drawString("Mean DPS: " + ROUNDER.format(getMeanDPS()), 5, getHeight() - g.getFontMetrics().getLeading() - 5);
            drawSpread(g, getWidth() - 65, getHeight() - 30, 60);
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
    	if (property != null && value != null && !value.isEmpty()) {
			try {
				Field propertyField = getClass().getDeclaredField(property);
				propertyField.setAccessible(true);
				Object trueValue;
				if (int.class.isAssignableFrom(propertyField.getType())) {
					trueValue = Integer.parseInt(value);
				} else if (float.class.isAssignableFrom(propertyField.getType())) {
					trueValue = Float.parseFloat(value);
				} else {
					trueValue = value;
				}
				propertyField.set(this, trueValue);
				repaint();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
    	} 
    }
    
    private double getMaxDPS() {
    	return damage * roundsPerShot * clipSize / (clipSize / (shotsPerMinute / 60D) + (reloadTime / 1000));
    }
    
    private double getMeanDPS() {
    	double maxDPS = getMaxDPS();
    	return Math.min((Math.atan2(1, range) / MathHelper.toRadians(projectileSpread)) * maxDPS, maxDPS);
    }
    
    private void drawSpread(Graphics g, int x, int y, int width) {
    	int[] xPoints = new int[]{x, x + width, x + width};
    	int deltaY = (int) Math.max(1, (width * Math.tan(MathHelper.toRadians(projectileSpread / 2))));
    	int[] yPoints = new int[]{y, y + deltaY, y - deltaY};
    	g.fillPolygon(xPoints, yPoints, xPoints.length);
    }
    
    private BufferedImage[] loadBackground() {
		InputStream is = getClass().getResourceAsStream("/terrain.png");
		if (is != null) {
			BufferedImage image = ImageHelper.readImage(is);
			if (image != null) {
				return new BufferedImage[]{image.getSubimage(48, 0, 16, 16), image.getSubimage(32, 0, 16, 16)};
			}
		}
		return null;
	}
    @Override
    public void textureChanged(DisplayableImageButton b, BufferedImage texture) {
    	if (gun == null || texture.getWidth() >= gun.getWidth()) {
	        texture = ImageHelper.resizeImage(texture, 160, 160);
	        AffineTransform af = AffineTransform.getTranslateInstance(0.5 * texture.getHeight(), 0.5 * texture.getWidth());
	        af.rotate(-Math.PI / 4);
	        af.translate(-0.3 * texture.getHeight(), -0.3 * texture.getWidth());
	        AffineTransformOp rotate = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
	        gun = new BufferedImage((int) (texture.getWidth() * 1.5), texture.getHeight(), texture.getType());
	        rotate.filter(texture, gun);
	        repaint();
    	}
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
