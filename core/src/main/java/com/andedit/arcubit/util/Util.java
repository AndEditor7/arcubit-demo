package com.andedit.arcubit.util;

import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public final class Util {
	
	public static float lerp(float fromValue, float toValue, float progress, float clamp) {
		final float delta = (toValue - fromValue) * progress;
		return fromValue + MathUtils.clamp(delta, -clamp, clamp);
	}

	public static boolean isGL30() {
		return Gdx.gl30 != null;
	}

	/** Returns the width of the client area in logical pixels. */
	public static int getW() {
		return Gdx.graphics.getWidth();
	}

	/** Returns the height of the client area in logical pixels. */
	public static int getH() {
		return Gdx.graphics.getHeight();
	}

	public static void glClear() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
	
	public static ShaderProgram newShader(String path) {
		return new ShaderProgram(Gdx.files.internal(path + ".vert"), Gdx.files.internal(path + ".frag"));
	}
	
	public static void scale(Actor actor, float scl) {
		actor.setSize(actor.getWidth()*scl, actor.getHeight()*scl);
	}
	
	public static TextureRegion flip(TextureRegion region, boolean x, boolean y) {
		TextureRegion var = new TextureRegion(region);
		var.flip(x, y);
		return var;
	}
	
	public static float modAngle(float angle) {
		float mod = angle % 360f;
		return MathUtils.clamp(mod < 0f ? mod + 360f : mod, 0f, 360f);
	}

	/** Create a new change listener using java 8 lambda. */
	public static EventListener newListener(Consumer<Event> listener) {
		return new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				listener.accept(event);
			}
		};
	}

	/** Create a new change listener without Event. */
	public static EventListener newListener(Runnable runnable) {
		return new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				runnable.run();
			}
		};
	}
}
