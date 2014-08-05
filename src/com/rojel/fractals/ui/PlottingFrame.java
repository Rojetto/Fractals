package com.rojel.fractals.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import com.rojel.fractals.plottables.Mandelbrot;
import com.rojel.fractals.render.ColorScheme;
import com.rojel.fractals.render.Plotter;
import com.rojel.fractals.render.PlottingListener;

public class PlottingFrame extends JFrame implements PlottingListener, ColorSchemeListener {
	private static final long serialVersionUID = -7367816153167274339L;

	private PlottingDisplay display;
	private JProgressBar progress;
	private ColorSchemeDisplay csDisplay;
	private JComboBox<String> schemeNameBox;
	private JButton saveButton;
	private JButton loadButton;
	
	private ColorSchemeManager schemeManager;

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
		
		display = new PlottingDisplay(new Plotter(new Mandelbrot(), scheme));
		this.add(display, BorderLayout.CENTER);

		progress = new JProgressBar(0, 100);
		this.add(progress, BorderLayout.SOUTH);
		
		display.getPlotter().addPlottingListener(this);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		
		csDisplay = new ColorSchemeDisplay(scheme);
		csDisplay.addColorSchemeListener(this);
		topPanel.add(csDisplay, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		schemeNameBox = new JComboBox<String>();
		schemeNameBox.setPreferredSize(new Dimension(150, 21));
		schemeNameBox.setEditable(true);
		buttonPanel.add(schemeNameBox);
		
		loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String schemeName = (String) schemeNameBox.getSelectedItem();
				if (schemeManager.getSchemeNames().contains(schemeName)) {
					ColorScheme scheme = schemeManager.getScheme(schemeName);
					csDisplay.setColorScheme(scheme);
					display.getPlotter().setColorScheme(scheme);
					display.getPlotter().plot();
				}
			}
		});
		buttonPanel.add(loadButton);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String schemeName = (String) schemeNameBox.getSelectedItem();
				if (!schemeName.equals("")) {
					schemeManager.putScheme(schemeName, csDisplay.getScheme());
					
					boolean hasItem = false;
					for (int i = 0; i < schemeNameBox.getItemCount(); i++)
						if (schemeNameBox.getItemAt(i).equals(schemeName))
							hasItem = true;
					if (!hasItem)
						schemeNameBox.addItem(schemeName);
					
					try {
						schemeManager.save();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		buttonPanel.add(saveButton);
		
		topPanel.add(buttonPanel, BorderLayout.EAST);
		this.add(topPanel, BorderLayout.NORTH);

		schemeManager = new ColorSchemeManager("color_schemes.txt");
		for (String schemeName : schemeManager.getSchemeNames())
			schemeNameBox.addItem(schemeName);
		
		String schemeName = (String) schemeNameBox.getSelectedItem();
		if (schemeManager.getSchemeNames().contains(schemeName)) {
			ColorScheme newScheme = schemeManager.getScheme(schemeName);
			csDisplay.setColorScheme(newScheme);
			display.getPlotter().setColorScheme(newScheme);
			display.getPlotter().plot();
		}
		
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

	@Override
	public void colorSchemeChanged(ColorScheme newScheme) {
		display.getPlotter().setColorScheme(newScheme);
		display.getPlotter().plot();
	}
}
