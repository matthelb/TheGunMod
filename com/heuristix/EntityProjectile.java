package com.heuristix;

import net.minecraft.src.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/4/11
 * Time: 10:44 PM
 */
public abstract class EntityProjectile extends Entity {

    public static final float GRAVITY = 0.1f;

    private final EntityLiving owner;
    private final double startX, startY, startZ;

    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    private int inData;
    private int ticksInAir;
    private int ticksInGround;
    private boolean inGround;
    public boolean doesBelongToPlayer;

    public EntityProjectile(World world, EntityLiving owner) {
        super(world);
        this.owner = owner;
        setLocationAndAngles(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ, owner.rotationYaw + (Utilities.randomFloat(-getSpread(), getSpread())), owner.rotationPitch + (Utilities.randomFloat(-getSpread(), getSpread())));
        this.startX = posX;
        this.startY = posY;
        this.startZ = posZ;
        float yawRadians = Utilities.toRadians(rotationYaw);
        float pitchRadians = Utilities.toRadians(rotationPitch);
        float cosPitch = MathHelper.cos(pitchRadians);
        setVelocity(-MathHelper.sin(yawRadians) * cosPitch, -MathHelper.sin(pitchRadians), MathHelper.cos(yawRadians) * cosPitch);
        changeVelocity(getSpeed());
    }

    public abstract int getDamage();

    public abstract float getSpeed();

    public abstract int getMaxGroundTicks();

    public abstract String getHitSound();

    public abstract String getMoveSound();

    public abstract float getEffectiveRange();

    public abstract float getMass();

    public abstract float getSpread();

    public void onUpdate() {
        super.onUpdate();
        if(inGround) {
            ticksInGround++;
            if(ticksInGround >= getMaxGroundTicks()) {
                setEntityDead();
            }
            return;
        }
        Vec3D currentLocation = Vec3D.createVector(posX, posY, posZ);
        Vec3D newLocation = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition position = worldObj.rayTraceBlocks_do_do(currentLocation, newLocation, false, true);
        currentLocation = Vec3D.createVector(posX, posY, posZ);
        newLocation = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        if(position != null) {
            newLocation = Vec3D.createVector(position.hitVec.xCoord, position.hitVec.yCoord, position.hitVec.zCoord);
        }
        Entity hit = null;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1, 1, 1));
        double d = 0;
        for (Object aList : list) {
            Entity entity = (Entity) aList;
            if (!entity.canBeCollidedWith() || entity == owner && ticksInAir < 5) {
                continue;
            }
            float expandAmount = 0.3f;
            AxisAlignedBB aabb = entity.boundingBox.expand(expandAmount, expandAmount, expandAmount);
            MovingObjectPosition position1 = aabb.func_1169_a(currentLocation, newLocation);
            if (position1 == null) {
                continue;
            }
            double d1 = currentLocation.distanceTo(position1.hitVec);
            if (d1 < d || d == 0) {
                hit = entity;
                d = d1;
            }
        }

        if(hit != null) {
            position = new MovingObjectPosition(hit);
        }
        if(position != null) {
            if(position.entityHit != null) {
                if(onHit(position.entityHit)) {
                    worldObj.playSoundAtEntity(this, getHitSound(), 0.01f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f));
                    //worldObj.playSoundAtEntity(owner, getHitSound(), 0.01f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f));
                    setEntityDead();
                }
            } else {
                xTile = position.blockX;
                yTile = position.blockY;
                zTile = position.blockZ;
                inTile = worldObj.getBlockId(xTile, yTile, zTile);
                inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
                motionX = (float)(position.hitVec.xCoord - posX);
                motionY = (float)(position.hitVec.yCoord - posY);
                motionZ = (float)(position.hitVec.zCoord - posZ);
                float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                posX -= (motionX / (double)f1) * 0.05000000074505806D;
                posY -= (motionY / (double)f1) * 0.05000000074505806D;
                posZ -= (motionZ / (double)f1) * 0.05000000074505806D;
                //worldObj.playSoundAtEntity(this, getMoveSound(), 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
                inGround = true;
                onHit(null);
            }
        }
        motionY -= GRAVITY * getMass();
        setPosition(posX + motionX, posY + motionY, posZ + motionZ);
    }

    public boolean onHit(Entity hit) {
        if(hit != null)
            return hit.attackEntityFrom(new EntityDamageSource("living", owner), Math.round(getDamage() * getDamageModifier()));
        return false;
    }

    protected final float getDamageModifier() {
        return (float) Math.min(1, getEffectiveRange() / Vec3D.createVectorHelper(startX, startY, startZ).distanceTo(Vec3D.createVectorHelper(posX, posY, posZ)));
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        xTile = nbt.getShort("xTile");
        yTile = nbt.getShort("yTile");
        zTile = nbt.getShort("zTile");
        inTile = nbt.getByte("inTile") & 0xff;
        inData = nbt.getByte("inData") & 0xff;
        inGround = nbt.getByte("inGround") == 1;
        doesBelongToPlayer = nbt.getBoolean("player");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setShort("xTile", (short) xTile);
        nbt.setShort("yTile", (short) yTile);
        nbt.setShort("zTile", (short) zTile);
        nbt.setByte("inTile", (byte) inTile);
        nbt.setByte("inData", (byte) inData);
        nbt.setByte("inGround", (byte) (inGround ? 1 : 0));
        nbt.setBoolean("player", doesBelongToPlayer);
    }

    public void changeVelocity(float factor) {
        changeVelocity(factor, factor, factor);
    }

    public void changeVelocity(float xFactor, float yFactor, float zFactor) {
        motionX *= xFactor;
        motionY *= yFactor;
        motionZ *= zFactor;
    }
}
