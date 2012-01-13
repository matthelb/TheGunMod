package com.heuristix.util;

import com.heuristix.asm.AnnotationVisitor;
import com.heuristix.asm.Attribute;
import com.heuristix.asm.FieldVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/12/12
 * Time: 9:04 PM
 */
public class DefaultFieldVisitor implements FieldVisitor {
    public DefaultFieldVisitor(int access, String name, String desc, String signature, Object value) {
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new DefaultAnnotationVisitor(desc, visible);
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitEnd() {
    }
}
