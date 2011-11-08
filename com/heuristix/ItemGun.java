package com.heuristix;

import net.minecraft.src.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 5:51 PM
 */
public abstract class ItemGun extends ItemProjectileShooter {

    private boolean reloading;
    private long reloadFinishTime;
        private EntityPlayer reloadingPlayer;
        private ItemStack reloadingStack;

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

    public boolean handleAmmunitionConsumption(EntityPlayer player) {
        ItemStack gun = player.getCurrentEquippedItem();
        if(isReloading())
            stopReloading();
        if(gun.getItemDamage() < gun.getMaxDamage()) {
            gun.damageItem(1, player);
            return true;
        }
        return false;
    }

    @Override
    public void onFire(World world, EntityPlayer player) {
        double factor = Math.max(0.75, Math.min(Utilities.nextGaussian() + 1, 1.25));
        mod_Guns.recoilY +=  Math.min(factor * getRecoilY(), player.rotationPitch + 90.0F);
        mod_Guns.recoilX += factor * getRecoilX();
    }

    public boolean reload(EntityPlayer player) {
        ItemStack equipped = player.getCurrentEquippedItem();
        if(equipped != null && equipped.itemID == shiftedIndex && equipped.getItemDamage() > 0) {
            if(!reloading) {
                int slot = Utilities.getItemSlot(player.inventory, getProjectile().shiftedIndex);
                if(slot != -1) {
                    reloading = true;
                    reloadFinishTime = System.currentTimeMillis() + getReloadTime();
                    reloadingPlayer = player;
                    reloadingStack = equipped;
                }
            }
            return true;
        }
        reloading = false;
        return false;
    }

    public void finishReloading() {
        int amount = Math.min(Utilities.getCount(reloadingPlayer.inventory, getProjectile().shiftedIndex), Math.min(reloadingStack.getItemDamage(), reloadingStack.getMaxDamage()));
        reloadingStack.damageItem(-amount, reloadingPlayer);
        Utilities.remove(reloadingPlayer.inventory, getProjectile().shiftedIndex, amount);
        stopReloading();
    }

    public void stopReloading() {
        reloadFinishTime = System.currentTimeMillis();
        reloading = false;
    }

    public int getCraftingAmount() {
        return 1;
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


}
