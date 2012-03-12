package net.minecraft.src;

public class PathNavigate
{
    private EntityLiving field_46039_a;
    private World field_46037_b;
    private PathEntity field_46038_c;
    private float field_46036_d;
    private float field_48672_e;
    private boolean noSunPathfind;
    private int field_48671_g;
    private int field_48677_h;
    private Vec3D field_48678_i;
    private boolean field_48675_j;
    private boolean field_48676_k;
    private boolean field_48673_l;
    private boolean field_48674_m;

    public PathNavigate(EntityLiving par1EntityLiving, World par2World, float par3)
    {
        noSunPathfind = false;
        field_48678_i = Vec3D.createVectorHelper(0.0D, 0.0D, 0.0D);
        field_48675_j = true;
        field_48676_k = false;
        field_48673_l = false;
        field_48674_m = false;
        field_46039_a = par1EntityLiving;
        field_46037_b = par2World;
        field_48672_e = par3;
    }

    public void func_48656_a(boolean par1)
    {
        field_48673_l = par1;
    }

    public boolean func_48649_a()
    {
        return field_48673_l;
    }

    public void func_48663_b(boolean par1)
    {
        field_48676_k = par1;
    }

    public void func_48655_c(boolean par1)
    {
        field_48675_j = par1;
    }

    public boolean func_48657_b()
    {
        return field_48676_k;
    }

    public void func_48669_d(boolean par1)
    {
        noSunPathfind = par1;
    }

    public void func_48654_a(float par1)
    {
        field_46036_d = par1;
    }

    public void func_48660_e(boolean par1)
    {
        field_48674_m = par1;
    }

    public PathEntity func_48650_a(double par1, double par3, double par5)
    {
        if (!canNavigate())
        {
            return null;
        }
        else
        {
            return field_46037_b.getEntityPathToXYZ(field_46039_a, MathHelper.floor_double(par1), (int)par3, MathHelper.floor_double(par5), field_48672_e, field_48675_j, field_48676_k, field_48673_l, field_48674_m);
        }
    }

    public boolean func_48658_a(double par1, double par3, double par5, float par7)
    {
        PathEntity pathentity = func_48650_a(MathHelper.floor_double(par1), (int)par3, MathHelper.floor_double(par5));
        return setPath(pathentity, par7);
    }

    public PathEntity func_48661_a(EntityLiving par1EntityLiving)
    {
        if (!canNavigate())
        {
            return null;
        }
        else
        {
            return field_46037_b.getPathEntityToEntity(field_46039_a, par1EntityLiving, field_48672_e, field_48675_j, field_48676_k, field_48673_l, field_48674_m);
        }
    }

    public boolean func_48652_a(EntityLiving par1EntityLiving, float par2)
    {
        PathEntity pathentity = func_48661_a(par1EntityLiving);

        if (pathentity != null)
        {
            return setPath(pathentity, par2);
        }
        else
        {
            return false;
        }
    }

    /**
     * sets the active path data if path is 100% unique compared to old path, checks to adjust path for sun avoiding
     * ents and stores end coords
     */
    public boolean setPath(PathEntity par1PathEntity, float par2)
    {
        if (par1PathEntity == null)
        {
            field_46038_c = null;
            return false;
        }

        if (!par1PathEntity.func_48427_a(field_46038_c))
        {
            field_46038_c = par1PathEntity;
        }

        if (noSunPathfind)
        {
            removeSunnyPath();
        }

        if (field_46038_c.getCurrentPathLength() == 0)
        {
            return false;
        }
        else
        {
            field_46036_d = par2;
            Vec3D vec3d = func_48665_h();
            field_48677_h = field_48671_g;
            field_48678_i.xCoord = vec3d.xCoord;
            field_48678_i.yCoord = vec3d.yCoord;
            field_48678_i.zCoord = vec3d.zCoord;
            return true;
        }
    }

    public PathEntity func_48668_c()
    {
        return field_46038_c;
    }

    public void onUpdateNavigation()
    {
        field_48671_g++;

        if (noPath())
        {
            return;
        }

        if (canNavigate())
        {
            pathFollow();
        }

        if (noPath())
        {
            return;
        }

        Vec3D vec3d = field_46038_c.getPosition(field_46039_a);

        if (vec3d == null)
        {
            return;
        }
        else
        {
            field_46039_a.getMoveHelper().func_48439_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, field_46036_d);
            return;
        }
    }

    private void pathFollow()
    {
        Vec3D vec3d = func_48665_h();
        int i = field_46038_c.getCurrentPathLength();
        int f = field_46038_c.getCurrentPathIndex();

        do
        {
            if (f >= field_46038_c.getCurrentPathLength())
            {
                break;
            }

            if (field_46038_c.getPathPointFromIndex(f).yCoord != (int)vec3d.yCoord)
            {
                i = f;
                break;
            }

            f++;
        }
        while (true);

        float fa = field_46039_a.width * field_46039_a.width;

        for (int j = field_46038_c.getCurrentPathIndex(); j < i; j++)
        {
            if (vec3d.squareDistanceTo(field_46038_c.func_48428_a(field_46039_a, j)) < (double)fa)
            {
                field_46038_c.setCurrentPathIndex(j + 1);
            }
        }

        int k = (int)Math.ceil(field_46039_a.width);
        int l = (int)field_46039_a.height + 1;
        int i1 = k;
        int j1 = i - 1;

        do
        {
            if (j1 < field_46038_c.getCurrentPathIndex())
            {
                break;
            }

            if (func_48653_a(vec3d, field_46038_c.func_48428_a(field_46039_a, j1), k, l, i1))
            {
                field_46038_c.setCurrentPathIndex(j1);
                break;
            }

            j1--;
        }
        while (true);

        if (field_48671_g - field_48677_h > 100)
        {
            if (vec3d.squareDistanceTo(field_48678_i) < 2.25D)
            {
                func_48662_f();
            }

            field_48677_h = field_48671_g;
            field_48678_i.xCoord = vec3d.xCoord;
            field_48678_i.yCoord = vec3d.yCoord;
            field_48678_i.zCoord = vec3d.zCoord;
        }
    }

    /**
     * If null path or reached the end
     */
    public boolean noPath()
    {
        return field_46038_c == null || field_46038_c.isFinished();
    }

    public void func_48662_f()
    {
        field_46038_c = null;
    }

    private Vec3D func_48665_h()
    {
        return Vec3D.createVector(field_46039_a.posX, func_48659_i(), field_46039_a.posZ);
    }

    private int func_48659_i()
    {
        if (!field_46039_a.isInWater() || !field_48674_m)
        {
            return (int)(field_46039_a.boundingBox.minY + 0.5D);
        }

        int i = (int)field_46039_a.boundingBox.minY;
        int j = field_46037_b.getBlockId(MathHelper.floor_double(field_46039_a.posX), i, MathHelper.floor_double(field_46039_a.posZ));
        int k = 0;

        while (j == Block.waterMoving.blockID || j == Block.waterStill.blockID)
        {
            i++;
            j = field_46037_b.getBlockId(MathHelper.floor_double(field_46039_a.posX), i, MathHelper.floor_double(field_46039_a.posZ));

            if (++k > 16)
            {
                return (int)field_46039_a.boundingBox.minY;
            }
        }

        return i;
    }

    /**
     * If on ground or swimming and can swim
     */
    private boolean canNavigate()
    {
        return field_46039_a.onGround || field_48674_m && func_48648_k();
    }

    private boolean func_48648_k()
    {
        return field_46039_a.isInWater() || field_46039_a.handleLavaMovement();
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    private void removeSunnyPath()
    {
        if (field_46037_b.canBlockSeeTheSky(MathHelper.floor_double(field_46039_a.posX), (int)(field_46039_a.boundingBox.minY + 0.5D), MathHelper.floor_double(field_46039_a.posZ)))
        {
            return;
        }

        for (int i = 0; i < field_46038_c.getCurrentPathLength(); i++)
        {
            PathPoint pathpoint = field_46038_c.getPathPointFromIndex(i);

            if (field_46037_b.canBlockSeeTheSky(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord))
            {
                field_46038_c.setCurrentPathLength(i - 1);
                return;
            }
        }
    }

    private boolean func_48653_a(Vec3D par1Vec3D, Vec3D par2Vec3D, int par3, int par4, int par5)
    {
        int i = MathHelper.floor_double(par1Vec3D.xCoord);
        int j = MathHelper.floor_double(par1Vec3D.zCoord);
        double d = par2Vec3D.xCoord - par1Vec3D.xCoord;
        double d1 = par2Vec3D.zCoord - par1Vec3D.zCoord;
        double d2 = d * d + d1 * d1;

        if (d2 < 1E-008D)
        {
            return false;
        }

        double d3 = 1.0D / Math.sqrt(d2);
        d *= d3;
        d1 *= d3;
        par3 += 2;
        par5 += 2;

        if (!func_48646_a(i, (int)par1Vec3D.yCoord, j, par3, par4, par5, par1Vec3D, d, d1))
        {
            return false;
        }

        par3 -= 2;
        par5 -= 2;
        double d4 = 1.0D / Math.abs(d);
        double d5 = 1.0D / Math.abs(d1);
        double d6 = (double)(i * 1) - par1Vec3D.xCoord;
        double d7 = (double)(j * 1) - par1Vec3D.zCoord;

        if (d >= 0.0D)
        {
            d6++;
        }

        if (d1 >= 0.0D)
        {
            d7++;
        }

        d6 /= d;
        d7 /= d1;
        byte byte0 = ((byte)(d >= 0.0D ? 1 : -1));
        byte byte1 = ((byte)(d1 >= 0.0D ? 1 : -1));
        int k = MathHelper.floor_double(par2Vec3D.xCoord);
        int l = MathHelper.floor_double(par2Vec3D.zCoord);
        int i1 = k - i;

        for (int j1 = l - j; i1 * byte0 > 0 || j1 * byte1 > 0;)
        {
            if (d6 < d7)
            {
                d6 += d4;
                i += byte0;
                i1 = k - i;
            }
            else
            {
                d7 += d5;
                j += byte1;
                j1 = l - j;
            }

            if (!func_48646_a(i, (int)par1Vec3D.yCoord, j, par3, par4, par5, par1Vec3D, d, d1))
            {
                return false;
            }
        }

        return true;
    }

    private boolean func_48646_a(int par1, int par2, int par3, int par4, int par5, int par6, Vec3D par7Vec3D, double par8, double par10)
    {
        int i = par1 - par4 / 2;
        int j = par3 - par6 / 2;

        if (!func_48666_b(i, par2, j, par4, par5, par6, par7Vec3D, par8, par10))
        {
            return false;
        }

        for (int k = i; k < i + par4; k++)
        {
            for (int l = j; l < j + par6; l++)
            {
                double d = ((double)k + 0.5D) - par7Vec3D.xCoord;
                double d1 = ((double)l + 0.5D) - par7Vec3D.zCoord;

                if (d * par8 + d1 * par10 < 0.0D)
                {
                    continue;
                }

                int i1 = field_46037_b.getBlockId(k, par2 - 1, l);

                if (i1 <= 0)
                {
                    return false;
                }

                Material material = Block.blocksList[i1].blockMaterial;

                if (material == Material.water && !field_46039_a.isInWater())
                {
                    return false;
                }

                if (material == Material.lava)
                {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean func_48666_b(int par1, int par2, int par3, int par4, int par5, int par6, Vec3D par7Vec3D, double par8, double par10)
    {
        for (int i = par1; i < par1 + par4; i++)
        {
            for (int j = par2; j < par2 + par5; j++)
            {
                for (int k = par3; k < par3 + par6; k++)
                {
                    double d = ((double)i + 0.5D) - par7Vec3D.xCoord;
                    double d1 = ((double)k + 0.5D) - par7Vec3D.zCoord;

                    if (d * par8 + d1 * par10 < 0.0D)
                    {
                        continue;
                    }

                    int l = field_46037_b.getBlockId(i, j, k);

                    if (l > 0 && !Block.blocksList[l].getBlocksMovement(field_46037_b, i, j, k))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
