package com.heuristix.util;

import com.heuristix.asm.ClassWriter;
import com.heuristix.asm.Opcodes;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/10/11
 * Time: 4:01 PM
 */
public class InvokeMethod {

    private final int[] pushStack, postInvoke;

    private final String owner, name, desc;
    private final boolean itf, special, stc;

    private int index;

    public InvokeMethod(int[] pushStack, int[] postInvoke, String owner, String name, String desc, boolean itf, boolean special, boolean stc) {
        this.pushStack = pushStack;
        this.postInvoke = postInvoke;
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.itf = itf;
        this.special = special;
        this.stc = stc;
        this.index = -1;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(ClassWriter cw) {
        index = cw.newMethod(owner, name, desc, itf);
    }

    public int[] getBytecode() {
        int[] bytes = new int[3 + postInvoke.length + pushStack.length];
        System.arraycopy(pushStack, 0, bytes, 0, pushStack.length);
        bytes[pushStack.length + 0] = (itf) ? Opcodes.INVOKEINTERFACE : (stc) ? Opcodes.INVOKESTATIC : (special) ? Opcodes.INVOKESPECIAL : Opcodes.INVOKEVIRTUAL;
        bytes[pushStack.length + 1] = (getIndex() >> 8) & 0xFF;
        bytes[pushStack.length + 2] = getIndex() & 0xFF;
        System.arraycopy(postInvoke, 0, bytes, pushStack.length + 3, postInvoke.length);
        return bytes;
    }

}
