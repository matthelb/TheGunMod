package com.heuristix;

import net.minecraft.client.Minecraft;
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

    public abstract String getReloadSound();

    public boolean handleAmmunitionConsumption(EntityPlayer player, Minecraft mc) {
        ItemStack gun = player.getCurrentEquippedItem();
        if (isReloading())
            stopReloading(mc);
        if (gun.getItemDamage() < gun.getMaxDamage()) {
            Util.damageItem(gun, player, 1, mod_Guns.class, mc.theWorld);
            return true;
        }
        return false;
    }

    @Override
    public void onFire(World world, EntityPlayer player) {
        double factor = Math.max(0.75, Math.min(Util.nextGaussian() + 1, 1.25));
        mod_Guns.recoilY += Math.min(factor * getRecoilY(), player.rotationPitch + 90.0F);
        mod_Guns.recoilX += factor * getRecoilX();
        Vec3D pos = Util.getProjectedPoint(player, 0.8);
        float radians = Util.toRadians(player.rotationYaw);
        world.spawnParticle("smoke", pos.xCoord - (MathHelper.cos(radians) * 0.3f), pos.yCoord, pos.zCoord - (MathHelper.sin(radians) * 0.3f), 0, 0, 0);
        //world.entityJoinedWorld(new EntityFlash(world, player.posX, player.posY + player.getEyeHeight(), player.posZ, 15, 1, 2));
    }

    public boolean reload(EntityPlayer player, Minecraft mc) {
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped != null && equipped.itemID == shiftedIndex && equipped.getItemDamage() > 0) {
            if (!reloading) {
                int slot = Util.getItemSlot(player.inventory, getProjectile().shiftedIndex);
                if (slot != -1) {
                    Util.playStreamingAtEntity(player, getReloadSound(), "guns.reloading", 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 0.8f), mc);
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

    public void finishReloading(Minecraft mc) {
        int amount = Math.min(Util.getCount(reloadingPlayer.inventory, getProjectile().shiftedIndex), Math.min(reloadingStack.getItemDamage(), reloadingStack.getMaxDamage()));
        Util.damageItem(reloadingStack, reloadingPlayer, -amount, mod_Guns.class, mc.theWorld);
        Util.remove(reloadingPlayer.inventory, getProjectile().shiftedIndex, amount);
        stopReloading(mc);
    }

    public void stopReloading(Minecraft mc) {
        Util.playStreamingAtEntity(reloadingPlayer, null, "guns.reloading", 0.0f, 0.0f, mc);
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
                Item.ingotIron, iron,
                Item.gunpowder, gunPowder,
                Item.diamond, diamond
            };
        }
        return craftingRecipe;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }
}
