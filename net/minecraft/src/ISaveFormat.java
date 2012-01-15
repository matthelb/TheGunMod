package net.minecraft.src;

public interface ISaveFormat
{
    public abstract boolean isOldSaveType(String s);

    public abstract boolean convertMapFormat(String s, IProgressUpdate iprogressupdate);
}
