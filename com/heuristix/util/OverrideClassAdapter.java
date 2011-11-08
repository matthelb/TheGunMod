package com.heuristix.util;

import com.heuristix.asm.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/17/11
 * Time: 12:42 PM
 */
public class OverrideClassAdapter extends ClassAdapter {

    private final Queue<int[]> constructorCode;
    private final HashMap<String, Object> code;
    private final String className;

    private final ClassWriter writer;

    private String superName;

    public OverrideClassAdapter(ClassWriter writer, String className, Queue<int[]> constructorCode, HashMap<String, Object> code) {
        super(writer);
        this.writer = writer;
        this.className = className;
        this.constructorCode = constructorCode;
        this.code = code;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.superName = name;
        if((access & Opcodes.ACC_ABSTRACT) != 0)
            access &= ~Opcodes.ACC_ABSTRACT;
        if((access & Opcodes.ACC_INTERFACE) != 0)
            access &= ~Opcodes.ACC_INTERFACE;
        cv.visit(version, access, className, signature, name, null);
    }

    @Override
    public void visitSource(String source, String debug) {
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        boolean isAbstract = (access & Opcodes.ACC_ABSTRACT) != 0;
        if(isAbstract)
            access &= ~Opcodes.ACC_ABSTRACT;
        MethodWriter mw = (MethodWriter) cv.visitMethod(access, name, desc, signature, exceptions);
        Object value;
        if(name.equals("<init>")) {
            value = constructorCode.poll();
            if(value != null) {
                for(int i = 0; i < ((int[])value).length; i++) {
                    if(((int[])value)[i] == 0) {
                        int index = writer.newMethod(superName, name, desc, false);
                        ((int[]) value)[i] = index >> 8;
                        ((int[]) value)[i+1] = index & 0xFF;
                    }
                }
            }
        } else {
            value = code.get(name);
        }
        if(isAbstract || value != null) {
            if(value instanceof BytecodeValue) {
                BytecodeValue returnableValue = (BytecodeValue) value;
                int[] byteCode = new int[returnableValue.getValueCode().length + 1];
                System.arraycopy(returnableValue.getValueCode(), 0, byteCode, 0, returnableValue.getValueCode().length);
                if(returnableValue.getValueCode().length > 1 && returnableValue.getValueCode()[1] == 0) {
                    byteCode[1] = writer.newConst(returnableValue.getValue());
                }
                byteCode[byteCode.length - 1] = returnableValue.getReturnCode();
                value = byteCode;
            }
            return new OverrideMethodAdapter(mw, (int[]) value);
        }
        return mw;
    }

    private class OverrideMethodAdapter extends MethodAdapter {

        private final int[] code;

        private final MethodWriter writer;

        public OverrideMethodAdapter(MethodWriter writer, int[] code) {
            super(writer);
            this.writer = writer;
            this.code = code;
        }

        @Override
        public void visitEnd() {
            try {
                Field code = MethodWriter.class.getDeclaredField("code");
                code.setAccessible(true);
                ByteVector bytes = new ByteVector();
                for(int b : this.code)
                    bytes.putByte(b);
                code.set(writer, bytes);
            } catch (Exception e) {
              e.printStackTrace();
            }
        }
    }

    public static byte[] extendClassBytes(Class clazz, String className, Queue<int[]> constructorCode, HashMap<String, Object> methodImpls) throws IOException {
        return extendClassBytes(new ClassReader(clazz), className, constructorCode, methodImpls);
    }

    public static byte[] extendClassBytes(byte[] classBytes, String className, Queue<int[]> constructorCode, HashMap<String, Object> methodImpls) throws IOException {
        return extendClassBytes(new ClassReader(classBytes), className, constructorCode, methodImpls);
    }

    private static byte[] extendClassBytes(ClassReader cr, String className, Queue<int[]> constructorCode, HashMap<String, Object> methodImpls) throws IOException {
        ClassWriter cw = new ClassWriter(0);
        cr.accept(new OverrideClassAdapter(cw, className, constructorCode, methodImpls), ClassReader.SKIP_DEBUG);
        cr = new ClassReader(cw.toByteArray());
        cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        //File file = new File(className + ".class");
        //new FileOutputStream(file).write(cw.toByteArray());
        cr.accept(cw, ClassReader.SKIP_DEBUG);
        //CheckClassAdapter.verify(new org.objectweb.asm.ClassReader(cw.toByteArray()), true, new PrintWriter(System.out));
        //file = new File(className + ".class");
        //new FileOutputStream(file).write(cw.toByteArray());
        return cw.toByteArray();
    }


    public static Class extendClass(Class clazz, String className, Queue<int[]> constructorCode, HashMap<String, Object> methodImpls) throws IOException {
        return defineClass(extendClassBytes(clazz, className, constructorCode, methodImpls), className, clazz.getClassLoader());
    }

    public static Class extendClass(byte[] classBytes, String className, Queue<int[]> constructorCode, HashMap<String, Object> methodImpls) throws IOException {
        return defineClass(extendClassBytes(classBytes, className, constructorCode, methodImpls), className);
    }

    private static Method methodDefineClass;

    public static Class defineClass(byte[] code, String name) {
        return defineClass(code, name, Thread.currentThread().getContextClassLoader());
    }

    public static Class defineClass(byte[] code, String name, ClassLoader cl) {
        try {
            if(methodDefineClass == null) {
                methodDefineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
                methodDefineClass.setAccessible(true);
            }
            return (Class) methodDefineClass.invoke(cl, name, code, 0, code.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
