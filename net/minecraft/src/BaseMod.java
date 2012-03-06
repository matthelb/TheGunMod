package net.minecraft.src;

import java.util.Random;
import net.minecraft.server.MinecraftServer;

public abstract class BaseMod
{
    public BaseMod()
    {
    }

    public int addFuel(int i, int j)
    {
        return 0;
    }

    public boolean dispenseEntity(World world, double d, double d1, double d2, int i, int j, ItemStack itemstack)
    {
        return false;
    }

    public void generateNether(World world, Random random, int i, int j)
    {
    }

    public void generateSurface(World world, Random random, int i, int j)
    {
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }

    public String getPriorities()
    {
        return "";
    }

    public abstract String getVersion();

    public abstract void load();

    public void modsLoaded()
    {
    }

    public void onItemPickup(EntityPlayer entityplayer, ItemStack itemstack)
    {
    }

    public void onTickInGame(MinecraftServer minecraftserver)
    {
    }

    public void takenFromCrafting(EntityPlayer entityplayer, ItemStack itemstack, IInventory iinventory)
    {
    }

    public void takenFromFurnace(EntityPlayer entityplayer, ItemStack itemstack)
    {
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(getName()))).append(' ').append(getVersion()).toString();
    }
}
