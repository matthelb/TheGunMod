package com.heuristix;

import com.heuristix.util.OverrideClassAdapter;
import com.heuristix.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/2/11
 * Time: 5:14 PM
 */
public class Gun {

    public static final int MAGIC = 0x47554E4D;

    private List<Pair<String, byte[]>> clazzes;
    private List<byte[]> resources;
    private int itemBulletId, itemGunId;

    public Gun(byte[] bytes) {
        this(new Buffer(bytes));
    }

    public Gun(Buffer buffer) {
        this.clazzes = new LinkedList<Pair<String, byte[]>>();
        this.resources = new LinkedList<byte[]>();
        if(!read(buffer))
            throw new IllegalArgumentException("Incorrectly formatted GUN2 file.");
    }

    public boolean read(Buffer buffer) {
        try {
            if(buffer.readInt() == MAGIC) {
                int classes = buffer.readInt();
                while(classes-- > 0) {
                    String name = buffer.readString();
                    int length = buffer.readInt();
                    byte[] bytes = buffer.readBytes(length);
                    clazzes.add(new Pair(name, bytes));
                }
                int resources = buffer.readInt();
                while(resources-- > 0) {
                    int length = buffer.readInt();
                    this.resources.add(buffer.readBytes(length));
                }
                itemBulletId = buffer.readInt();
                itemGunId = buffer.readInt();
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Pair<String, byte[]>> getClasses() {
        return clazzes;
    }

    public List<byte[]> getResources() {
        return resources;
    }

    public int getItemGunId() {
        return itemGunId;
    }

    public int getItemBulletId() {
        return itemBulletId;
    }
}
