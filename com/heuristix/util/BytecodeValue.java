package com.heuristix.util;

import com.heuristix.asm.Opcodes;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/18/11
 * Time: 12:28 AM
 */
public class BytecodeValue {

    private final Object value;
    private final int[] valueCode;
    private final int returnCode;

    public BytecodeValue(Object value) {
        this.value = value;
        if (value instanceof Float || value instanceof Double) {
            double realValue = Double.parseDouble(value.toString());
            int exponent = (int) ((Double.doubleToLongBits(realValue) >> 23) & 0xFF);
            if (exponent >= Float.MIN_EXPONENT && exponent <= Float.MAX_EXPONENT) {
                if (realValue == 0) {
                    valueCode = new int[1];
                    valueCode[0] = Opcodes.FCONST_0;
                } else if (realValue == 1) {
                    valueCode = new int[1];
                    valueCode[0] = Opcodes.FCONST_1;
                } else if (realValue == 2) {
                    valueCode = new int[1];
                    valueCode[0] = Opcodes.FCONST_2;
                } else {
                    valueCode = new int[2];
                    valueCode[0] = Opcodes.LDC;
                }
                returnCode = Opcodes.FRETURN;
            } else {
                valueCode = new int[3];
                valueCode[0] = Opcodes.LDC2_W;
                returnCode = Opcodes.DRETURN;
            }
        } else if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
            int realValue = Integer.parseInt(value.toString());
            if (realValue >= Byte.MIN_VALUE && realValue <= Byte.MAX_VALUE) {
                switch (realValue) {
                    case -1:
                        valueCode = new int[1];
                        valueCode[0] = Opcodes.ICONST_M1;
                        break;
                    case 0:
                        valueCode = new int[1];
                        valueCode[0] = Opcodes.ICONST_0;
                        break;
                    case 1:
                        valueCode = new int[1];
                        valueCode[0] = Opcodes.ICONST_1;
                        break;
                    case 2:
                        valueCode = new int[1];
                        valueCode[0] = Opcodes.ICONST_2;
                        break;
                    case 3:
                        valueCode = new int[1];
                        valueCode[0] = Opcodes.ICONST_3;
                        break;
                    case 4:
                        valueCode = new int[1];
                        valueCode[0] = Opcodes.ICONST_4;
                        break;
                    case 5:
                        valueCode = new int[1];
                        valueCode[0] = Opcodes.ICONST_5;
                        break;
                    default:
                        valueCode = new int[2];
                        valueCode[0] = Opcodes.BIPUSH;
                        valueCode[1] = realValue & 0xFF;
                        break;
                }
                returnCode = Opcodes.IRETURN;
            } else if (realValue >= Short.MIN_VALUE && realValue <= Short.MAX_VALUE) {
                valueCode = new int[3];
                valueCode[0] = Opcodes.SIPUSH;
                valueCode[1] = (realValue >> 8) & 0xFF;
                valueCode[2] = realValue & 0xFF;
                returnCode = Opcodes.IRETURN;
            } else if (realValue >= Integer.MIN_VALUE && realValue <= Integer.MAX_VALUE) {
                valueCode = new int[2];
                valueCode[0] = Opcodes.LDC;
                returnCode = Opcodes.IRETURN;
            } else {
                valueCode = new int[3];
                valueCode[0] = 0x14;
                returnCode = Opcodes.LRETURN;
            }
        } else {
            valueCode = new int[2];
            valueCode[0] = Opcodes.LDC;
            returnCode = Opcodes.ARETURN;
        }
    }

    public Object getValue() {
        return value;
    }

    public int[] getValueCode() {
        return valueCode;
    }

    public int getReturnCode() {
        return returnCode;
    }


}
