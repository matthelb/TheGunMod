package net.minecraft.src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.minecraft.server.MinecraftServer;

final class ServerWindowAdapter extends WindowAdapter
{
    final MinecraftServer mcServer;

    ServerWindowAdapter(MinecraftServer minecraftserver)
    {
        mcServer = minecraftserver;
    }

    public void windowClosing(WindowEvent windowevent)
    {
        mcServer.initiateShutdown();
        while (!mcServer.serverStopped)
        {
            try
            {
                Thread.sleep(100L);
            }
            catch (InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
        System.exit(0);
    }
}
