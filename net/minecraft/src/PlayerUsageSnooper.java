package net.minecraft.src;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PlayerUsageSnooper
{
    private Map field_52016_a;
    private final URL field_52015_b;

    public PlayerUsageSnooper(String par1Str)
    {
        field_52016_a = new HashMap();

        try
        {
            field_52015_b = new URL((new StringBuilder()).append("http://snoop.minecraft.net/").append(par1Str).toString());
        }
        catch (MalformedURLException malformedurlexception)
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Adds information to the report
     */
    public void addData(String par1Str, Object par2Obj)
    {
        field_52016_a.put(par1Str, par2Obj);
    }

    /**
     * Starts a new thread to send the information to the report server
     */
    public void sendReport()
    {
        PlayerUsageSnooperThread playerusagesnooperthread = new PlayerUsageSnooperThread(this, "reporter");
        playerusagesnooperthread.setDaemon(true);
        playerusagesnooperthread.start();
    }

    /**
     * Returns the server URL for the given usage snooper
     */
    static URL getServerURL(PlayerUsageSnooper par0PlayerUsageSnooper)
    {
        return par0PlayerUsageSnooper.field_52015_b;
    }

    /**
     * Returns the data map for the given usage snooper
     */
    static Map getDataMap(PlayerUsageSnooper par0PlayerUsageSnooper)
    {
        return par0PlayerUsageSnooper.field_52016_a;
    }
}
