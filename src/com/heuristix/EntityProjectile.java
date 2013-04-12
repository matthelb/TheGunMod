package com.heuristix;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.heuristix.guns.util.ReflectionFacade;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/4/11
 * Time: 10:44 PM
 */
public abstract class EntityProjectile extends Entity {

    public static final float GRAVITY = 1;

    private EntityLiving owner;
    private Vec3 start;

    public EntityProjectile(World world) {
        super(world);
    }

    public EntityProjectile(World world, EntityLiving owner) {
        super(world);
        this.owner = owner;
        setLocationAndAngles(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ, owner.rotationYaw + (com.heuristix.guns.helper.MathHelper.randomFloat(-getSpread(), getSpread())), owner.rotationPitch + (com.heuristix.guns.helper.MathHelper.randomFloat(-getSpread(), getSpread())));
        setVelocity(computeVelocity());
        changeVelocity(getSpeed());
    }

    public EntityProjectile(World world, double x, double y, double z) {
        super(world);
        setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
        setVelocity(computeVelocity());
        changeVelocity(getSpeed());
    }
    
    @Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
		super.setLocationAndAngles(x, y, z, yaw, pitch);
		if (start == null) {
			this.start = Vec3.createVectorHelper(posX, posY, posZ);
		}
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
    	if (prevRotationPitch == 0 && prevRotationYaw == 0) {
    		float change = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
    		prevRotationYaw = rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180 / Math.PI);
    		prevRotationPitch = rotationPitch = (float) (Math.atan2(motionY, change) * 180 / Math.PI);
    	}

    	Vec3 positionVector = Vec3.fakePool.getVecFromPool(posX, posY, posZ);
    	Vec3 newPositionVector = Vec3.fakePool.getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
    	MovingObjectPosition position = worldObj.rayTraceBlocks_do_do(positionVector, newPositionVector, false, true);
    	positionVector = Vec3.fakePool.getVecFromPool(posX, posY, posZ);
    	newPositionVector = Vec3.fakePool.getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
    	if (position != null) {
    		newPositionVector = Vec3.fakePool.getVecFromPool(position.hitVec.xCoord, position.hitVec.yCoord, position.hitVec.zCoord);
    	}

    	List<?> entitiesWithinAABB = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));

    	Iterator<?> itr = entitiesWithinAABB.iterator();
    	Entity closetEntity = null;
    	double closestDist = 0;
    	while (itr.hasNext()) {
    		Entity entity = (Entity) itr.next();

    		if (entity.canBeCollidedWith() && (entity != owner)) {
    			float padding = 0.3f;
    			AxisAlignedBB var12 = entity.boundingBox.expand(padding, padding, padding);
    			MovingObjectPosition intercept = var12.calculateIntercept(positionVector, newPositionVector);
    			if (intercept != null) {
    				double dist = positionVector.distanceTo(intercept.hitVec);
    				if (dist < closestDist || closestDist == 0) {
    					closetEntity = entity;
    					closestDist = dist;
    				}
    			}
    		}
    	}

    	if (closetEntity != null) {
    		position = new MovingObjectPosition(closetEntity);
    	}
    	if (position != null) {
    		if (position.entityHit != null) {
    			if (!(position.entityHit instanceof EntityProjectile) && onEntityHit(position.entityHit)) {
    				worldObj.playSoundAtEntity(owner, getHitSound(), 1.0f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f));
    				setDead();
    			} else {
    				motionX *= -0.10000000149011612D;
    				motionY *= -0.10000000149011612D;
    				motionZ *= -0.10000000149011612D;
    				rotationYaw += 180.0F;
    				prevRotationYaw += 180.0F;
    			}
    		} else {
    			if (onBlockHit(position)) {
    				worldObj.playSoundAtEntity(this, getMoveSound(), 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
    			}
    		}
    	}
    	posX += motionX;
    	posY += motionY;
    	posZ += motionZ;
    	if (isInWater()) {
    		for (int i = 0; i < 4; ++i) {
    			float var27 = 0.25f;
    			worldObj.spawnParticle("bubble", posX - motionX * var27, posY - motionY * var27, posZ - motionZ * var27, motionX, motionY, motionZ);
    		}
    	}
    	motionY -= GRAVITY * getMass();
    	setPosition(posX, posY, posZ);
    	doBlockCollisions();
    }

    public boolean onEntityHit(Entity hit) {
         if (hit != null && (hit instanceof EntityLiving || hit instanceof EntityDragonPart)) {
            return damageEntityWithoutDelay(hit, Math.round(getDamage() * getDamageModifier()));
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
    	start = Vec3.createVectorHelper(nbt.getDouble("startX"), nbt.getDouble("startY"), nbt.getDouble("startZ"));
    	setLocationAndAngles(nbt.getDouble("posX"), nbt.getDouble("posY"), nbt.getDouble("posZ"), nbt.getFloat("rotationYaw"), nbt.getFloat("rotationPitch"));
    	owner = (EntityLiving) worldObj.getEntityByID(nbt.getInteger("ownerId"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
    	nbt.setDouble("startX", start.xCoord);
    	nbt.setDouble("startY", start.yCoord);
    	nbt.setDouble("startZ", start.zCoord);
    	nbt.setDouble("posX", posX);
    	nbt.setDouble("posY", posY);
    	nbt.setDouble("posZ", posZ);
    	nbt.setFloat("rotationYaw", rotationYaw);
    	nbt.setFloat("rotationPitch", rotationPitch);
    	if (owner != null) {
    		nbt.setInteger("ownerId", owner.entityId);
    	}
    }

    public final void changeVelocity(float factor) {
        changeVelocity(factor, factor, factor);
    }

    public final void changeVelocity(float xFactor, float yFactor, float zFactor) {
        motionX *= xFactor;
        motionY *= yFactor;
        motionZ *= zFactor;
    }

    public final void setVelocity(Vec3 velocity) {
        setVelocity(velocity.xCoord, velocity.yCoord, velocity.zCoord);
    }

    public final Vec3 computeVelocity() {
        float yawRadians = com.heuristix.guns.helper.MathHelper.toRadians(rotationYaw);
        float pitchRadians = com.heuristix.guns.helper.MathHelper.toRadians(rotationPitch);
        float cosPitch = MathHelper.cos(pitchRadians);
        return Vec3.createVectorHelper(-MathHelper.sin(yawRadians) * cosPitch, -MathHelper.sin(pitchRadians), MathHelper.cos(yawRadians) * cosPitch);
    }

    public Vec3 getPosition() {
        return Vec3.createVectorHelper(posX, posY, posZ);
    }

    public void setOwner(EntityLiving owner) {
    	this.owner = owner;
    }
    
    public EntityLiving getOwner() {
        return owner;
    }

    public boolean damageEntityWithoutDelay(Entity entity, int damage) {
        DamageSource source = new EntityDamageSource("living", owner);
        ReflectionFacade.getInstance().setFieldValue(Entity.class, entity, "hurtResistantTime", 0);
        return entity.attackEntityFrom(source, damage);
    }
    
    public boolean isWithinDistanceOfOwner(double distance) {
    	return owner != null && isWithinDistance(owner, distance);
    }
    
    public boolean isWithinDistance(EntityLiving entity, double distance) {
    	return getPosition().subtract(entity.getPosition(1)).lengthVector() <= distance;
    }
}
