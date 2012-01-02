package com.heuristix.util;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/14/11
 * Time: 9:31 PM
 */
public class Buffer {

    private final byte[] bytes;

    private int index;
    public Buffer(byte[] bytes) {
        this.bytes = bytes;
    }

    public int readByte() {
		return bytes[index++] & 0xff;
	}

	public int readShort() {
		index += 2;
		return ((bytes[index - 2] & 0xff) << 8) + (bytes[index - 1] & 0xff);
	}

	public int readInt() {
		index += 4;
		return ((bytes[index - 4] & 0xff) << 24) + ((bytes[index - 3] & 0xff) << 16) + ((bytes[index - 2] & 0xff) << 8) + (bytes[index - 1] & 0xff);
	}

	public long readLong() {
		final long l = readInt() & 0xffffffffL;
		final long r = readInt() & 0xffffffffL;
		return (l << 32) + r;
	}

	public String readString() {
		final int i = index;
		while (bytes[index++] != 10);
		return new String(bytes, i, index - i - 1);
	}

	public byte[] readStringBytes() {
		final int i = index;
		while (bytes[index++] != 10);
		final byte str[] = new byte[index - i - 1];
		System.arraycopy(bytes, i, str, i - i, index - 1 - i);
		return str;
	}

    public byte[] readBytes(final int len) {
        byte[] bytes = new byte[len];
        readBytes(bytes, len, 0);
        return bytes;
    }

	public void readBytes(final byte[] data, final int len, final int off) {
		for (int i = off; i < off + len; i++) {
			data[i] = this.bytes[index++];
		}
	}
}
