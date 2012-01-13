package com.heuristix.guns;

import com.heuristix.Util;
import com.heuristix.util.Quaternion;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/22/11
 * Time: 7:17 AM
 */
public class EntityGuidedRocketGrenade extends EntityRocketGrenade {

    public EntityGuidedRocketGrenade(World world, EntityLiving living) {
        super(world, living);
    }

    public void onUpdate() {
        super.onUpdate();
        /*if (getOwner() != null) {
            Vec3D ownerPos = getOwner().getPosition(1);
            Vec3D projectedPos = Util.getProjectedPoint(ownerPos, getOwner().getLook(1), 1000);
            MovingObjectPosition rayTrace = worldObj.rayTraceBlocks(ownerPos, projectedPos);
            Vec3D vec = null;
            if (rayTrace != null) {
                vec = rayTrace.hitVec;
            }
            if (vec == null) {
                vec = projectedPos;
            }
            Vec3D position = Vec3D.createVector(posX, posY, posZ);
            Vec3D direction = vec.subtract(position);
            Vec3D rotationAxis = vec.crossProduct(direction).normalize();
            float theta = (float) Math.acos(direction.dotProduct(vec));
            Quaternion quaternion = new Quaternion(0.0f, rotationPitch, rotationYaw);
            quaternion.rotate(rotationAxis, theta);
            float[] angles = quaternion.getEulerAngles();
            rotationPitch = angles[1];
            rotationYaw = angles[2];
            System.out.println("P: " + rotationPitch + " ; Y: " + rotationYaw);
        }*/
    }

    public float getSpeed() {
        return 0.001f;
    }

}
