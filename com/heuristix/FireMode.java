package com.heuristix;

/**
* Created by IntelliJ IDEA.
* User: Matt
* Date: 10/15/11
* Time: 10:47 PM
*/
public enum FireMode {
    SINGLE, AUTOMATIC, BURST;

    public String toString() {
        return Util.normalize(name());
    }

}
