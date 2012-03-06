package net.minecraft.src;

import java.io.PrintStream;
import java.util.*;

public class Profiler
{
    public static boolean profilingEnabled = false;
    private static List sectionList = new ArrayList();
    private static List timestampList = new ArrayList();
    private static String profilingSection = "";
    private static Map profilingMap = new HashMap();

    public Profiler()
    {
    }

    public static void startSection(String par0Str)
    {
        if (!profilingEnabled)
        {
            return;
        }

        if (profilingSection.length() > 0)
        {
            profilingSection = (new StringBuilder()).append(profilingSection).append(".").toString();
        }

        profilingSection = (new StringBuilder()).append(profilingSection).append(par0Str).toString();
        sectionList.add(profilingSection);
        timestampList.add(Long.valueOf(System.nanoTime()));
    }

    public static void endSection()
    {
        if (!profilingEnabled)
        {
            return;
        }

        long l = System.nanoTime();
        long l1 = ((Long)timestampList.remove(timestampList.size() - 1)).longValue();
        sectionList.remove(sectionList.size() - 1);
        long l2 = l - l1;

        if (profilingMap.containsKey(profilingSection))
        {
            profilingMap.put(profilingSection, Long.valueOf(((Long)profilingMap.get(profilingSection)).longValue() + l2));
        }
        else
        {
            profilingMap.put(profilingSection, Long.valueOf(l2));
        }

        profilingSection = sectionList.size() <= 0 ? "" : (String)sectionList.get(sectionList.size() - 1);

        if (l2 > 0x5f5e100L)
        {
            System.out.println((new StringBuilder()).append(profilingSection).append(" ").append(l2).toString());
        }
    }

    public static void endStartSection(String par0Str)
    {
        endSection();
        startSection(par0Str);
    }
}
