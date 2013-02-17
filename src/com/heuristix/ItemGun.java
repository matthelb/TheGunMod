package com.heuristix;

import java.awt.image.BufferedImage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.heuristix.guns.Util;
import com.heuristix.guns.client.render.TextureManager;
import com.heuristix.guns.helper.InventoryHelper;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 5:51 PM
 */
public abstract class ItemGun extends ItemProjectileShooter {

    private boolean reloading;
    private long reloadFinishTime;
    private BufferedImage customScope;

    public ItemGun(int id, ItemProjectile projectile) {
        super(id, projectile);
        this.setMaxDamage(getClipSize());
    }

    public abstract int getClipSize();

    public abstract int getReloadTime();

    public abstract int getRecoilY();

    public abstract int getRecoilX();

    public abstract float getZoom();

    public abstract int getScope();

    public abstract String getReloadSound();

    public abstract int getReloadParts();

    public boolean handleAmmunitionConsumption(EntityPlayer player) {
        ItemStack gun = player.getCurrentEquippedItem();
        if (isReloading()) {
            stopReloading(gun, player);
        }
        if (gun.getItemDamage() < gun.getMaxDamage()) {
            gun.damageItem(1, player);
            return true;
        }
        return false;
    }

    @Override
    public void onFire(World world, EntityPlayer player) {
    	super.onFire(world, player);
        Vec3 pos = Util.getProjectedPoint(player, 0.8);
        float radians = com.heuristix.guns.helper.MathHelper.toRadians(player.rotationYaw);
        world.spawnParticle("smoke", pos.xCoord - (MathHelper.cos(radians) * 0.3f), pos.yCoord, pos.zCoord - (MathHelper.sin(radians) * 0.3f), 0, 0, 0);
    }

    public boolean reload(EntityPlayer player) {
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped != null && equipped.itemID == itemID && equipped.getItemDamage() > 0) {
            if (!reloading) {
                int slot = InventoryHelper.getItemSlot(player.inventory, getProjectile().itemID);
                if (slot != -1) {
                    Util.playStreamingAtEntity(player, getReloadSound(), "guns.reloading", 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 0.8f));
                    reloading = true;
                    reloadFinishTime = System.currentTimeMillis() + getReloadTime();
                }
            }
            return true;
        }
        reloading = false;
        return false;
    }

    public void finishReloading(ItemStack stack, EntityPlayer player) {
        int amount = Math.min(InventoryHelper.getCount(player.inventory, getProjectile().itemID), Math.min(stack.getItemDamage(), stack.getMaxDamage() / getReloadParts()));
        stack.damageItem(-amount, player);
        InventoryHelper.remove(player.inventory, getProjectile().itemID, amount);
        stopReloading(stack, player);
    }

    public void stopReloading(ItemStack stack, EntityPlayer player) {
        Util.playStreamingAtEntity(player, null, "guns.reloading", 0.0f, 0.0f);
        reloadFinishTime = System.currentTimeMillis();
        reloading = false;
        if(stack.isItemDamaged()) {
            reload(player);
        }
    }

    public int getCraftingAmount() {
        return 1;
    }

    public boolean isShapelessRecipe() {
        return false;
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        stack.damageItem(stack.getMaxDamage(), player);
    }

    public boolean isReloading() {
        return reloading;
    }

    public long getReloadFinishTime() {
        return reloadFinishTime;
    }
    
    public float getReloadPercent() {
    	return com.heuristix.guns.helper.MathHelper.maxmin((
    			System.currentTimeMillis() - (getReloadFinishTime() - getReloadTime())) / (float) getReloadTime(), 1, 0);
    }

    private Object[] craftingRecipe;

    public boolean hasWorkbenchRecipe() {
        return false;
    }

    public Object[] getCraftingRecipe() {
        if(craftingRecipe == null) {
            EntityProjectile entityProjectile = null;
            try {
                entityProjectile = getProjectile().getProjectileClass(this).getDeclaredConstructor(World.class).newInstance(new Object[]{null});
            } catch (Exception e) {
                return new Object[0];
            }
            int diamond = 1;
            int gunPowder = (entityProjectile == null) ? 1 : (int) MathHelper.sqrt_float(entityProjectile.getEffectiveRange());
            int iron = (entityProjectile == null) ? 1 : entityProjectile.getDamage() / 2;
            craftingRecipe =  new Object[]{
                    ingotIron, iron,
                    gunpowder, gunPowder,
                Item.diamond, diamond
            };
        }
        return craftingRecipe;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    public BufferedImage getCustomScope() {
        return customScope;
    }

    public void setCustomScope(BufferedImage image) {
        this.customScope = image;
    }

	@Override
	public String getTextureFile() {
		return "/" + TextureManager.getCurrentTextureFileName();
	}
    
}
