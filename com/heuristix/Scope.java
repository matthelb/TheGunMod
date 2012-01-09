package com.heuristix;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 10/7/11
 * Time: 3:01 PM
 */
public enum Scope {
    NONE("", 0),
    SNIPER("%blur%/heuristix/sniper_scope.png", 1),
    HOLOGRAPHIC_SIGHT("%blur%/heuristix/holographic.png", 2),
    ADVANCED_COMBAT_OPTICAL_GUNSIGHT("%blur%/heuristix/acog.png", 4);

    private final String texturePath;
    private int bit;

    private Scope(String texturePath, int bit) {
        this.texturePath = texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getBit() {
        return bit;
    }

    public String toString() {
        return Util.normalize(name());
    }
}
