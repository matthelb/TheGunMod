package com.heuristix.util;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 10/12/11
 * Time: 7:41 PM
 */
public class LoadingAgent implements ClassFileTransformer {

    private static BufferedWriter out;

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            out.write("Loaded: " + className + " with: " + loader.toString());
            out.newLine();
        } catch (IOException e) {

        }  finally {
            try {
                out.flush();
            } catch (IOException e) {
            }
        }
        return classfileBuffer;
    }

    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        out = new BufferedWriter(new FileWriter(new File("loaded.txt")));
        inst.addTransformer(new LoadingAgent());
    }
}
