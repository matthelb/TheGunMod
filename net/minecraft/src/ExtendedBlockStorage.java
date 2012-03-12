package net.minecraft.src;

public class ExtendedBlockStorage
{
    private int field_48615_a;

    /**
     * A total count of the number of non-air blocks in this block storage's Chunk.
     */
    private int blockRefCount;

    /**
     * Contains the number of blocks in this block storage's parent chunk that require random ticking. Used to cull the
     * Chunk from random tick updates for performance reasons.
     */
    private int tickRefCount;
    private byte blockLSBArray[];

    /**
     * Contains the most significant 4 bits of each block ID belonging to this block storage's parent Chunk.
     */
    private NibbleArray blockMSBArray;
    private NibbleArray field_48609_f;

    /** The NibbleArray containing a block of Block-light data. */
    private NibbleArray blocklightArray;

    /** The NibbleArray containing a block of Sky-light data. */
    private NibbleArray skylightArray;

    public ExtendedBlockStorage(int par1)
    {
        field_48615_a = par1;
        blockLSBArray = new byte[4096];
        field_48609_f = new NibbleArray(blockLSBArray.length, 4);
        skylightArray = new NibbleArray(blockLSBArray.length, 4);
        blocklightArray = new NibbleArray(blockLSBArray.length, 4);
    }

    /**
     * Returns the extended block ID for a location in a chunk, merged from a byte array and a NibbleArray to form a
     * full 12-bit block ID.
     */
    public int getExtBlockID(int par1, int par2, int par3)
    {
        int i = blockLSBArray[par2 << 8 | par3 << 4 | par1] & 0xff;

        if (blockMSBArray != null)
        {
            return blockMSBArray.get(par1, par2, par3) << 8 | i;
        }
        else
        {
            return i;
        }
    }

    /**
     * Sets the extended block ID for a location in a chunk, splitting bits 11..8 into a NibbleArray and bits 7..0 into
     * a byte array. Also performs reference counting to determine whether or not to broadly cull this Chunk from the
     * random-update tick list.
     */
    public void setExtBlockID(int par1, int par2, int par3, int par4)
    {
        int i = blockLSBArray[par2 << 8 | par3 << 4 | par1] & 0xff;

        if (blockMSBArray != null)
        {
            i = blockMSBArray.get(par1, par2, par3) << 8 | i;
        }

        if (i == 0 && par4 != 0)
        {
            blockRefCount++;

            if (Block.blocksList[par4] != null && Block.blocksList[par4].getTickRandomly())
            {
                tickRefCount++;
            }
        }
        else if (i != 0 && par4 == 0)
        {
            blockRefCount--;

            if (Block.blocksList[i] != null && Block.blocksList[i].getTickRandomly())
            {
                tickRefCount--;
            }
        }
        else if (Block.blocksList[i] != null && Block.blocksList[i].getTickRandomly() && (Block.blocksList[par4] == null || !Block.blocksList[par4].getTickRandomly()))
        {
            tickRefCount--;
        }
        else if ((Block.blocksList[i] == null || !Block.blocksList[i].getTickRandomly()) && Block.blocksList[par4] != null && Block.blocksList[par4].getTickRandomly())
        {
            tickRefCount++;
        }

        blockLSBArray[par2 << 8 | par3 << 4 | par1] = (byte)(par4 & 0xff);

        if (par4 > 255)
        {
            if (blockMSBArray == null)
            {
                blockMSBArray = new NibbleArray(blockLSBArray.length, 4);
            }

            blockMSBArray.set(par1, par2, par3, (par4 & 0xf00) >> 8);
        }
        else if (blockMSBArray != null)
        {
            blockMSBArray.set(par1, par2, par3, 0);
        }
    }

    public int func_48598_b(int par1, int par2, int par3)
    {
        return field_48609_f.get(par1, par2, par3);
    }

    public void func_48585_b(int par1, int par2, int par3, int par4)
    {
        field_48609_f.set(par1, par2, par3, par4);
    }

    /**
     * Returns whether or not this block storage's Chunk is fully empty, based on its internal reference count.
     */
    public boolean getIsEmpty()
    {
        return blockRefCount == 0;
    }

    /**
     * Returns whether or not this block storage's Chunk will require random ticking, used to avoid looping through
     * random block ticks when there are no blocks that would randomly tick.
     */
    public boolean getNeedsRandomTick()
    {
        return tickRefCount > 0;
    }

    public int func_48597_c()
    {
        return field_48615_a;
    }

    /**
     * Sets the saved Sky-light value in the extended block storage structure.
     */
    public void setExtSkylightValue(int par1, int par2, int par3, int par4)
    {
        skylightArray.set(par1, par2, par3, par4);
    }

    /**
     * Gets the saved Sky-light value in the extended block storage structure.
     */
    public int getExtSkylightValue(int par1, int par2, int par3)
    {
        return skylightArray.get(par1, par2, par3);
    }

    /**
     * Sets the saved Block-light value in the extended block storage structure.
     */
    public void setExtBlocklightValue(int par1, int par2, int par3, int par4)
    {
        blocklightArray.set(par1, par2, par3, par4);
    }

    /**
     * Gets the saved Block-light value in the extended block storage structure.
     */
    public int getExtBlocklightValue(int par1, int par2, int par3)
    {
        return blocklightArray.get(par1, par2, par3);
    }

    public void func_48599_d()
    {
        blockRefCount = 0;
        tickRefCount = 0;

        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                for (int k = 0; k < 16; k++)
                {
                    int l = getExtBlockID(i, j, k);

                    if (l <= 0)
                    {
                        continue;
                    }

                    if (Block.blocksList[l] == null)
                    {
                        blockLSBArray[j << 8 | k << 4 | i] = 0;

                        if (blockMSBArray != null)
                        {
                            blockMSBArray.set(i, j, k, 0);
                        }

                        continue;
                    }

                    blockRefCount++;

                    if (Block.blocksList[l].getTickRandomly())
                    {
                        tickRefCount++;
                    }
                }
            }
        }
    }

    public void func_48603_e()
    {
    }

    public int func_48587_f()
    {
        return blockRefCount;
    }

    public byte[] func_48590_g()
    {
        return blockLSBArray;
    }

    /**
     * Returns the block ID MSB (bits 11..8) array for this storage array's Chunk.
     */
    public NibbleArray getBlockMSBArray()
    {
        return blockMSBArray;
    }

    public NibbleArray func_48594_i()
    {
        return field_48609_f;
    }

    /**
     * Returns the NibbleArray instance containing Block-light data.
     */
    public NibbleArray getBlocklightArray()
    {
        return blocklightArray;
    }

    /**
     * Returns the NibbleArray instance containing Sky-light data.
     */
    public NibbleArray getSkylightArray()
    {
        return skylightArray;
    }

    public void func_48596_a(byte par1ArrayOfByte[])
    {
        blockLSBArray = par1ArrayOfByte;
    }

    public void func_48593_a(NibbleArray par1NibbleArray)
    {
        blockMSBArray = par1NibbleArray;
    }

    public void func_48586_b(NibbleArray par1NibbleArray)
    {
        field_48609_f = par1NibbleArray;
    }

    /**
     * Sets the NibbleArray instance used for Block-light values in this particular storage block.
     */
    public void setBlocklightArray(NibbleArray par1NibbleArray)
    {
        blocklightArray = par1NibbleArray;
    }

    /**
     * Sets the NibbleArray instance used for Sky-light values in this particular storage block.
     */
    public void setSkylightArray(NibbleArray par1NibbleArray)
    {
        skylightArray = par1NibbleArray;
    }
}
