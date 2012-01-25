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

    private final Mod[] mods;

    public ModLogRecord(Level level, String msg, Mod... mod) {
        super(level, msg);
        this.mods = mod;
    }

    public Mod[] getMods() {
        return mods;
    }
}
