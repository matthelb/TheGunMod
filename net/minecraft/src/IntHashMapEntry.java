package net.minecraft.src;

class IntHashMapEntry
{
    final int hashEntry;
    Object valueEntry;
    IntHashMapEntry nextEntry;
    final int slotHash;

    IntHashMapEntry(int par1, int par2, Object par3Obj, IntHashMapEntry par4IntHashMapEntry)
    {
        valueEntry = par3Obj;
        nextEntry = par4IntHashMapEntry;
        hashEntry = par2;
        slotHash = par1;
    }

    public final int getHash()
    {
        return hashEntry;
    }

    public final Object getValue()
    {
        return valueEntry;
    }

    public final boolean equals(Object par1Obj)
    {
        if (!(par1Obj instanceof IntHashMapEntry))
        {
            return false;
        }

        IntHashMapEntry inthashmapentry = (IntHashMapEntry)par1Obj;
        Integer integer = Integer.valueOf(getHash());
        Integer integer1 = Integer.valueOf(inthashmapentry.getHash());

        if (integer == integer1 || integer != null && integer.equals(integer1))
        {
            Object obj = getValue();
            Object obj1 = inthashmapentry.getValue();

            if (obj == obj1 || obj != null && obj.equals(obj1))
            {
                return true;
            }
        }

        return false;
    }

    public final int hashCode()
    {
        return IntHashMap.getHash(hashEntry);
    }

    public final String toString()
    {
        return (new StringBuilder()).append(getHash()).append("=").append(getValue()).toString();
    }
}
