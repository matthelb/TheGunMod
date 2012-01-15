package net.minecraft.src;

import java.io.*;
import java.util.*;

public class StringTranslate
{
    private static StringTranslate instance = new StringTranslate();
    private Properties translateTable;
    private TreeMap field_44013_c;
    private String field_44012_d;
    private boolean field_46121_e;

    private StringTranslate()
    {
        translateTable = new Properties();
        func_44009_b();
        func_44010_a("en_US");
    }

    public static StringTranslate getInstance()
    {
        return instance;
    }

    private void func_44009_b()
    {
        TreeMap treemap = new TreeMap();
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\Matt\\My Files\\Developement\\minecraft-mods\\MCP-MP\\server\\lang")), "UTF-8"));
            for (String s = bufferedreader.readLine(); s != null; s = bufferedreader.readLine())
            {
                String as[] = s.split("=");
                if (as != null && as.length == 2)
                {
                    treemap.put(as[0], as[1]);
                }
            }
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            return;
        }
        field_44013_c = treemap;
    }

    private void func_44011_a(Properties properties, String s)
    throws IOException
    {
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\Matt\\My Files\\Developement\\minecraft-mods\\MCP-MP\\server\\lang\\" + s + ".lang")), "UTF-8"));
        for (String s1 = bufferedreader.readLine(); s1 != null; s1 = bufferedreader.readLine())
        {
            s1 = s1.trim();
            if (s1.startsWith("#"))
            {
                continue;
            }
            String as[] = s1.split("=");
            if (as != null && as.length == 2)
            {
                properties.setProperty(as[0], as[1]);
            }
        }
    }

    public void func_44010_a(String var1)
    {
        if (!var1.equals(this.field_44012_d))
        {
            Properties var2 = new Properties();

            try
            {
                this.func_44011_a(var2, "en_US");
            }
            catch (IOException var8)
            {
                ;
            }

            this.field_46121_e = false;
            if (!"en_US".equals(var1))
            {
                try
                {
                    this.func_44011_a(var2, var1);
                    Enumeration var3 = var2.propertyNames();

                    while (var3.hasMoreElements() && !this.field_46121_e)
                    {
                        Object var4 = var3.nextElement();
                        Object var5 = var2.get(var4);
                        if (var5 != null)
                        {
                            String var6 = var5.toString();

                            for (int var7 = 0; var7 < var6.length(); ++var7)
                            {
                                if (var6.charAt(var7) >= 256)
                                {
                                    this.field_46121_e = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                catch (IOException var9)
                {
                    var9.printStackTrace();
                    return;
                }
            }

            this.field_44012_d = var1;
            this.translateTable = var2;
        }
    }

    public String translateKey(String s)
    {
        return translateTable.getProperty(s, s);
    }

    public String translateKeyFormat(String s, Object aobj[])
    {
        String s1 = translateTable.getProperty(s, s);
        return String.format(s1, aobj);
    }
}
