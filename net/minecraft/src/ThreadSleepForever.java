package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ThreadSleepForever extends Thread
{
    final MinecraftServer mc;

    public ThreadSleepForever(MinecraftServer minecraftserver)
    {
        mc = minecraftserver;

        setDaemon(true);
        start();
    }

    public void run()
    {
        do
        {
            try
            {
                Thread.sleep(0x7fffffffL);
            }
            catch (InterruptedException interruptedexception) { }
        }
        while (true);
    }
}
