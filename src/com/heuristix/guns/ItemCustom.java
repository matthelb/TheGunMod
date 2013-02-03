package com.heuristix.guns;

import net.minecraft.item.Item;

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
