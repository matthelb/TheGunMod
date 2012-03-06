package net.minecraft.src;

public class PathEntity
{
    private final PathPoint points[];
    private int field_48430_b;

    /** The total length of the path */
    private int pathLength;

    public PathEntity(PathPoint par1ArrayOfPathPoint[])
    {
        points = par1ArrayOfPathPoint;
        pathLength = par1ArrayOfPathPoint.length;
    }

    /**
     * Directs this path to the next point in its array
     */
    public void incrementPathIndex()
    {
        field_48430_b++;
    }

    /**
     * Returns true if this path has reached the end
     */
    public boolean isFinished()
    {
        return field_48430_b >= pathLength;
    }

    public PathPoint func_48425_c()
    {
        if (pathLength > 0)
        {
            return points[pathLength - 1];
        }
        else
        {
            return null;
        }
    }

    public PathPoint func_48429_a(int par1)
    {
        return points[par1];
    }

    public int func_48424_d()
    {
        return pathLength;
    }

    public void func_48421_b(int par1)
    {
        pathLength = par1;
    }

    public int func_48423_e()
    {
        return field_48430_b;
    }

    public void func_48422_c(int par1)
    {
        field_48430_b = par1;
    }

    public Vec3D func_48428_a(Entity par1Entity, int par2)
    {
        double d = (double)points[par2].xCoord + (double)(int)(par1Entity.width + 1.0F) * 0.5D;
        double d1 = points[par2].yCoord;
        double d2 = (double)points[par2].zCoord + (double)(int)(par1Entity.width + 1.0F) * 0.5D;
        return Vec3D.createVector(d, d1, d2);
    }

    public Vec3D func_48420_a(Entity par1Entity)
    {
        return func_48428_a(par1Entity, field_48430_b);
    }

    public boolean func_48427_a(PathEntity par1PathEntity)
    {
        if (par1PathEntity == null)
        {
            return false;
        }

        if (par1PathEntity.points.length != points.length)
        {
            return false;
        }

        for (int i = 0; i < points.length; i++)
        {
            if (points[i].xCoord != par1PathEntity.points[i].xCoord || points[i].yCoord != par1PathEntity.points[i].yCoord || points[i].zCoord != par1PathEntity.points[i].zCoord)
            {
                return false;
            }
        }

        return true;
    }

    public boolean func_48426_a(Vec3D par1Vec3D)
    {
        PathPoint pathpoint = func_48425_c();

        if (pathpoint == null)
        {
            return false;
        }
        else
        {
            return pathpoint.xCoord == (int)par1Vec3D.xCoord && pathpoint.zCoord == (int)par1Vec3D.zCoord;
        }
    }
}
