package com.heuristix.guns.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;

import com.heuristix.guns.IMod;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/24/12
 * Time: 9:15 PM
 */
public class ModHandler extends FileHandler {

    private final IMod mod;

    public ModHandler(IMod mod) throws IOException {
        super(mod.getLogFile().getPath());
        this.mod = mod;
    }

    public IMod getMod() {
        return mod;
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        if(super.isLoggable(record) && record instanceof ModLogRecord) {
            for( Class<? extends IMod> m : ((ModLogRecord) record).getMods()) {
                if(getMod().getClass().equals(m)) {
                    return true;
                }
            }
        }
        return false;
    }
}
