package com.heuristix.guns.client.render;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/26/11
 * Time: 3:43 PM
 */
public class RenderGrenade extends RenderCube {
	
	public static final float SCALE = 0.025f;

    public RenderGrenade() {
		super(SCALE, SCALE, SCALE, 0.2f, 0.5f, 0.2f, 1);
	}

}
