package com.heuristix;

import com.heuristix.asm.ByteVector;
import com.heuristix.util.Buffer;
import com.heuristix.util.Log;
import com.heuristix.util.Pair;
import com.heuristix.util.ReverseBuffer;
import net.minecraft.src.mod_Guns;

import java.io.IOException;
import java.io.OutputStream;
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

    public static final int MAGIC = 0x47554E54;
    public static final int OLD_MAGIC = 0x47554E53;
    public static final int OLDER_MAGIC = 0x47554E4D;

    public static final int CLASSES = 3;
    public static final int RESOURCES = 4;

    private List<Pair<String, byte[]>> clazzes;
    private Map<String, byte[]> resources;
    private Map<String, int[]> properties;

    private int itemBulletId, itemGunId;

    public Gun(byte[] bytes) {
        this(new Buffer(bytes));
    }

    public Gun(Buffer buffer) {
        this.clazzes = new LinkedList<Pair<String, byte[]>>();
        this.resources = new HashMap<String, byte[]>();
        this.properties = new HashMap<String, int[]>();
        if (!read(buffer)) {
            throw new IllegalArgumentException("Incorrectly formatted GUN2 file.");
        }
    }

    public final boolean read(Buffer buffer) throws ArrayIndexOutOfBoundsException{
        try {
            int magic = buffer.readInt();
            return (magic == MAGIC) ? readPriv(buffer) : (magic == OLD_MAGIC) ? readOld(buffer) : (magic == OLDER_MAGIC) ? readOlder(buffer) : false;
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.throwing(getClass(), "read(Buffer buffer)", e, mod_Guns.class);
        }
        return false;
    }

    private boolean readPriv(Buffer buffer) {
        int classes = buffer.readInt();
        while (classes-- > 0) {
            String name = buffer.readString();
            int length = buffer.readInt();
            byte[] bytes = buffer.readBytes(length);
            clazzes.add(new Pair(name, bytes));
        }
        int resources = buffer.readInt();
        while (resources-- > 0) {
            String key = buffer.readString();
            int length = buffer.readInt();
            byte[] bytes = buffer.readBytes(length);
            this.resources.put(key, bytes);
        }
        int properties = buffer.readInt();
        while (properties-- > 0) {
            String key = buffer.readString();
            int length = buffer.readInt();
            int[] bytes = Util.getIntArray(buffer.readBytes(length));
            this.properties.put(key, bytes);
        }
        return true;
    }

    private boolean readOld(Buffer buffer) {
        int classes = buffer.readInt();
        while (classes-- > 0) {
            String name = buffer.readString();
            int length = buffer.readInt();
            byte[] bytes = buffer.readBytes(length);
            clazzes.add(new Pair(name, bytes));
        }
        int resources = buffer.readInt();
        int begin = resources;
        while (resources-- > 0) {
            int length = buffer.readInt();
            putOldResource(buffer.readBytes(length), begin - resources - 1);
        }
        int properties = buffer.readInt();
        while (properties-- > 0) {
            String key = buffer.readString();
            int length = buffer.readInt();
            int[] bytes = Util.getIntArray(buffer.readBytes(length));
            this.properties.put(key, bytes);
        }
        return true;
    }

    private boolean readOlder(Buffer buffer) {
        int classes = buffer.readInt();
        while (classes-- > 0) {
            String name = buffer.readString();
            int length = buffer.readInt();
            byte[] bytes = buffer.readBytes(length);
            clazzes.add(new Pair(name, bytes));
        }
        int resources = buffer.readInt();
        int begin = resources;
        while (resources-- > 0) {
            int length = buffer.readInt();
            putOldResource(buffer.readBytes(length), begin - resources - 1);
        }
        itemBulletId = buffer.readInt();
        itemGunId = buffer.readInt();
        properties.put("itemGunId", ReverseBuffer.getInt(itemGunId));
        properties.put("itemBulletId", ReverseBuffer.getInt(itemBulletId));
        return true;
    }

    public void write(OutputStream out) throws IOException {
        ByteVector outBytes = new ByteVector();
        outBytes.putInt(MAGIC);
        outBytes.putInt(clazzes.size());
        for (int i = 0; i < clazzes.size(); i++) {
            byte[] stringBytes = Util.getStringBytes(clazzes.get(i).getFirst());
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            byte[] bytes = clazzes.get(i).getSecond();
            outBytes.putInt(bytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }
        outBytes.putInt(resources.size());
        for (Map.Entry<String, byte[]> entry : resources.entrySet()) {
            byte[] stringBytes = Util.getStringBytes(entry.getKey());
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            byte[] bytes = entry.getValue();
            outBytes.putInt(bytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }
        outBytes.putInt(properties.size());
        for (Map.Entry<String, int[]> property : properties.entrySet()) {
            byte[] stringBytes = Util.getStringBytes(property.getKey());
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            byte[] bytes = Util.getByteArray(property.getValue());
            outBytes.putInt(bytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }
        out.write(outBytes.toByteArray());
    }

    private void putOldResource(byte[] bytes, int index) {
        switch(index) {
            case 0:
                resources.put("16x16projectile", bytes);
                break;
            case 1:
                resources.put("16x16gun", bytes);
                break;
            case 2:
                resources.put("shootSound", bytes);
                break;
            case 3:
                resources.put("reloadSound", bytes);
                break;
            default:
                break;
        }
    }

    public List<Pair<String, byte[]>> getClasses() {
        return clazzes;
    }

    public Map<String, byte[]> getResources() {
        return resources;
    }

    public Map<String, int[]> getProperties() {
        return properties;
    }

    public int getItemGunId() {
        int[] bytes = properties.get("itemGunId");
        return ((bytes[0] & 0xFF) << 24) + ((bytes[1] & 0xFF) << 16) + ((bytes[2] & 0xFF) << 8) + (bytes[3] & 0xFF);
    }

    public int getItemProjectileId() {
        int[] bytes = properties.get("itemProjectileId");
        if(bytes == null) {
            bytes = properties.get("itemBulletId");
        }
        return ((bytes[0] & 0xFF) << 24) + ((bytes[1] & 0xFF) << 16) + ((bytes[2] & 0xFF) << 8) + (bytes[3] & 0xFF);
    }
}
