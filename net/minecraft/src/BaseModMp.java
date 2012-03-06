package net.minecraft.src;

import java.util.logging.Logger;

public abstract class BaseModMp extends BaseMod
{
    public BaseModMp()
    {
    }

    public final int getId()
    {
        return toString().hashCode();
    }

    public void modsLoaded()
    {
        ModLoaderMp.initialize();
        super.modsLoaded();
    }

    public void handlePacket(Packet230ModLoader packet230modloader, EntityPlayerMP entityplayermp)
    {
    }

    public void handleLogin(EntityPlayerMP entityplayermp)
    {
    }

    public void handleSendKey(EntityPlayerMP entityplayermp, int i)
    {
    }

    public void getCommandInfo(ICommandListener icommandlistener)
    {
    }

    public boolean handleCommand(String s, String s1, Logger logger, boolean flag)
    {
        return false;
    }

    public boolean hasClientSide()
    {
        return true;
    }
}
