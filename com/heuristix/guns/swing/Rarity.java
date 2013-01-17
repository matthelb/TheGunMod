package com.heuristix.guns.swing;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 8/30/12
 * Time: 7:28 PM
 */
public enum Rarity {
    POOR(new Color(0x9D9D9D)),
    COMMON(new Color(0xFFFFFF)),
    UNCOMMON(new Color(0x1EFF00)),
    RARE(new Color(0x0070DD)),
    EPIC(new Color(0xA355EE)),
    LEGENDARY(new Color(0xFF8000)),
    ARTIFACT(new Color(0xE6CC80));

    private final Color color;

    private Rarity(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
