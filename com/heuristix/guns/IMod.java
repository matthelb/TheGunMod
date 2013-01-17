package com.heuristix.guns;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/24/12
 * Time: 9:10 PM
 */
public interface IMod {

    String getModName();

    String getModVersion();

    File getConfigFile();

    Properties getDefaultConfig();

    String getPropertiesId();

    void loadConfig(Properties config);

    File getLogFile();

}
