package com.heuristix;

import com.heuristix.util.ReflectionFacade;
import net.minecraft.src.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/4/11
 * Time: 10:44 PM
 */
public abstract class EntityProjectile extends Entity {

    public static final float GRAVITY = 1;
    static {
        ReflectionFacade.getInstance().putMethod(EntityLiving.class, "damageEntity", "c", DamageSource.class, int.class);
    }
    public EntityLiving owner;
    private Vec3D start;

    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    private int inData;
    private int ticksInAir;
    private int ticksInGround;
    private boolean inGround;
    private boolean doesBelongToPlayer;

    public EntityProjectile(World world) {
        super(world);
    }

    public EntityProjectile(World world, EntityLiving owner) {
        super(world);
        this.owner = owner;
        setLocationAndAngles(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ, owner.rotationYaw + (Util.randomFloat(-getSpread(), getSpread())), owner.rotationPitch + (Util.randomFloat(-getSpread(), getSpread())));
        this.start = Vec3D.createVector(posX, posY, posZ);
        setVelocity(computeVelocity());
        changeVelocity(getSpeed());
    }

    public EntityProjectile(World world, double x, double y, double z) {
        super(world);
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.start = Vec3D.createVector(posX, posY, posZ);
        setVelocity(computeVelocity());
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
        if(!worldObj.isRemote) {
            if(prevRotationPitch == 0 && prevRotationYaw == 0) {
                float horizontalDist = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                prevRotationYaw = rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180) / Util.PI) + Util.randomFloat(-getSpread(), getSpread());
                prevRotationPitch = rotationPitch = (float)((Math.atan2(motionY, horizontalDist) * 180) / Util.PI) + Util.randomFloat(-getSpread(), getSpread());
                setVelocity(computeVelocity());
                changeVelocity(getSpeed());
            }
            if (inGround) {
                ticksInGround++;
                if (ticksInGround >= getMaxGroundTicks()) {
                    setEntityDead();
                }
                return;
            }
            Vec3D currentLocation = getPosition();
            Vec3D newLocation = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition position = worldObj.rayTraceBlocks_do_do(currentLocation, newLocation, false, true);
            currentLocation = getPosition();
            newLocation = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
            if (position != null) {
                newLocation = Vec3D.createVector(position.hitVec.xCoord, position.hitVec.yCoord, position.hitVec.zCoord);
            }
            Entity hit = null;
            List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1, 1, 1));
            double d = 0;
            for (Entity entity : list) {
                if (!entity.canBeCollidedWith() || entity == owner && ticksInAir < 5) {
                    continue;
                }
                float expandAmount = 0.3f;
                AxisAlignedBB aabb = entity.boundingBox.expand(expandAmount, expandAmount, expandAmount);
                MovingObjectPosition position1 = aabb.calculateIntercept(currentLocation, newLocation);
                if (position1 == null) {
                    continue;
                }
                double d1 = currentLocation.distanceTo(position1.hitVec);
                if (d1 < d || d == 0) {
                    hit = entity;
                    d = d1;
                }
            }

            if (hit != null) {
                position = new MovingObjectPosition(hit);
            }
            if (position != null) {
                if (position.entityHit != null && !(position.entityHit instanceof EntityProjectile) && onEntityHit(position.entityHit)) {
                    worldObj.playSoundAtEntity(owner, getHitSound(), 1.0f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f));
                    setEntityDead();
                } else {
                    if(onBlockHit(position)) {
                        xTile = position.blockX;
                        yTile = position.blockY;
                        zTile = position.blockZ;
                        inTile = worldObj.getBlockId(xTile, yTile, zTile);
                        inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
                        motionX = (float) (position.hitVec.xCoord - posX);
                        motionY = (float) (position.hitVec.yCoord - posY);
                        motionZ = (float) (position.hitVec.zCoord - posZ);
                        float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                        posX -= (motionX / f1) * 0.05000000074505806D;
                        posY -= (motionY / f1) * 0.05000000074505806D;
                        posZ -= (motionZ / f1) * 0.05000000074505806D;
                        //worldObj.playSoundAtEntity(this, getMoveSound(), 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
                        inGround = true;
                    }
                }
            }
            motionY -= GRAVITY * getMass();
            setPosition(posX + motionX, posY + motionY, posZ + motionZ);
        }
    }

    public boolean onEntityHit(Entity hit) {
        if (hit != null) {
            if(hit instanceof EntityLiving) {
                ReflectionFacade.getInstance().invokeMethod(EntityLiving.class, hit, "damageEntity", new EntityDamageSource("living", owner), Math.round(getDamage() * getDamageModifier()));
            }
            return true;
        }
        return false;
    }

    public boolean onBlockHit(MovingObjectPosition blockPosition) {
        return true;
    }

    protected final float getDamageModifier() {
        return (float) Math.min(1, getEffectiveRange() / start.distanceTo(getPosition()));
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

    public final void changeVelocity(float factor) {
        changeVelocity(factor, factor, factor);
    }

    public final void changeVelocity(float xFactor, float yFactor, float zFactor) {
        motionX *= xFactor;
        motionY *= yFactor;
        motionZ *= zFactor;
    }

    public final void setVelocity(Vec3D velocity) {
        setVelocity(velocity.xCoord, velocity.yCoord, velocity.zCoord);
    }

    public final Vec3D computeVelocity() {
        float yawRadians = Util.toRadians(rotationYaw);
        float pitchRadians = Util.toRadians(rotationPitch);
        float cosPitch = MathHelper.cos(pitchRadians);
        return Vec3D.createVector(-MathHelper.sin(yawRadians) * cosPitch, -MathHelper.sin(pitchRadians), MathHelper.cos(yawRadians) * cosPitch);
    }

    public Vec3D getPosition() {
        return Vec3D.createVector(posX, posY, posZ);
    }

    public EntityLiving getOwner() {
        return owner;
    }
}
