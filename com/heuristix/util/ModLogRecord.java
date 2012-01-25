package com.heuristix.util;

import com.heuristix.Mod;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/24/12
 * Time: 9:21 PM
 */
public class ModLogRecord extends LogRecord {

    private final  Class<? extends Mod>[] mods;

    public ModLogRecord(Level level, String msg,  Class<? extends Mod>... mod) {
        super(level, msg);
        this.mods = mod;
    }

    public  Class<? extends Mod>[] getMods() {
        return mods;
    }
}
