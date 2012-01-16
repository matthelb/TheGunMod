package com.heuristix.util;

import com.heuristix.asm.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/12/12
 * Time: 9:01 PM
 */
public class DefaultClassVisitor implements ClassVisitor {
    
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    }

    public void visitSource(String source, String debug) {
    }

    public void visitOuterClass(String owner, String name, String desc) {
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new DefaultAnnotationVisitor(desc, visible);
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return new DefaultFieldVisitor(access, name, desc, signature, value);
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new DefaultMethodVisitor(access, name, desc, signature, exceptions);
    }

    public void visitEnd() {
    }
}
