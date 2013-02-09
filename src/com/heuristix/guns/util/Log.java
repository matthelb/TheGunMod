package com.heuristix.guns.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.heuristix.TheGunMod;
import com.heuristix.guns.IMod;
import com.heuristix.guns.helper.IOHelper;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/24/12
 * Time: 9:04 PM
 */
public class Log {

	public static final boolean DEBUG = TheGunMod.DEBUG;
	
    private static File defaultLog = IOHelper.getHeuristixFile("", "log.txt");

    private static Logger logger;
    private static boolean init;

    private Log() { }

    public static Logger getLogger() {
        if(logger == null) {
            logger = Logger.getLogger(Log.class.toString());
        } else if(defaultLog != null && !init) {
            try {
                Handler defaultHandler = new FileHandler(defaultLog.getAbsolutePath());
                defaultHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(defaultHandler);
            } catch (IOException e) {
            }
            logger.setLevel(Level.ALL);
            init = true;
        }
        return logger;
    }

    public static void addHandler(IMod mod) throws IOException {
        addHandler(mod, new SimpleFormatter());
    }

    public static void addHandler(IMod mod, Formatter formatter) throws IOException {
        ModHandler handler = new ModHandler(mod);
        handler.setFormatter(formatter);
        getLogger().addHandler(handler);
    }

    @SafeVarargs
	public static void fine(Object o, Class<? extends IMod>... mods) {
        log(Level.FINE, o, mods);
    }

    @SafeVarargs
	public static void finer(Object o,  Class<? extends IMod>... mods) {
        log(Level.FINER, o, mods);
    }

    @SafeVarargs
	public static void finest(Object o,  Class<? extends IMod>... mods) {
        log(Level.FINEST, o, mods);
    }

    @SafeVarargs
	public static void severe(Object o,  Class<? extends IMod>... mods) {
        log(Level.SEVERE, o, mods);
    }
    @SafeVarargs
	public static void warning(Object o,  Class<? extends IMod>... mods) {
        log(Level.WARNING, o, mods);
    }

    @SafeVarargs
	public static void config(Object o,  Class<? extends IMod>... mods) {
        log(Level.CONFIG, o, mods);
    }

    @SafeVarargs
	public static void throwing(Class<?> clazz, String method, Throwable t, Class<? extends IMod>... mods) {
        throwing(clazz.getName(), method, t, mods);
    }

    @SafeVarargs
	public static void throwing(String clazz, String method, Throwable t,  Class<? extends IMod>... mods) {
        ModLogRecord record = new ModLogRecord(Level.FINE, "THROW", mods);
        record.setSourceClassName(clazz);
        record.setSourceMethodName(method);
        record.setThrown(t);
        log(record);
    }

    @SafeVarargs
	public static void log(Level level, Object o,  Class<? extends IMod>... mods) {
        log(level, o, new Object[0], mods);
    }

    @SafeVarargs
	public static void log(Level level, Object o, Object[] params,  Class<? extends IMod>... mods) {
        ModLogRecord record = new ModLogRecord(level, o.toString(), mods);
        record.setParameters(params);
        log(record);
    }

    public static void log(LogRecord record) {
        getLogger().log(record);
        if (DEBUG) {
        	System.out.print(record.toString());
        }
    }
}
