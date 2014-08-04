package com.rojel.fractals;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Plotter {
	private Plottable plottable;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	private int width;
	private int height;
	
	public Plotter(Plottable plottable) {
		this.plottable = plottable;
		
		this.minX = -1;
		this.minY = -1;
		this.maxX = 1;
		this.maxY = 1;
		this.width = 100;
		this.height = 100;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public int getRenderWidth() {
		return width;
	}

	public void setRenderWidth(int width) {
		this.width = width;
	}

	public int getRenderHeight() {
		return height;
	}

	public void setRenderHeight(int height) {
		this.height = height;
	}
	
	public double getWidth() {
		return maxX - minX;
	}
	
	public void setWidth(double width) {
		this.maxX = this.minX + width;
	}
	
	public double getHeight() {
		return maxY - minY;
	}
	
	public void setHeight(double height) {
		this.maxY = this.minY + height;
	}
	
	public double getCenterX() {
		return (maxX + minX) / 2;
	}
	
	public void setCenterX(double x) {
		this.minX = x - getWidth() / 2f;
		this.maxX = x + getWidth() / 2f;
	}
	
	public double getCenterY() {
		return (maxY + minY) / 2;
	}
	
	public void setCenterY(double y) {
		this.minY = y - getHeight() / 2f;
		this.maxY = y + getHeight() / 2f;
	}

	public BufferedImage plot() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		double xRes = getWidth() / getRenderWidth();
		double yRes = getHeight() / getRenderHeight();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float pixel = plottable.getPixel(screenToPlotX(x), screenToPlotY(y), xRes, yRes);
				
				try {
					image.setRGB(x, y, new Color(1 - pixel, 1 - pixel, 1 - pixel).getRGB());
				} catch (Exception e) {
					
				}
			}
		}
		
		return image;
	}
	
	public double screenToPlotX(double screenX) {
		return getMinX() + screenX / getRenderWidth() * getWidth();
	}
	
	public double screenToPlotY(double screenY) {
		return getMinY() + (getRenderHeight() - screenY) / getRenderHeight() * getHeight();
	}
	
	public int plotToScreenX(double plotX) {
		return (int) ((plotX - getMinX()) * getRenderWidth() / getWidth());
	}
	
	public int plotToScreenY(double plotY) {
		return (int) (getRenderHeight() - (plotY - getMinY()) * getRenderHeight() / getHeight());
	}
}
