package com.rojel.fractals.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.rojel.fractals.render.ColorScheme;

public class ColorSchemeDisplay extends JComponent {
	private static final long serialVersionUID = -3782880793289155494L;
	
	private ColorScheme scheme;
	
	public ColorSchemeDisplay(ColorScheme scheme) {
		this.scheme = scheme;
		this.setPreferredSize(new Dimension(0, 30));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		for (int x = 0; x < getWidth(); x++) {
			g.setColor(new Color(scheme.getRGB((double) x / (double) getWidth())));
			g.drawLine(x, 0, x, getHeight());
		}
		
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}
}
