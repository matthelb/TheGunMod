// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            Entity, EntityLiving, MathHelper, World, 
//            Vec3D, MovingObjectPosition, AxisAlignedBB, EntityDamageSource, 
//            Block, BlockLeaves, ModLoader, StepSound, 
//            SoundManager, NBTTagCompound, EntityPlayer, ItemStack, 
//            Item, InventoryPlayer

public class EntityBullet2 extends Entity
{

    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    private boolean inGround;
    public int arrowShake;
    public EntityLiving shootingEntity;
    private int timeTillDeath;
    private int flyTime;
    private EntityPlayer owner;
    private int damage;

    public EntityBullet2(World world)
    {
        super(world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        arrowShake = 0;
        flyTime = 0;
        setSize(0.5F, 0.5F);
    }

    public EntityBullet2(World world, double d, double d1, double d2, 
            double d3, double d4, double d5, EntityPlayer entityplayer)
    {
        super(world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        arrowShake = 0;
        flyTime = 0;
        setSize(0.5F, 0.5F);
        setPosition(d, d1, d2);
        yOffset = 0.0F;
        setVelocity(d3, d4, d5);
        owner = entityplayer;
    }

    public EntityBullet2(World world, double d, double d1, double d2)
    {
        super(world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        arrowShake = 0;
        flyTime = 0;
        setSize(0.5F, 0.5F);
        setPosition(d, d1, d2);
        yOffset = 0.0F;
    }

    public EntityBullet2(World world, EntityLiving entityliving, int i, int j)
    {
        super(world);
        damage = i;
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        arrowShake = 0;
        flyTime = 0;
        shootingEntity = entityliving;
        setSize(0.5F, 0.5F);
        setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = 100F * -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        motionZ = 100F * MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        motionY = 100F * -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
        setArrowHeading(motionX, motionY, motionZ, 1.5F, 1.0F, j);
    }

    protected void entityInit()
    {
    }

    public void setArrowHeading(double d, double d1, double d2, float f, 
            float f1, int i)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += rand.nextGaussian() * 0.0034999998323619365D * (double)f1 * (double)i;
        d1 += rand.nextGaussian() * 0.0034999998323619365D * (double)f1 * (double)i;
        d2 += rand.nextGaussian() * 0.0034999998323619365D * (double)f1 * (double)i;
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / 3.1415927410125732D);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / 3.1415927410125732D);
        timeTillDeath = 0;
    }

    public void setVelocity(double d, double d1, double d2)
    {
        motionX = d;
        motionY = d1;
        motionZ = d2;
        if(prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / 3.1415927410125732D);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f) * 180D) / 3.1415927410125732D);
        }
    }

    public void onUpdate()
    {
        super.onUpdate();
        if(flyTime > 1000)
        {
            setEntityDead();
        }
        if(prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D);
        }
        if(arrowShake > 0)
        {
            arrowShake--;
        }
        if(inGround)
        {
            int i = worldObj.getBlockId(xTile, yTile, zTile);
            if(i != inTile)
            {
                inGround = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                timeTillDeath = 0;
                flyTime = 0;
            } else
            {
                timeTillDeath++;
                if(timeTillDeath == 1200)
                {
                    setEntityDead();
                }
                return;
            }
        } else
        {
            flyTime++;
        }
        Vec3D vec3d = Vec3D.createVector(posX, posY, posZ);
        Vec3D vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3d, vec3d1);
        vec3d = Vec3D.createVector(posX, posY, posZ);
        vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        if(movingobjectposition != null)
        {
            vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }
        Entity entity = null;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;
        for(int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);
            if(!entity1.canBeCollidedWith() || entity1 == shootingEntity && flyTime < 5)
            {
                continue;
            }
            float f4 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.func_1169_a(vec3d, vec3d1);
            if(movingobjectposition1 == null)
            {
                continue;
            }
            double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
            if(d1 < d || d == 0.0D)
            {
                entity = entity1;
                d = d1;
            }
        }

        if(entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }
        if(movingobjectposition != null)
        {
            if(movingobjectposition.entityHit != null)
            {
                if(movingobjectposition.entityHit.attackEntityFrom(new EntityDamageSource("player", owner), damage))
                {
                    worldObj.playSoundAtEntity(this, "BulletHit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
                    setEntityDead();
                } else
                {
                    motionX *= 0.10000000149011612D;
                    motionY *= 0.10000000149011612D;
                    motionZ *= 0.10000000149011612D;
                    flyTime = 0;
                    setEntityDead();
                }
            } else
            {
                xTile = movingobjectposition.blockX;
                yTile = movingobjectposition.blockY;
                zTile = movingobjectposition.blockZ;
                inTile = worldObj.getBlockId(xTile, yTile, zTile);
                if(inTile == Block.glass.blockID || inTile == Block.glowStone.blockID || inTile == Block.leaves.blockID)
                {
                    Block block = Block.blocksList[inTile];
                    ModLoader.getMinecraftInstance().sndManager.playSound(block.stepSound.stepSoundDir(), (float)xTile + 0.5F, (float)yTile + 0.5F, (float)zTile + 0.5F, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                    worldObj.setBlockWithNotify(xTile, yTile, zTile, 0);
                } else
                {
                    motionX = (float)(movingobjectposition.hitVec.xCoord - posX);
                    motionY = (float)(movingobjectposition.hitVec.yCoord - posY);
                    motionZ = (float)(movingobjectposition.hitVec.zCoord - posZ);
                    float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    posX -= (motionX / (double)f1) * 0.05000000074505806D;
                    posY -= (motionY / (double)f1) * 0.05000000074505806D;
                    posZ -= (motionZ / (double)f1) * 0.05000000074505806D;
                    worldObj.playSoundAtEntity(this, "Bullet2Hit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
                    setEntityDead();
                }
            }
        }
        posX += motionX * 3D;
        posY += motionY * 3D;
        posZ += motionZ * 3D;
        float f2 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
        for(rotationPitch = (float)((Math.atan2(motionY, f2) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f3 = 0.99F;
        float f5 = 0.03F;
        if(handleWaterMovement())
        {
            for(int k = 0; k < 4; k++)
            {
                float f6 = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * (double)f6, posY - motionY * (double)f6, posZ - motionZ * (double)f6, motionX, motionY, motionZ);
            }

            f3 = 0.8F;
        }
        motionX *= f3;
        motionY *= f3;
        motionZ *= f3;
        setPosition(posX, posY, posZ);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)xTile);
        nbttagcompound.setShort("yTile", (short)yTile);
        nbttagcompound.setShort("zTile", (short)zTile);
        nbttagcompound.setByte("inTile", (byte)inTile);
        nbttagcompound.setByte("shake", (byte)arrowShake);
        nbttagcompound.setByte("inGround", (byte)(inGround ? 1 : 0));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        xTile = nbttagcompound.getShort("xTile");
        yTile = nbttagcompound.getShort("yTile");
        zTile = nbttagcompound.getShort("zTile");
        inTile = nbttagcompound.getByte("inTile") & 0xff;
        arrowShake = nbttagcompound.getByte("shake") & 0xff;
        inGround = nbttagcompound.getByte("inGround") == 1;
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
        if(worldObj.multiplayerWorld)
        {
            return;
        }
        if(inGround && shootingEntity == entityplayer && arrowShake <= 0 && entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
        {
            worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayer.onItemPickup(this, 1);
            setEntityDead();
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }
}
