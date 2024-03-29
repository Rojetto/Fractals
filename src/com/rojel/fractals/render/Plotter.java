package com.rojel.fractals.render;

import java.util.ArrayList;
import java.util.List;

import com.rojel.fractals.plottables.Plottable;

public class Plotter {
	private Plottable plottable;
	private ColorScheme scheme;
	private List<PlottingListener> listeners;
	private PlottingThread plottingThread;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	private int width;
	private int height;

	public Plotter(Plottable plottable, ColorScheme scheme) {
		this.plottable = plottable;
		this.scheme = scheme;
		this.listeners = new ArrayList<PlottingListener>();

		this.minX = -1;
		this.minY = -1;
		this.maxX = 1;
		this.maxY = 1;
		this.width = 100;
		this.height = 100;
	}

	public void addPlottingListener(PlottingListener listener) {
		listeners.add(listener);
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
		double centerX = getCenterX();
		this.minX = centerX - width / 2.0;
		this.maxX = centerX + width / 2.0;
	}

	public double getHeight() {
		return maxY - minY;
	}

	public void setHeight(double height) {
		double centerY = getCenterY();
		this.minY = centerY - height / 2.0;
		this.maxY = centerY + height / 2.0;
	}

	public double getCenterX() {
		return (maxX + minX) / 2;
	}

	public void setCenterX(double x) {
		double width = getWidth();

		this.minX = x - width / 2f;
		this.maxX = x + width / 2f;
	}

	public double getCenterY() {
		return (maxY + minY) / 2;
	}

	public void setCenterY(double y) {
		double height = getHeight();

		this.minY = y - height / 2f;
		this.maxY = y + height / 2f;
	}

	public void plot() {
		if (plottingThread != null)
			plottingThread.setCancelled(true);
		
		plottingThread = new PlottingThread(this);
		plottingThread.start();
	}

	public Plottable getPlottable() {
		return plottable;
	}

	public void setColorScheme(ColorScheme scheme) {
		this.scheme = scheme;
	}

	public ColorScheme getColorScheme() {
		return scheme;
	}

	public List<PlottingListener> getListeners() {
		return new ArrayList<PlottingListener>(listeners);
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
