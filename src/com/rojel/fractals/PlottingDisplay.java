package com.rojel.fractals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class PlottingDisplay extends JComponent implements MouseListener, ComponentListener, PlottingListener {
	private static final long serialVersionUID = -8158869774426846742L;
	
	private Plotter plotter;
	private BufferedImage lastPlot;
	private TweenManager manager;
	
	public PlottingDisplay(Plotter plotter) {
		this.plotter = plotter;
		this.plotter.addPlottingListener(this);
		
		this.addMouseListener(this);
		this.addComponentListener(this);
		
		Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor(Plotter.class, new PlotterAccessor());
		manager = new TweenManager();
		
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					manager.update(1f / 40f);
					if (manager.getRunningTweensCount() > 0) {
						plotter.plot();
					}
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(lastPlot, 0, 0, plotter.getRenderWidth(), plotter.getRenderHeight(), null);
		
		g.setColor(Color.RED);
		
		int xHeight = plotter.plotToScreenY(0);
		if (xHeight < 0)
			xHeight = 0;
		if (xHeight >= plotter.getRenderHeight())
			xHeight = plotter.getRenderHeight() - 1;
		
		g.drawLine(0, xHeight, getWidth(), xHeight);
		
		for (int i = (int) plotter.screenToPlotX(0); i <= (int) plotter.screenToPlotX(getWidth()) + 1; i++)
			g.drawString("" + i, plotter.plotToScreenX(i) + 2, xHeight + 12);
		
		int yWidth = plotter.plotToScreenX(0);
		if (yWidth < 0)
			yWidth = 0;
		if (yWidth >= plotter.getRenderWidth())
			yWidth = plotter.getRenderWidth() - 1;
		
		g.drawLine(yWidth, 0, yWidth, getHeight());
		
		for (int i = (int) plotter.screenToPlotY(getHeight()) - 1; i <= (int) plotter.screenToPlotY(0); i++)
			if (i != 0)
				g.drawString("" + i, yWidth - 12, plotter.plotToScreenY(i) - 2);
	}

	public Plotter getPlotter() {
		return plotter;
	}

	public void setPlotter(Plotter plotter) {
		this.plotter = plotter;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		
		double plotX = plotter.screenToPlotX(mouseX);
		double plotY = plotter.screenToPlotY(mouseY);
		
		System.out.println(plotX + " " + plotY);
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			Tween.set(plotter, PlotterAccessor.CENTER_WIDTH_HEIGHT)
			.target((float) plotX, (float) plotY, (float) plotter.getWidth() * 0.5f, (float) plotter.getHeight() * 0.5f)
			.start(manager);
		}
		
		if (e.getButton() == MouseEvent.BUTTON2) {
			Tween.set(plotter, PlotterAccessor.CENTER)
			.target((float) plotX, (float) plotY, (float) plotter.getWidth() * 2f, (float) plotter.getHeight() * 2f)
			.start(manager);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		plotter.setWidth(plotter.getWidth() * this.getWidth() / plotter.getRenderWidth());
		plotter.setRenderWidth(this.getWidth());
		plotter.setHeight(plotter.getHeight() * this.getHeight() / plotter.getRenderHeight());
		plotter.setRenderHeight(this.getHeight());
		
		plotter.plot();
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

	@Override
	public void plottingFinished(BufferedImage image) {
		lastPlot = image;
		repaint();
	}

	@Override
	public void plottingProgress(int progress) {
	}
}
