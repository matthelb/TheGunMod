package com.heuristix.util;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/12/12
 * Time: 8:41 PM
 */
public class InnerClass {

    private String name, outerName, innerName;
    private int access;

    public InnerClass(String name, String outerName, String innerName, int access) {
        this.name = name;
        this.outerName = outerName;
        this.innerName = innerName;
        this.access = access;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOuterName() {
        return outerName;
    }

    public void setOuterName(String outerName) {
        this.outerName = outerName;
    }

    public String getInnerName() {
        return innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

}
