package net.minecraft.src;

public class PathNavigate
    implements INavigate
{
    private EntityLiving field_46039_a;
    private World field_46037_b;
    private PathEntity field_46038_c;
    private float field_46036_d;

    public PathNavigate(EntityLiving entityliving, World world)
    {
        field_46039_a = entityliving;
        field_46037_b = world;
    }

    public void func_46033_a(double d, double d1, double d2, float f)
    {
        field_46038_c = field_46037_b.getEntityPathToXYZ(field_46039_a, (int)d, (int)d1, (int)d2, 10F);
        field_46036_d = f;
    }

    public void func_46035_a(EntityLiving entityliving, float f)
    {
        field_46038_c = field_46037_b.getPathToEntity(field_46039_a, entityliving, 16F);
        field_46036_d = f;
    }

    public void onUpdateNavigation()
    {
        if (field_46038_c == null)
        {
            return;
        }
        float f = field_46039_a.width;
        Vec3D vec3d;
        for (vec3d = field_46038_c.getPosition(field_46039_a); vec3d != null && vec3d.squareDistanceTo(field_46039_a.posX, vec3d.yCoord, field_46039_a.posZ) < (double)(f * f);)
        {
            field_46038_c.incrementPathIndex();
            if (field_46038_c.isFinished())
            {
                vec3d = null;
                field_46038_c = null;
            }
            else
            {
                vec3d = field_46038_c.getPosition(field_46039_a);
            }
        }

        if (vec3d == null)
        {
            return;
        }
        else
        {
            field_46039_a.getMoveHelper().setMoveSpeed(field_46036_d);
            field_46039_a.getMoveHelper().func_46073_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            return;
        }
    }

    public boolean func_46034_b()
    {
        return field_46038_c == null || field_46038_c.isFinished();
    }
}
