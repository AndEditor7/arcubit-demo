package com.andedit.arcubit.util.math;

import com.badlogic.gdx.math.MathUtils;

// FastNoise.java
public class FastNoise {
	private int m_seed = 1337;
	private float m_frequency = 0.01f;

	private int m_octaves = 3;
	private float m_lacunarity = 2.0f;
	private float m_gain = 0.5f;

	private float m_fractalBounding;

	public FastNoise() {
		this(1337);
	}

	public FastNoise(int seed) {
		m_seed = seed;
		CalculateFractalBounding();
	}

	// Returns a 0 float/double
	public static float GetDecimalType() {
		return 0;
	}

	// Returns the seed used by this object
	public int GetSeed() {
		return m_seed;
	}

	// Sets seed used for all noise types
	// Default: 1337
	public void SetSeed(int seed) {
		m_seed = seed;
	}

	// Sets frequency for all noise types
	// Default: 0.01
	public void SetFrequency(float frequency) {
		m_frequency = frequency;
	}

	// Sets octave count for all fractal noise types
	// Default: 3
	public void SetFractalOctaves(int octaves) {
		m_octaves = octaves;
		CalculateFractalBounding();
	}

	// Sets octave lacunarity for all fractal noise types
	// Default: 2.0
	public void SetFractalLacunarity(float lacunarity) {
		m_lacunarity = lacunarity;
	}

	// Sets octave gain for all fractal noise types
	// Default: 0.5
	public void SetFractalGain(float gain) {
		m_gain = gain;
		CalculateFractalBounding();
	}

	private static class Float2 {
		public final float x, y;

		public Float2(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	private static class Float3 {
		public final float x, y, z;

		public Float3(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	private static final Float2[] GRAD_2D = {
		new Float2(-1, -1), new Float2(1, -1), new Float2(-1, 1), new Float2(1, 1),
		new Float2(0, -1), new Float2(-1, 0), new Float2(0, 1), new Float2(1, 0),
	};

	private static final Float3[] GRAD_3D = {
		new Float3(1, 1, 0), new Float3(-1, 1, 0), new Float3(1, -1, 0), new Float3(-1, -1, 0),
		new Float3(1, 0, 1), new Float3(-1, 0, 1), new Float3(1, 0, -1), new Float3(-1, 0, -1),
		new Float3(0, 1, 1), new Float3(0, -1, 1), new Float3(0, 1, -1), new Float3(0, -1, -1),
		new Float3(1, 1, 0), new Float3(0, -1, 1), new Float3(-1, 1, 0), new Float3(0, -1, -1),
	};

	public static float Lerp(float a, float b, float t) {
		return a + t * (b - a);
	}

	public static float InterpQuinticFunc(float t) {
		return t * t * t * (t * (t * 6f - 15f) + 10f);
	}

	private void CalculateFractalBounding() {
		float amp = m_gain;
		float ampFractal = 1;
		for (int i = 1; i < m_octaves; i++) {
			ampFractal += amp;
			amp *= m_gain;
		}
		m_fractalBounding = 1 / ampFractal;
	}

	// Hashing
	private final static int X_PRIME = 1619;
	private final static int Y_PRIME = 31337;
	private final static int Z_PRIME = 6971;

	public static float GradCoord2D(int seed, int x, int y, float xd, float yd) {
		int hash = seed;
		hash ^= X_PRIME * x;
		hash ^= Y_PRIME * y;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		Float2 g = GRAD_2D[hash & 7];

		return xd * g.x + yd * g.y;
	}

	private static float GradCoord3D(int seed, int x, int y, int z, float xd, float yd, float zd) {
		int hash = seed;
		hash ^= X_PRIME * x;
		hash ^= Y_PRIME * y;
		hash ^= Z_PRIME * z;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		Float3 g = GRAD_3D[hash & 15];

		return xd * g.x + yd * g.y + zd * g.z;
	}
	
	// Gradient Noise
	public float GetPerlinFractal(float x, float y, float z) {
		x *= m_frequency;
		y *= m_frequency;
		z *= m_frequency;

		return SinglePerlinFractalFBM(x, y, z);
	}

	private float SinglePerlinFractalFBM(float x, float y, float z) {
		int seed = m_seed;
		float sum = SinglePerlin(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < m_octaves; i++) {
			x *= m_lacunarity;
			y *= m_lacunarity;
			z *= m_lacunarity;

			amp *= m_gain;
			sum += SinglePerlin(++seed, x, y, z) * amp;
		}

		return sum * m_fractalBounding;
	}

	public float GetPerlin(float x, float y, float z) {
		return SinglePerlin(m_seed, x * m_frequency, y * m_frequency, z * m_frequency);
	}

	private float SinglePerlin(int seed, float x, float y, float z) {
		int x0 = MathUtils.floor(x);
		int y0 = MathUtils.floor(y);
		int z0 = MathUtils.floor(z);
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		int z1 = z0 + 1;

		float xs, ys, zs;
		xs = InterpQuinticFunc(x - x0);
		ys = InterpQuinticFunc(y - y0);
		zs = InterpQuinticFunc(z - z0);

		float xd0 = x - x0;
		float yd0 = y - y0;
		float zd0 = z - z0;
		float xd1 = xd0 - 1f;
		float yd1 = yd0 - 1f;
		float zd1 = zd0 - 1f;

		float xf00 = Lerp(GradCoord3D(seed, x0, y0, z0, xd0, yd0, zd0), GradCoord3D(seed, x1, y0, z0, xd1, yd0, zd0), xs);
		float xf10 = Lerp(GradCoord3D(seed, x0, y1, z0, xd0, yd1, zd0), GradCoord3D(seed, x1, y1, z0, xd1, yd1, zd0), xs);
		float xf01 = Lerp(GradCoord3D(seed, x0, y0, z1, xd0, yd0, zd1), GradCoord3D(seed, x1, y0, z1, xd1, yd0, zd1), xs);
		float xf11 = Lerp(GradCoord3D(seed, x0, y1, z1, xd0, yd1, zd1), GradCoord3D(seed, x1, y1, z1, xd1, yd1, zd1), xs);

		float yf0 = Lerp(xf00, xf10, ys);
		float yf1 = Lerp(xf01, xf11, ys);

		return Lerp(yf0, yf1, zs);
	}

	public float GetPerlinFractal(float x, float y) {
		x *= m_frequency;
		y *= m_frequency;
		return SinglePerlinFractalFBM(x, y);
	}

	private float SinglePerlinFractalFBM(float x, float y) {
		int seed = m_seed;
		float sum = SinglePerlin(seed, x, y);
		float amp = 1;

		for (int i = 1; i < m_octaves; i++) {
			x *= m_lacunarity;
			y *= m_lacunarity;

			amp *= m_gain;
			sum += SinglePerlin(++seed, x, y) * amp;
		}

		return sum * m_fractalBounding;
	}

	public float GetPerlin(float x, float y) {
		return SinglePerlin(m_seed, x * m_frequency, y * m_frequency);
	}

	private float SinglePerlin(int seed, float x, float y) {
		int x0 = MathUtils.floor(x);
		int y0 = MathUtils.floor(y);
		int x1 = x0 + 1;
		int y1 = y0 + 1;

		float xs, ys;
		xs = InterpQuinticFunc(x - x0);
		ys = InterpQuinticFunc(y - y0);

		float xd0 = x - x0;
		float yd0 = y - y0;
		float xd1 = xd0 - 1f;
		float yd1 = yd0 - 1f;

		float xf0 = Lerp(GradCoord2D(seed, x0, y0, xd0, yd0), GradCoord2D(seed, x1, y0, xd1, yd0), xs);
		float xf1 = Lerp(GradCoord2D(seed, x0, y1, xd0, yd1), GradCoord2D(seed, x1, y1, xd1, yd1), xs);

		return Lerp(xf0, xf1, ys);
	}

}
