package com.rojel.fractals.ui;

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
		schemes.put(name, scheme);
	}

	public ColorScheme getScheme(String name) {
		return schemes.get(name);
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
				int rgb = Integer.parseInt(split[1]);
				scheme.putColor(pos, rgb);
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
			for (double pos : scheme.getColorPositions())
				bw.write(pos + " " + scheme.getRGB(pos) + "\n");
			bw.write(";\n");
		}

		bw.close();
	}
}
