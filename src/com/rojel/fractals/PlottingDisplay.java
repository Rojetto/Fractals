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
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;

public class PlottingDisplay extends JComponent implements MouseListener, ComponentListener, PlottingListener, TweenAccessor<PlottingDisplay> {
	private static final long serialVersionUID = -8158869774426846742L;
	public static final float ZOOM_FACTOR = 5f;
	public static final int IMAGE_X_Y_WIDTH_HEIGHT = 1;

	private Plotter plotter;
	private BufferedImage lastPlot;
	private TweenManager manager;

	private float imageX;
	private float imageY;
	private float imageWidth;
	private float imageHeight;

	public PlottingDisplay(Plotter plotter) {
		this.plotter = plotter;
		this.plotter.addPlottingListener(this);

		this.addMouseListener(this);
		this.addComponentListener(this);

		Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor(Plotter.class, new PlotterAccessor());
		Tween.registerAccessor(PlottingDisplay.class, this);
		manager = new TweenManager();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					manager.update(1f / 40f);
					if (manager.getRunningTweensCount() > 0)
						repaint();
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
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(lastPlot, (int) imageX, (int) imageY, (int) imageWidth, (int) imageHeight, null);

		// g.setColor(Color.RED);
		//
		// int xHeight = plotter.plotToScreenY(0);
		// if (xHeight < 0)
		// xHeight = 0;
		// if (xHeight >= plotter.getRenderHeight())
		// xHeight = plotter.getRenderHeight() - 1;
		//
		// g.drawLine(0, xHeight, getWidth(), xHeight);
		//
		// for (int i = (int) plotter.screenToPlotX(0); i <= (int)
		// plotter.screenToPlotX(getWidth()) + 1; i++)
		// g.drawString("" + i, plotter.plotToScreenX(i) + 2, xHeight + 12);
		//
		// int yWidth = plotter.plotToScreenX(0);
		// if (yWidth < 0)
		// yWidth = 0;
		// if (yWidth >= plotter.getRenderWidth())
		// yWidth = plotter.getRenderWidth() - 1;
		//
		// g.drawLine(yWidth, 0, yWidth, getHeight());
		//
		// for (int i = (int) plotter.screenToPlotY(getHeight()) - 1; i <= (int)
		// plotter.screenToPlotY(0); i++)
		// if (i != 0)
		// g.drawString("" + i, yWidth - 12, plotter.plotToScreenY(i) - 2);
	}

	public Plotter getPlotter() {
		return plotter;
	}

	public void setPlotter(Plotter plotter) {
		this.plotter = plotter;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (manager.getRunningTweensCount() > 0)
			return;
		
		int mouseX = e.getX();
		int mouseY = e.getY();

		double plotX = plotter.screenToPlotX(mouseX);
		double plotY = plotter.screenToPlotY(mouseY);
		
		if (e.getButton() == MouseEvent.BUTTON1) {			
			float newWidth = imageWidth * ZOOM_FACTOR;
			float newHeight = imageHeight * ZOOM_FACTOR;
			float deltaX = (mouseX - this.getWidth() / 2f) * (float) newWidth / (float) this.getWidth();
			float newX = imageX + (imageWidth - newWidth) / 2f - deltaX;
			float deltaY = (mouseY - this.getHeight() / 2f) * (float) newHeight / (float) this.getHeight();
			float newY = imageY + (imageHeight - newHeight) / 2f - deltaY;
			
			plotter.setCenterX(plotX);
			plotter.setCenterY(plotY);
			plotter.setWidth(plotter.getWidth() / ZOOM_FACTOR);
			plotter.setHeight(plotter.getHeight() / ZOOM_FACTOR);

			plotter.plot();
			
			Tween.to(this, IMAGE_X_Y_WIDTH_HEIGHT, 0.8f)
			.target(newX, newY, newWidth, newHeight)
			.ease(Cubic.OUT)
			.start(manager);
		}

		if (e.getButton() == MouseEvent.BUTTON3) {
			float newWidth = imageWidth / ZOOM_FACTOR;
			float newHeight = imageHeight / ZOOM_FACTOR;
			float newX = imageX + (imageWidth - newWidth) / 2f - (mouseX - this.getWidth() / 2f) / ZOOM_FACTOR;
			float newY = imageY + (imageHeight - newHeight) / 2f - (mouseY - this.getHeight() / 2f) / ZOOM_FACTOR;
			
			plotter.setCenterX(plotX);
			plotter.setCenterY(plotY);
			plotter.setWidth(plotter.getWidth() * ZOOM_FACTOR);
			plotter.setHeight(plotter.getHeight() * ZOOM_FACTOR);

			plotter.plot();
			
			Tween.to(this, IMAGE_X_Y_WIDTH_HEIGHT, 0.8f)
			.target(newX, newY, newWidth, newHeight)
			.ease(Cubic.OUT)
			.start(manager);
		}

		if (e.getButton() == MouseEvent.BUTTON2) {
			plotter.setCenterX(plotX);
			plotter.setCenterY(plotY);

			plotter.plot();
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
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					int tweenCount = manager.getRunningTweensCount();
					if (tweenCount == 0)
						break;
					
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				lastPlot = image;
				imageX = 0;
				imageY = 0;
				imageWidth = image.getWidth();
				imageHeight = image.getHeight();
				repaint();
			}
		}).start();
	}

	@Override
	public void plottingProgress(int progress) {
	}

	@Override
	public int getValues(PlottingDisplay target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case IMAGE_X_Y_WIDTH_HEIGHT:
			returnValues[0] = imageX;
			returnValues[1] = imageY;
			returnValues[2] = imageWidth;
			returnValues[3] = imageHeight;
			return 4;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(PlottingDisplay target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case IMAGE_X_Y_WIDTH_HEIGHT:
			imageX = newValues[0];
			imageY = newValues[1];
			imageWidth = newValues[2];
			imageHeight = newValues[3];
			break;
		default:
			assert false;
			break;
		}
	}
}
