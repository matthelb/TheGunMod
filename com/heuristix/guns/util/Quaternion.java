package com.heuristix.guns.util;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/26/11
 * Time: 2:21 PM
 */
public class Quaternion {

    private double x, y, z;
    private float w;

    public Quaternion(Vec3 vec, float angle) {
        this(vec.xCoord, vec.yCoord, vec.zCoord, angle);
    }

    public Quaternion(double x, double y, double z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(float roll, float pitch, float yaw) {
        this.w = MathHelper.cos(roll / 2) * MathHelper.cos(pitch / 2) * MathHelper.cos(yaw / 2) + MathHelper.sin(roll / 2) * MathHelper.sin(pitch / 2) * MathHelper.sin(yaw / 2);
        this.x = MathHelper.sin(roll / 2) * MathHelper.cos(pitch / 2) * MathHelper.cos(yaw / 2) - MathHelper.cos(roll / 2) * MathHelper.sin(pitch / 2) * MathHelper.sin(yaw / 2);
        this.y = MathHelper.cos(roll / 2) * MathHelper.sin(pitch / 2) * MathHelper.cos(yaw / 2) + MathHelper.sin(roll / 2) * MathHelper.cos(pitch / 2) * MathHelper.sin(yaw / 2);
        this.z = MathHelper.cos(roll / 2) * MathHelper.cos(pitch / 2) * MathHelper.sin(yaw / 2) - MathHelper.sin(roll / 2) * MathHelper.sin(pitch / 2) * MathHelper.cos(yaw / 2);
    }

    public double getSquaredSum() {
        return x * x + y * y + z * z + w * w;
    }

    public double getMagnitude() {
        return getMagnitude(getSquaredSum());
    }

    public double getMagnitude(double squaredSum) {
        return Math.sqrt(squaredSum);
    }

    public Quaternion normalize() {
        double magnitude = getMagnitude();
        x /= magnitude;
        y /= magnitude;
        z /= magnitude;
        w /= magnitude;
        return this;
    }

    public Quaternion multiply(Quaternion quaternion) {
        float newW = (float) (w * quaternion.w - x * quaternion.x - y * quaternion.y - z * quaternion.z);
        double newX = (w * quaternion.x + x * quaternion.w + y * quaternion.z - z * quaternion.y);
        double newY = (w * quaternion.y - x * quaternion.z + y * quaternion.w + z * quaternion.x);
        double newZ = (w * quaternion.z + x * quaternion.y - y * quaternion.x + z * quaternion.w);
        w = newW;
        x = newX;
        y = newY;
        z = newZ;
        return this;
    }

    public Quaternion getRotation(Vec3 axis, float angle) {
        return getRotation(axis.xCoord, axis.yCoord, axis.zCoord, angle);
    }

    public Quaternion getRotation(double x, double y, double z, float angle) {
        return new Quaternion(x * MathHelper.sin(angle / 2), y * MathHelper.sin(angle / 2), z * MathHelper.sin(angle / 2), MathHelper.cos(angle / 2));
    }

    public Quaternion rotate(Vec3 axis, float angle) {
        return rotate(axis.xCoord, axis.yCoord, axis.zCoord, angle);
    }

    public Quaternion rotate(double x, double y, double z, float angle) {
        Quaternion rotation = getRotation(x, y, z, angle);
        Quaternion rotated = rotation.multiply(this);
        this.x = rotated.x;
        this.y = rotated.y;
        this.z = rotated.z;
        this.w = rotated.w;
        return this;
    }

    public float[] getEulerAngles() {
        float[] angles = new float[3];
        angles[0] = (float) Math.atan2(2 * (w * x + y * z), 1 - 2 * (x * x + y * y));
        angles[1] = (float) Math.asin(2 * (w * y - z * x));
        angles[2] = (float) Math.atan2(2 * (w * z + x * y), 1 - 2 * (y * y + z * z));
        return angles;
    }


}
