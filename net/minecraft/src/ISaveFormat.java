package net.minecraft.src;

public interface ISaveFormat
{
    public abstract boolean isOldMapFormat(String s);

    public abstract boolean convertMapFormat(String s, IProgressUpdate iprogressupdate);
}
