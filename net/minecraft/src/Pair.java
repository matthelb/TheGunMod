// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;


public class Pair
{

    private final Object left;
    private final Object right;

    public Pair(Object obj, Object obj1)
    {
        left = obj;
        right = obj1;
    }

    public Object getLeft()
    {
        return left;
    }

    public Object getRight()
    {
        return right;
    }

    public int hashCode()
    {
        return left.hashCode() ^ right.hashCode();
    }

    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        if(!(obj instanceof Pair))
        {
            return false;
        } else
        {
            Pair pair = (Pair)obj;
            return left.equals(pair.getLeft()) && right.equals(pair.getRight());
        }
    }
}
