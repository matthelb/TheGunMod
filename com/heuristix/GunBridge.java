package com.heuristix;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 2/1/12
 * Time: 6:06 PM
 */
public interface GunBridge {

    void read(Gun gun, boolean deobfuscate);

    void write(OutputStream out) throws IOException;

}
