package com.rojel.fractals.plottables;


public class Parabola implements Plottable {
	@Override
	public int getPixel(double x, double y, double xRes, double yRes) {
		if (Math.abs(y - function(x)) < 2 * yRes)
			return 1;

		return 0;
	}

	private double function(double x) {
		return x * x;
	}
}
