// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.Random;
import net.minecraft.server.MinecraftServer;

// Referenced classes of package net.minecraft.src:
//            World, ItemStack, EntityPlayer

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
        return (new StringBuilder(String.valueOf(getClass().getSimpleName()))).append(" ").append(Version()).toString();
    }

    public abstract String Version();
}
