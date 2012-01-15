package net.minecraft.src;

public interface IPlayerFileData
{
    public abstract void writePlayerData(EntityPlayer entityplayer);

    public abstract void readPlayerData(EntityPlayer entityplayer);
}
