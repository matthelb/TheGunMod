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

    public void ModsLoaded()
    {
        ModLoaderMp.InitModLoaderMp();
    }

    public void HandlePacket(Packet230ModLoader packet230modloader, EntityPlayerMP entityplayermp)
    {
    }

    public void HandleLogin(EntityPlayerMP entityplayermp)
    {
    }

    public void HandleSendKey(EntityPlayerMP entityplayermp, int i)
    {
    }

    public void GetCommandInfo(ICommandListener icommandlistener)
    {
    }

    public boolean HandleCommand(String s, String s1, Logger logger, boolean flag)
    {
        return false;
    }

    public boolean hasClientSide()
    {
        return true;
    }
}
