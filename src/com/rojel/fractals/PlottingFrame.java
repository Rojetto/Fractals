package com.rojel.fractals;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import com.rojel.fractals.plottables.Julia;

public class PlottingFrame extends JFrame implements ComponentListener {
	private static final long serialVersionUID = -7367816153167274339L;

	private PlottingDisplay display;
	
	public PlottingFrame() {
		super("Plotter");
		this.setLayout(null);
		this.setSize(500, 500);
		this.addComponentListener(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		display = new PlottingDisplay(new Julia());
		display.setLocation(0, 0);
		this.add(display);
		
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new PlottingFrame();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		display.setSize(this.getWidth() - 16, this.getHeight() - 38);
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
}
