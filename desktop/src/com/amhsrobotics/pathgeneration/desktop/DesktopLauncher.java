package com.amhsrobotics.pathgeneration.desktop;

import com.amhsrobotics.pathgeneration.Main;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;

public class DesktopLauncher {
	public static void main (String[] arg) {

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setResizable(false);
		config.setWindowedMode(1600, 800);
		config.setHdpiMode(HdpiMode.Logical);
		config.setTitle("Path Generator");
		config.setWindowIcon("favicon/tko.png");

		new Lwjgl3Application(new Main(), config);
	}
}
