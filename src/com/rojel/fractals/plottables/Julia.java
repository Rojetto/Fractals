package com.rojel.fractals.plottables;

import org.apache.commons.math3.complex.Complex;

import com.rojel.fractals.Plottable;

public class Julia implements Plottable {
	@Override
	public float getPixel(double x, double y, double xRes, double yRes) {
		int maxIterations = 25;
		double bailout = 2;
		Complex c = new Complex(0.4, 0.6);
		
		Complex z = new Complex(x, y);
		int i = 0;
		while (i < maxIterations) {
			z = z.pow(2).add(c);
			if (z.abs() > bailout)
				break;
			i++;
		}
		
		return (float) i / (float) maxIterations;
	}

}
