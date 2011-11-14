package com.heuristix.util;

/**
* Created by IntelliJ IDEA.
* User: Matt
* Date: 11/9/11
* Time: 4:20 PM
*/
public class Method {
    public int access;
    public String name;
    public String desc;
    public String signature;
    public String[] exceptions;
    public Object code;

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
}
