package net.minecraft.src;

import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class ConvertProgressUpdater
    implements IProgressUpdate
{
    private long lastTimeMillis;
    final MinecraftServer mcServer;

    public ConvertProgressUpdater(MinecraftServer minecraftserver)
    {
        mcServer = minecraftserver;

        lastTimeMillis = System.currentTimeMillis();
    }

    public void displaySavingString(String s)
    {
    }

    public void setLoadingProgress(int i)
    {
        if (System.currentTimeMillis() - lastTimeMillis >= 1000L)
        {
            lastTimeMillis = System.currentTimeMillis();
            MinecraftServer.logger.info((new StringBuilder()).append("Converting... ").append(i).append("%").toString());
        }
    }

    public void displayLoadingString(String s)
    {
    }
}
