package com.rojel.fractals.render;

import java.awt.image.BufferedImage;

public class PlottingThread extends Thread {
	private Plotter plotter;
	private boolean cancelled;

	public PlottingThread(Plotter plotter) {
		this.plotter = plotter;
		this.cancelled = false;
	}

	@Override
	public void run() {
		BufferedImage image = new BufferedImage(plotter.getRenderWidth(), plotter.getRenderHeight(), BufferedImage.TYPE_INT_RGB);
		double xRes = plotter.getWidth() / plotter.getRenderWidth();
		double yRes = plotter.getHeight() / plotter.getRenderHeight();

		int lastProgress = 0;

		for (int y = 0; y < plotter.getRenderHeight(); y++) {
			for (int x = 0; x < plotter.getRenderWidth(); x++) {
				if (cancelled)
					return;
				
				int rgb = plotter.getPlottable().getPixel(plotter.screenToPlotX(x), plotter.screenToPlotY(y), xRes, yRes);
				try {
					image.setRGB(x, y, rgb);
				} catch (Exception e) {
				}

				int progress = (int) ((float) (y * plotter.getRenderWidth() + x) / (float) (plotter.getRenderWidth() * plotter.getRenderHeight()) * 100f) + 1;
				if (lastProgress != progress) {
					lastProgress = progress;
					for (PlottingListener listener : plotter.getListeners())
						listener.plottingProgress(progress);
				}
			}
		}

		for (PlottingListener listener : plotter.getListeners())
			listener.plottingFinished(image);
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
