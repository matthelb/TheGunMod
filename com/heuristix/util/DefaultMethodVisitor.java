package com.heuristix.util;

import com.heuristix.asm.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/12/12
 * Time: 9:05 PM
 */
public class DefaultMethodVisitor implements MethodVisitor {
    public DefaultMethodVisitor(int access, String name, String desc, String signature, String[] exceptions) {
    }

    public AnnotationVisitor visitAnnotationDefault() {
        return new DefaultAnnotationVisitor();
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new DefaultAnnotationVisitor(desc, visible);
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        return new DefaultAnnotationVisitor(parameter, desc, visible);
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitCode() {
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
    }

    public void visitInsn(int opcode) {
    }

    public void visitIntInsn(int opcode, int operand) {
    }

    public void visitVarInsn(int opcode, int var) {
    }

    public void visitTypeInsn(int opcode, String type) {
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
    }

    public void visitInvokeDynamicInsn(String name, String desc, MethodHandle bsm, Object... bsmArgs) {
    }

    public void visitJumpInsn(int opcode, Label label) {
    }

    public void visitLabel(Label label) {
    }

    public void visitLdcInsn(Object cst) {
    }

    public void visitIincInsn(int var, int increment) {
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
    }

    public void visitLineNumber(int line, Label start) {
    }

    public void visitMaxs(int maxStack, int maxLocals) {
    }

    public void visitEnd() {
    }
}
