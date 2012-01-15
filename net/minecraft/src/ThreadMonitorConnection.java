package net.minecraft.src;

class ThreadMonitorConnection extends Thread
{
    final NetworkManager netManager;

    ThreadMonitorConnection(NetworkManager networkmanager)
    {
        netManager = networkmanager;
    }

    public void run()
    {
        try
        {
            Thread.sleep(2000L);
            if (NetworkManager.isRunning(netManager))
            {
                NetworkManager.getWriteThread(netManager).interrupt();
                netManager.networkShutdown("disconnect.closed", new Object[0]);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
