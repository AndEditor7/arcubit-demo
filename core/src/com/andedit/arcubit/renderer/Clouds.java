package com.andedit.arcubit.renderer;

import static com.andedit.arcubit.util.math.FastNoise.GradCoord2D;

import com.andedit.arcubit.util.Camera;
import com.andedit.arcubit.util.math.FastNoise;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public interface Clouds extends Disposable 
{
	public static final float 
	SIZE = 10f, // 10f
	DIST = 18f,
	SCALE = 4f,
	DENSE = 0.4f,
	HEIGHT = 85f, // 69f
	SPEED = 0.015f,
	FAR = 8000f; // 16000f
	
	public static final int SEED = MathUtils.random.nextInt();
	
	/** Gdx.gl.glEnable(GL20.GL_CULL_FACE) must be enable in this method */
	public void render(Camera cam, IndexData index);
	
	public static float getPerlin(float x, float y) 
	{
		final int x0 = MathUtils.floor(x);
		final int y0 = MathUtils.floor(y);
		final int x1 = x0 + 1;
		final int y1 = y0 + 1;

		final float xs, ys;
		xs = FastNoise.InterpQuinticFunc(x - x0);
		ys = FastNoise.InterpQuinticFunc(y - y0);

		final float xd0 = x - x0;
		final float yd0 = y - y0;
		final float xd1 = xd0 - 1f;
		final float yd1 = yd0 - 1f;

		final float xf0 = FastNoise.Lerp(GradCoord2D(SEED, x0, y0, xd0, yd0), GradCoord2D(SEED, x1, y0, xd1, yd0), xs);
		final float xf1 = FastNoise.Lerp(GradCoord2D(SEED, x0, y1, xd0, yd1), GradCoord2D(SEED, x1, y1, xd1, yd1), xs);

		return FastNoise.Lerp(xf0, xf1, ys);
	}
}
