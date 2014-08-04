package com.rojel.fractals;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.rojel.fractals.plottables.Julia;

public class PlottingFrame extends JFrame implements PlottingListener {
	private static final long serialVersionUID = -7367816153167274339L;

	private JLabel label;
	private PlottingDisplay display;
	private JProgressBar progress;

	public PlottingFrame() {
		super("Plotter");

		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		double real = (Math.random() - 0.5) * 1.5;
		double imaginary = (Math.random() - 0.5) * 1.5;
		
		label = new JLabel(real + " + " + imaginary + "i");
		this.add(label, BorderLayout.NORTH);
		
		display = new PlottingDisplay(new Plotter(new Julia(real, imaginary)));
		this.add(display, BorderLayout.CENTER);

		progress = new JProgressBar(0, 100);
		this.add(progress, BorderLayout.SOUTH);

		display.getPlotter().addPlottingListener(this);

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
