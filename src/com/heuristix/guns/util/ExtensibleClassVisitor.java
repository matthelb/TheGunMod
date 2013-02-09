package com.heuristix.guns.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import com.heuristix.guns.Util;
import com.heuristix.guns.asm.ByteVector;
import com.heuristix.guns.asm.ClassReader;
import com.heuristix.guns.asm.ClassVisitor;
import com.heuristix.guns.asm.ClassWriter;
import com.heuristix.guns.asm.MethodVisitor;
import com.heuristix.guns.asm.MethodWriter;
import com.heuristix.guns.asm.Opcodes;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/17/11
 * Time: 12:42 PM
 */
public class ExtensibleClassVisitor extends ClassVisitor {

    private final Map<String, Method> methods;
    private final String className;
    private final boolean extend;

    private final ClassWriter writer;

    public ExtensibleClassVisitor(int api, ClassWriter writer, String className, Map<String, Method> methods, boolean extend) {
        super(api, writer);
        this.writer = writer;
        this.className = className;
        this.methods = methods;
        this.extend = extend;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            access &= ~Opcodes.ACC_ABSTRACT;
        }
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            access &= ~Opcodes.ACC_INTERFACE;
        }
        cv.visit(version, access, className, signature, (extend) ? name : superName, null);
    }

    @Override
    public void visitSource(String source, String debug) {
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Method method = methods.get(name + desc);
        MethodVisitor mv = null;
        if (method != null) {
            mv = cv.visitMethod((method.getAccess() == -1) ? access : method.getAccess(), (method.getName() == null) ? name : method.getName(),
                    (method.getDesc() == null) ? desc : method.getDesc(), (method.getSignature() == null) ? signature : method.getSignature(),
                    (method.getExceptions() == null) ? exceptions : method.getExceptions());
            Object code = method.getCode();
            if (code != null) {
                if (code instanceof BytecodeValue) {
                    BytecodeValue returnableValue = (BytecodeValue) code;
                    int[] valueCode = returnableValue.getValueCode();
                    int[] byteCode = new int[valueCode.length + 1];
                    System.arraycopy(valueCode, 0, byteCode, 0, valueCode.length);
                    if (valueCode.length > 0 && (valueCode[0] == Opcodes.LDC || valueCode[0] == Opcodes.LDC2_W)) {
                        byteCode[1] = writer.newConst(returnableValue.getValue());
                    }
                    byteCode[byteCode.length - 1] = returnableValue.getReturnCode();
                    code = byteCode;
                } else if (code instanceof InvokeMethod) {
                    InvokeMethod invokeMethod = (InvokeMethod) code;
                    invokeMethod.setIndex(writer);
                    code = invokeMethod.getBytecode();
                }
                return new OverrideMethodVisitor(api, (MethodWriter) mv, (int[]) code);
            }
        } else {
            mv = cv.visitMethod(access, name, desc, signature, exceptions);
        }
        return mv;
    }

    private class OverrideMethodVisitor extends MethodVisitor {

        private final int[] code;

        private final MethodWriter writer;

        public OverrideMethodVisitor(int api, MethodWriter writer, int[] code) {
            super(api, writer);
            this.writer = writer;
            this.code = code;
        }

        @Override
        public void visitEnd() {
            try {
                Field code = MethodWriter.class.getDeclaredField("code");
                code.setAccessible(true);
                ByteVector bytes = new ByteVector();
                for (int b : this.code) {
                    bytes.putByte(b);
                }
                code.set(writer, bytes);
            } catch (Exception e) {
                Log.getLogger().throwing(getClass().getName(), "updateFile(File file)", e);
            }
        }
    }

    public static byte[] modifyClassBytes(int api, Class<?> clazz, String className, Map<String, Method> methods, boolean extend) throws IOException {
        return modifyClassBytes(api, new ClassReader(clazz.getClassLoader().getResourceAsStream(clazz.getName().replace('.', '/') + ".class")), className, methods, extend);
    }

    public static byte[] modifyClassBytes(int api, byte[] classBytes, String className, Map<String, Method> methods, boolean extend) throws IOException {
        return modifyClassBytes(api, new ClassReader(classBytes), className, methods, extend);
    }

    private static byte[] modifyClassBytes(int api, ClassReader cr, String className, Map<String, Method> methods, boolean extend) throws IOException {
        ClassWriter cw = new ClassWriter(0);
        cr.accept(new ExtensibleClassVisitor(api, cw, className, methods, extend), ClassReader.SKIP_DEBUG);
        cr = new ClassReader(cw.toByteArray());
        cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cr.accept(cw, ClassReader.SKIP_DEBUG);
        //org.objectweb.asm.util.CheckClassAdapter.verify(new org.objectweb.asm.ClassReader(cw.toByteArray()), false, new PrintWriter(System.out));
        java.io.File file = new java.io.File(className + ".class");
        new java.io.FileOutputStream(file).write(cw.toByteArray());
        return cw.toByteArray();
    }


    public static Class<?> modifyClass(int api, Class<?> clazz, String className, Map<String, Method> methods, boolean extend) throws IOException {
        return Util.defineClass(modifyClassBytes(api, clazz, className, methods, extend), className, clazz.getClassLoader());
    }

    public static Class<?> modifyClass(int api, byte[] classBytes, String className, Map<String, Method> methods, boolean extend) throws IOException {
        return Util.defineClass(modifyClassBytes(api, classBytes, className, methods, extend), className);
    }

}
