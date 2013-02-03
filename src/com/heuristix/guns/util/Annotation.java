package com.heuristix.guns.util;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/12/12
 * Time: 8:37 PM
 */
public class Annotation {

    private String desc;
    private boolean visible;

    public Annotation(String desc, boolean visible) {
        this.desc = desc;
        this.visible = visible;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
