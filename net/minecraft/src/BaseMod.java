package net.minecraft.src;

import java.util.Random;
import net.minecraft.server.MinecraftServer;

public abstract class BaseMod
{
    public BaseMod()
    {
    }

    public int AddFuel(int i)
    {
        return 0;
    }

    public boolean DispenseEntity(World world, double d, double d1, double d2,
            int i, int j, ItemStack itemstack)
    {
        return false;
    }

    public void GenerateNether(World world, Random random, int i, int j)
    {
    }

    public void GenerateSurface(World world, Random random, int i, int j)
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

    public void OnTickInGame(MinecraftServer minecraftserver)
    {
    }

    public void ModsLoaded()
    {
    }

    public void TakenFromCrafting(EntityPlayer entityplayer, ItemStack itemstack)
    {
    }

    public void TakenFromFurnace(EntityPlayer entityplayer, ItemStack itemstack)
    {
    }

    public void OnItemPickup(EntityPlayer entityplayer, ItemStack itemstack)
    {
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(getName()))).append(' ').append(getVersion()).toString();
    }
}
