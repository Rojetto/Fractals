package com.rojel.fractals.plottables;

import com.rojel.fractals.Plottable;

public class Parabola implements Plottable {
	@Override
	public float getPixel(double x, double y, double xRes, double yRes) {
		if (Math.abs(y - function(x)) < 2 * yRes)
			return 1;

		return 0;
	}

	private double function(double x) {
		return x * x;
	}
}
