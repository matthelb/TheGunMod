package com.heuristix.util;

import com.heuristix.Mod;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/24/12
 * Time: 9:15 PM
 */
public class ModHandler extends FileHandler {

    private final Mod mod;

    public ModHandler(Mod mod) throws IOException {
        super(mod.getLogFile().getPath());
        this.mod = mod;
    }

    public Mod getMod() {
        return mod;
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        if(super.isLoggable(record) && record instanceof ModLogRecord) {
            for(Mod m : ((ModLogRecord) record).getMods()) {
                if(getMod().equals(m)) {
                    return true;
                }
            }
        }
        return false;
    }
}
