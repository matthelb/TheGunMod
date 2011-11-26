package com.heuristix;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 10/7/11
 * Time: 3:01 PM
 */
public enum Scope {
    NONE(""),
    //RED_DOT("%blur%/heuristix/red_dot.png"),
    SNIPER("%blur%/heuristix/sniper_scope.png");

    private final String texturePath;

    private Scope(String texturePath) {
        this.texturePath = texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public String toString() {
        return Util.normalize(name());
    }
}
