package net.minecraft.src;

import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class ConvertProgressUpdater implements IProgressUpdate
{
    /** lastTimeMillis */
    private long lastTimeMillis;

    /** Reference to the MinecraftServer object. */
    final MinecraftServer mcServer;

    public ConvertProgressUpdater(MinecraftServer par1MinecraftServer)
    {
        mcServer = par1MinecraftServer;
        lastTimeMillis = System.currentTimeMillis();
    }

    /**
     * Shows the 'Saving level' string.
     */
    public void displaySavingString(String s)
    {
    }

    public void setLoadingProgress(int par1)
    {
        if (System.currentTimeMillis() - lastTimeMillis >= 1000L)
        {
            lastTimeMillis = System.currentTimeMillis();
            MinecraftServer.logger.info((new StringBuilder()).append("Converting... ").append(par1).append("%").toString());
        }
    }

    public void displayLoadingString(String s)
    {
    }
}
