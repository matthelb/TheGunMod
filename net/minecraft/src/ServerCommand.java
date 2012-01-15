package net.minecraft.src;

public class ServerCommand
{
    public final String command;
    public final ICommandListener commandListener;

    public ServerCommand(String s, ICommandListener icommandlistener)
    {
        command = s;
        commandListener = icommandlistener;
    }
}
