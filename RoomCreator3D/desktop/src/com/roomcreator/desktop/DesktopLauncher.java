package com.roomcreator.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roomcreator.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=960;
		config.height=540;
		config.x=300;
		config.y=150;
		System.setProperty("user.name","CorrectUserName");
		new LwjglApplication(new Main(), config);
	}
}
