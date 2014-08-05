package com.rojel.fractals.plottables;

import java.awt.Color;

import org.apache.commons.math3.complex.Complex;

import com.rojel.fractals.render.ColorScheme;

public class Mandelbrot implements Plottable {
	@Override
	public int getPixel(double x, double y, double xRes, double yRes, ColorScheme scheme) {
		int maxIterations = (int) (100 + 0.006d / xRes);
		double bailout = 50;
		Complex c = new Complex(x, y);

		Complex z = new Complex(0, 0);
		int i = 0;
		while (i < maxIterations) {
			z = z.multiply(z).add(c);
			if (z.abs() > bailout)
				break;
			i++;
		}

		int color = Color.BLACK.getRGB();
		
		if (i < maxIterations - 1)
			color = scheme.getRGB((double) i / (double) maxIterations);
		
		return color;
	}
}
