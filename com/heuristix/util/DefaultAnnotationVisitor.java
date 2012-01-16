package com.heuristix.util;

import com.heuristix.asm.AnnotationVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/12/12
 * Time: 9:02 PM
 */
public class DefaultAnnotationVisitor implements AnnotationVisitor {

    public DefaultAnnotationVisitor(String desc, boolean visible) {
    }

    public DefaultAnnotationVisitor(String name, String desc) {
    }

    public DefaultAnnotationVisitor(String name) {
    }

    public DefaultAnnotationVisitor(int parameter, String desc, boolean visible) {
    }

    public DefaultAnnotationVisitor() {
    }

    public void visit(String name, Object value) {
    }

    public void visitEnum(String name, String desc, String value) {
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return new DefaultAnnotationVisitor(name, desc);
    }

    public AnnotationVisitor visitArray(String name) {
        return new DefaultAnnotationVisitor(name);
    }

    public void visitEnd() {
    }
}
