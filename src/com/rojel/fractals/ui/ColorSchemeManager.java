package com.rojel.fractals.ui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.rojel.fractals.render.ColorScheme;

public class ColorSchemeManager {
	private File schemeFile;
	private Map<String, ColorScheme> schemes;

	public ColorSchemeManager(String fileName) {
		schemeFile = new File(fileName);
		schemes = new HashMap<String, ColorScheme>();
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void putScheme(String name, ColorScheme scheme) {
		schemes.put(name, new ColorScheme(scheme));
	}

	public ColorScheme getScheme(String name) {
		return new ColorScheme(schemes.get(name));
	}

	public Set<String> getSchemeNames() {
		return schemes.keySet();
	}

	public void load() throws IOException {
		schemeFile.createNewFile();
		BufferedReader br = new BufferedReader(new FileReader(schemeFile));
		String name;
		while ((name = br.readLine()) != null) {
			ColorScheme scheme = new ColorScheme();
			String line;
			while (!(line = br.readLine()).equals(";")) {
				String[] split = line.split(" ");
				double pos = Double.parseDouble(split[0]);
				scheme.putColor(pos, new Color(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])));
			}
			schemes.put(name, scheme);
		}

		br.close();
	}

	public void save() throws IOException {
		schemeFile.delete();
		schemeFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(schemeFile));
		for (String name : schemes.keySet()) {
			bw.write(name + "\n");
			ColorScheme scheme = schemes.get(name);
			for (double pos : scheme.getColorPositions()) {
				Color color = scheme.getColor(pos);
				System.out.println(pos + " " + color);
				bw.write(pos + " " + color.getRed() + " " + color.getGreen() + " " + color.getBlue() + "\n");
			}
			bw.write(";\n");
		}

		bw.close();
	}
}
