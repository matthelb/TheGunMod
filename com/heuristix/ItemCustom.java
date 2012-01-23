package com.heuristix;

import net.minecraft.src.Item;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 10/28/11
 * Time: 6:18 PM
 */
public abstract class ItemCustom extends Item implements CustomEntity {

    protected ItemCustom(int i) {
        super(i);
    }

}
