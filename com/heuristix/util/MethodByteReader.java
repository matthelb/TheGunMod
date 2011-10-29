package com.heuristix.util;

import com.heuristix.Utilities;
import com.heuristix.test.EntityGrenade;
import com.heuristix.asm.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 10/23/11
 * Time: 6:24 PM
 */
public class MethodByteReader extends ClassAdapter {

    private final HashMap<String, byte[]> methodBytes;

    public MethodByteReader(final ClassWriter cw) {
        super(cw);
        this.methodBytes = new HashMap<String, byte[]>();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodWriter mw = (MethodWriter) super.visitMethod(access, name, desc, signature, exceptions);
        Field code = null;
        try {
            code = MethodWriter.class.getDeclaredField("code");
            code.setAccessible(true);
            ByteVector byteVector = (ByteVector) code.get(mw);
            code = ByteVector.class.getDeclaredField("data");
            code.setAccessible(true);
            methodBytes.put(name + "+" + signature, (byte[]) code.get(byteVector));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return mw;
    }

    public HashMap<String, byte[]> getMethodBytes() {
        return methodBytes;
    }

    public static void printClassMethods(Class clazz) {
        try {
            ClassReader reader = new ClassReader(clazz);
            MethodByteReader mbr = new MethodByteReader(new ClassWriter(0));
            reader.accept(mbr, 0);
            for(Map.Entry<String, byte[]> entry : mbr.getMethodBytes().entrySet()) {
                System.out.println(entry.getKey() + " : " + Arrays.toString(entry.getValue()).replace(" ", ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        //printClassMethods(EntityGrenade.class);
        new FileOutputStream(new File("Test.class")).write(Utilities.parseByteArray("-54,-2,-70,-66,0,0,0,50,0,28,1,0,16,69,110,116,105,116,121,77,50,48,51,66,117,108,108,101,116,7,0,1,1,0,30,99,111,109,47,104,101,117,114,105,115,116,105,120,47,69,110,116,105,116,121,66,117,108,108,101,116,66,97,115,101,7,0,3,1,0,6,60,105,110,105,116,62,1,0,60,40,76,110,101,116,47,109,105,110,101,99,114,97,102,116,47,115,114,99,47,87,111,114,108,100,59,76,110,101,116,47,109,105,110,101,99,114,97,102,116,47,115,114,99,47,69,110,116,105,116,121,76,105,118,105,110,103,59,41,86,1,0,26,99,111,109,47,104,101,117,114,105,115,116,105,120,47,69,110,116,105,116,121,66,117,108,108,101,116,7,0,7,12,0,5,0,6,10,0,8,0,9,1,0,9,103,101,116,68,97,109,97,103,101,1,0,3,40,41,73,1,0,17,103,101,116,69,102,102,101,99,116,105,118,101,82,97,110,103,101,1,0,3,40,41,70,4,66,-56,0,0,1,0,9,103,101,116,83,112,114,101,97,100,1,0,8,103,101,116,83,112,101,101,100,12,0,17,0,14,10,0,8,0,18,1,0,7,103,101,116,77,97,115,115,12,0,20,0,14,10,0,8,0,21,1,0,5,111,110,72,105,116,1,0,29,40,76,110,101,116,47,109,105,110,101,99,114,97,102,116,47,115,114,99,47,69,110,116,105,116,121,59,41,90,12,0,23,0,24,10,0,8,0,25,1,0,4,67,111,100,101,0,33,0,2,0,4,0,0,0,0,0,7,0,1,0,5,0,6,0,1,0,27,0,0,0,19,0,3,0,3,0,0,0,7,42,43,44,-73,0,10,-79,0,0,0,0,0,1,0,11,0,12,0,1,0,27,0,0,0,14,0,1,0,1,0,0,0,2,7,-84,0,0,0,0,0,1,0,13,0,14,0,1,0,27,0,0,0,15,0,1,0,1,0,0,0,3,18,15,-82,0,0,0,0,0,1,0,16,0,14,0,1,0,27,0,0,0,14,0,1,0,1,0,0,0,2,11,-82,0,0,0,0,0,1,0,17,0,14,0,1,0,27,0,0,0,14,0,1,0,1,0,0,0,2,12,-82,0,0,0,0,0,1,0,20,0,14,0,1,0,27,0,0,0,15,0,1,0,1,0,0,0,3,18,48,-82,0,0,0,0,0,1,0,23,0,24,0,1,0,27,0,0,0,50,0,2,0,2,0,0,0,38,43,-58,0,9,42,43,-73,0,19,-84,42,-76,0,22,42,42,-76,0,26,42,-76,0,29,42,-76,0,32,42,-74,0,36,-122,-74,0,42,87,4,-84,0,0,0,0,0,0"));
    }

}
