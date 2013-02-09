package com.heuristix.guns.helper;

import java.util.Random;

public class MathHelper {
	
	private static final float PI = 3.141593f;
	private static final Random RANDOM = new Random();

	private MathHelper() { }
	
	public static int pow(int base, int exp) {
	    int result = 1;
	    while (exp != 0) {
	        if ((exp & 1) != 0) {
	            result *= base;
	        }
	        exp >>= 1;
	        base *= base;
	    }

	    return result;
	}
	
	public static int maxmin(int n, int max, int min) {
		return Math.max(min, Math.min(n, max));
	}
	
	public static float maxmin(float n, float max, float min) {
		return Math.max(min, Math.min(n, max));
	}
	
	public static double maxmin(double n, double max, double min) {
		return Math.max(min, Math.min(n, max));
	}
	
	private static final int b[] = {0x2, 0xC, 0xF0, 0xFF00, 0xFFFF0000};
	private static final int S[] = {1, 2, 4, 8, 16};
	
	public static int log2(int n) {
		
		int r = 0;
		for (int i = 4; i >= 0; i--) {
		  if ((n & b[i]) != 0) {
		    n >>= S[i];
		    r |= S[i];
		  } 
		}
		return r;
	}

	public static int nextInt() {
	    return RANDOM.nextInt();
	}

	public static int nextInt(int n) {
	    return RANDOM.nextInt(n);
	}

	public static double nextGaussian() {
	    return RANDOM.nextGaussian();
	}

	public static float nextFloat() {
	    return RANDOM.nextFloat();
	}

	public static int randomInt(int min, int max) {
	    int realMin = Math.min(min, max);
	    int realMax = Math.max(min, max);
	    int difference = realMax - realMin;
	    if (difference <= 0) {
	        difference++;
	    }
	    return nextInt(++difference) + realMin;
	}

	public static float randomFloat(float min, float max) {
	    return Math.min(min, max) + nextFloat() * Math.abs(max - min);
	}

	public static float toRadians(double deg) {
	    return (float) (deg / 180.0f *PI);
	}

	public static float toDegrees(double radians) {
	    return (float) (radians * 180 / PI);
	}
	
	public static int roundUp(int n, int r) {
		return (int) (Math.ceil((float) n / r) * r);
	}
}