package com.heuristix.util;

import com.heuristix.Gun;
import com.heuristix.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/30/11
 * Time: 5:21 PM
 */
public final class GunDumper {

    private GunDumper() { }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Incorrect args length");
            System.exit(1);
        }
        File inDir = new File(args[0]);
        File outDir = new File(args[1]);
        outDir.mkdirs();
        for (File f : inDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".gun2");
            }
        })) {
            try {
                Gun gun = new Gun(Util.read(f));
                for (Pair<String, byte[]> clazz : gun.getClasses()) {
                    File dir = new File(outDir.getAbsolutePath() + File.separator + f.getName());
                    dir.mkdirs();
                    FileOutputStream out = new FileOutputStream(new File(dir.getAbsolutePath() + File.separator + clazz.getFirst() + ".class"));
                    out.write(clazz.getSecond());
                }
                for (int i = 0; i < gun.getResources().size(); i++) {
                    String name = "";
                    switch (i) {
                        case 0:
                            name = "projectile-icon.png";
                            break;
                        case 1:
                            name = "gun-icon.png";
                            break;
                        case 2:
                            name = "shoot-sound.ogg";
                            break;
                        case 3:
                            name = "reload-sound.ogg";
                            break;
                        default:
                            break;
                    }
                    File dir = new File(outDir.getAbsolutePath() + File.separator + f.getName());
                    dir.mkdirs();
                    FileOutputStream out = new FileOutputStream(new File(dir.getAbsolutePath() + File.separator + name));
                    out.write(gun.getResources().get(i));
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Could not load gun file: " + f.toString());
            }
        }
    }
}
