package com.heuristix.guns.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.heuristix.guns.asm.AnnotationVisitor;
import com.heuristix.guns.asm.Attribute;
import com.heuristix.guns.asm.ClassReader;
import com.heuristix.guns.asm.ClassVisitor;
import com.heuristix.guns.asm.FieldVisitor;
import com.heuristix.guns.asm.MethodVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/12/12
 * Time: 8:29 PM
 */
public class ClassDescriptor extends ClassVisitor {

    private int version, access;
    private String name, signature, superName, source, debug, owner, outerName, outerDesc;
    private String[] interfaces;

    private Map<String, Method> methods;
    private Map<String, Field> fields;
    private Map<String, InnerClass> innerClasses;

    private List<Annotation> annotations;
    private List<Attribute> attributes;

    public ClassDescriptor(int api, final ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        this.interfaces = interfaces;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitSource(String source, String debug) {
        this.source = source;
        this.debug = debug;
        super.visitSource(source, debug);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        this.owner = owner;
        this.outerName = name;
        this.outerDesc = desc;
        super.visitOuterClass(owner, name, desc);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        Annotation annotation = new Annotation(desc, visible);
        if(annotations == null) {
            annotations = new LinkedList<Annotation>();
        }
        annotations.add(annotation);
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitAttribute(Attribute attr) {
        if(attributes == null) {
            attributes = new LinkedList<Attribute>();
        }
        attributes.add(attr);
        super.visitAttribute(attr);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        InnerClass innerClass = new InnerClass(name, outerName, innerName, access);
        if(innerClasses == null) {
            innerClasses = new HashMap<String, InnerClass>();
        }
        innerClasses.put(name, innerClass);
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        Field field = new Field(access, name, desc, signature, value);
        if(fields == null) {
            fields = new HashMap<String, Field>();
        }
        fields.put(name+desc, field);
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Method method = new Method(access, name, desc, signature, exceptions);
        if(methods == null) {
            methods = new HashMap<String, Method>();
        }
        methods.put(name+desc, method);
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    public int getVersion() {
        return version;
    }

    public int getAccess() {
        return access;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public String getSuperName() {
        return superName;
    }

    public String getSource() {
        return source;
    }

    public String getDebug() {
        return debug;
    }

    public String getOwner() {
        return owner;
    }

    public String getOuterName() {
        return outerName;
    }

    public String getOuterDesc() {
        return outerDesc;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public Map<String, InnerClass> getInnerClasses() {
        return innerClasses;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public static ClassDescriptor getClassDescription(int api, Class clazz) throws IOException {
        return getClassDescription(api, clazz, new ClassVisitor(api), 0);
    }

    public static ClassDescriptor getClassDescription(int api, Class clazz, ClassVisitor visitor, int flags) throws IOException {
        ClassDescriptor cd = new ClassDescriptor(api, visitor);
        ClassReader cr = new ClassReader(clazz.getName());
        cr.accept(cd, flags);
        return cd;
    }

    public static ClassDescriptor getClassDescription(int api, byte[] bytes) throws IOException {
        return getClassDescription(bytes, api, new ClassVisitor(api), 0);
    }

    public static ClassDescriptor getClassDescription(byte[] bytes, int api, ClassVisitor visitor, int flags) throws IOException {
        ClassDescriptor cd = new ClassDescriptor(api, visitor);
        ClassReader cr = new ClassReader(bytes);
        cr.accept(cd, flags);
        return cd;
    }
}
