package com.ts.rts;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
	LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	cfg.title = "RTS";
	cfg.useGL20 = true;
	cfg.width = 900;
	cfg.height = 600;
	cfg.fullscreen = false;
	cfg.backgroundFPS = 0;
	cfg.resizable = false;
	cfg.samples = 0;
	cfg.vSyncEnabled = true;

	new LwjglApplication(new RTSGame(), cfg);
    }
}
