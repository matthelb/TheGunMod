package net.minecraft.src;

import com.heuristix.*;
import com.heuristix.test.EntityGuidedRocketGrenade;
import com.heuristix.test.ItemGuidedRocketLauncher;
import com.heuristix.test.ItemRocketGrenade;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 10/14/11
 * Time: 5:03 PM
 */
public class mod_Test extends Mod {


    public mod_Test() {
        ItemRocketGrenade rocketGrenade = new ItemRocketGrenade(6001);
        ItemGuidedRocketLauncher gun = new ItemGuidedRocketLauncher(6000, rocketGrenade);
        rocketGrenade.putProjectileClass(gun, EntityGuidedRocketGrenade.class);
        registerItem(rocketGrenade);
        registerItem(gun);
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

}
