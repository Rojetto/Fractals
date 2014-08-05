package com.rojel.fractals.render;

import java.awt.image.BufferedImage;

public interface PlottingListener {
	public void plottingFinished(BufferedImage image);

	public void plottingProgress(int progress);
}
