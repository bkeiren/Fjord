package com.bryankeiren.fjord;

public class Util {
	public static float lerp(float from, float to, float f) {
		return from + (to - from) * f;
	}

	public static double lerp(double from, double to, double f) {
		return from + (to - from) * f;
	}

	public static float lerpFactor(float min, float max, float v) {
		return (v - min) / (max - min);
	}
}
