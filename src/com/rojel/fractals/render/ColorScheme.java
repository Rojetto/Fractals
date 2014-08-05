package com.rojel.fractals.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorScheme {
	private Map<Double, Integer> colorMap;
	
	public ColorScheme() {
		this.colorMap = new HashMap<Double, Integer>();
		colorMap.put(0.0, Color.BLACK.getRGB());
		colorMap.put(1.0, Color.WHITE.getRGB());
	}
	
	public void putColor(double position, int rgb) {
		if (position < 0.0 || position > 1.0) {
			System.out.println("Color position " + position + " out of range.");
			return;
		}
		colorMap.put(position, rgb);
	}
	
	public void putColor(double position, Color color) {
		putColor(position, color.getRGB());
	}
	
	public int getRGB(double position) {
		if (position < 0.0 || position > 1.0) {
			System.out.println("Color position " + position + " out of range.");
			return 0;
		}
		
		double before = getPositionBefore(position);
		double after = getPositionAfter(position);
		Color colorBefore = new Color(colorMap.get(before));
		Color colorAfter = new Color(colorMap.get(after));
		double positionBetween = (position - before) / (after - before);
		
		Color color = new Color((int) (colorBefore.getRed() + (colorAfter.getRed() - colorBefore.getRed()) * positionBetween)
								, (int) (colorBefore.getGreen() + (colorAfter.getGreen() - colorBefore.getGreen()) * positionBetween)
								, (int) (colorBefore.getBlue() + (colorAfter.getBlue() - colorBefore.getBlue()) * positionBetween));
		
		return color.getRGB();
	}
	
	private double getPositionBefore(double position) {
		double before = 0;
		
		for (double candidate : colorMap.keySet())
			if (candidate > before && candidate <= position)
				before = candidate;
		
		return before;
	}
	
	private double getPositionAfter(double position) {
		double after = 1;
		
		for (double candidate : colorMap.keySet())
			if (candidate < after && candidate > position)
				after = candidate;
		
		return after;
	}
}
