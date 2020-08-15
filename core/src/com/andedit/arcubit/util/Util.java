package com.andedit.arcubit.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

/** A useful utilities for this game and LibGDX. */
public final class Util 
{	
	/** A cached screen size. */
	public static final Size screen = new Size();
	/** A cached world screen size. */
	public static final Size world  = new Size();
	
	/** Fast floor for double. */
	public static int floor(final double x) {
		final int xi = (int)x;
		return x < xi ? xi - 1 : xi;
	}
	
	/** Null-safe dispose method for disposable object. */
	public static void disposes(Disposable dis) {
		if (dis != null) dis.dispose();
	}
	
	/** Null-safe disposes method for disposable objects. */
	public static void disposes(Disposable... dis) {
		final int size = dis.length;
		for (int i = 0; i < size; ++i) {
			final Disposable d = dis[i];
			if (d != null) d.dispose();
		}
	}
	
	/** Resets the mouse position to the center of the screen. */
	public static void resetMouse() {
		Gdx.input.setCursorPosition(screen.w/2, screen.h/2);
	}
	
	/** Seconds to 60 tick update. */
	public static final int seconds(int second) {
		return second * 60;
	}
	
	/** Milliseconds to 60 tick update. */
	public static final int milseconds(int mils) {
		return MathUtils.roundPositive((mils / 1000f) * 60f);
	}
	
	/** Utility log. */
	public static void log(Object tag, Object obj) {
		if (tag instanceof Class) {
			Gdx.app.log(((Class<?>)tag).getSimpleName(), obj.toString());
		} else {
			Gdx.app.log(tag.toString(), obj.toString());
		}
	}
	
	/** Convenience method that returns a FileType.Internal file handle. */
	public static FileHandle getFile(String path) {
		return Gdx.files.internal(path);
	}
}
