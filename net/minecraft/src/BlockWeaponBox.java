// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            Block, Material, GunRecipe, mod_WW2Guns, 
//            ModLoader, World, InventoryPlayer, ItemStack, 
//            Item, GuiWeaponBox, EntityPlayer

public class BlockWeaponBox extends Block
{

    private int sideTexture;
    public GunRecipe guns[];
    public String teamName;
    private int nextGun;

    protected BlockWeaponBox(int i, int j, int k, String s)
    {
        super(i, Material.iron);
        blockIndexInTexture = j;
        sideTexture = k;
        guns = new GunRecipe[16];
        teamName = s;
        nextGun = 0;
    }

    public void addGun(String s, Item item, int i, int j, int k, int l, Item item1, 
            int i1, int j1, int k1, int l1)
    {
        guns[nextGun] = new GunRecipe(s);
        guns[nextGun].addGun(item, i, j, k, l);
        guns[nextGun].addClip(item1, i1, j1, k1, l1);
        nextGun++;
    }

    public void buyGun(int i, int j, int k, int l, int i1, InventoryPlayer inventoryplayer)
    {
        mod_WW2Guns.shootTime = 10;
        if(ModLoader.getMinecraftInstance().theWorld.multiplayerWorld)
        {
            mod_WW2Guns.buyGun(this, i);
        } else
        if(guns[i] != null)
        {
            int j1 = guns[i].ironNeeded;
            int k1 = guns[i].gunpowderNeeded;
            int l1 = guns[i].woodNeeded;
            int i2 = guns[i].glassNeeded;
            if(j >= j1 && k >= k1 && l >= l1 && i1 >= i2)
            {
                for(int j2 = 0; j2 < inventoryplayer.getSizeInventory(); j2++)
                {
                    ItemStack itemstack = inventoryplayer.getStackInSlot(j2);
                    if(itemstack == null)
                    {
                        continue;
                    }
                    if(itemstack.itemID == Item.ingotIron.shiftedIndex)
                    {
                        j1 -= inventoryplayer.decrStackSize(j2, j1).stackSize;
                    }
                    if(itemstack.itemID == Item.gunpowder.shiftedIndex)
                    {
                        k1 -= inventoryplayer.decrStackSize(j2, k1).stackSize;
                    }
                    if(itemstack.itemID == Block.wood.blockID)
                    {
                        l1 -= inventoryplayer.decrStackSize(j2, l1).stackSize;
                    }
                    if(itemstack.itemID == Block.glass.blockID)
                    {
                        i2 -= inventoryplayer.decrStackSize(j2, i2).stackSize;
                    }
                }

                if(inventoryplayer.addItemStackToInventory(new ItemStack(guns[i].gun)));
            }
        }
    }

    public void buyClip(int i, int j, int k, int l, int i1, InventoryPlayer inventoryplayer)
    {
        if(ModLoader.getMinecraftInstance().theWorld.multiplayerWorld)
        {
            mod_WW2Guns.buyClip(this, i);
        } else
        if(guns[i] != null)
        {
            int j1 = guns[i].clipIronNeeded;
            int k1 = guns[i].clipGunpowderNeeded;
            int l1 = guns[i].clipWoodNeeded;
            int i2 = guns[i].clipGlassNeeded;
            if(j >= j1 && k >= k1 && l >= l1 && i1 >= i2)
            {
                for(int j2 = 0; j2 < inventoryplayer.getSizeInventory(); j2++)
                {
                    ItemStack itemstack = inventoryplayer.getStackInSlot(j2);
                    if(itemstack == null)
                    {
                        continue;
                    }
                    if(itemstack.itemID == Item.ingotIron.shiftedIndex)
                    {
                        j1 -= inventoryplayer.decrStackSize(j2, j1).stackSize;
                    }
                    if(itemstack.itemID == Item.gunpowder.shiftedIndex)
                    {
                        k1 -= inventoryplayer.decrStackSize(j2, k1).stackSize;
                    }
                    if(itemstack.itemID == Block.wood.blockID)
                    {
                        l1 -= inventoryplayer.decrStackSize(j2, l1).stackSize;
                    }
                    if(itemstack.itemID == Block.glass.blockID)
                    {
                        i2 -= inventoryplayer.decrStackSize(j2, i2).stackSize;
                    }
                }

                if(inventoryplayer.addItemStackToInventory(new ItemStack(guns[i].clip)));
            }
        }
    }

    public int getBlockTextureFromSide(int i)
    {
        if(i == 1)
        {
            return blockIndexInTexture;
        } else
        {
            return sideTexture;
        }
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        ModLoader.OpenGUI(entityplayer, new GuiWeaponBox(entityplayer.inventory, this));
        return true;
    }
}
