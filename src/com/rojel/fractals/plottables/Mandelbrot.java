package com.rojel.fractals.plottables;

import org.apache.commons.math3.complex.Complex;

import com.rojel.fractals.Plottable;

public class Mandelbrot implements Plottable {
	@Override
	public float getPixel(double x, double y, double xRes, double yRes) {
		int maxIterations = 500;
		double bailout = 2;
		Complex c = new Complex(x, y);

		Complex z = new Complex(0, 0);
		int i = 0;
		while (i < maxIterations) {
			z = z.multiply(z).add(c);
			if (z.abs() > bailout)
				break;
			i++;
		}

		return (float) i / (float) maxIterations;
	}
}
