package com.heuristix.guns.util;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.heuristix.guns.IMod;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/24/12
 * Time: 9:21 PM
 */
public class ModLogRecord extends LogRecord {

	private static final long serialVersionUID = -1257847293904411992L;
	private final  Class<? extends IMod>[] mods;

    public ModLogRecord(Level level, String msg,  Class<? extends IMod>... mod) {
        super(level, msg);
        this.mods = mod;
    }

    public  Class<? extends IMod>[] getMods() {
        return mods;
    }
}
