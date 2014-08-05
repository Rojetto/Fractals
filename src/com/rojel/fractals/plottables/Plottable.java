package com.rojel.fractals.plottables;

import com.rojel.fractals.render.ColorScheme;

public interface Plottable {
	public int getPixel(double x, double y, double xRes, double yRes, ColorScheme scheme);
}
