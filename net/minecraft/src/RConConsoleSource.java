package net.minecraft.src;

public class RConConsoleSource
    implements ICommandListener
{
    public static final RConConsoleSource instance = new RConConsoleSource();
    private StringBuffer buffer;

    public RConConsoleSource()
    {
        buffer = new StringBuffer();
    }

    public void resetLog()
    {
        buffer.setLength(0);
    }

    public String getLogContents()
    {
        return buffer.toString();
    }

    public void log(String s)
    {
        buffer.append(s);
    }

    public String getUsername()
    {
        return "Rcon";
    }
}
