package com.heuristix.util;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/9/11
 * Time: 4:20 PM
 */
public class Method {

    private int access;
    private String name;
    private String desc;
    private String signature;
    private String[] exceptions;
    private Object code;

    public Method(String desc) {
        this(desc, null);
    }

    public Method(Object code) {
        this(null, code);
    }

    public Method(String desc, Object code) {
        this(null, desc, code);
    }

    public Method(String name, String desc, Object code) {
        this(name, desc, null, null, code);
    }

    public Method(String name, String desc, String signature, String[] exceptions, Object code) {
        this(-1, name, desc, signature, exceptions, code);
    }

    public Method(int access, String name, String desc, String signature, String[] exceptions, Object code) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
        this.code = code;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String[] getExceptions() {
        return exceptions;
    }

    public void setExceptions(String[] exceptions) {
        this.exceptions = exceptions;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }
}
