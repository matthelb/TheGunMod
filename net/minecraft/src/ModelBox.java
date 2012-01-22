package net.minecraft.src;

public class ModelBox
{
    private PositionTextureVertex field_40679_h[];
    private TexturedQuad field_40680_i[];
    public final float x;
    public final float y;
    public final float z;
    public final float x1;
    public final float y1;
    public final float z1;
    public String field_40673_g;

    public ModelBox(ModelRenderer modelrenderer, int i, int j, float x, float y, float z, int width,
            int length, int depth, float f3)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        x1 = x + (float)width;
        y1 = y + (float)length;
        z1 = z + (float)depth;
        field_40679_h = new PositionTextureVertex[8];
        field_40680_i = new TexturedQuad[6];
        float f4 = x + (float)width;
        float f5 = y + (float)length;
        float f6 = z + (float)depth;
        x -= f3;
        y -= f3;
        z -= f3;
        f4 += f3;
        f5 += f3;
        f6 += f3;
        if (modelrenderer.mirror)
        {
            float f7 = f4;
            f4 = x;
            x = f7;
        }
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f4, y, z, 0.0F, 8F);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(f4, f5, z, 8F, 8F);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(x, f5, z, 8F, 0.0F);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(x, y, f6, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f4, y, f6, 0.0F, 8F);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(f4, f5, f6, 8F, 8F);
        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(x, f5, f6, 8F, 0.0F);
        field_40679_h[0] = positiontexturevertex;
        field_40679_h[1] = positiontexturevertex1;
        field_40679_h[2] = positiontexturevertex2;
        field_40679_h[3] = positiontexturevertex3;
        field_40679_h[4] = positiontexturevertex4;
        field_40679_h[5] = positiontexturevertex5;
        field_40679_h[6] = positiontexturevertex6;
        field_40679_h[7] = positiontexturevertex7;
        field_40680_i[0] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex5, positiontexturevertex1, positiontexturevertex2, positiontexturevertex6
                }, i + depth + width, j + depth, i + depth + width + depth, j + depth + length, modelrenderer.textureWidth, modelrenderer.textureHeight);
        field_40680_i[1] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex, positiontexturevertex4, positiontexturevertex7, positiontexturevertex3
                }, i + 0, j + depth, i + depth, j + depth + length, modelrenderer.textureWidth, modelrenderer.textureHeight);
        field_40680_i[2] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex5, positiontexturevertex4, positiontexturevertex, positiontexturevertex1
                }, i + depth, j + 0, i + depth + width, j + depth, modelrenderer.textureWidth, modelrenderer.textureHeight);
        field_40680_i[3] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex2, positiontexturevertex3, positiontexturevertex7, positiontexturevertex6
                }, i + depth + width, j + depth, i + depth + width + width, j + 0, modelrenderer.textureWidth, modelrenderer.textureHeight);
        field_40680_i[4] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex1, positiontexturevertex, positiontexturevertex3, positiontexturevertex2
                }, i + depth, j + depth, i + depth + width, j + depth + length, modelrenderer.textureWidth, modelrenderer.textureHeight);
        field_40680_i[5] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex4, positiontexturevertex5, positiontexturevertex6, positiontexturevertex7
                }, i + depth + width + depth, j + depth, i + depth + width + depth + width, j + depth + length, modelrenderer.textureWidth, modelrenderer.textureHeight);
        if (modelrenderer.mirror)
        {
            for (int j1 = 0; j1 < field_40680_i.length; j1++)
            {
                field_40680_i[j1].flipFace();
            }
        }
    }

    public void func_40670_a(Tessellator tessellator, float f)
    {
        for (int i = 0; i < field_40680_i.length; i++)
        {
            field_40680_i[i].draw(tessellator, f);
        }
    }

    public ModelBox func_40671_a(String s)
    {
        field_40673_g = s;
        return this;
    }
}
