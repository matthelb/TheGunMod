package com.heuristix.guns.util;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 7/9/11
 * Time: 11:46 AM
 */
public final class ReverseBuffer {

    private ReverseBuffer() { }

    public static int getByte(int value) {
        return value & 0xFF;
    }

    public static int[] getShort(int value) {
        return new int[]{((value >> 8) & 0xFF), value & 0xFF};
    }

    public static int[] getInt(int value) {
        return new int[]{((value >> 24) & 0xFF), ((value >> 16) & 0xFF), ((value >> 8) & 0xFF), (value & 0xFF)};
    }

    public static int[] getLong(long value) {
        return new int[]{(int) ((value >> 32) & 0xFFFFFFFF), (int) (value & 0xFFFFFFFF)};
    }

    public static int[] getString(String value) {
        if (value == null) {
            return new int[]{10};
        }
        byte[] bytes = value.getBytes();
        int[] ints = new int[bytes.length + 1];
        for (int i = 0; i < bytes.length; i++) {
            ints[i] = getByte(bytes[i]);
        }
        ints[ints.length - 1] = 0xA;
        return ints;
    }

}
