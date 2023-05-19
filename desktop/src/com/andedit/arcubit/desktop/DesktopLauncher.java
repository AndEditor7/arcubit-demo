package com.andedit.arcubit.desktop;

import javax.swing.UIManager;

import com.andedit.arcubit.Arcubit;
import com.andedit.arcubit.Options;
import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.handle.Saver;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	
	/* LWJGL 2
	@SuppressWarnings("unused")
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.vSyncEnabled = false;
		config.foregroundFPS = 60;
		config.backgroundFPS = -1;
		
		if (false) {
			config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
		} else {
			config.width = 1280;
			config.height = 700;
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Blocks.loadBlocks();
		World world = Saver.load();
		System.gc();
		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		new LwjglApplication(new MClone(world), config);
	} */
	
	@SuppressWarnings("unused")
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.enableGLDebugOutput(true, System.err);
		config.disableAudio(true);
		
		config.useVsync(true);
		config.setIdleFPS(1);
		config.setTitle("Arcubit v1");
		config.setOpenGLEmulation(Options.GL3
				? Lwjgl3ApplicationConfiguration.GLEmulation.GL30
				: Lwjgl3ApplicationConfiguration.GLEmulation.GL20, 3, 2);
		
		if (false) { //
			DisplayMode display = Lwjgl3ApplicationConfiguration.getDisplayMode();
			System.out.println(display.toString());
			config.setFullscreenMode(display);
		} else {
			config.setWindowedMode(1280, 700);
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//GL43.glMultiDrawElements(mode, count, type, indices);
		
		Blocks.loadBlocks();
		World world = Saver.load();
		System.gc();
		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			new Lwjgl3Application(new Arcubit(world), config);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
