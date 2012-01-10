// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.Random;
import org.lwjgl.input.Mouse;

// Referenced classes of package net.minecraft.src:
//            Item, EntityPlayer, InventoryPlayer, ModLoader, 
//            mod_WW2Guns, World, ItemStack, EntityAntiTank, 
//            EntityBullet2, Entity

public class ItemGun extends Item
{

    private int clipSize;
    private int reloadTime;
    private int recoil;
    private int damage;
    private int accuracy;
    private int shootDelay;
    private Item clip;
    private String shootSound;
    private String reloadSound;
    public String type;
    private static boolean mouseHeld;
    private static boolean lastMouseHeld;

    public ItemGun(int i, int j, int k, int l, int i1, int j1, Item item, 
            String s, String s1, String s2)
    {
        super(i);
        maxStackSize = 1;
        reloadTime = j;
        recoil = k;
        damage = l;
        accuracy = i1;
        shootDelay = j1;
        clip = item;
        shootSound = s;
        reloadSound = s1;
        type = s2;
    }

    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        if((entity instanceof EntityPlayer) && ((EntityPlayer)entity).inventory.getCurrentItem() == itemstack && ModLoader.isGUIOpen(null))
        {
            lastMouseHeld = mouseHeld;
            if(Mouse.isButtonDown(1))
            {
                mouseHeld = true;
            } else
            {
                mouseHeld = false;
            }
            if(type.equals("SMG") && mouseHeld)
            {
                itemstack = onItemRightClick2(itemstack, world, (EntityPlayer)entity);
            }
            if((type.equals("Rifle") || type.equals("Sniper") || type.equals("AntiTank")) && mouseHeld && !lastMouseHeld)
            {
                itemstack = onItemRightClick2(itemstack, world, (EntityPlayer)entity);
            }
            if(type.equals("Sniper") && Mouse.isButtonDown(0) && mod_WW2Guns.shootTime <= 0)
            {
                if(!mod_WW2Guns.zoomOverlay)
                {
                    mod_WW2Guns.zoomOverlay = true;
                    mod_WW2Guns.newZoom = 8F;
                } else
                {
                    mod_WW2Guns.newZoom = 1.0F;
                }
                mod_WW2Guns.shootTime = 10;
            }
        }
    }

    public ItemStack onItemRightClick2(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if(mod_WW2Guns.shootTime <= 0)
        {
            if(world.multiplayerWorld)
            {
                mod_WW2Guns.shoot();
            }
            for(int i = 0; i < entityplayer.inventory.getSizeInventory(); i++)
            {
                ItemStack itemstack1 = entityplayer.inventory.getStackInSlot(i);
                if(itemstack1 != null && itemstack1.itemID == clip.shiftedIndex)
                {
                    int j = itemstack1.getItemDamage();
                    if(j == itemstack1.getMaxDamage())
                    {
                        entityplayer.inventory.setInventorySlotContents(i, null);
                        world.playSoundAtEntity(entityplayer, reloadSound, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
                        mod_WW2Guns.shootTime = reloadTime;
                        mod_WW2Guns.zoomOverlay = false;
                        mod_WW2Guns.newZoom = 1.0F;
                        return itemstack;
                    }
                    world.playSoundAtEntity(entityplayer, shootSound, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
                    mod_WW2Guns.playerRecoil += recoil;
                    if(!world.multiplayerWorld)
                    {
                        if(type.equals("AntiTank"))
                        {
                            world.spawnEntityInWorld(new EntityAntiTank(world, entityplayer, damage, accuracy));
                        } else
                        {
                            world.spawnEntityInWorld(new EntityBullet2(world, entityplayer, damage, accuracy));
                        }
                        itemstack1.setItemDamage(j + 1);
                        entityplayer.inventory.setInventorySlotContents(i, itemstack1);
                    }
                    mod_WW2Guns.shootTime = shootDelay;
                    return itemstack;
                }
            }

        }
        return itemstack;
    }

    public boolean isItemStackDamageable()
    {
        return true;
    }
}
