package com.agmcleod.nwt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.agmcleod.nwt.CoreGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 704;
        config.height = 704;
		new LwjglApplication(new CoreGame(), config);
	}
}
