package com.heuristix.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/23/12
 * Time: 4:32 PM
 */
public final class ObfuscatedNames {

    private static ObfuscatedNames instance;

    private final Map<Class, String> names;

    private final Map<Class, Map<String, java.lang.reflect.Field>> fields;
    private final Map<Class, Map<String, java.lang.reflect.Method>> methods;

    private ObfuscatedNames() {
        this.names = new HashMap<Class, String>();
        this.fields = new HashMap<Class, Map<String, java.lang.reflect.Field>>();
        this.methods = new HashMap<Class, Map<String, java.lang.reflect.Method>>();
    }

    public static ObfuscatedNames getInstance() {
        if(instance == null) {
            instance = new ObfuscatedNames();
        }
        return instance;
    }

    public Object getFieldValue(Class clazz, Object o, String name) {
        java.lang.reflect.Field field = getField(clazz, name);
        if(field != null) {
            try {
                return field.get(o);
            } catch (IllegalAccessException e) {
                Log.getLogger().fine("Not allowed to access " + Modifier.toString(field.getModifiers()) + " field value " + field.getDeclaringClass().getName() + "+" + field.getName());
            }
        }
        return null;
    }

    public void setFieldValue(Class clazz, Object o, String name, Object value) {
        java.lang.reflect.Field field = getField(clazz, name);
        if(field != null) {
            try {
                field.set(o, value);
            } catch (IllegalAccessException e) {
                Log.getLogger().fine("Not allowed to access " + Modifier.toString(field.getModifiers()) + " field value " + field.getDeclaringClass().getName() + "+" + field.getName());
            }
        }
    }

    public Object invokeMethod(Class clazz, Object o, String name, Object... params) {
        java.lang.reflect.Method method = getMethod(clazz, name);
        if(method != null) {
            try {
                return method.invoke(o, params);
            } catch (IllegalAccessException e) {
               Log.getLogger().fine("Not allowed to invoke " + Modifier.toString(method.getModifiers()) + " method " + method.getDeclaringClass().getName() + "+" + method.getName());
            } catch (InvocationTargetException e) {
               Log.getLogger().fine("Could not invoke " + method.getDeclaringClass().getName() + "+" + method.getName());
            }
        }
        return null;
    }

    public java.lang.reflect.Field getField(Class clazz, String name) {
        Map<String, java.lang.reflect.Field> classFields = fields.get(clazz);
        if(classFields != null) {
            return classFields.get(name);
        }
        return null;
    }

    public java.lang.reflect.Method getMethod(Class clazz, String name) {
        Map<String, java.lang.reflect.Method> classMethods = methods.get(clazz);
        if(classMethods != null) {
            return classMethods.get(name);
        }
        return null;
    }

     public String getName(Class clazz) {
        return names.get(clazz);
    }

    public java.lang.reflect.Method putMethod(Class clazz, String name, String obfuscatedName, Class... params) {
        Map<String, java.lang.reflect.Method> classMethods = methods.get(clazz);
        if(classMethods == null) {
            classMethods = new HashMap<String, java.lang.reflect.Method>();
        }
        java.lang.reflect.Method method = null;
        try {
            try {
                method = clazz.getDeclaredMethod(obfuscatedName, params);
            } catch (NoSuchMethodException e) {
                try {
                    method = clazz.getDeclaredMethod(name, params);
                } catch (NoSuchMethodException e1) {
                    Log.getLogger().fine("Method (" + name + "|" + obfuscatedName + ") not found in Class " + clazz.getName());
                    return null;
                }
            }
        method.setAccessible(true);
        classMethods.put(name, method);
        methods.put(clazz, classMethods);
        } catch (NoClassDefFoundError e) {
            Log.getLogger().fine("Class not found " + clazz);
        }
        return method;
    }

     public java.lang.reflect.Field putField(Class clazz, String name, String obfuscatedName) {
        Map<String, java.lang.reflect.Field> classFields = fields.get(clazz);
        if(classFields == null) {
            classFields = new HashMap<String, java.lang.reflect.Field>();
        }
        java.lang.reflect.Field field = null;
        try {
            try {
                field = clazz.getDeclaredField(obfuscatedName);
            } catch (NoSuchFieldException e) {
                try {
                    field = clazz.getDeclaredField(name);
                } catch (NoSuchFieldException e1) {
                    Log.getLogger().fine("Field (" + name + "|" + obfuscatedName + ") not found in Class " + clazz.getName());
                    return null;
                }
            }
            field.setAccessible(true);
            classFields.put(name, field);
            fields.put(clazz, classFields);
        } catch (NoClassDefFoundError e) {
            Log.getLogger().fine("Class not found " + clazz);
        }
        return field;
    }

    public void putName(Class clazz, String obfuscatedName) {
        names.put(clazz, obfuscatedName);
    }

}