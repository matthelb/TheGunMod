package net.minecraft.src;

import java.io.*;
import java.util.zip.*;

public class Packet51MapChunk extends Packet
{
    public int field_48109_a;
    public int field_48107_b;
    public int field_48108_c;
    public int field_48105_d;
    public byte field_48106_e[];
    public boolean field_48103_f;
    private int field_48104_g;
    private int field_48110_h;
    private static byte field_48111_i[] = new byte[0];

    public Packet51MapChunk()
    {
        isChunkDataPacket = true;
    }

    public Packet51MapChunk(Chunk par1Chunk, boolean par2, int par3)
    {
        isChunkDataPacket = true;
        field_48109_a = par1Chunk.xPosition;
        field_48107_b = par1Chunk.zPosition;
        field_48103_f = par2;

        if (par2)
        {
            par3 = 65535;
        }

        ExtendedBlockStorage aextendedblockstorage[] = par1Chunk.func_48553_h();
        int i = 0;
        int j = 0;

        for (int k = 0; k < aextendedblockstorage.length; k++)
        {
            if (aextendedblockstorage[k] == null || par2 && aextendedblockstorage[k].func_48595_a() || (par3 & 1 << k) == 0)
            {
                continue;
            }

            field_48108_c |= 1 << k;
            i++;

            if (aextendedblockstorage[k].func_48601_h() != null)
            {
                field_48105_d |= 1 << k;
                j++;
            }
        }

        int l = 2048 * (5 * i + j);

        if (par2)
        {
            l += 256;
        }

        if (field_48111_i.length < l)
        {
            field_48111_i = new byte[l];
        }

        byte abyte0[] = field_48111_i;
        int i1 = 0;

        for (int j1 = 0; j1 < aextendedblockstorage.length; j1++)
        {
            if (aextendedblockstorage[j1] != null && (!par2 || !aextendedblockstorage[j1].func_48595_a()) && (par3 & 1 << j1) != 0)
            {
                byte abyte2[] = aextendedblockstorage[j1].func_48590_g();
                System.arraycopy(abyte2, 0, abyte0, i1, abyte2.length);
                i1 += abyte2.length;
            }
        }

        for (int k1 = 0; k1 < aextendedblockstorage.length; k1++)
        {
            if (aextendedblockstorage[k1] != null && (!par2 || !aextendedblockstorage[k1].func_48595_a()) && (par3 & 1 << k1) != 0)
            {
                NibbleArray nibblearray = aextendedblockstorage[k1].func_48594_i();
                System.arraycopy(nibblearray.data, 0, abyte0, i1, nibblearray.data.length);
                i1 += nibblearray.data.length;
            }
        }

        for (int l1 = 0; l1 < aextendedblockstorage.length; l1++)
        {
            if (aextendedblockstorage[l1] != null && (!par2 || !aextendedblockstorage[l1].func_48595_a()) && (par3 & 1 << l1) != 0)
            {
                NibbleArray nibblearray1 = aextendedblockstorage[l1].func_48600_j();
                System.arraycopy(nibblearray1.data, 0, abyte0, i1, nibblearray1.data.length);
                i1 += nibblearray1.data.length;
            }
        }

        for (int i2 = 0; i2 < aextendedblockstorage.length; i2++)
        {
            if (aextendedblockstorage[i2] != null && (!par2 || !aextendedblockstorage[i2].func_48595_a()) && (par3 & 1 << i2) != 0)
            {
                NibbleArray nibblearray2 = aextendedblockstorage[i2].func_48605_k();
                System.arraycopy(nibblearray2.data, 0, abyte0, i1, nibblearray2.data.length);
                i1 += nibblearray2.data.length;
            }
        }

        if (j > 0)
        {
            for (int j2 = 0; j2 < aextendedblockstorage.length; j2++)
            {
                if (aextendedblockstorage[j2] != null && (!par2 || !aextendedblockstorage[j2].func_48595_a()) && aextendedblockstorage[j2].func_48601_h() != null && (par3 & 1 << j2) != 0)
                {
                    NibbleArray nibblearray3 = aextendedblockstorage[j2].func_48601_h();
                    System.arraycopy(nibblearray3.data, 0, abyte0, i1, nibblearray3.data.length);
                    i1 += nibblearray3.data.length;
                }
            }
        }

        if (par2)
        {
            byte abyte1[] = par1Chunk.func_48552_l();
            System.arraycopy(abyte1, 0, abyte0, i1, abyte1.length);
            i1 += abyte1.length;
        }

        Deflater deflater = new Deflater(-1);

        try
        {
            deflater.setInput(abyte0, 0, i1);
            deflater.finish();
            field_48106_e = new byte[i1];
            field_48104_g = deflater.deflate(field_48106_e);
        }
        finally
        {
            deflater.end();
        }
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        field_48109_a = par1DataInputStream.readInt();
        field_48107_b = par1DataInputStream.readInt();
        field_48103_f = par1DataInputStream.readBoolean();
        field_48108_c = par1DataInputStream.readShort();
        field_48105_d = par1DataInputStream.readShort();
        field_48104_g = par1DataInputStream.readInt();
        field_48110_h = par1DataInputStream.readInt();

        if (field_48111_i.length < field_48104_g)
        {
            field_48111_i = new byte[field_48104_g];
        }

        par1DataInputStream.readFully(field_48111_i, 0, field_48104_g);
        int i = 0;

        for (int j = 0; j < 16; j++)
        {
            i += field_48108_c >> j & 1;
        }

        int k = 12288 * i;

        if (field_48103_f)
        {
            k += 256;
        }

        field_48106_e = new byte[k];
        Inflater inflater = new Inflater();
        inflater.setInput(field_48111_i, 0, field_48104_g);

        try
        {
            inflater.inflate(field_48106_e);
        }
        catch (DataFormatException dataformatexception)
        {
            throw new IOException("Bad compressed data format");
        }
        finally
        {
            inflater.end();
        }
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(field_48109_a);
        par1DataOutputStream.writeInt(field_48107_b);
        par1DataOutputStream.writeBoolean(field_48103_f);
        par1DataOutputStream.writeShort((short)(field_48108_c & 0xffff));
        par1DataOutputStream.writeShort((short)(field_48105_d & 0xffff));
        par1DataOutputStream.writeInt(field_48104_g);
        par1DataOutputStream.writeInt(field_48110_h);
        par1DataOutputStream.write(field_48106_e, 0, field_48104_g);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.func_48070_a(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 17 + field_48104_g;
    }
}
