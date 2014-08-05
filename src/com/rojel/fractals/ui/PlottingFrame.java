package com.rojel.fractals.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import com.rojel.fractals.plottables.Mandelbrot;
import com.rojel.fractals.render.ColorScheme;
import com.rojel.fractals.render.Plotter;
import com.rojel.fractals.render.PlottingListener;

public class PlottingFrame extends JFrame implements PlottingListener {
	private static final long serialVersionUID = -7367816153167274339L;

	private PlottingDisplay display;
	private JProgressBar progress;
	private ColorSchemeDisplay csDisplay;

	public PlottingFrame() {
		super("Plotter");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		ColorScheme scheme = new ColorScheme();
		scheme.putColor(0.0, new Color(100, 0, 0));
		scheme.putColor(0.3, Color.RED);
		
		display = new PlottingDisplay(new Plotter(new Mandelbrot(), scheme));
		this.add(display, BorderLayout.CENTER);

		progress = new JProgressBar(0, 100);
		this.add(progress, BorderLayout.SOUTH);
		
		display.getPlotter().addPlottingListener(this);
		
		csDisplay = new ColorSchemeDisplay(scheme);
		this.add(csDisplay, BorderLayout.NORTH);

		this.pack();

		this.setSize(500, 500);

		display.getPlotter().setCenterX(0);
		display.getPlotter().setCenterY(0);
		display.getPlotter().setWidth(1);
		display.getPlotter().setHeight(1);

		this.setVisible(true);
	}

	public static void main(String[] args) {
		new PlottingFrame();
	}

	@Override
	public void plottingFinished(BufferedImage image) {
	}

	@Override
	public void plottingProgress(int progress) {
		this.progress.setValue(progress);
	}
}
