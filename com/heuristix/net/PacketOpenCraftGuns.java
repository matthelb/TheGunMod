package com.heuristix.net;

import net.minecraft.src.Packet100OpenWindow;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/23/12
 * Time: 7:48 PM
 */
public class PacketOpenCraftGuns extends Packet100OpenWindow {

    public static final int INVENTORY_TYPE = 6;

    public PacketOpenCraftGuns(int currentWindowId, String invName, int sizeInventory) {
        super(currentWindowId, INVENTORY_TYPE, invName, sizeInventory);
    }
}
