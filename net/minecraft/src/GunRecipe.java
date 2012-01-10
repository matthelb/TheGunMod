// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Item

public class GunRecipe
{

    public String name;
    public int ironNeeded;
    public int gunpowderNeeded;
    public int woodNeeded;
    public int glassNeeded;
    public int clipIronNeeded;
    public int clipGunpowderNeeded;
    public int clipWoodNeeded;
    public int clipGlassNeeded;
    public Item gun;
    public Item clip;

    public GunRecipe(String s)
    {
        name = s;
    }

    public void addGun(Item item, int i, int j, int k, int l)
    {
        gun = item;
        ironNeeded = i;
        gunpowderNeeded = j;
        woodNeeded = k;
        glassNeeded = l;
    }

    public void addClip(Item item, int i, int j, int k, int l)
    {
        clip = item;
        clipIronNeeded = i;
        clipGunpowderNeeded = j;
        clipWoodNeeded = k;
        clipGlassNeeded = l;
    }
}
