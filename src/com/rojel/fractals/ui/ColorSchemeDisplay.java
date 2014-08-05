package com.rojel.fractals.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

import com.rojel.fractals.render.ColorScheme;

public class ColorSchemeDisplay extends JComponent implements MouseListener {
	private static final long serialVersionUID = -3782880793289155494L;
	
	private ColorScheme scheme;
	
	public ColorSchemeDisplay(ColorScheme scheme) {
		this.scheme = scheme;
		this.setPreferredSize(new Dimension(0, 30));
		
		this.addMouseListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		for (int x = 0; x < getWidth(); x++) {
			g.setColor(scheme.getColor((double) x / (double) getWidth()));
			g.drawLine(x, 0, x, getHeight());
		}
		
		for (double position : scheme.getColorPositions()) {
			Color color = scheme.getColor(position);
			Color marker = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
			g.setColor(marker);
			
			int markerPosition = (int) (position * getWidth());
			g.drawLine(markerPosition, 0, markerPosition, getHeight());
		}
		
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		double mousePos = (double) e.getX() / (double) getWidth();
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			Color defaultColor = scheme.getColor(mousePos);
			Color newColor = JColorChooser.showDialog(this, "Color for " + Math.round(mousePos * 100) / 100d, defaultColor);
			if (newColor != null)
				scheme.putColor(mousePos, newColor);
		}
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			List<Double> markersInRange = new ArrayList<Double>();
			for (double pos : scheme.getColorPositions())
				if (Math.abs(pos - mousePos) <= 0.05)
					markersInRange.add(pos);
			
			for (double marker : markersInRange) {
				scheme.removeColor(marker);
				if (marker == 0.0)
					scheme.putColor(0.0, Color.BLACK);
				if (marker == 1.0)
					scheme.putColor(1.0, Color.WHITE);
			}
		}
		
		repaint();
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
}
