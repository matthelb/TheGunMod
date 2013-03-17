package com.heuristix.guns.client.render;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/5/11
 * Time: 7:42 PM
 */
public class RenderBullet extends RenderProjectile {

	private static final float SCALE = 0.02f;
    private static final float GRAY_LIGHTNESS = 0.4f;
    
    public RenderBullet() {
		super(SCALE, SCALE, 2 * SCALE, GRAY_LIGHTNESS, GRAY_LIGHTNESS, GRAY_LIGHTNESS, 1);
	}

}
