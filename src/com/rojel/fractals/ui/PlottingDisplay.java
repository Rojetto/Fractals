package com.rojel.fractals.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;

import com.rojel.fractals.render.Plotter;
import com.rojel.fractals.render.PlottingListener;

public class PlottingDisplay extends JComponent implements MouseListener, MouseMotionListener, ComponentListener, PlottingListener, TweenAccessor<PlottingDisplay> {
	private static final long serialVersionUID = -8158869774426846742L;
	public static final float ZOOM_FACTOR = 5f;
	public static final int ZOOM_RECT_BORDER = 3;
	public static final int IMAGE_X_Y_WIDTH_HEIGHT = 1;

	private Plotter plotter;
	private BufferedImage lastPlot;
	private TweenManager manager;

	private boolean coordinates;

	private boolean mouseDown;
	private int dragStartX;
	private int dragStartY;
	private int dragCurrentX;
	private int dragCurrentY;

	private float imageX;
	private float imageY;
	private float imageWidth;
	private float imageHeight;

	public PlottingDisplay(Plotter plotter) {
		this.plotter = plotter;
		this.plotter.addPlottingListener(this);

		this.addMouseListener(this);
		this.addComponentListener(this);
		this.addMouseMotionListener(this);

		this.coordinates = true;

		Tween.setCombinedAttributesLimit(4);
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

		if (mouseDown)
			drawComplementaryRect(g, dragStartX, dragStartY, dragCurrentX, dragCurrentY);

		if (coordinates) {
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
	}

	private void drawComplementaryRect(Graphics g, int x1, int y1, int x2, int y2) {
		int smallX = Math.min(x1, x2);
		int largeX = Math.max(x1, x2);
		int smallY = Math.min(y1, y2);
		int largeY = Math.max(y1, y2);

		for (int y = smallY; y <= largeY; y++) {
			for (int x = smallX; x <= largeX; x++) {
				if (x - smallX < ZOOM_RECT_BORDER || largeX - x < ZOOM_RECT_BORDER || y - smallY < ZOOM_RECT_BORDER || largeY - y < ZOOM_RECT_BORDER) {
					if (x >= 0 && y >= 0 && x < lastPlot.getWidth() && y < lastPlot.getHeight()) {
						Color plotColor = new Color(lastPlot.getRGB(x, y));
						Color rectColor = new Color(255 - plotColor.getRed(), 255 - plotColor.getGreen(), 255 - plotColor.getBlue());
						g.setColor(rectColor);
						g.drawLine(x, y, x, y);
					}
				}
			}
		}
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

			Tween.to(this, IMAGE_X_Y_WIDTH_HEIGHT, 0.8f).target(newX, newY, newWidth, newHeight).ease(Cubic.OUT).start(manager);
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

			Tween.to(this, IMAGE_X_Y_WIDTH_HEIGHT, 0.8f).target(newX, newY, newWidth, newHeight).ease(Cubic.OUT).start(manager);
		}

		if (e.getButton() == MouseEvent.BUTTON2) {
			plotter.setCenterX(plotX);
			plotter.setCenterY(plotY);

			plotter.plot();
		}
	}

	public void zoom(double centerX, double centerY, double width, double height) {
		plotter.setCenterX(centerX);
		plotter.setCenterY(centerY);
		plotter.setWidth(width);
		plotter.setHeight(height);

		System.out.println("Zoom to " + centerX + " " + centerY + " " + width + " " + height);

		plotter.plot();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			mouseDown = true;
			dragStartX = e.getX();
			dragStartY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;

		if (Math.abs(dragCurrentX - dragStartX) >= 5 && Math.abs(dragCurrentY - dragStartY) >= 5) {
			double startXPlot = plotter.screenToPlotX(dragStartX);
			double startYPlot = plotter.screenToPlotY(dragStartY);
			double endXPlot = plotter.screenToPlotX(dragCurrentX);
			double endYPlot = plotter.screenToPlotY(dragCurrentY);
			double minX = Math.min(startXPlot, endXPlot);
			double maxX = Math.max(startXPlot, endXPlot);
			double minY = Math.min(startYPlot, endYPlot);
			double maxY = Math.max(startYPlot, endYPlot);

			double screenRatio = (double) getWidth() / (double) getHeight();
			double oneWidth = maxX - minX;
			double otherWidth = (maxY - minY) * screenRatio;

			double newWidth = Math.min(oneWidth, otherWidth);
			double newHeight = newWidth / screenRatio;

			double newCenterX = (minX + maxX) / 2d;
			double newCenterY = (minY + maxY) / 2d;

			zoom(newCenterX, newCenterY, newWidth, newHeight);
		}

		repaint();
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

	@Override
	public void mouseDragged(MouseEvent e) {
		dragCurrentX = e.getX();
		dragCurrentY = e.getY();

		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
