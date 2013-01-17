package com.heuristix.guns.util;

import com.heuristix.guns.Gun;
import com.heuristix.guns.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/11/11
 * Time: 10:26 AM
 */
public final class CustomGunWriter {

    private CustomGunWriter() { }

    /**
     * @param args args 0 -> 2
     *             Class file bytes
     *             args 3 -> 5
     *             Resource bytes
     *             args 6 -> 8
     *             Class names
     *             args 9
     *             Item bullet ID
     *             args 10
     *             Item gun ID
     *             args 11
     *             Gun file name
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        if (args.length != 12) {
            System.out.println("Incorrect args amount");
        } else {
            byte[][] bytes = new byte[6][];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = Util.read(new File(args[i]));
            }
            List<Pair<String, byte[]>> classes = new LinkedList<Pair<String, byte[]>>();
            for (int i = 0; i < 3; i++) {
                classes.add(new Pair(args[i + 6], bytes[i]));
            }
            List<byte[]> resources = new LinkedList<byte[]>();
            for (int i = 3; i < 6; i++) {
                resources.add(bytes[i]);
            }
            Gun gun = null;
            try {
                gun = new Gun(new byte[4]);
            } catch (IllegalArgumentException e) {
            }
            Class<Gun> gunClass = Gun.class;
            Field gunClasses = gunClass.getDeclaredField("clazzes");
            gunClasses.setAccessible(true);
            Field gunResources = gunClass.getDeclaredField("resources");
            gunResources.setAccessible(true);
            Field gunId = gunClass.getDeclaredField("itemGunId");
            gunId.setAccessible(true);
            Field bulletId = gunClass.getDeclaredField("itemBulletId");
            bulletId.setAccessible(true);
            gunClasses.set(gun, classes);
            gunResources.set(gun, resources);
            bulletId.set(gun, Integer.parseInt(args[10]));
            gunId.set(gun, Integer.parseInt(args[9]));
            FileOutputStream out = new FileOutputStream(new File(args[args.length - 1] + ".gun2"));
            gun.write(out);
        }
    }
}
