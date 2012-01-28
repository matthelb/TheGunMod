package com.heuristix;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/24/12
 * Time: 9:10 PM
 */
public interface Mod {

    File getLogFile();

    String getModVersion();

    Properties getConfig() throws IOException;

    void loadConfig(Properties config);

}
