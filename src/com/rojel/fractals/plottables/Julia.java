package com.rojel.fractals.plottables;

import java.awt.Color;

import org.apache.commons.math3.complex.Complex;

import com.rojel.fractals.render.ColorScheme;

public class Julia implements Plottable {
	private Complex c;
	
	public Julia(double real, double imaginary) {
		c = new Complex(real, imaginary);
	}
	
	@Override
	public int getPixel(double x, double y, double xRes, double yRes, ColorScheme scheme) {
		int maxIterations = (int) (100 + 0.006d / xRes);;
		double bailout = 50;

		Complex z = new Complex(x, y);
		int i = 0;
		while (i < maxIterations) {
			z = z.pow(2).add(c);
			if (z.abs() > bailout)
				break;
			i++;
		}
		
		if (i == maxIterations)
			return Color.BLACK.getRGB();

		return scheme.getRGB((double) i / (double) maxIterations);
	}
}
