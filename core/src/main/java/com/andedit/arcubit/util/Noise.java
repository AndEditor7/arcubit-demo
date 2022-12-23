// This file was originally from https://github.com/Auburns/FastNoise_Java as:
// FastNoise.java
//
// MIT License
//
// Copyright(c) 2017 Jordan Peck
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// The developer's email is jorzixdan.me2@gzixmail.com (for great email, take
// off every 'zix'.)
//

package com.andedit.arcubit.util;

/**
 * A wide range of noise functions that can all be called from one configurable
 * object. Originally from Jordan Peck's FastNoise library, the implementation
 * here is meant to be fast without sacrificing quality. Usage requires a Noise
 * object, which can be {@link #instance} in simple cases and otherwise should
 * be constructed with whatever adjustment is needed. You can choose Perlin
 * (also called "classic Perlin") noise, Simplex (Ken Perlin's later creation,
 * which is a reasonable default), Cubic (a slower sort of very smooth noise
 * that doesn't have many predictable patterns), Value (simple noise with lots
 * of artifacts), Cellular (a kind of Voronoi-based noise), or plain White noise
 * (a "TV static" pattern). Most of these have fractal variants that allow
 * layering multiple frequencies of noise. After construction you can set how a
 * fractal variant is layered using {@link #setFractalType(int)}, with
 * {@link #FBM} as the normal mode and {@link #RIDGED_MULTI} as a not-uncommon
 * way of altering the form noise takes. This supports 2D, 3D, and 4D fully,
 * with partial support for 6D (which can be used for tiling 3D maps).
 */
public class Noise {
	/**
	 * Simple, very fast but very low-quality noise that forms a grid of squares,
	 * with their values blending at shared edges somewhat. <br>
	 * <a href="https://i.imgur.com/egjotwb.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int VALUE = 0;
	/**
	 * Simple, very fast but very low-quality noise that forms a grid of squares,
	 * with their values blending at shared edges somewhat; this version can use
	 * {@link #setFractalType(int)}, {@link #setFractalOctaves(int)}, and more, but
	 * none of these really disguise the grid it uses. <br>
	 * <a href="https://i.imgur.com/egjotwb.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int VALUE_FRACTAL = 1;
	/**
	 * Also called Gradient Noise or Classic Perlin noise, this is fast and
	 * mid-to-low-quality in 2D, but slows down significantly in higher dimensions
	 * while mostly improving in quality. This may have a noticeable grid at 90
	 * degree angles (and a little at 45 degree angles). <br>
	 * <a href="https://i.imgur.com/MO7hwSI.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int PERLIN = 2;
	/**
	 * Also called Gradient Noise or Classic Perlin noise, this is fast and
	 * mid-to-low-quality in 2D, but slows down significantly in higher dimensions
	 * while mostly improving in quality. This may have a noticeable grid at 90
	 * degree angles (and a little at 45 degree angles). This version can use
	 * {@link #setFractalType(int)}, {@link #setFractalOctaves(int)}, and more. <br>
	 * <a href="https://i.imgur.com/MO7hwSI.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int PERLIN_FRACTAL = 3;
	/**
	 * Also called Improved Perlin noise, this is always fast but tends to have
	 * better quality in lower dimensions. This may have a noticeable grid at 60
	 * degree angles, made of regular triangles in 2D. <br>
	 * <a href="https://i.imgur.com/wg3kq5A.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int SIMPLEX = 4;
	/**
	 * Also called Improved Perlin noise, this is always fast but tends to have
	 * better quality in lower dimensions. This may have a noticeable grid at 60
	 * degree angles, made of regular triangles in 2D. This version can use
	 * {@link #setFractalType(int)}, {@link #setFractalOctaves(int)}, and more; it
	 * is the default noise type if none is specified. <br>
	 * <a href="https://i.imgur.com/wg3kq5A.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int SIMPLEX_FRACTAL = 5;
	/**
	 * Creates a Voronoi diagram of 2D or 3D space and fills cells based on the
	 * {@link #setCellularReturnType(int)},
	 * {@link #setCellularDistanceFunction(int)}, and possibly the
	 * {@link #setCellularNoiseLookup(Noise)}. This is more of an advanced usage,
	 * but can yield useful results when oddly-shaped areas should have similar
	 * values. <br>
	 * <a href="https://i.imgur.com/ScRves7.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int CELLULAR = 6;
	/**
	 * Purely chaotic, non-continuous random noise per position; looks like static
	 * on a TV screen. <br>
	 * <a href="https://i.imgur.com/vBtISSx.jpg">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int WHITE_NOISE = 7;
	/**
	 * A simple kind of noise that gets a random float for each vertex of a square
	 * or cube, and interpolates between all of them to get a smoothly changing
	 * value (using cubic interpolation, also called {@link #HERMITE}, of course).
	 * <br>
	 * <a href="https://i.imgur.com/foV90pn.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int CUBIC = 8;
	/**
	 * A simple kind of noise that gets a random float for each vertex of a square
	 * or cube, and interpolates between all of them to get a smoothly changing
	 * value (using cubic interpolation, also called {@link #HERMITE}, of course).
	 * This version can use {@link #setFractalType(int)},
	 * {@link #setFractalOctaves(int)}, and more. <br>
	 * <a href="https://i.imgur.com/foV90pn.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int CUBIC_FRACTAL = 9;
	/**
	 * A novel kind of noise that works in n-dimensions by averaging n+1 value noise
	 * calls, all of them rotated around each other, and with all of the value noise
	 * calls after the first adding in the last call's result to part of the
	 * position. This yields rather high-quality noise (especially when comparing
	 * one octave of FOAM to one octave of {@link #PERLIN} or {@link #SIMPLEX}), but
	 * is somewhat slow. <br>
	 * <a href="https://i.imgur.com/4ZC9h5t.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int FOAM = 10;
	/**
	 * A novel kind of noise that works in n-dimensions by averaging n+1 value noise
	 * calls, all of them rotated around each other, and with all of the value noise
	 * calls after the first adding in the last call's result to part of the
	 * position. This yields rather high-quality noise (especially when comparing
	 * one octave of FOAM to one octave of {@link #PERLIN} or {@link #SIMPLEX}), but
	 * is somewhat slow. This version can use {@link #setFractalType(int)},
	 * {@link #setFractalOctaves(int)}, and more, and usually doesn't need as many
	 * octaves as PERLIN or SIMPLEX to attain comparable quality. <br>
	 * <a href="https://i.imgur.com/4ZC9h5t.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int FOAM_FRACTAL = 11;
	/**
	 * A simple combination of {@link #SIMPLEX} and {@link #VALUE} noise, averaging
	 * a call to each and then distorting the result's distribution so it isn't as
	 * centrally-biased. The result is somewhere between {@link #FOAM} and
	 * {@link #SIMPLEX}, and has less angular bias than Simplex or Value. This gets
	 * its name from how it mixes two different geometric honeycombs (a triangular
	 * one for 2D Simplex noise and a square one for 2D Value noise). <br>
	 * <a href="https://i.imgur.com/bMEPiBA.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int HONEY = 12;
	/**
	 * A simple combination of {@link #SIMPLEX_FRACTAL} and {@link #VALUE_FRACTAL}
	 * noise, averaging a call to each and then distorting the result's distribution
	 * so it isn't as centrally-biased. The result is somewhere between
	 * {@link #FOAM_FRACTAL} and {@link #SIMPLEX_FRACTAL}, and has less angular bias
	 * than Simplex or Value. This gets its name from how it mixes two different
	 * geometric honeycombs (a triangular one for 2D Simplex noise and a square one
	 * for 2D Value noise). This version can use {@link #setFractalType(int)},
	 * {@link #setFractalOctaves(int)}, and more, and usually doesn't need as many
	 * octaves as PERLIN or SIMPLEX to attain comparable quality, though it
	 * drastically improves with just two octaves. <br>
	 * <a href="https://i.imgur.com/bMEPiBA.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int HONEY_FRACTAL = 13;
	/**
	 * A kind of noise that allows extra configuration via
	 * {@link #setMutation(float)}, producing small changes when the mutation value
	 * is similar, or large changes if it is very different. This contrasts with
	 * changes to the seed, which almost always cause large changes for any
	 * difference in seed. The implementation here is the same as {@link #FOAM} with
	 * one more dimension, which is filled by the mutation value. <br>
	 * <a href="https://i.imgur.com/4ZC9h5t.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int MUTANT = 14;
	/**
	 * A kind of noise that allows extra configuration via
	 * {@link #setMutation(float)}, producing small changes when the mutation value
	 * is similar, or large changes if it is very different. This contrasts with
	 * changes to the seed, which almost always cause large changes for any
	 * difference in seed. The implementation here is the same as
	 * {@link #FOAM_FRACTAL} with one more dimension, which is filled by the
	 * mutation value. <br>
	 * <a href="https://i.imgur.com/4ZC9h5t.png">Noise sample at left, FFT at
	 * right.</a> <br>
	 * This is meant to be used with {@link #setNoiseType(int)}.
	 */
	public static final int MUTANT_FRACTAL = 15;

	/**
	 * Simple linear interpolation. May result in artificial-looking noise. Meant to
	 * be used with {@link #setInterpolation(int)}.
	 */
	public static final int LINEAR = 0;
	/**
	 * Cubic interpolation via Hermite spline, more commonly known as "smoothstep".
	 * Can be very natural-looking, but can also have problems in higher dimensions
	 * (including 3D when used with normals) with seams appearing. Meant to be used
	 * with {@link #setInterpolation(int)}.
	 */
	public static final int HERMITE = 1;
	/**
	 * Quintic interpolation, sometimes known as "smootherstep". This has somewhat
	 * steeper transitions than {@link #HERMITE}, but doesn't have any issues with
	 * seams. Meant to be used with {@link #setInterpolation(int)}.
	 */
	public static final int QUINTIC = 2;

	/**
	 * "Standard" layered octaves of noise, where each octave has a different
	 * frequency and weight. Tends to look cloudy with more octaves, and generally
	 * like a natural process. <br>
	 * Meant to be used with {@link #setFractalType(int)}.
	 */
	public static final int FBM = 0;
	/**
	 * A less common way to layer octaves of noise, where most results are biased
	 * toward higher values, but "valleys" show up filled with much lower values.
	 * This probably has some good uses in 3D or higher noise, but it isn't used too
	 * frequently. <br>
	 * Meant to be used with {@link #setFractalType(int)}.
	 */
	public static final int BILLOW = 1;
	/**
	 * A way to layer octaves of noise so most values are biased toward low values
	 * but "ridges" of high values run across the noise. This can be a good way of
	 * highlighting the least-natural aspects of some kinds of noise;
	 * {@link #PERLIN_FRACTAL} has mostly ridges along 45-degree angles,
	 * {@link #SIMPLEX_FRACTAL} has many ridges along a triangular grid, and so on.
	 * {@link #FOAM_FRACTAL} and {@link #HONEY_FRACTAL} do well with this mode,
	 * though, and look something like lightning or bubbling fluids, respectively.
	 * Using FOAM or HONEY will have this look natural, but PERLIN in particular
	 * will look unnatural if the grid is visible. <br>
	 * Meant to be used with {@link #setFractalType(int)}.
	 */
	public static final int RIDGED_MULTI = 2;

	/**
	 * Measures distances "as the crow flies." All points at an equal distance from
	 * the origin form a circle. Used only with {@link #CELLULAR} noise. Meant to be
	 * used with {@link #setCellularDistanceFunction(int)}.
	 */
	public static final int EUCLIDEAN = 0;
	/**
	 * Measures distances on a grid, as if allowing only orthogonal movement (with
	 * no diagonals). All points at an equal distance from the origin form a diamond
	 * shape. Used only with {@link #CELLULAR} noise. Meant to be used with
	 * {@link #setCellularDistanceFunction(int)}.
	 */
	public static final int MANHATTAN = 1;
	/**
	 * Measures distances with an approximation of Euclidean distance that's not
	 * 100% accurate. All points at an equal distance from the origin form a rough
	 * octagon. Used only with {@link #CELLULAR} noise. Meant to be used with
	 * {@link #setCellularDistanceFunction(int)}.
	 */
	public static final int NATURAL = 2;

	/**
	 * Meant to be used with {@link #setCellularReturnType(int)}.
	 */
	public static final int CELL_VALUE = 0;
	/**
	 * Meant to be used with {@link #setCellularReturnType(int)}. Note that this
	 * does not allow configuring an extra Noise value to use for lookup (anymore);
	 * it always uses 3 octaves of {@link #SIMPLEX_FRACTAL} with {@link #FBM}.
	 */
	public static final int NOISE_LOOKUP = 1;
	/**
	 * Meant to be used with {@link #setCellularReturnType(int)}.
	 */
	public static final int DISTANCE = 2;
	/**
	 * Meant to be used with {@link #setCellularReturnType(int)}.
	 */
	public static final int DISTANCE_2 = 3;
	/**
	 * Meant to be used with {@link #setCellularReturnType(int)}.
	 */
	public static final int DISTANCE_2_ADD = 4;
	/**
	 * Meant to be used with {@link #setCellularReturnType(int)}.
	 */
	public static final int DISTANCE_2_SUB = 5;
	/**
	 * Meant to be used with {@link #setCellularReturnType(int)}.
	 */
	public static final int DISTANCE_2_MUL = 6;
	/**
	 * Meant to be used with {@link #setCellularReturnType(int)}.
	 */
	public static final int DISTANCE_2_DIV = 7;

	/**
	 * @see #getSeed()
	 */
	protected int seed;

	/**
	 * @see #getFrequency()
	 */
	protected float frequency = 0.03125f;

	/**
	 * @see #getInterpolation()
	 */
	protected int interpolation = HERMITE;

	/**
	 * @see #getNoiseType()
	 */
	protected int noiseType = SIMPLEX_FRACTAL;

	/**
	 * @see #getFractalOctaves()
	 */
	protected int octaves = 1;

	/**
	 * @see #getFractalLacunarity()
	 */
	protected float lacunarity = 2f;
	/**
	 * @see #getFractalGain()
	 */
	protected float gain = 0.5f;
	/**
	 * @see #getFractalType()
	 */
	protected int fractalType = FBM;

	private float fractalBounding;

	/**
	 * @see #getCellularDistanceFunction()
	 */
	protected int cellularDistanceFunction = EUCLIDEAN;

	/**
	 * @see #getCellularReturnType()
	 */
	protected int cellularReturnType = CELL_VALUE;

	/**
	 * @see #getGradientPerturbAmp()
	 */
	protected float gradientPerturbAmp = 1f / 0.45f;

	/**
	 * @see #getFoamSharpness()
	 */
	protected float foamSharpness = 1f;

	/**
	 * @see #getMutation()
	 */
	protected float mutation = 0f;

	/**
	 * A publicly available Noise object with seed 1337, frequency 1.0f/32.0f, 1
	 * octave of Simplex noise using SIMPLEX_FRACTAL noiseType, 2f lacunarity and
	 * 0.5f gain. It's encouraged to use methods that temporarily configure this
	 * variable, like {@link #getNoiseWithSeed(float, float, int)} rather than
	 * changing its settings and using a method that needs that lasting
	 * configuration, like {@link #getConfiguredNoise(float, float)}.
	 */
	public static final Noise instance = new Noise();

	/**
	 * A constructor that takes no parameters, and uses all default settings with a
	 * seed of 1337. An example call to this would be {@code new Noise()}, which
	 * makes noise with the seed 1337, a default frequency of 1.0f/32.0f, 1 octave
	 * of Simplex noise (since this doesn't specify octave count, it always uses 1
	 * even for the SIMPLEX_FRACTAL noiseType this uses, but you can call
	 * {@link #setFractalOctaves(int)} later to benefit from the fractal noiseType),
	 * and normal lacunarity and gain (when unspecified, they are 2f and 0.5f).
	 */
	public Noise() {
		this(1337);
	}

	/**
	 * A constructor that takes only a parameter for the Noise's seed, which should
	 * produce different results for any different seeds. An example call to this
	 * would be {@code new Noise(1337)}, which makes noise with the seed 1337, a
	 * default frequency of 1.0f/32.0f, 1 octave of Simplex noise (since this
	 * doesn't specify octave count, it always uses 1 even for the SIMPLEX_FRACTAL
	 * noiseType this uses, but you can call {@link #setFractalOctaves(int)} later
	 * to benefit from the fractal noiseType), and normal lacunarity and gain (when
	 * unspecified, they are 2f and 0.5f).
	 * 
	 * @param seed the int seed for the noise, which should significantly affect the
	 *             produced noise
	 */
	public Noise(int seed) {
		this.seed = seed;
		calculateFractalBounding();
	}

	/**
	 * A constructor that takes two parameters to specify the Noise from the start.
	 * An example call to this would be {@code new Noise(1337, 0.02f)}, which makes
	 * noise with the seed 1337, a lower frequency, 1 octave of Simplex noise (since
	 * this doesn't specify octave count, it always uses 1 even for the
	 * SIMPLEX_FRACTAL noiseType this uses, but you can call
	 * {@link #setFractalOctaves(int)} later to benefit from the fractal noiseType),
	 * and normal lacunarity and gain (when unspecified, they are 2f and 0.5f).
	 * 
	 * @param seed      the int seed for the noise, which should significantly
	 *                  affect the produced noise
	 * @param frequency the multiplier for all dimensions, which is usually fairly
	 *                  small (1.0f/32.0f is the default)
	 */
	public Noise(int seed, float frequency) {
		this(seed, frequency, SIMPLEX_FRACTAL, 1, 2f, 0.5f);
	}

	/**
	 * A constructor that takes a few parameters to specify the Noise from the
	 * start. An example call to this would be
	 * {@code new Noise(1337, 0.02f, Noise.SIMPLEX)}, which makes noise with the
	 * seed 1337, a lower frequency, 1 octave of Simplex noise (since this doesn't
	 * specify octave count, it always uses 1 even for noiseTypes like
	 * SIMPLEX_FRACTAL, but using a fractal noiseType can make sense if you call
	 * {@link #setFractalOctaves(int)} later), and normal lacunarity and gain (when
	 * unspecified, they are 2f and 0.5f).
	 * 
	 * @param seed      the int seed for the noise, which should significantly
	 *                  affect the produced noise
	 * @param frequency the multiplier for all dimensions, which is usually fairly
	 *                  small (1.0f/32.0f is the default)
	 * @param noiseType the noiseType, which should be a constant from this class
	 *                  (see {@link #setNoiseType(int)})
	 */
	public Noise(int seed, float frequency, int noiseType) {
		this(seed, frequency, noiseType, 1, 2f, 0.5f);
	}

	/**
	 * A constructor that takes several parameters to specify the Noise from the
	 * start. An example call to this would be
	 * {@code new Noise(1337, 0.02f, Noise.SIMPLEX_FRACTAL, 4)}, which makes noise
	 * with the seed 1337, a lower frequency, 4 octaves of Simplex noise, and normal
	 * lacunarity and gain (when unspecified, they are 2f and 0.5f).
	 * 
	 * @param seed      the int seed for the noise, which should significantly
	 *                  affect the produced noise
	 * @param frequency the multiplier for all dimensions, which is usually fairly
	 *                  small (1.0f/32.0f is the default)
	 * @param noiseType the noiseType, which should be a constant from this class
	 *                  (see {@link #setNoiseType(int)})
	 * @param octaves   how many octaves of noise to use when the noiseType is one
	 *                  of the _FRACTAL types
	 */
	public Noise(int seed, float frequency, int noiseType, int octaves) {
		this(seed, frequency, noiseType, octaves, 2f, 0.5f);
	}

	/**
	 * A constructor that takes a lot of parameters to specify the Noise from the
	 * start. An example call to this would be
	 * {@code new Noise(1337, 0.02f, Noise.SIMPLEX_FRACTAL, 4, 0.5f, 2f)}, which
	 * makes noise with a lower frequency, 4 octaves of Simplex noise, and the
	 * "inverse" effect on how those octaves work (which makes the extra added
	 * octaves be more significant to the final result and also have a lower
	 * frequency, while normally added octaves have a higher frequency and tend to
	 * have a minor effect on the large-scale shape of the noise).
	 * 
	 * @param seed       the int seed for the noise, which should significantly
	 *                   affect the produced noise
	 * @param frequency  the multiplier for all dimensions, which is usually fairly
	 *                   small (1.0f/32.0f is the default)
	 * @param noiseType  the noiseType, which should be a constant from this class
	 *                   (see {@link #setNoiseType(int)})
	 * @param octaves    how many octaves of noise to use when the noiseType is one
	 *                   of the _FRACTAL types
	 * @param lacunarity typically 2.0, or 0.5 to change how extra octaves work
	 *                   (inverse mode)
	 * @param gain       typically 0.5, or 2.0 to change how extra octaves work
	 *                   (inverse mode)
	 */
	public Noise(int seed, float frequency, int noiseType, int octaves, float lacunarity, float gain) {
		this.seed = seed;
		this.frequency = Math.max(0.0001f, frequency);
		this.noiseType = noiseType;
		this.octaves = octaves;
		this.lacunarity = lacunarity;
		this.gain = gain;
		calculateFractalBounding();
	}

	/**
	 * Copy constructor; copies all non-temporary fields from {@code other} into
	 * this. Everything this copies is a primitive value.
	 * 
	 * @param other another Noise, which must not be null
	 */
	public Noise(final Noise other) {
		this(other.seed, other.frequency, other.noiseType, other.octaves, other.lacunarity, other.gain);
		this.fractalType = other.fractalType;
		this.interpolation = other.interpolation;
		this.gradientPerturbAmp = other.gradientPerturbAmp;
		this.cellularReturnType = other.cellularReturnType;
		this.cellularDistanceFunction = other.cellularDistanceFunction;
		this.foamSharpness = other.foamSharpness;
		this.mutation = other.mutation;
	}

	protected static float dotf(final float[] g, final float x, final float y) {
		return g[0] * x + g[1] * y;
	}

	/**
	 * @return Returns the seed used by this object
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * Sets the seed used for all noise types, as a long. If this is not called,
	 * defaults to 1337L.
	 * 
	 * @param seed a seed as a long
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}

	/**
	 * Sets the frequency for all noise types. If this is not called, it defaults to
	 * 0.03125f (or 1f/32f).
	 * 
	 * @param frequency the frequency for all noise types, as a positive non-zero
	 *                  float
	 */
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	/**
	 * Gets the frequency for all noise types. The default is 0.03125f, or 1f/32f.
	 * 
	 * @return the frequency for all noise types, which should be a positive
	 *         non-zero float
	 */
	public float getFrequency() {
		return frequency;
	}

	/**
	 * Changes the interpolation method used to smooth between noise values, using
	 * one of the following constants from this class (lowest to highest quality):
	 * {@link #LINEAR} (0), {@link #HERMITE} (1), or {@link #QUINTIC} (2). If this
	 * is not called, it defaults to HERMITE. This is used in Value, Perlin, and
	 * Position Perturbing, and because it is used in Value, that makes it also
	 * apply to Foam, Honey, and Mutant.
	 * 
	 * @param interpolation an int (0, 1, or 2) corresponding to a constant from
	 *                      this class for an interpolation type
	 */
	public void setInterpolation(int interpolation) {
		this.interpolation = Math.min(Math.max(interpolation, 0), 2);
	}

	/**
	 * Gets the constant corresponding to the interpolation method used to smooth
	 * between noise values. This is always one of the constants {@link #LINEAR}
	 * (0), {@link #HERMITE} (1), or {@link #QUINTIC} (2). If this is not called, it
	 * defaults to HERMITE. This is used in Value, Perlin, and Position Perturbing,
	 * and because it is used in Value, that makes it also apply to Foam, Honey, and
	 * Mutant.
	 * 
	 * @return an int (0, 1, or 2) corresponding to a constant from this class for
	 *         an interpolation type
	 */
	public int getInterpolation() {
		return interpolation;
	}

	/**
	 * Sets the default type of noise returned by
	 * {@link #getConfiguredNoise(float, float)}, using one of the following
	 * constants in this class: {@link #VALUE} (0), {@link #VALUE_FRACTAL} (1),
	 * {@link #PERLIN} (2), {@link #PERLIN_FRACTAL} (3), {@link #SIMPLEX} (4),
	 * {@link #SIMPLEX_FRACTAL} (5), {@link #CELLULAR} (6), {@link #WHITE_NOISE}
	 * (7), {@link #CUBIC} (8), {@link #CUBIC_FRACTAL} (9), {@link #FOAM} (10),
	 * {@link #FOAM_FRACTAL} (11), {@link #HONEY} (12), {@link #HONEY_FRACTAL} (13),
	 * {@link #MUTANT} (14), or {@link #MUTANT_FRACTAL} (15). If this isn't called,
	 * getConfiguredNoise() will default to SIMPLEX_FRACTAL. Note that if you have a
	 * fractal noise type, you can get the corresponding non-fractal noise type by
	 * subtracting 1 from the constant this returns. The reverse is not always true,
	 * because Cellular and White Noise have no fractal version.
	 * 
	 * @param noiseType an int from 0 to 15 corresponding to a constant from this
	 *                  class for a noise type
	 */
	public void setNoiseType(int noiseType) {
		this.noiseType = noiseType;
	}

	/**
	 * Gets the default type of noise returned by
	 * {@link #getConfiguredNoise(float, float)}, using one of the following
	 * constants in this class: {@link #VALUE} (0), {@link #VALUE_FRACTAL} (1),
	 * {@link #PERLIN} (2), {@link #PERLIN_FRACTAL} (3), {@link #SIMPLEX} (4),
	 * {@link #SIMPLEX_FRACTAL} (5), {@link #CELLULAR} (6), {@link #WHITE_NOISE}
	 * (7), {@link #CUBIC} (8), {@link #CUBIC_FRACTAL} (9), {@link #FOAM} (10),
	 * {@link #FOAM_FRACTAL} (11), {@link #HONEY} (12), {@link #HONEY_FRACTAL} (13),
	 * {@link #MUTANT} (14), or {@link #MUTANT_FRACTAL} (15). The default is
	 * SIMPLEX_FRACTAL. Note that if you have a fractal noise type, you can get the
	 * corresponding non-fractal noise type by subtracting 1 from the constant this
	 * returns. The reverse is not always true, because Cellular and White Noise
	 * have no fractal version.
	 * 
	 * @return the noise type as a code, from 0 to 15 inclusive
	 */
	public int getNoiseType() {
		return noiseType;
	}

	/**
	 * Sets the octave count for all fractal noise types. If this isn't called, it
	 * will default to 3.
	 * 
	 * @param octaves the number of octaves to use for fractal noise types, as a
	 *                positive non-zero int
	 */
	public void setFractalOctaves(int octaves) {
		this.octaves = octaves;
		calculateFractalBounding();
	}

	/**
	 * Gets the octave count for all fractal noise types. The default is 3.
	 * 
	 * @return the number of octaves to use for fractal noise types, as a positive
	 *         non-zero int
	 */
	public int getFractalOctaves() {
		return octaves;
	}

	/**
	 * Sets the octave lacunarity for all fractal noise types. Lacunarity is a
	 * multiplicative change to frequency between octaves. If this isn't called, it
	 * defaults to 2.
	 * 
	 * @param lacunarity a non-0 float that will be used for the lacunarity of
	 *                   fractal noise types; commonly 2.0 or 0.5
	 */
	public void setFractalLacunarity(float lacunarity) {
		this.lacunarity = lacunarity;
	}

	/**
	 * Gets the octave lacunarity for all fractal noise types. Lacunarity is a
	 * multiplicative change to frequency between octaves. If this wasn't changed,
	 * it defaults to 2.
	 * 
	 * @return a float that will be used for the lacunarity of fractal noise types;
	 *         commonly 2.0 or 0.5
	 */
	public float getFractalLacunarity() {
		return lacunarity;
	}

	/**
	 * Sets the octave gain for all fractal noise types. If this isn't called, it
	 * defaults to 0.5.
	 * 
	 * @param gain the gain between octaves, as a float
	 */
	public void setFractalGain(float gain) {
		this.gain = gain;
		calculateFractalBounding();
	}

	/**
	 * Sets the octave gain for all fractal noise types. This is typically related
	 * to {@link #getFractalLacunarity()}, with gain falling as lacunarity rises. If
	 * this wasn't changed, it defaults to 0.5.
	 * 
	 * @return the gain between octaves, as a float
	 */
	public float getFractalGain() {
		return gain;
	}

	/**
	 * Sets the method for combining octaves in all fractal noise types, allowing an
	 * int argument corresponding to one of the following constants from this class:
	 * {@link #FBM} (0), {@link #BILLOW} (1), or {@link #RIDGED_MULTI} (2). If this
	 * hasn't been called, it will use FBM.
	 * 
	 * @param fractalType an int (0, 1, or 2) that corresponds to a constant like
	 *                    {@link #FBM} or {@link #RIDGED_MULTI}
	 */
	public void setFractalType(int fractalType) {
		this.fractalType = fractalType;
	}

	/**
	 * Gets the method for combining octaves in all fractal noise types, allowing an
	 * int argument corresponding to one of the following constants from this class:
	 * {@link #FBM} (0), {@link #BILLOW} (1), or {@link #RIDGED_MULTI} (2). The
	 * default is FBM.
	 * 
	 * @return the fractal type as a code; 0, 1, or 2
	 */
	public int getFractalType() {
		return fractalType;
	}

	/**
	 * Sets the distance function used in cellular noise calculations, allowing an
	 * int argument corresponding to one of the following constants from this class:
	 * {@link #EUCLIDEAN} (0), {@link #MANHATTAN} (1), or {@link #NATURAL} (2). If
	 * this hasn't been called, it will use EUCLIDEAN.
	 * 
	 * @param cellularDistanceFunction an int that can be 0, 1, or 2, corresponding
	 *                                 to a constant from this class
	 */
	public void setCellularDistanceFunction(int cellularDistanceFunction) {
		this.cellularDistanceFunction = cellularDistanceFunction;
	}

	/**
	 * Gets the distance function used in cellular noise calculations, as an int
	 * constant from this class: {@link #EUCLIDEAN} (0), {@link #MANHATTAN} (1), or
	 * {@link #NATURAL} (2). If this wasn't changed, it will use EUCLIDEAN.
	 * 
	 * @return an int that can be 0, 1, or 2, corresponding to a constant from this
	 *         class
	 */
	public int getCellularDistanceFunction() {
		return cellularDistanceFunction;
	}

	/**
	 * Sets the return type from cellular noise calculations, allowing an int
	 * argument corresponding to one of the following constants from this class:
	 * {@link #CELL_VALUE} (0), {@link #NOISE_LOOKUP} (1), {@link #DISTANCE} (2),
	 * {@link #DISTANCE_2} (3), {@link #DISTANCE_2_ADD} (4), {@link #DISTANCE_2_SUB}
	 * (5), {@link #DISTANCE_2_MUL} (6), or {@link #DISTANCE_2_DIV} (7). If this
	 * isn't called, it will use CELL_VALUE.
	 * 
	 * @param cellularReturnType a constant from this class (see above JavaDoc)
	 */
	public void setCellularReturnType(int cellularReturnType) {
		this.cellularReturnType = cellularReturnType;
	}

	/**
	 * Gets the return type from cellular noise calculations, corresponding to a
	 * constant from this class: {@link #CELL_VALUE} (0), {@link #NOISE_LOOKUP} (1),
	 * {@link #DISTANCE} (2), {@link #DISTANCE_2} (3), {@link #DISTANCE_2_ADD} (4),
	 * {@link #DISTANCE_2_SUB} (5), {@link #DISTANCE_2_MUL} (6), or
	 * {@link #DISTANCE_2_DIV} (7). If this wasn't changed, it will use CELL_VALUE.
	 * 
	 * @return a constant from this class representing a type of cellular noise
	 *         calculation
	 */
	public int getCellularReturnType() {
		return cellularReturnType;
	}

	/**
	 * A no-op method that is here for compatibility with earlier versions.
	 * 
	 * @param noise ignored
	 */
	public void setCellularNoiseLookup(Noise noise) {
	}

	/**
	 * Sets the maximum perturb distance from original location when using
	 * {@link #gradientPerturb2(float[])}, {@link #gradientPerturb3(float[])},
	 * {@link #gradientPerturbFractal2(float[])}, or
	 * {@link #gradientPerturbFractal3(float[])}; the default is 1.0.
	 * 
	 * @param gradientPerturbAmp the maximum perturb distance from the original
	 *                           location when using relevant methods
	 */
	public void setGradientPerturbAmp(float gradientPerturbAmp) {
		this.gradientPerturbAmp = gradientPerturbAmp / 0.45f;
	}

	/**
	 * Gets the maximum perturb distance from original location when using
	 * {@link #gradientPerturb2(float[])}, {@link #gradientPerturb3(float[])},
	 * {@link #gradientPerturbFractal2(float[])}, or
	 * {@link #gradientPerturbFractal3(float[])}; the default is 1.0.
	 * 
	 * @return the maximum perturb distance from the original location when using
	 *         relevant methods
	 */
	public float getGradientPerturbAmp() {
		return gradientPerturbAmp * 0.45f;
	}

	/**
	 * Gets the "sharpness" for the {@link #FOAM}, {@link #FOAM_FRACTAL},
	 * {@link #MUTANT}, and {@link #MUTANT_FRACTAL} noise types, which is usually
	 * around 0.25f to 2.0f, and defaults to 1.0f. High values produce extreme
	 * results more often, and low values produce mid-range values more often.
	 * 
	 * @return the current "sharpness" {@link #FOAM} and {@link #FOAM_FRACTAL} noise
	 *         types
	 */
	public float getFoamSharpness() {
		return foamSharpness;
	}

	/**
	 * Only used with {@link #FOAM}, {@link #FOAM_FRACTAL}, {@link #MUTANT}, and
	 * {@link #MUTANT_FRACTAL} noise types, this affects how often the noise will
	 * produce very high and very low results (more often with high values of
	 * foamSharpness), as opposed to mid-range (more often with low values of
	 * foamSharpness). <br>
	 * This defaults to 1.0f if not set.
	 * 
	 * @param foamSharpness higher results (above 1) tend to produce extremes, lower
	 *                      results (below 1) produce mid-range
	 */
	public void setFoamSharpness(float foamSharpness) {
		this.foamSharpness = foamSharpness;
	}

	/**
	 * Gets the mutation value used by {@link #MUTANT} and {@link #MUTANT_FRACTAL}
	 * noise types, which allows making small changes to the result when the
	 * mutation values are slightly different.
	 * 
	 * @return the current mutation value, which can be any finite float
	 */
	public float getMutation() {
		return mutation;
	}

	/**
	 * Sets the mutation value used by {@link #MUTANT} and {@link #MUTANT_FRACTAL}
	 * noise types, which can be any finite float. Small changes to the mutation
	 * value cause small changes in the result, unlike changes to the seed.
	 * 
	 * @param mutation the mutation value to use, which can be any finite float
	 */
	public void setMutation(float mutation) {
		this.mutation = mutation;
	}

	public double getNoise(double x, double y) {
		return getConfiguredNoise((float) x, (float) y);
	}

	public double getNoiseWithSeed(double x, double y, long seed) {
		int s = this.seed;
		this.seed = (int) (seed ^ seed >>> 32);
		double r = getConfiguredNoise((float) x, (float) y);
		this.seed = s;
		return r;
	}

	public double getNoise(double x, double y, double z) {
		return getConfiguredNoise((float) x, (float) y, (float) z);
	}

	public double getNoiseWithSeed(double x, double y, double z, long seed) {
		int s = this.seed;
		this.seed = (int) (seed ^ seed >>> 32);
		double r = getConfiguredNoise((float) x, (float) y, (float) z);
		this.seed = s;
		return r;
	}

	public float getNoiseWithSeed(float x, float y, int seed) {
		final int s = this.seed;
		this.seed = seed;
		float r = getConfiguredNoise(x, y);
		this.seed = s;
		return r;
	}

	public float getNoiseWithSeed(float x, float y, float z, int seed) {
		final int s = this.seed;
		this.seed = seed;
		float r = getConfiguredNoise(x, y, z);
		this.seed = s;
		return r;
	}

	protected static int fastFloor(float f) {
		return (f >= 0 ? (int) f : (int) f - 1);
	}

	protected static int fastRound(float f) {
		return (f >= 0) ? (int) (f + 0.5f) : (int) (f - 0.5f);
	}

	private static float lerp(float a, float b, float t) {
		return a + t * (b - a);
	}

	protected static float hermiteInterpolator(float t) {
		return t * t * (3 - 2 * t);
	}

	protected static float quinticInterpolator(float t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	protected static float cubicLerp(float a, float b, float c, float d, float t) {
		float p = (d - c) - (a - b);
		return t * (t * t * p + t * ((a - b) - p) + (c - a)) + b;
	}

	private void calculateFractalBounding() {
		float amp = gain;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= gain;
		}
		fractalBounding = 1 / ampFractal;
	}

	private float valCoord2D(int seed, int x, int y) {
		return (hashAll(x, y, seed) >> 7) * 0x1.0p-24f;
	}

	private float valCoord3D(int seed, int x, int y, int z) {
		return (hashAll(x, y, z, seed) >> 7) * 0x1.0p-24f;
	}

	protected float gradCoord2D(int seed, int x, int y, float xd, float yd) {
		final int hash = hash256(x, y, seed) << 1;
		return xd * GRAD_2D[hash] + yd * GRAD_2D[hash + 1];
	}

	protected float gradCoord3D(int seed, int x, int y, int z, float xd, float yd, float zd) {
		final int hash = hash32(x, y, z, seed) << 2;
		return xd * GRAD_3D[hash] + yd * GRAD_3D[hash + 1] + zd * GRAD_3D[hash + 2];
	}

	/**
	 * After being configured with the setters in this class, such as
	 * {@link #setNoiseType(int)}, {@link #setFrequency(float)},
	 * {@link #setFractalOctaves(int)}, and {@link #setFractalType(int)}, among
	 * others, you can call this method to get the particular variety of noise you
	 * specified, in 2D.
	 * 
	 * @param x x position, as a float; the range this should have depends on
	 *          {@link #getFrequency()}
	 * @param y y position, as a float; the range this should have depends on
	 *          {@link #getFrequency()}
	 * @return noise as a float from -1f to 1f
	 */
	public float getConfiguredNoise(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (noiseType) {
		case VALUE:
			return singleValue(seed, x, y);
		case VALUE_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleValueFractalBillow(x, y);
			case RIDGED_MULTI:
				return singleValueFractalRidgedMulti(x, y);
			default:
				return singleValueFractalFBM(x, y);
			}
		case FOAM:
			return singleFoam(seed, x, y);
		case FOAM_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleFoamFractalBillow(x, y);
			case RIDGED_MULTI:
				return singleFoamFractalRidgedMulti(x, y);
			default:
				return singleFoamFractalFBM(x, y);
			}
		case MUTANT:
			return singleFoam(seed, x, y, mutation);
		case MUTANT_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleFoamFractalBillow(x, y, mutation);
			case RIDGED_MULTI:
				return singleFoamFractalRidgedMulti(x, y, mutation);
			default:
				return singleFoamFractalFBM(x, y, mutation);
			}
		case HONEY:
			return singleHoney(seed, x, y);
		case HONEY_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleHoneyFractalBillow(x, y);
			case RIDGED_MULTI:
				return singleHoneyFractalRidgedMulti(x, y);
			default:
				return singleHoneyFractalFBM(x, y);
			}
		case PERLIN:
			return singlePerlin(seed, x, y);
		case PERLIN_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singlePerlinFractalBillow(x, y);
			case RIDGED_MULTI:
				return singlePerlinFractalRidgedMulti(x, y);
			default:
				return singlePerlinFractalFBM(x, y);
			}
		case SIMPLEX_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleSimplexFractalBillow(x, y);
			case RIDGED_MULTI:
				return singleSimplexFractalRidgedMulti(x, y);
			default:
				return singleSimplexFractalFBM(x, y);
			}
		case CELLULAR:
			switch (cellularReturnType) {
			case CELL_VALUE:
			case NOISE_LOOKUP:
			case DISTANCE:
				return singleCellular(x, y);
			default:
				return singleCellular2Edge(x, y);
			}
		case WHITE_NOISE:
			return getWhiteNoise(x, y);
		case CUBIC:
			return singleCubic(seed, x, y);
		case CUBIC_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleCubicFractalBillow(x, y);
			case RIDGED_MULTI:
				return singleCubicFractalRidgedMulti(x, y);
			default:
				return singleCubicFractalFBM(x, y);
			}
		default:
			return singleSimplex(seed, x, y);
		}
	}

	/**
	 * After being configured with the setters in this class, such as
	 * {@link #setNoiseType(int)}, {@link #setFrequency(float)},
	 * {@link #setFractalOctaves(int)}, and {@link #setFractalType(int)}, among
	 * others, you can call this method to get the particular variety of noise you
	 * specified, in 3D.
	 * 
	 * @param x x position, as a float; the range this should have depends on
	 *          {@link #getFrequency()}
	 * @param y y position, as a float; the range this should have depends on
	 *          {@link #getFrequency()}
	 * @param z z position, as a float; the range this should have depends on
	 *          {@link #getFrequency()}
	 * @return noise as a float from -1f to 1f
	 */
	public float getConfiguredNoise(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (noiseType) {
		case VALUE:
			return singleValue(seed, x, y, z);
		case VALUE_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleValueFractalBillow(x, y, z);
			case RIDGED_MULTI:
				return singleValueFractalRidgedMulti(x, y, z);
			default:
				return singleValueFractalFBM(x, y, z);
			}
		case FOAM:
			return singleFoam(seed, x, y, z);
		case FOAM_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleFoamFractalBillow(x, y, z);
			case RIDGED_MULTI:
				return singleFoamFractalRidgedMulti(x, y, z);
			default:
				return singleFoamFractalFBM(x, y, z);
			}
		case HONEY:
			return singleHoney(seed, x, y, z);
		case HONEY_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleHoneyFractalBillow(x, y, z);
			case RIDGED_MULTI:
				return singleHoneyFractalRidgedMulti(x, y, z);
			default:
				return singleHoneyFractalFBM(x, y, z);
			}
		case PERLIN:
			return singlePerlin(seed, x, y, z);
		case PERLIN_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singlePerlinFractalBillow(x, y, z);
			case RIDGED_MULTI:
				return singlePerlinFractalRidgedMulti(x, y, z);
			default:
				return singlePerlinFractalFBM(x, y, z);
			}
		case SIMPLEX_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleSimplexFractalBillow(x, y, z);
			case RIDGED_MULTI:
				return singleSimplexFractalRidgedMulti(x, y, z);
			default:
				return singleSimplexFractalFBM(x, y, z);
			}
		case CELLULAR:
			switch (cellularReturnType) {
			case CELL_VALUE:
			case NOISE_LOOKUP:
			case DISTANCE:
				return singleCellular(x, y, z);
			default:
				return singleCellular2Edge(x, y, z);
			}
		case WHITE_NOISE:
			return getWhiteNoise(x, y, z);
		case CUBIC:
			return singleCubic(seed, x, y, z);
		case CUBIC_FRACTAL:
			switch (fractalType) {
			case BILLOW:
				return singleCubicFractalBillow(x, y, z);
			case RIDGED_MULTI:
				return singleCubicFractalRidgedMulti(x, y, z);
			default:
				return singleCubicFractalFBM(x, y, z);
			}
		default:
			return singleSimplex(seed, x, y, z);
		}
	}

	// White Noise

	/**
	 * Gets the bit representation of a float with
	 * {@link Float#floatToIntBits(float)} and mixes its typically-more-varied high
	 * bits with its low bits, returning an int. NOTE: if you target GWT, this
	 * method will be unnecessarily slow because of GWT's poor implementation of
	 * floatToIntBits. If you use libGDX and want to use the white noise methods
	 * here, you should extend this class and override this method like so:
	 * 
	 * <pre>
	 * <code>
	 * public int floatToIntMixed(final float f) {
	 *     final int i = com.badlogic.gdx.utils.NumberUtils.floatToIntBits(f);
	 *     return i ^ i >>> 16;
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param f can be any float except for NaN, though this will technically work
	 *          on NaN
	 * @return a slightly-mixed version of the bits that make up {@code f}, as an
	 *         int
	 */
	public int floatToIntMixed(final float f) {
		final int i = Float.floatToIntBits(f);
		return i ^ i >>> 16;
	}

	public float getWhiteNoise(float x, float y) {
		int xi = floatToIntMixed(x);
		int yi = floatToIntMixed(y);

		return valCoord2D(seed, xi, yi);
	}

	public float getWhiteNoise(float x, float y, float z) {
		int xi = floatToIntMixed(x);
		int yi = floatToIntMixed(y);
		int zi = floatToIntMixed(z);

		return valCoord3D(seed, xi, yi, zi);
	}

	// Value Noise
	// x should be premultiplied by 0xD1B55
	// y should be premultiplied by 0xABC99
	private static int hashPart1024(final int x, final int y, int s) {
		s += x ^ y;
		return (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493 >> 22;
	}

	// x should be premultiplied by 0xDB4F1
	// y should be premultiplied by 0xBBE05
	// z should be premultiplied by 0xA0F2F
	private static int hashPart1024(final int x, final int y, final int z, int s) {
		s += x ^ y ^ z;
		return (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493 >> 22;
	}

	public float getValueFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
		case FBM:
			return singleValueFractalFBM(x, y);
		case BILLOW:
			return singleValueFractalBillow(x, y);
		case RIDGED_MULTI:
			return singleValueFractalRidgedMulti(x, y);
		default:
			return 0;
		}
	}

	private float singleValueFractalFBM(float x, float y) {
		int seed = this.seed;
		float sum = singleValue(seed, x, y);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += singleValue(++seed, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleValueFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singleValue(seed, x, y)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			amp *= gain;
			sum += (Math.abs(singleValue(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleValueFractalRidgedMulti(float x, float y) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleValue(seed + i, x, y));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getValue(float x, float y) {
		return singleValue(seed, x * frequency, y * frequency);
	}

	public float singleValue(int seed, float x, float y) {
		int xFloor = x >= 0 ? (int) x : (int) x - 1;
		x -= xFloor;
		int yFloor = y >= 0 ? (int) y : (int) y - 1;
		y -= yFloor;
		switch (interpolation) {
		case HERMITE:
			x = hermiteInterpolator(x);
			y = hermiteInterpolator(y);
			break;
		case QUINTIC:
			x = quinticInterpolator(x);
			y = quinticInterpolator(y);
			break;
		}
		xFloor *= 0xD1B55;
		yFloor *= 0xABC99;
		return ((1 - y)
				* ((1 - x) * hashPart1024(xFloor, yFloor, seed) + x * hashPart1024(xFloor + 0xD1B55, yFloor, seed))
				+ y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xABC99, seed)
						+ x * hashPart1024(xFloor + 0xD1B55, yFloor + 0xABC99, seed)))
				* 0x1p-9f;
	}

	/**
	 * Produces noise from 0 to 1, instead of the normal -1 to 1.
	 * 
	 * @param seed
	 * @param x
	 * @param y
	 * @return noise from 0 to 1.
	 */
	protected float valueNoise(int seed, float x, float y) {
		int xFloor = x >= 0 ? (int) x : (int) x - 1;
		x -= xFloor;
		x *= x * (3 - 2 * x);
		int yFloor = y >= 0 ? (int) y : (int) y - 1;
		y -= yFloor;
		y *= y * (3 - 2 * y);
		xFloor *= 0xD1B55;
		yFloor *= 0xABC99;
		return ((1 - y)
				* ((1 - x) * hashPart1024(xFloor, yFloor, seed) + x * hashPart1024(xFloor + 0xD1B55, yFloor, seed))
				+ y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xABC99, seed)
						+ x * hashPart1024(xFloor + 0xD1B55, yFloor + 0xABC99, seed)))
				* 0x1p-10f + 0.5f;
	}

	public float getValueFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
		case BILLOW:
			return singleValueFractalBillow(x, y, z);
		case RIDGED_MULTI:
			return singleValueFractalRidgedMulti(x, y, z);
		default:
			return singleValueFractalFBM(x, y, z);
		}
	}

	private float singleValueFractalFBM(float x, float y, float z) {
		int seed = this.seed;
		float sum = singleValue(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singleValue(++seed, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleValueFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singleValue(seed, x, y, z)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleValue(++seed, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleValueFractalRidgedMulti(float x, float y, float z) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleValue(seed + i, x, y, z));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getValue(float x, float y, float z) {
		return singleValue(seed, x * frequency, y * frequency, z * frequency);
	}

	public float singleValue(int seed, float x, float y, float z) {
		int xFloor = x >= 0 ? (int) x : (int) x - 1;
		x -= xFloor;
		int yFloor = y >= 0 ? (int) y : (int) y - 1;
		y -= yFloor;
		int zFloor = z >= 0 ? (int) z : (int) z - 1;
		z -= zFloor;
		switch (interpolation) {
		case HERMITE:
			x = hermiteInterpolator(x);
			y = hermiteInterpolator(y);
			z = hermiteInterpolator(z);
			break;
		case QUINTIC:
			x = quinticInterpolator(x);
			y = quinticInterpolator(y);
			z = quinticInterpolator(z);
			break;
		}
		// 0xDB4F1, 0xBBE05, 0xA0F2F
		xFloor *= 0xDB4F1;
		yFloor *= 0xBBE05;
		zFloor *= 0xA0F2F;
		return ((1 - z)
				* ((1 - y)
						* ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, seed)
								+ x * hashPart1024(xFloor + 0xDB4F1, yFloor, zFloor, seed))
						+ y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xBBE05, zFloor, seed)
								+ x * hashPart1024(xFloor + 0xDB4F1, yFloor + 0xBBE05, zFloor, seed)))
				+ z * ((1 - y)
						* ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xA0F2F, seed)
								+ x * hashPart1024(xFloor + 0xDB4F1, yFloor, zFloor + 0xA0F2F, seed))
						+ y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xBBE05, zFloor + 0xA0F2F, seed)
								+ x * hashPart1024(xFloor + 0xDB4F1, yFloor + 0xBBE05, zFloor + 0xA0F2F, seed))))
				* 0x1p-9f;
	}

	/**
	 * Produces noise from 0 to 1, instead of the normal -1 to 1.
	 * 
	 * @param seed
	 * @param x
	 * @param y
	 * @param z
	 * @return noise from 0 to 1.
	 */
	protected float valueNoise(int seed, float x, float y, float z) {
		int xFloor = x >= 0 ? (int) x : (int) x - 1;
		x -= xFloor;
		x *= x * (3 - 2 * x);
		int yFloor = y >= 0 ? (int) y : (int) y - 1;
		y -= yFloor;
		y *= y * (3 - 2 * y);
		int zFloor = z >= 0 ? (int) z : (int) z - 1;
		z -= zFloor;
		z *= z * (3 - 2 * z);
		// 0xDB4F1, 0xBBE05, 0xA0F2F
		xFloor *= 0xDB4F1;
		yFloor *= 0xBBE05;
		zFloor *= 0xA0F2F;
		return ((1 - z)
				* ((1 - y)
						* ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, seed)
								+ x * hashPart1024(xFloor + 0xDB4F1, yFloor, zFloor, seed))
						+ y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xBBE05, zFloor, seed)
								+ x * hashPart1024(xFloor + 0xDB4F1, yFloor + 0xBBE05, zFloor, seed)))
				+ z * ((1 - y)
						* ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xA0F2F, seed)
								+ x * hashPart1024(xFloor + 0xDB4F1, yFloor, zFloor + 0xA0F2F, seed))
						+ y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xBBE05, zFloor + 0xA0F2F, seed)
								+ x * hashPart1024(xFloor + 0xDB4F1, yFloor + 0xBBE05, zFloor + 0xA0F2F, seed))))
				* 0x1p-10f + 0.5f;
	}

	// Foam Noise

	public float getFoam(float x, float y) {
		return singleFoam(seed, x * frequency, y * frequency);
	}

	public float singleFoam(int seed, float x, float y) {
		final float p0 = x;
		final float p1 = x * -0.5f + y * 0.8660254037844386f;
		final float p2 = x * -0.5f + y * -0.8660254037844387f;

		float xin = p2;
		float yin = p0;
		final float a = valueNoise(seed, xin, yin);
		seed += 0x9E3779BD;
		seed ^= seed >>> 14;
		xin = p1;
		yin = p2;
		final float b = valueNoise(seed, xin + a, yin);
		seed += 0x9E3779BD;
		seed ^= seed >>> 14;
		xin = p0;
		yin = p1;
		final float c = valueNoise(seed, xin + b, yin);
		final float result = (a + b + c) * F3f;
		final float sharp = foamSharpness * 2.2f;
		final float diff = 0.5f - result;
		final int sign = Float.floatToIntBits(diff) >> 31, one = sign | 1;
		return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
	}

	public float getFoamFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
		case FBM:
			return singleFoamFractalFBM(x, y);
		case BILLOW:
			return singleFoamFractalBillow(x, y);
		case RIDGED_MULTI:
			return singleFoamFractalRidgedMulti(x, y);
		default:
			return 0;
		}
	}

	private float singleFoamFractalFBM(float x, float y) {
		int seed = this.seed;
		float sum = singleFoam(seed, x, y);
		float amp = 1, t;

		for (int i = 1; i < octaves; i++) {
			t = x;
			x = y * lacunarity;
			y = t * lacunarity;

			amp *= gain;
			sum += singleFoam(seed + i, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleFoamFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singleFoam(seed, x, y)) * 2 - 1;
		float amp = 1, t;

		for (int i = 1; i < octaves; i++) {
			t = x;
			x = y * lacunarity;
			y = t * lacunarity;

			amp *= gain;
			sum += (Math.abs(singleFoam(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleFoamFractalRidgedMulti(float x, float y) {
		int seed = this.seed;
		float t;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleFoam(seed + i, x, y));
			correction += (exp *= 0.5);
			sum += spike * exp;
			t = x;
			x = y * lacunarity;
			y = t * lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getFoamFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
		case FBM:
			return singleFoamFractalFBM(x, y, z);
		case BILLOW:
			return singleFoamFractalBillow(x, y, z);
		case RIDGED_MULTI:
			return singleFoamFractalRidgedMulti(x, y, z);
		default:
			return 0;
		}
	}

	private float singleFoamFractalFBM(float x, float y, float z) {
		int seed = this.seed;
		float sum = singleFoam(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singleFoam(++seed, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleFoamFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singleFoam(seed, x, y, z)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleFoam(++seed, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleFoamFractalRidgedMulti(float x, float y, float z) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleFoam(seed + i, x, y, z));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getFoam(float x, float y, float z) {
		return singleFoam(seed, x * frequency, y * frequency, z * frequency);
	}

	public float singleFoam(int seed, float x, float y, float z) {
		final float p0 = x;
		final float p1 = x * -0.3333333333333333f + y * 0.9428090415820634f;
		final float p2 = x * -0.3333333333333333f + y * -0.4714045207910317f + z * 0.816496580927726f;
		final float p3 = x * -0.3333333333333333f + y * -0.4714045207910317f + z * -0.816496580927726f;

		float xin = p3;
		float yin = p2;
		float zin = p0;
		final float a = valueNoise(seed, xin, yin, zin);
		seed += 0x9E3779BD;
		seed ^= seed >>> 14;
		xin = p0;
		yin = p1;
		zin = p3;
		final float b = valueNoise(seed, xin + a, yin, zin);
		seed += 0x9E3779BD;
		seed ^= seed >>> 14;
		xin = p1;
		yin = p2;
		zin = p3;
		final float c = valueNoise(seed, xin + b, yin, zin);
		seed += 0x9E3779BD;
		seed ^= seed >>> 14;
		xin = p0;
		yin = p1;
		zin = p2;
		final float d = valueNoise(seed, xin + c, yin, zin);

		final float result = (a + b + c + d) * 0.25f;
		final float sharp = foamSharpness * 3.3f;
		final float diff = 0.5f - result;
		final int sign = Float.floatToIntBits(diff) >> 31, one = sign | 1;
		return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;

	}

	// (Classic) Perlin Noise, AKA Gradient Noise

	public float getPerlinFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
		case FBM:
			return singlePerlinFractalFBM(x, y);
		case BILLOW:
			return singlePerlinFractalBillow(x, y);
		case RIDGED_MULTI:
			return singlePerlinFractalRidgedMulti(x, y);
		default:
			return 0;
		}
	}

	private float singlePerlinFractalFBM(float x, float y) {
		int seed = this.seed;
		float sum = singlePerlin(seed, x, y);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += singlePerlin(++seed, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	private float singlePerlinFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singlePerlin(seed, x, y)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singlePerlin(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singlePerlinFractalRidgedMulti(float x, float y) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singlePerlin(seed + i, x, y));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getPerlin(float x, float y) {
		return singlePerlin(seed, x * frequency, y * frequency);
	}

	public float singlePerlin(int seed, float x, float y) {
		int x0 = fastFloor(x);
		int y0 = fastFloor(y);
		int x1 = x0 + 1;
		int y1 = y0 + 1;

		float xs, ys;
		switch (interpolation) {
		default:
		case LINEAR:
			xs = x - x0;
			ys = y - y0;
			break;
		case HERMITE:
			xs = hermiteInterpolator(x - x0);
			ys = hermiteInterpolator(y - y0);
			break;
		case QUINTIC:
			xs = quinticInterpolator(x - x0);
			ys = quinticInterpolator(y - y0);
			break;
		}

		float xd0 = x - x0;
		float yd0 = y - y0;
		float xd1 = xd0 - 1;
		float yd1 = yd0 - 1;

		float xf0 = lerp(gradCoord2D(seed, x0, y0, xd0, yd0), gradCoord2D(seed, x1, y0, xd1, yd0), xs);
		float xf1 = lerp(gradCoord2D(seed, x0, y1, xd0, yd1), gradCoord2D(seed, x1, y1, xd1, yd1), xs);

		return lerp(xf0, xf1, ys);
	}

	public float getPerlinFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
		case FBM:
			return singlePerlinFractalFBM(x, y, z);
		case BILLOW:
			return singlePerlinFractalBillow(x, y, z);
		case RIDGED_MULTI:
			return singlePerlinFractalRidgedMulti(x, y, z);
		default:
			return 0;
		}
	}

	private float singlePerlinFractalFBM(float x, float y, float z) {
		int seed = this.seed;
		float sum = singlePerlin(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singlePerlin(++seed, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singlePerlinFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singlePerlin(seed, x, y, z)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singlePerlin(++seed, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singlePerlinFractalRidgedMulti(float x, float y, float z) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singlePerlin(seed + i, x, y, z));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getPerlin(float x, float y, float z) {
		return singlePerlin(seed, x * frequency, y * frequency, z * frequency);
	}

	public float singlePerlin(int seed, float x, float y, float z) {
		int x0 = fastFloor(x);
		int y0 = fastFloor(y);
		int z0 = fastFloor(z);
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		int z1 = z0 + 1;

		float xs, ys, zs;
		switch (interpolation) {
		default:
		case LINEAR:
			xs = x - x0;
			ys = y - y0;
			zs = z - z0;
			break;
		case HERMITE:
			xs = hermiteInterpolator(x - x0);
			ys = hermiteInterpolator(y - y0);
			zs = hermiteInterpolator(z - z0);
			break;
		case QUINTIC:
			xs = quinticInterpolator(x - x0);
			ys = quinticInterpolator(y - y0);
			zs = quinticInterpolator(z - z0);
			break;
		}

		final float xd0 = x - x0;
		final float yd0 = y - y0;
		final float zd0 = z - z0;
		final float xd1 = xd0 - 1;
		final float yd1 = yd0 - 1;
		final float zd1 = zd0 - 1;

		final float xf00 = lerp(gradCoord3D(seed, x0, y0, z0, xd0, yd0, zd0),
				gradCoord3D(seed, x1, y0, z0, xd1, yd0, zd0), xs);
		final float xf10 = lerp(gradCoord3D(seed, x0, y1, z0, xd0, yd1, zd0),
				gradCoord3D(seed, x1, y1, z0, xd1, yd1, zd0), xs);
		final float xf01 = lerp(gradCoord3D(seed, x0, y0, z1, xd0, yd0, zd1),
				gradCoord3D(seed, x1, y0, z1, xd1, yd0, zd1), xs);
		final float xf11 = lerp(gradCoord3D(seed, x0, y1, z1, xd0, yd1, zd1),
				gradCoord3D(seed, x1, y1, z1, xd1, yd1, zd1), xs);

		final float yf0 = lerp(xf00, xf10, ys);
		final float yf1 = lerp(xf01, xf11, ys);

		return lerp(yf0, yf1, zs);
	}

	// Simplex Noise
	public float getSimplexFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
		case FBM:
			return singleSimplexFractalFBM(x, y);
		case BILLOW:
			return singleSimplexFractalBillow(x, y);
		case RIDGED_MULTI:
			return singleSimplexFractalRidgedMulti(x, y);
		default:
			return 0;
		}
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves and
	 * default frequency (0.03125), lacunarity (2) and gain (0.5) in 2D.
	 * 
	 * @param x
	 * @param y
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float layered2D(float x, float y, int seed, int octaves) {
		x *= 0.03125f;
		y *= 0.03125f;

		float sum = 1 - Math.abs(singleSimplex(seed, x, y));
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= 2f;
			y *= 2f;

			amp *= 0.5f;
			sum -= (1 - Math.abs(singleSimplex(seed + i, x, y))) * amp;
		}
		amp = gain;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= gain;
		}
		return sum / ampFractal;
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves and
	 * default frequency (0.03125), lacunarity (2) and gain (0.5) in 2D.
	 * 
	 * @param x
	 * @param y
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float layered2D(float x, float y, int seed, int octaves, float frequency) {
		x *= frequency;
		y *= frequency;

		float sum = singleSimplex(seed, x, y);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= 2f;
			y *= 2f;

			amp *= 0.5f;
			sum += singleSimplex(seed + i, x, y) * amp;
		}
		amp = gain;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= gain;
		}
		return sum / ampFractal;
	}

	/**
	 * Generates layered simplex noise with the given amount of octaves and
	 * specified lacunarity (the amount of frequency change between octaves) and
	 * gain (0.5) in D.
	 * 
	 * @param x
	 * @param y
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float layered2D(float x, float y, int seed, int octaves, float frequency, float lacunarity) {
		x *= frequency;
		y *= frequency;

		float sum = singleSimplex(seed, x, y);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= 0.5f;
			sum += singleSimplex(seed + i, x, y) * amp;
		}
		amp = gain;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= gain;
		}
		return sum / ampFractal;
	}

	/**
	 * Generates layered simplex noise with the given amount of octaves and
	 * specified lacunarity (the amount of frequency change between octaves) and
	 * gain (loosely, how much to emphasize lower-frequency octaves) in 2D.
	 * 
	 * @param x
	 * @param y
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float layered2D(float x, float y, int seed, int octaves, float frequency, float lacunarity, float gain) {
		x *= frequency;
		y *= frequency;

		float sum = singleSimplex(seed, x, y);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += singleSimplex(seed + i, x, y) * amp;
		}
		amp = gain;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= gain;
		}
		return sum / ampFractal;
	}

	private float singleSimplexFractalFBM(float x, float y) {
		int seed = this.seed;
		float sum = singleSimplex(seed, x, y);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += singleSimplex(seed + i, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleSimplexFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singleSimplex(seed, x, y)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleSimplex(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves and
	 * default frequency (0.03125), lacunarity (2) and gain (0.5).
	 * 
	 * @param x
	 * @param y
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float ridged2D(float x, float y, int seed, int octaves) {
		return ridged2D(x, y, seed, octaves, 0.03125f, 2f);
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves and
	 * default frequency (0.03125), lacunarity (2) and gain (0.5).
	 * 
	 * @param x
	 * @param y
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float ridged2D(float x, float y, int seed, int octaves, float frequency) {
		return ridged2D(x, y, seed, octaves, frequency, 2f);
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves and
	 * specified lacunarity (the amount of frequency change between octaves); gain
	 * is not used.
	 * 
	 * @param x
	 * @param y
	 * @param seed       any int
	 * @param octaves    how many "layers of detail" to generate; at least 1, but
	 *                   note this slows down with many octaves
	 * @param frequency  often about {@code 1f / 32f}, but generally adjusted for
	 *                   the use case
	 * @param lacunarity when {@code octaves} is 2 or more, this affects the change
	 *                   between layers
	 * @return noise as a float between -1f and 1f
	 */
	public float ridged2D(float x, float y, int seed, int octaves, float frequency, float lacunarity) {
		x *= frequency;
		y *= frequency;

		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleSimplex(seed + i, x, y));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	private float singleSimplexFractalRidgedMulti(float x, float y) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleSimplex(seed + i, x, y));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getSimplex(float x, float y) {
		return singleSimplex(seed, x * frequency, y * frequency);
	}

	public float singleSimplex(int seed, float x, float y) {
		float t = (x + y) * F2f;
		int i = fastFloor(x + t);
		int j = fastFloor(y + t);

		t = (i + j) * G2f;
		float X0 = i - t;
		float Y0 = j - t;

		float x0 = x - X0;
		float y0 = y - Y0;

		int i1, j1;
		if (x0 > y0) {
			i1 = 1;
			j1 = 0;
		} else {
			i1 = 0;
			j1 = 1;
		}

		float x1 = x0 - i1 + G2f;
		float y1 = y0 - j1 + G2f;
		float x2 = x0 - 1 + H2f;
		float y2 = y0 - 1 + H2f;

		float n = 0f;

		t = 0.5f - x0 * x0 - y0 * y0;
		if (t >= 0) {
			t *= t;
			n += t * t * gradCoord2D(seed, i, j, x0, y0);
		}

		t = 0.5f - x1 * x1 - y1 * y1;
		if (t > 0) {
			t *= t;
			n += t * t * gradCoord2D(seed, i + i1, j + j1, x1, y1);
		}

		t = 0.5f - x2 * x2 - y2 * y2;
		if (t > 0) {
			t *= t;
			n += t * t * gradCoord2D(seed, i + 1, j + 1, x2, y2);
		}
		return n * 99.20689070704672f; // this is 99.83685446303647 / 1.00635 ; the first number was found by kdotjpg
	}

	public float getSimplexFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
		case FBM:
			return singleSimplexFractalFBM(x, y, z);
		case BILLOW:
			return singleSimplexFractalBillow(x, y, z);
		case RIDGED_MULTI:
			return singleSimplexFractalRidgedMulti(x, y, z);
		default:
			return 0;
		}
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves and
	 * default frequency (0.03125), lacunarity (2) and gain (0.5) in 3D.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float layered3D(float x, float y, float z, int seed, int octaves) {
		x *= 0.03125f;
		y *= 0.03125f;
		z *= 0.03125f;

		float sum = 1 - Math.abs(singleSimplex(seed, x, y, z));
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= 2f;
			y *= 2f;
			z *= 2f;

			amp *= 0.5f;
			sum -= (1 - Math.abs(singleSimplex(seed + i, x, y, z))) * amp;
		}
		amp = 0.5f;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= 0.5f;
		}
		return sum / ampFractal;
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves and
	 * default frequency (0.03125), lacunarity (2) and gain (0.5) in 3D.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float layered3D(float x, float y, float z, int seed, int octaves, float frequency) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		float sum = singleSimplex(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= 2f;
			y *= 2f;
			z *= 2f;

			amp *= 0.5f;
			sum += singleSimplex(seed + i, x, y, z) * amp;
		}
		amp = 0.5f;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= 0.5f;
		}
		return sum / ampFractal;
	}

	/**
	 * Generates layered simplex noise with the given amount of octaves and
	 * specified lacunarity (the amount of frequency change between octaves) and
	 * gain (0.5) in 3D.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param seed
	 * @param octaves
	 * @param frequency
	 * @param lacunarity
	 * @return noise as a float between -1f and 1f
	 */
	public float layered3D(float x, float y, float z, int seed, int octaves, float frequency, float lacunarity) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		float sum = singleSimplex(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= 0.5f;
			sum += singleSimplex(seed + i, x, y, z) * amp;
		}
		amp = 0.5f;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= 0.5f;
		}
		return sum / ampFractal;
	}

	/**
	 * Generates layered simplex noise with the given amount of octaves and
	 * specified lacunarity (the amount of frequency change between octaves) and
	 * gain (loosely, how much to emphasize lower-frequency octaves) in 3D.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param seed
	 * @param octaves
	 * @param frequency
	 * @param lacunarity
	 * @param gain
	 * @return noise as a float between -1f and 1f
	 */
	public float layered3D(float x, float y, float z, int seed, int octaves, float frequency, float lacunarity,
			float gain) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		float sum = singleSimplex(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singleSimplex(seed + i, x, y, z) * amp;
		}
		amp = gain;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= gain;
		}
		return sum / ampFractal;
	}

	private float singleSimplexFractalFBM(float x, float y, float z) {
		int seed = this.seed;
		float sum = singleSimplex(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singleSimplex(seed + i, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleSimplexFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singleSimplex(seed, x, y, z)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleSimplex(seed + i, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves and
	 * default frequency (0.03125), lacunarity (2) and gain (0.5).
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float ridged3D(float x, float y, float z, int seed, int octaves) {
		return ridged3D(x, y, z, seed, octaves, 0.03125f, 2f);
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves,
	 * specified frequency, and the default lacunarity (2) and gain (0.5).
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param seed
	 * @param octaves
	 * @return noise as a float between -1f and 1f
	 */
	public float ridged3D(float x, float y, float z, int seed, int octaves, float frequency) {
		return ridged3D(x, y, z, seed, octaves, frequency, 2f);
	}

	/**
	 * Generates ridged-multi simplex noise with the given amount of octaves and
	 * specified lacunarity (the amount of frequency change between octaves); gain
	 * is not used.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param seed       any int
	 * @param octaves    how many "layers of detail" to generate; at least 1, but
	 *                   note this slows down with many octaves
	 * @param frequency  often about {@code 1f / 32f}, but generally adjusted for
	 *                   the use case
	 * @param lacunarity when {@code octaves} is 2 or more, this affects the change
	 *                   between layers
	 * @return noise as a float between -1f and 1f
	 */
	public float ridged3D(float x, float y, float z, int seed, int octaves, float frequency, float lacunarity) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleSimplex(seed + i, x, y, z));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	private float singleSimplexFractalRidgedMulti(float x, float y, float z) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleSimplex(seed + i, x, y, z));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getSimplex(float x, float y, float z) {
		return singleSimplex(seed, x * frequency, y * frequency, z * frequency);
	}

	public float singleSimplex(int seed, float x, float y, float z) {
		float t = (x + y + z) * F3f;
		int i = fastFloor(x + t);
		int j = fastFloor(y + t);
		int k = fastFloor(z + t);

		t = (i + j + k) * G3f;
		float x0 = x - (i - t);
		float y0 = y - (j - t);
		float z0 = z - (k - t);

		int i1, j1, k1;
		int i2, j2, k2;

		if (x0 >= y0) {
			if (y0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} else if (x0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} else // x0 < z0
			{
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			}
		} else // x0 < y0
		{
			if (y0 < z0) {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} else if (x0 < z0) {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} else // x0 >= z0
			{
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			}
		}

		float x1 = x0 - i1 + G3f;
		float y1 = y0 - j1 + G3f;
		float z1 = z0 - k1 + G3f;
		float x2 = x0 - i2 + F3f;
		float y2 = y0 - j2 + F3f;
		float z2 = z0 - k2 + F3f;
		float x3 = x0 - 0.5f;
		float y3 = y0 - 0.5f;
		float z3 = z0 - 0.5f;

		float n = 0;

		t = 0.6f - x0 * x0 - y0 * y0 - z0 * z0;
		if (t > 0) {
			t *= t;
			n += t * t * gradCoord3D(seed, i, j, k, x0, y0, z0);
		}

		t = 0.6f - x1 * x1 - y1 * y1 - z1 * z1;
		if (t > 0) {
			t *= t;
			n += t * t * gradCoord3D(seed, i + i1, j + j1, k + k1, x1, y1, z1);
		}

		t = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;
		if (t > 0) {
			t *= t;
			n += t * t * gradCoord3D(seed, i + i2, j + j2, k + k2, x2, y2, z2);
		}

		t = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;
		if (t > 0) {
			t *= t;
			n += t * t * gradCoord3D(seed, i + 1, j + 1, k + 1, x3, y3, z3);
		}
		return 31.5f * n;
	}

	// 5D Simplex

	// 6D Simplex

	// Cubic Noise
	public float getCubicFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
		case FBM:
			return singleCubicFractalFBM(x, y);
		case BILLOW:
			return singleCubicFractalBillow(x, y);
		case RIDGED_MULTI:
			return singleCubicFractalRidgedMulti(x, y);
		default:
			return 0;
		}
	}

	private float singleCubicFractalFBM(float x, float y) {
		int seed = this.seed;
		float sum = singleCubic(seed, x, y);
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += singleCubic(++seed, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleCubicFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singleCubic(seed, x, y)) * 2 - 1;
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleCubic(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleCubicFractalRidgedMulti(float x, float y) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleCubic(seed + i, x, y));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getCubic(float x, float y) {
		x *= frequency;
		y *= frequency;

		return singleCubic(0, x, y);
	}

	private final static float CUBIC_2D_BOUNDING = 1 / 2.25f;

	private float singleCubic(int seed, float x, float y) {
		int x1 = fastFloor(x);
		int y1 = fastFloor(y);

		int x0 = x1 - 1;
		int y0 = y1 - 1;
		int x2 = x1 + 1;
		int y2 = y1 + 1;
		int x3 = x1 + 2;
		int y3 = y1 + 2;

		float xs = x - (float) x1;
		float ys = y - (float) y1;

		return cubicLerp(
				cubicLerp(valCoord2D(seed, x0, y0), valCoord2D(seed, x1, y0), valCoord2D(seed, x2, y0),
						valCoord2D(seed, x3, y0), xs),
				cubicLerp(valCoord2D(seed, x0, y1), valCoord2D(seed, x1, y1), valCoord2D(seed, x2, y1),
						valCoord2D(seed, x3, y1), xs),
				cubicLerp(valCoord2D(seed, x0, y2), valCoord2D(seed, x1, y2), valCoord2D(seed, x2, y2),
						valCoord2D(seed, x3, y2), xs),
				cubicLerp(valCoord2D(seed, x0, y3), valCoord2D(seed, x1, y3), valCoord2D(seed, x2, y3),
						valCoord2D(seed, x3, y3), xs),
				ys) * CUBIC_2D_BOUNDING;
	}

	public float getCubicFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
		case FBM:
			return singleCubicFractalFBM(x, y, z);
		case BILLOW:
			return singleCubicFractalBillow(x, y, z);
		case RIDGED_MULTI:
			return singleCubicFractalRidgedMulti(x, y, z);
		default:
			return 0;
		}
	}

	private float singleCubicFractalFBM(float x, float y, float z) {
		int seed = this.seed;
		float sum = singleCubic(seed, x, y, z);
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singleCubic(++seed, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleCubicFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singleCubic(seed, x, y, z)) * 2 - 1;
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleCubic(++seed, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleCubicFractalRidgedMulti(float x, float y, float z) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleCubic(seed + i, x, y, z));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getCubic(float x, float y, float z) {
		return singleCubic(seed, x * frequency, y * frequency, z * frequency);
	}

	private final static float CUBIC_3D_BOUNDING = 1 / (float) (1.5 * 1.5 * 1.5);

	private float singleCubic(int seed, float x, float y, float z) {
		int x1 = fastFloor(x);
		int y1 = fastFloor(y);
		int z1 = fastFloor(z);

		int x0 = x1 - 1;
		int y0 = y1 - 1;
		int z0 = z1 - 1;
		int x2 = x1 + 1;
		int y2 = y1 + 1;
		int z2 = z1 + 1;
		int x3 = x1 + 2;
		int y3 = y1 + 2;
		int z3 = z1 + 2;

		float xs = x - (float) x1;
		float ys = y - (float) y1;
		float zs = z - (float) z1;

		return cubicLerp(
				cubicLerp(
						cubicLerp(valCoord3D(seed, x0, y0, z0), valCoord3D(seed, x1, y0, z0),
								valCoord3D(seed, x2, y0, z0), valCoord3D(seed, x3, y0, z0), xs),
						cubicLerp(valCoord3D(seed, x0, y1, z0), valCoord3D(seed, x1, y1, z0),
								valCoord3D(seed, x2, y1, z0), valCoord3D(seed, x3, y1, z0), xs),
						cubicLerp(valCoord3D(seed, x0, y2, z0), valCoord3D(seed, x1, y2, z0),
								valCoord3D(seed, x2, y2, z0), valCoord3D(seed, x3, y2, z0), xs),
						cubicLerp(valCoord3D(seed, x0, y3, z0), valCoord3D(seed, x1, y3, z0),
								valCoord3D(seed, x2, y3, z0), valCoord3D(seed, x3, y3, z0), xs),
						ys),
				cubicLerp(
						cubicLerp(valCoord3D(seed, x0, y0, z1), valCoord3D(seed, x1, y0, z1),
								valCoord3D(seed, x2, y0, z1), valCoord3D(seed, x3, y0, z1), xs),
						cubicLerp(valCoord3D(seed, x0, y1, z1), valCoord3D(seed, x1, y1, z1),
								valCoord3D(seed, x2, y1, z1), valCoord3D(seed, x3, y1, z1), xs),
						cubicLerp(valCoord3D(seed, x0, y2, z1), valCoord3D(seed, x1, y2, z1),
								valCoord3D(seed, x2, y2, z1), valCoord3D(seed, x3, y2, z1), xs),
						cubicLerp(valCoord3D(seed, x0, y3, z1), valCoord3D(seed, x1, y3, z1),
								valCoord3D(seed, x2, y3, z1), valCoord3D(seed, x3, y3, z1), xs),
						ys),
				cubicLerp(
						cubicLerp(valCoord3D(seed, x0, y0, z2), valCoord3D(seed, x1, y0, z2),
								valCoord3D(seed, x2, y0, z2), valCoord3D(seed, x3, y0, z2), xs),
						cubicLerp(valCoord3D(seed, x0, y1, z2), valCoord3D(seed, x1, y1, z2),
								valCoord3D(seed, x2, y1, z2), valCoord3D(seed, x3, y1, z2), xs),
						cubicLerp(valCoord3D(seed, x0, y2, z2), valCoord3D(seed, x1, y2, z2),
								valCoord3D(seed, x2, y2, z2), valCoord3D(seed, x3, y2, z2), xs),
						cubicLerp(valCoord3D(seed, x0, y3, z2), valCoord3D(seed, x1, y3, z2),
								valCoord3D(seed, x2, y3, z2), valCoord3D(seed, x3, y3, z2), xs),
						ys),
				cubicLerp(
						cubicLerp(valCoord3D(seed, x0, y0, z3), valCoord3D(seed, x1, y0, z3),
								valCoord3D(seed, x2, y0, z3), valCoord3D(seed, x3, y0, z3), xs),
						cubicLerp(valCoord3D(seed, x0, y1, z3), valCoord3D(seed, x1, y1, z3),
								valCoord3D(seed, x2, y1, z3), valCoord3D(seed, x3, y1, z3), xs),
						cubicLerp(valCoord3D(seed, x0, y2, z3), valCoord3D(seed, x1, y2, z3),
								valCoord3D(seed, x2, y2, z3), valCoord3D(seed, x3, y2, z3), xs),
						cubicLerp(valCoord3D(seed, x0, y3, z3), valCoord3D(seed, x1, y3, z3),
								valCoord3D(seed, x2, y3, z3), valCoord3D(seed, x3, y3, z3), xs),
						ys),
				zs) * CUBIC_3D_BOUNDING;
	}

	// Cellular Noise
	public float getCellular(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (cellularReturnType) {
		case CELL_VALUE:
		case NOISE_LOOKUP:
		case DISTANCE:
			return singleCellular(x, y, z);
		default:
			return singleCellular2Edge(x, y, z);
		}
	}

	private float singleCellular(float x, float y, float z) {
		int xr = fastRound(x);
		int yr = fastRound(y);
		int zr = fastRound(z);

		float distance = 999999;
		int xc = 0, yc = 0, zc = 0;

		switch (cellularDistanceFunction) {
		case EUCLIDEAN:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					for (int zi = zr - 1; zi <= zr + 1; zi++) {
						Float3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;
						float vecZ = zi - z + vec.z;

						float newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;

						if (newDistance < distance) {
							distance = newDistance;
							xc = xi;
							yc = yi;
							zc = zi;
						}
					}
				}
			}
			break;
		case MANHATTAN:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					for (int zi = zr - 1; zi <= zr + 1; zi++) {
						Float3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;
						float vecZ = zi - z + vec.z;

						float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);

						if (newDistance < distance) {
							distance = newDistance;
							xc = xi;
							yc = yi;
							zc = zi;
						}
					}
				}
			}
			break;
		case NATURAL:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					for (int zi = zr - 1; zi <= zr + 1; zi++) {
						Float3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;
						float vecZ = zi - z + vec.z;

						float newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ))
								+ (vecX * vecX + vecY * vecY + vecZ * vecZ);

						if (newDistance < distance) {
							distance = newDistance;
							xc = xi;
							yc = yi;
							zc = zi;
						}
					}
				}
			}
			break;
		}

		switch (cellularReturnType) {
		case CELL_VALUE:
			return valCoord3D(0, xc, yc, zc);

		case NOISE_LOOKUP:
			Float3 vec = CELL_3D[hash256(xc, yc, zc, seed)];
			return layered3D(xc + vec.x, yc + vec.y, zc + vec.z, 123, 3);

		case DISTANCE:
			return distance - 1;
		default:
			return 0;
		}
	}

	private float singleCellular2Edge(float x, float y, float z) {
		int xr = fastRound(x);
		int yr = fastRound(y);
		int zr = fastRound(z);

		float distance = 999999;
		float distance2 = 999999;

		switch (cellularDistanceFunction) {
		case EUCLIDEAN:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					for (int zi = zr - 1; zi <= zr + 1; zi++) {
						Float3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;
						float vecZ = zi - z + vec.z;

						float newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;

						distance2 = Math.max(Math.min(distance2, newDistance), distance);
						distance = Math.min(distance, newDistance);
					}
				}
			}
			break;
		case MANHATTAN:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					for (int zi = zr - 1; zi <= zr + 1; zi++) {
						Float3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;
						float vecZ = zi - z + vec.z;

						float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);

						distance2 = Math.max(Math.min(distance2, newDistance), distance);
						distance = Math.min(distance, newDistance);
					}
				}
			}
			break;
		case NATURAL:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					for (int zi = zr - 1; zi <= zr + 1; zi++) {
						Float3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;
						float vecZ = zi - z + vec.z;

						float newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ))
								+ (vecX * vecX + vecY * vecY + vecZ * vecZ);

						distance2 = Math.max(Math.min(distance2, newDistance), distance);
						distance = Math.min(distance, newDistance);
					}
				}
			}
			break;
		default:
			break;
		}

		switch (cellularReturnType) {
		case DISTANCE_2:
			return distance2 - 1;
		case DISTANCE_2_ADD:
			return distance2 + distance - 1;
		case DISTANCE_2_SUB:
			return distance2 - distance - 1;
		case DISTANCE_2_MUL:
			return distance2 * distance - 1;
		case DISTANCE_2_DIV:
			return distance / distance2 - 1;
		default:
			return 0;
		}
	}

	public float getCellular(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (cellularReturnType) {
		case CELL_VALUE:
		case NOISE_LOOKUP:
		case DISTANCE:
			return singleCellular(x, y);
		default:
			return singleCellular2Edge(x, y);
		}
	}

	private float singleCellular(float x, float y) {
		int xr = fastRound(x);
		int yr = fastRound(y);

		float distance = 999999;
		int xc = 0, yc = 0;

		switch (cellularDistanceFunction) {
		default:
		case EUCLIDEAN:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					Float2 vec = CELL_2D[hash256(xi, yi, seed)];

					float vecX = xi - x + vec.x;
					float vecY = yi - y + vec.y;

					float newDistance = vecX * vecX + vecY * vecY;

					if (newDistance < distance) {
						distance = newDistance;
						xc = xi;
						yc = yi;
					}
				}
			}
			break;
		case MANHATTAN:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					Float2 vec = CELL_2D[hash256(xi, yi, seed)];

					float vecX = xi - x + vec.x;
					float vecY = yi - y + vec.y;

					float newDistance = (Math.abs(vecX) + Math.abs(vecY));

					if (newDistance < distance) {
						distance = newDistance;
						xc = xi;
						yc = yi;
					}
				}
			}
			break;
		case NATURAL:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					Float2 vec = CELL_2D[hash256(xi, yi, seed)];

					float vecX = xi - x + vec.x;
					float vecY = yi - y + vec.y;

					float newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);

					if (newDistance < distance) {
						distance = newDistance;
						xc = xi;
						yc = yi;
					}
				}
			}
			break;
		}

		switch (cellularReturnType) {
		case CELL_VALUE:
			return valCoord2D(0, xc, yc);

		case NOISE_LOOKUP:
			Float2 vec = CELL_2D[hash256(xc, yc, seed)];
			return layered2D(xc + vec.x, yc + vec.y, 123, 3);

		case DISTANCE:
			return distance - 1;
		default:
			return 0;
		}
	}

	private float singleCellular2Edge(float x, float y) {
		int xr = fastRound(x);
		int yr = fastRound(y);

		float distance = 999999;
		float distance2 = 999999;

		switch (cellularDistanceFunction) {
		default:
		case EUCLIDEAN:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					Float2 vec = CELL_2D[hash256(xi, yi, seed)];

					float vecX = xi - x + vec.x;
					float vecY = yi - y + vec.y;

					float newDistance = vecX * vecX + vecY * vecY;

					distance2 = Math.max(Math.min(distance2, newDistance), distance);
					distance = Math.min(distance, newDistance);
				}
			}
			break;
		case MANHATTAN:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					Float2 vec = CELL_2D[hash256(xi, yi, seed)];

					float vecX = xi - x + vec.x;
					float vecY = yi - y + vec.y;

					float newDistance = Math.abs(vecX) + Math.abs(vecY);

					distance2 = Math.max(Math.min(distance2, newDistance), distance);
					distance = Math.min(distance, newDistance);
				}
			}
			break;
		case NATURAL:
			for (int xi = xr - 1; xi <= xr + 1; xi++) {
				for (int yi = yr - 1; yi <= yr + 1; yi++) {
					Float2 vec = CELL_2D[hash256(xi, yi, seed)];

					float vecX = xi - x + vec.x;
					float vecY = yi - y + vec.y;

					float newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);

					distance2 = Math.max(Math.min(distance2, newDistance), distance);
					distance = Math.min(distance, newDistance);
				}
			}
			break;
		}

		switch (cellularReturnType) {
		case DISTANCE_2:
			return distance2 - 1;
		case DISTANCE_2_ADD:
			return distance2 + distance - 1;
		case DISTANCE_2_SUB:
			return distance2 - distance - 1;
		case DISTANCE_2_MUL:
			return distance2 * distance - 1;
		case DISTANCE_2_DIV:
			return distance / distance2 - 1;
		default:
			return 0;
		}
	}

	public void gradientPerturb3(float[] v3) {
		singleGradientPerturb3(seed, gradientPerturbAmp, frequency, v3);
	}

	public void gradientPerturbFractal3(float[] v3) {
		int seed = this.seed;
		float amp = gradientPerturbAmp * fractalBounding;
		float freq = frequency;

		singleGradientPerturb3(seed, amp, frequency, v3);

		for (int i = 1; i < octaves; i++) {
			freq *= lacunarity;
			amp *= gain;
			singleGradientPerturb3(++seed, amp, freq, v3);
		}
	}

	private void singleGradientPerturb3(int seed, float perturbAmp, float frequency, float[] v3) {
		float xf = v3[0] * frequency;
		float yf = v3[1] * frequency;
		float zf = v3[2] * frequency;

		int x0 = fastFloor(xf);
		int y0 = fastFloor(yf);
		int z0 = fastFloor(zf);
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		int z1 = z0 + 1;

		float xs, ys, zs;
		switch (interpolation) {
		default:
		case LINEAR:
			xs = xf - x0;
			ys = yf - y0;
			zs = zf - z0;
			break;
		case HERMITE:
			xs = hermiteInterpolator(xf - x0);
			ys = hermiteInterpolator(yf - y0);
			zs = hermiteInterpolator(zf - z0);
			break;
		case QUINTIC:
			xs = quinticInterpolator(xf - x0);
			ys = quinticInterpolator(yf - y0);
			zs = quinticInterpolator(zf - z0);
			break;
		}

		Float3 vec0 = CELL_3D[hash256(x0, y0, z0, seed)];
		Float3 vec1 = CELL_3D[hash256(x1, y0, z0, seed)];

		float lx0x = lerp(vec0.x, vec1.x, xs);
		float ly0x = lerp(vec0.y, vec1.y, xs);
		float lz0x = lerp(vec0.z, vec1.z, xs);

		vec0 = CELL_3D[hash256(x0, y1, z0, seed)];
		vec1 = CELL_3D[hash256(x1, y1, z0, seed)];

		float lx1x = lerp(vec0.x, vec1.x, xs);
		float ly1x = lerp(vec0.y, vec1.y, xs);
		float lz1x = lerp(vec0.z, vec1.z, xs);

		float lx0y = lerp(lx0x, lx1x, ys);
		float ly0y = lerp(ly0x, ly1x, ys);
		float lz0y = lerp(lz0x, lz1x, ys);

		vec0 = CELL_3D[hash256(x0, y0, z1, seed)];
		vec1 = CELL_3D[hash256(x1, y0, z1, seed)];

		lx0x = lerp(vec0.x, vec1.x, xs);
		ly0x = lerp(vec0.y, vec1.y, xs);
		lz0x = lerp(vec0.z, vec1.z, xs);

		vec0 = CELL_3D[hash256(x0, y1, z1, seed)];
		vec1 = CELL_3D[hash256(x1, y1, z1, seed)];

		lx1x = lerp(vec0.x, vec1.x, xs);
		ly1x = lerp(vec0.y, vec1.y, xs);
		lz1x = lerp(vec0.z, vec1.z, xs);

		v3[0] += lerp(lx0y, lerp(lx0x, lx1x, ys), zs) * perturbAmp;
		v3[1] += lerp(ly0y, lerp(ly0x, ly1x, ys), zs) * perturbAmp;
		v3[2] += lerp(lz0y, lerp(lz0x, lz1x, ys), zs) * perturbAmp;
	}

	public void gradientPerturb2(float[] v2) {
		singleGradientPerturb2(seed, gradientPerturbAmp, frequency, v2);
	}

	public void gradientPerturbFractal2(float[] v2) {
		int seed = this.seed;
		float amp = gradientPerturbAmp * fractalBounding;
		float freq = frequency;

		singleGradientPerturb2(seed, amp, frequency, v2);

		for (int i = 1; i < octaves; i++) {
			freq *= lacunarity;
			amp *= gain;
			singleGradientPerturb2(++seed, amp, freq, v2);
		}
	}

	private void singleGradientPerturb2(int seed, float perturbAmp, float frequency, float[] v2) {
		float xf = v2[0] * frequency;
		float yf = v2[1] * frequency;

		int x0 = fastFloor(xf);
		int y0 = fastFloor(yf);
		int x1 = x0 + 1;
		int y1 = y0 + 1;

		float xs, ys;
		switch (interpolation) {
		default:
		case LINEAR:
			xs = xf - x0;
			ys = yf - y0;
			break;
		case HERMITE:
			xs = hermiteInterpolator(xf - x0);
			ys = hermiteInterpolator(yf - y0);
			break;
		case QUINTIC:
			xs = quinticInterpolator(xf - x0);
			ys = quinticInterpolator(yf - y0);
			break;
		}

		Float2 vec0 = CELL_2D[hash256(x0, y0, seed)];
		Float2 vec1 = CELL_2D[hash256(x1, y0, seed)];

		float lx0x = lerp(vec0.x, vec1.x, xs);
		float ly0x = lerp(vec0.y, vec1.y, xs);

		vec0 = CELL_2D[hash256(x0, y1, seed)];
		vec1 = CELL_2D[hash256(x1, y1, seed)];

		float lx1x = lerp(vec0.x, vec1.x, xs);
		float ly1x = lerp(vec0.y, vec1.y, xs);

		v2[0] += lerp(lx0x, lx1x, ys) * perturbAmp;
		v2[1] += lerp(ly0x, ly1x, ys) * perturbAmp;
	}

	public float getHoney(float x, float y) {
		return singleHoney(seed, x * frequency, y * frequency);
	}

	public float singleHoney(int seed, float x, float y) {
		final float result = (singleSimplex(seed, x, y) + singleValue(seed ^ 0x9E3779B9, x, y)) * 0.5f + 1f;
		return (result <= 1f) ? result * result - 1f : (result - 2f) * -(result - 2f) + 1f;
	}

	public float getHoneyFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
		case FBM:
			return singleHoneyFractalFBM(x, y);
		case BILLOW:
			return singleHoneyFractalBillow(x, y);
		case RIDGED_MULTI:
			return singleHoneyFractalRidgedMulti(x, y);
		default:
			return 0;
		}
	}

	private float singleHoneyFractalFBM(float x, float y) {
		int seed = this.seed;
		float sum = singleHoney(seed, x, y);
		float amp = 1, t;

		for (int i = 1; i < octaves; i++) {
			t = x;
			x = y * lacunarity;
			y = t * lacunarity;

			amp *= gain;
			sum += singleHoney(seed + i, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleHoneyFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singleHoney(seed, x, y)) * 2 - 1;
		float amp = 1, t;

		for (int i = 1; i < octaves; i++) {
			t = x;
			x = y * lacunarity;
			y = t * lacunarity;

			amp *= gain;
			sum += (Math.abs(singleHoney(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleHoneyFractalRidgedMulti(float x, float y) {
		int seed = this.seed;
		float t;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleHoney(seed + i, x, y));
			correction += (exp *= 0.5);
			sum += spike * exp;
			t = x;
			x = y * lacunarity;
			y = t * lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getHoneyFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
		case FBM:
			return singleHoneyFractalFBM(x, y, z);
		case BILLOW:
			return singleHoneyFractalBillow(x, y, z);
		case RIDGED_MULTI:
			return singleHoneyFractalRidgedMulti(x, y, z);
		default:
			return 0;
		}
	}

	private float singleHoneyFractalFBM(float x, float y, float z) {
		int seed = this.seed;
		float sum = singleHoney(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singleHoney(++seed, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleHoneyFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singleHoney(seed, x, y, z)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleHoney(++seed, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleHoneyFractalRidgedMulti(float x, float y, float z) {
		int seed = this.seed;
		float sum = 0f, exp = 2f, correction = 0f, spike;
		for (int i = 0; i < octaves; i++) {
			spike = 1f - Math.abs(singleHoney(seed + i, x, y, z));
			correction += (exp *= 0.5);
			sum += spike * exp;
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}
		return sum * 2f / correction - 1f;
	}

	public float getHoney(float x, float y, float z) {
		return singleHoney(seed, x * frequency, y * frequency, z * frequency);
	}

	public float singleHoney(int seed, float x, float y, float z) {
		final float result = (singleSimplex(seed, x, y, z) + singleValue(seed ^ 0x9E3779B9, x, y, z)) * 0.5f + 1f;
		return (result <= 1f) ? result * result - 1f : (result - 2f) * -(result - 2f) + 1f;
	}

	/**
	 * Produces 1D noise that "tiles" by repeating its output every {@code sizeX}
	 * units that {@code x} increases or decreases by. This doesn't precalculate an
	 * array, instead calculating just one value so that later calls with different
	 * x will tile seamlessly. <br>
	 * Internally, this just samples out of a circle from a source of 2D noise.
	 * 
	 * @param x     the x-coordinate to sample
	 * @param sizeX the range of x to generate before repeating; must be greater
	 *              than 0
	 * @param seed  the noise seed, as a long
	 * @return continuous noise from -1.0 to 1.0, inclusive
	 */
	public float seamless1D(float x, float sizeX, int seed) {
		x /= sizeX;
		return getNoiseWithSeed(cosTurns(x), sinTurns(x), seed);
	}

	/**
	 * A fairly-close approximation of {@link Math#sin(double)} that can be
	 * significantly faster (between 8x and 80x faster sin() calls in benchmarking,
	 * and both takes and returns floats; if you have access to libGDX you should
	 * consider its more-precise and sometimes-faster MathUtils.sin() method.
	 * Because this method doesn't rely on a lookup table, where libGDX's MathUtils
	 * does, applications that have a bottleneck on memory may perform better with
	 * this method than with MathUtils. Takes the same arguments Math.sin() does, so
	 * one angle in radians, which may technically be any float (but this will lose
	 * precision on fairly large floats, such as those that are larger than
	 * {@link Integer#MAX_VALUE}, because those floats themselves will lose
	 * precision at that scale). The difference between the result of this method
	 * and {@link Math#sin(double)} should be under 0.0011 at all points between -pi
	 * and pi, with an average difference of about 0.0005; not all points have been
	 * checked for potentially higher errors, though. <br>
	 * Unlike in previous versions of this method, the sign of the input doesn't
	 * affect performance here, at least not by a measurable amount. <br>
	 * The technique for sine approximation is mostly from <a href=
	 * "https://web.archive.org/web/20080228213915/http://devmaster.net/forums/showthread.php?t=5784">this
	 * archived DevMaster thread</a>, with credit to "Nick". Changes have been made
	 * to accelerate wrapping from any float to the valid input range.
	 * 
	 * @param radians an angle in radians as a float, often from 0 to pi * 2, though
	 *                not required to be.
	 * @return the sine of the given angle, as a float between -1f and 1f (both
	 *         inclusive)
	 */
	public static float sin(float radians) {
		radians *= 0.6366197723675814f;
		final int floor = (radians >= 0.0 ? (int) radians : (int) radians - 1) & -2;
		radians -= floor;
		radians *= 2f - radians;
		return radians * (-0.775f - 0.225f * radians) * ((floor & 2) - 1);
	}

	/**
	 * A fairly-close approximation of {@link Math#cos(double)} that can be
	 * significantly faster (between 8x and 80x faster cos() calls in benchmarking,
	 * and both takes and returns floats; if you have access to libGDX you should
	 * consider its more-precise and sometimes-faster MathUtils.cos() method.
	 * Because this method doesn't rely on a lookup table, where libGDX's MathUtils
	 * does, applications that have a bottleneck on memory may perform better with
	 * this method than with MathUtils. Takes the same arguments Math.cos() does, so
	 * one angle in radians, which may technically be any float (but this will lose
	 * precision on fairly large floats, such as those that are larger than
	 * {@link Integer#MAX_VALUE}, because those floats themselves will lose
	 * precision at that scale). The difference between the result of this method
	 * and {@link Math#cos(double)} should be under 0.0011 at all points between -pi
	 * and pi, with an average difference of about 0.0005; not all points have been
	 * checked for potentially higher errors, though. <br>
	 * Unlike in previous versions of this method, the sign of the input doesn't
	 * affect performance here, at least not by a measurable amount. <br>
	 * The technique for cosine approximation is mostly from <a href=
	 * "https://web.archive.org/web/20080228213915/http://devmaster.net/forums/showthread.php?t=5784">this
	 * archived DevMaster thread</a>, with credit to "Nick". Changes have been made
	 * to accelerate wrapping from any float to the valid input range.
	 * 
	 * @param radians an angle in radians as a float, often from 0 to pi * 2, though
	 *                not required to be.
	 * @return the cosine of the given angle, as a float between -1f and 1f (both
	 *         inclusive)
	 */
	public static float cos(float radians) {
		radians = radians * 0.6366197723675814f + 1f;
		final int floor = (radians >= 0.0 ? (int) radians : (int) radians - 1) & -2;
		radians -= floor;
		radians *= 2f - radians;
		return radians * (-0.775f - 0.225f * radians) * ((floor & 2) - 1);
	}

	/**
	 * A variation on {@link Math#sin(double)} that takes its input as a fraction of
	 * a turn instead of in radians (it also takes and returns a float); one turn is
	 * equal to 360 degrees or two*PI radians. This can be useful as a building
	 * block for other measurements; to make a sine method that takes its input in
	 * grad (with 400 grad equal to 360 degrees), you would just divide the grad
	 * value by 400.0 (or multiply it by 0.0025) and pass it to this method.
	 * Similarly for binary degrees, also called brad (with 256 brad equal to 360
	 * degrees), you would divide by 256.0 or multiply by 0.00390625 before passing
	 * that value here. The brad case is especially useful because you can use a
	 * byte for any brad values, and adding up those brad values will wrap correctly
	 * (256 brad goes back to 0) while keeping perfect precision for the results
	 * (you still divide by 256.0 when you pass the brad value to this method). <br>
	 * The technique for sine approximation is mostly from <a href=
	 * "https://web.archive.org/web/20080228213915/http://devmaster.net/forums/showthread.php?t=5784">this
	 * archived DevMaster thread</a>, with credit to "Nick". Changes have been made
	 * to accelerate wrapping from any double to the valid input range.
	 * 
	 * @param turns an angle as a fraction of a turn as a float, with 0.5 here
	 *              equivalent to PI radians in {@link #sin(float)}
	 * @return the sine of the given angle, as a float between -1.0 and 1.0 (both
	 *         inclusive)
	 */
	public static float sinTurns(float turns) {
		turns *= 4f;
		final long floor = (turns >= 0.0 ? (long) turns : (long) turns - 1L) & -2L;
		turns -= floor;
		turns *= 2f - turns;
		return turns * (-0.775f - 0.225f * turns) * ((floor & 2L) - 1L);
	}

	/**
	 * A variation on {@link Math#cos(double)} that takes its input as a fraction of
	 * a turn instead of in radians (it also takes and returns a float); one turn is
	 * equal to 360 degrees or two*PI radians. This can be useful as a building
	 * block for other measurements; to make a cosine method that takes its input in
	 * grad (with 400 grad equal to 360 degrees), you would just divide the grad
	 * value by 400.0 (or multiply it by 0.0025) and pass it to this method.
	 * Similarly for binary degrees, also called brad (with 256 brad equal to 360
	 * degrees), you would divide by 256.0 or multiply by 0.00390625 before passing
	 * that value here. The brad case is especially useful because you can use a
	 * byte for any brad values, and adding up those brad values will wrap correctly
	 * (256 brad goes back to 0) while keeping perfect precision for the results
	 * (you still divide by 256.0 when you pass the brad value to this method). <br>
	 * The technique for cosine approximation is mostly from <a href=
	 * "https://web.archive.org/web/20080228213915/http://devmaster.net/forums/showthread.php?t=5784">this
	 * archived DevMaster thread</a>, with credit to "Nick". Changes have been made
	 * to accelerate wrapping from any double to the valid input range.
	 * 
	 * @param turns an angle as a fraction of a turn as a float, with 0.5 here
	 *              equivalent to PI radians in {@link #cos(float)}
	 * @return the cosine of the given angle, as a float between -1.0 and 1.0 (both
	 *         inclusive)
	 */
	public static float cosTurns(float turns) {
		turns = turns * 4f + 1f;
		final long floor = (turns >= 0.0 ? (long) turns : (long) turns - 1L) & -2L;
		turns -= floor;
		turns *= 2f - turns;
		return turns * (-0.775f - 0.225f * turns) * ((floor & 2L) - 1L);
	}

	public static float swayRandomized(int seed, float value) {
		final int floor = value >= 0f ? (int) value : (int) value - 1;
		final float start = ((((seed += floor) ^ 0xD1B54A35) * 0x1D2473 & 0x1FFFFF) - 0x100000) * 0x1p-20f,
				end = (((seed + 1 ^ 0xD1B54A35) * 0x1D2473 & 0x1FFFFF) - 0x100000) * 0x1p-20f;
		value -= floor;
		value *= value * (3f - 2f * value);
		return (1f - value) * start + value * end;
	}

	/**
	 * A 32-bit point hash that smashes x and y into s using XOR and multiplications
	 * by harmonious numbers, then runs a simple unary hash on s and returns it. Has
	 * better performance than HastyPointHash, especially for ints, and has slightly
	 * fewer collisions in a hash table of points. GWT-optimized. Inspired by Pelle
	 * Evensen's rrxmrrxmsx_0 unary hash, though this doesn't use its code or its
	 * full algorithm. The unary hash used here has been stripped down heavily, both
	 * for speed and because unless points are selected specifically to target flaws
	 * in the hash, it doesn't need the intense resistance to bad inputs that
	 * rrxmrrxmsx_0 has.
	 * 
	 * @param x x position, as an int
	 * @param y y position, as an int
	 * @param s any int, a seed to be able to produce many hashes for a given point
	 * @return 32-bit hash of the x,y point with the given state s
	 */
	public static int hashAll(int x, int y, int s) {
		s ^= x * 0x1827F5 ^ y * 0x123C21;
		return (s = (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493) ^ s >>> 11;
	}

	/**
	 * A 32-bit point hash that smashes x, y, and z into s using XOR and
	 * multiplications by harmonious numbers, then runs a simple unary hash on s and
	 * returns it. Has better performance than HastyPointHash, especially for ints,
	 * and has slightly fewer collisions in a hash table of points. GWT-optimized.
	 * Inspired by Pelle Evensen's rrxmrrxmsx_0 unary hash, though this doesn't use
	 * its code or its full algorithm. The unary hash used here has been stripped
	 * down heavily, both for speed and because unless points are selected
	 * specifically to target flaws in the hash, it doesn't need the intense
	 * resistance to bad inputs that rrxmrrxmsx_0 has.
	 * 
	 * @param x x position, as an int
	 * @param y y position, as an int
	 * @param z z position, as an int
	 * @param s any int, a seed to be able to produce many hashes for a given point
	 * @return 32-bit hash of the x,y,z point with the given state s
	 */
	public static int hashAll(int x, int y, int z, int s) {
		s ^= x * 0x1A36A9 ^ y * 0x157931 ^ z * 0x119725;
		return (s = (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493) ^ s >>> 11;
	}

	/**
	 * A 8-bit point hash that smashes x and y into s using XOR and multiplications
	 * by harmonious numbers, then runs a simple unary hash on s and returns it. Has
	 * better performance than HastyPointHash, especially for ints, and has slightly
	 * fewer collisions in a hash table of points. GWT-optimized. Inspired by Pelle
	 * Evensen's rrxmrrxmsx_0 unary hash, though this doesn't use its code or its
	 * full algorithm. The unary hash used here has been stripped down heavily, both
	 * for speed and because unless points are selected specifically to target flaws
	 * in the hash, it doesn't need the intense resistance to bad inputs that
	 * rrxmrrxmsx_0 has.
	 * 
	 * @param x x position, as an int
	 * @param y y position, as an int
	 * @param s any int, a seed to be able to produce many hashes for a given point
	 * @return 8-bit hash of the x,y point with the given state s
	 */
	public static int hash256(int x, int y, int s) {
		s ^= x * 0x1827F5 ^ y * 0x123C21;
		return (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493 >>> 24;
	}

	/**
	 * A 8-bit point hash that smashes x, y, and z into s using XOR and
	 * multiplications by harmonious numbers, then runs a simple unary hash on s and
	 * returns it. Has better performance than HastyPointHash, especially for ints,
	 * and has slightly fewer collisions in a hash table of points. GWT-optimized.
	 * Inspired by Pelle Evensen's rrxmrrxmsx_0 unary hash, though this doesn't use
	 * its code or its full algorithm. The unary hash used here has been stripped
	 * down heavily, both for speed and because unless points are selected
	 * specifically to target flaws in the hash, it doesn't need the intense
	 * resistance to bad inputs that rrxmrrxmsx_0 has.
	 * 
	 * @param x x position, as an int
	 * @param y y position, as an int
	 * @param z z position, as an int
	 * @param s any int, a seed to be able to produce many hashes for a given point
	 * @return 8-bit hash of the x,y,z point with the given state s
	 */
	public static int hash256(int x, int y, int z, int s) {
		s ^= x * 0x1A36A9 ^ y * 0x157931 ^ z * 0x119725;
		return (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493 >>> 24;
	}

	/**
	 * A 5-bit point hash that smashes x and y into s using XOR and multiplications
	 * by harmonious numbers, then runs a simple unary hash on s and returns it. Has
	 * better performance than HastyPointHash, especially for ints, and has slightly
	 * fewer collisions in a hash table of points. GWT-optimized. Inspired by Pelle
	 * Evensen's rrxmrrxmsx_0 unary hash, though this doesn't use its code or its
	 * full algorithm. The unary hash used here has been stripped down heavily, both
	 * for speed and because unless points are selected specifically to target flaws
	 * in the hash, it doesn't need the intense resistance to bad inputs that
	 * rrxmrrxmsx_0 has.
	 * 
	 * @param x x position, as an int
	 * @param y y position, as an int
	 * @param s any int, a seed to be able to produce many hashes for a given point
	 * @return 5-bit hash of the x,y point with the given state s
	 */
	public static int hash32(int x, int y, int s) {
		s ^= x * 0x1827F5 ^ y * 0x123C21;
		return (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493 >>> 27;
	}

	/**
	 * A 5-bit point hash that smashes x, y, and z into s using XOR and
	 * multiplications by harmonious numbers, then runs a simple unary hash on s and
	 * returns it. Has better performance than HastyPointHash, especially for ints,
	 * and has slightly fewer collisions in a hash table of points. GWT-optimized.
	 * Inspired by Pelle Evensen's rrxmrrxmsx_0 unary hash, though this doesn't use
	 * its code or its full algorithm. The unary hash used here has been stripped
	 * down heavily, both for speed and because unless points are selected
	 * specifically to target flaws in the hash, it doesn't need the intense
	 * resistance to bad inputs that rrxmrrxmsx_0 has.
	 * 
	 * @param x x position, as an int
	 * @param y y position, as an int
	 * @param z z position, as an int
	 * @param s any int, a seed to be able to produce many hashes for a given point
	 * @return 5-bit hash of the x,y,z point with the given state s
	 */
	public static int hash32(int x, int y, int z, int s) {
		s ^= x * 0x1A36A9 ^ y * 0x157931 ^ z * 0x119725;
		return (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493 >>> 27;
	}

	public static final float F2f = 0.3660254f;
	public static final float G2f = 0.21132487f;
	public static final float H2f = 0.42264974f;

	public static final float F3f = 0.33333334f;
	public static final float G3f = 0.16666667f;

	/**
	 * Simple container class that holds 2 floats. Takes slightly less storage than
	 * an array of float[2] and may avoid array index bounds check speed penalty.
	 */
	public static class Float2 {
		public final float x, y;

		public Float2(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * Simple container class that holds 3 floats. Takes slightly less storage than
	 * an array of float[3] and may avoid array index bounds check speed penalty.
	 */
	public static class Float3 {
		public final float x, y, z;

		public Float3(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	protected static final float[] GRAD_2D = { +0.6499429579167653f, +0.7599829941876370f, -0.1551483029088119f,
			+0.9878911904175052f, -0.8516180517334043f, +0.5241628506120981f, -0.9518580082090311f,
			-0.3065392833036837f, -0.3856887670108717f, -0.9226289476282616f, +0.4505066120763985f,
			-0.8927730912586049f, +0.9712959670388622f, -0.2378742197339624f, +0.8120673355833279f,
			+0.5835637432865366f, +0.0842989251943661f, +0.9964405106232257f, -0.7024883500032670f,
			+0.7116952424385647f, -0.9974536374007479f, -0.0713178886116052f, -0.5940875849508908f,
			-0.8044003613917750f, +0.2252075529515288f, -0.9743108118529653f, +0.8868317111719171f,
			-0.4620925405802277f, +0.9275724981153959f, +0.3736432265409930f, +0.3189067150428103f,
			+0.9477861083074618f, -0.5130301507665112f, +0.8583705868705491f, -0.9857873824221494f,
			+0.1679977281313266f, -0.7683809836504446f, -0.6399927061806058f, -0.0130202362193748f,
			-0.9999152331316848f, +0.7514561619680513f, -0.6597830223946701f, +0.9898275175279653f,
			+0.1422725748147741f, +0.5352066871710182f, +0.8447211386057674f, -0.2941198828144364f,
			+0.9557685360657266f, -0.9175289804081126f, +0.3976689202229027f, -0.8985631161871687f,
			-0.4388443075032474f, -0.2505005588110731f, -0.9681164547900940f, +0.5729409678802212f,
			-0.8195966369650838f, +0.9952584535626074f, -0.0972656702653466f, +0.7207814785200723f,
			+0.6931623620930514f, -0.0583247612407003f, +0.9982976621360060f, -0.7965970142012075f,
			+0.6045107087270838f, -0.9771604781144960f, -0.2125027058911242f, -0.4736001288089817f,
			-0.8807399831914728f, +0.3615343409387538f, -0.9323587937709286f, +0.9435535266854258f,
			-0.3312200813348966f, +0.8649775992346886f, +0.5018104750024599f, +0.1808186720712497f,
			+0.9835164502083277f, -0.6299339540895539f, +0.7766487066139361f, -0.9996609468975833f,
			+0.0260382650694516f, -0.6695112313914258f, -0.7428019325774111f, +0.1293727267195084f,
			-0.9915960354807594f, +0.8376810167470904f, -0.5461597881403947f, +0.9595170289111490f,
			+0.2816506190824391f, +0.4095816551369482f, +0.9122734610714476f, -0.4271076040148479f,
			+0.9042008043530463f, -0.9647728141412515f, +0.2630844295924223f, -0.8269869890664444f,
			-0.5622210596507540f, -0.1102159255238020f, -0.9939076666174438f, +0.6837188597775012f,
			-0.7297455178242300f, +0.9989724417383330f, +0.0453217458550843f, +0.6148313475439905f,
			+0.7886586169422362f, -0.1997618324529528f, +0.9798444827088829f, -0.8744989400706802f,
			+0.4850274258382270f, -0.9369870231562731f, -0.3493641630687752f, -0.3434772946489506f,
			-0.9391609809082988f, +0.4905057254335028f, -0.8714379687143274f, +0.9810787787756657f,
			-0.1936089611460388f, +0.7847847614201463f, +0.6197684069414349f, +0.0390518795551629f,
			+0.9992371844077906f, -0.7340217731995672f, +0.6791259356474049f, -0.9931964444524306f,
			-0.1164509455824639f, -0.5570202966000876f, -0.8304988796955420f, +0.2691336060685578f,
			-0.9631028512493016f, +0.9068632806061000f, -0.4214249521425399f, +0.9096851999779008f,
			+0.4152984913783901f, +0.2756236986873733f, +0.9612656119522284f, -0.5514058359842319f,
			+0.8342371389734039f, -0.9923883787916933f, +0.1231474954645637f, -0.7385858406439617f,
			-0.6741594440488484f, +0.0323110469045428f, -0.9994778618098213f, +0.7805865154410089f,
			-0.6250477517051506f, +0.9823623706068018f, +0.1869870926448790f, +0.4963724943556111f,
			+0.8681096398768929f, -0.3371347561867868f, +0.9414564016304079f, -0.9346092156607797f,
			+0.3556762769737983f, -0.8777506000588920f, -0.4791178185960681f, -0.2063664269701996f,
			-0.9784747813917093f, +0.6094977881394418f, -0.7927877687333024f, +0.9986440175043460f,
			-0.0520588734297966f, +0.6886255051458764f, +0.7251171723677399f, -0.1035094220814735f,
			+0.9946284731196666f, -0.8231759450656516f, +0.5677863713275190f, -0.9665253951623188f,
			-0.2565709658288005f, -0.4331968034012919f, -0.9012993562201753f, +0.4034189716368784f,
			-0.9150153732716426f, +0.9575954428121146f, -0.2881162402667889f, +0.8413458575409575f,
			+0.5404971304259356f, +0.1360581877502697f, +0.9907008476558967f, -0.6644857355505560f,
			+0.7473009482463117f, -0.9998138366647180f, -0.0192948701414780f, -0.6351581891853917f,
			-0.7723820781910558f, +0.1741806522163015f, -0.9847137149413040f, +0.8615731658120597f,
			-0.5076334109892543f, +0.9457661714829020f, +0.3248481935898273f, +0.3678149601703667f,
			+0.9298990026206456f, -0.4676486851245607f, +0.8839144230643990f, -0.9757048995218635f,
			+0.2190889067228882f, -0.8006563717736747f, -0.5991238388999518f, -0.0650570415691071f,
			-0.9978815467490495f, +0.7160896397121960f, -0.6980083293893113f, +0.9958918787052943f,
			+0.0905503502413954f, +0.5784561871098056f, +0.8157134543418942f, -0.2439648281544816f,
			+0.9697840804135497f, -0.8955826311865743f, +0.4448952131872543f, -0.9201904205900768f,
			-0.3914710587696841f, -0.3005599364234082f, -0.9537629289384008f, +0.5294967923694863f,
			-0.8483119396014800f, +0.9888453593035162f, -0.1489458135829932f, +0.7558893631265085f,
			+0.6546993743025888f, -0.0062754222469803f, +0.9999803093439501f, -0.7640466961212760f,
			+0.6451609459244744f, -0.9868981170802014f, -0.1613446822909051f, -0.5188082666339063f,
			-0.8548906260290385f, +0.3125065582647844f, -0.9499156020623616f, +0.9250311403279032f,
			-0.3798912863223621f, +0.8899283927548960f, +0.4561002694240463f, +0.2317742435145519f,
			+0.9727696027545563f, -0.5886483179573486f, +0.8083892365475831f, -0.9969499014064180f,
			+0.0780441803450664f, -0.7072728176724660f, -0.7069407057042696f, +0.0775759270620736f,
			-0.9969864470194466f, +0.8081126726681943f, -0.5890279350532263f, +0.9728783545459001f,
			+0.2313173302112532f, +0.4565181982253288f, +0.8897140746830408f, -0.3794567783511009f,
			+0.9252094645881026f, -0.9497687200714887f, +0.3129526775309106f, -0.8551342041690687f,
			-0.5184066867432686f, -0.1618081880753845f, -0.9868222283024238f, +0.6448020194233159f,
			-0.7643496292585048f, +0.9999772516247822f, -0.0067450895432855f, +0.6550543261176665f,
			+0.7555817823601425f, -0.1484813589986064f, +0.9889152066936411f, -0.8480631534437840f,
			+0.5298951667745091f, -0.9539039899003245f, -0.3001119425351840f, -0.3919032080850608f,
			-0.9200064540494471f, +0.4444745293405786f, -0.8957914895596358f, +0.9696693887216105f,
			-0.2444202867526717f, +0.8159850520735595f, +0.5780730012658526f, +0.0910180879994953f,
			+0.9958492394217692f, -0.6976719213969089f, +0.7164173993520435f, -0.9979119924958648f,
			-0.0645883521459785f, -0.5994998228898376f, -0.8003748886334786f, +0.2186306161766729f,
			-0.9758076929755208f, +0.8836946816279001f, -0.4680637880274058f, +0.9300716543684309f,
			+0.3673781672069940f, +0.3252923626016029f, +0.9456134933645286f, -0.5072286936943775f,
			+0.8618114946396893f, -0.9846317976415725f, +0.1746431306210620f, -0.7726803123417516f,
			-0.6347953488483143f, -0.0197644578133314f, -0.9998046640256011f, +0.7469887719961158f,
			-0.6648366525032559f, +0.9907646418168752f, +0.1355928631067248f, +0.5408922318074902f,
			+0.8410919055432124f, -0.2876664477065717f, +0.9577306588304888f, -0.9148257956391065f,
			+0.4038486890325085f, -0.9015027194859215f, -0.4327734358292892f, -0.2570248925062563f,
			-0.9664047830139022f, +0.5673996816983953f, -0.8234425306046317f, +0.9945797473944409f,
			-0.1039765650173647f, +0.7254405241129018f, +0.6882848581617921f, -0.0515898273251730f,
			+0.9986683582233687f, -0.7925014140531963f, +0.6098700752813540f, -0.9785715990807187f,
			-0.2059068368767903f, -0.4795300252265173f, -0.8775254725113429f, +0.3552372730694574f,
			-0.9347761656258549f, +0.9412979532686209f, -0.3375768996425928f, +0.8683426789873530f,
			+0.4959647082697184f, +0.1874484652642005f, +0.9822744386728669f, -0.6246810590458048f,
			+0.7808800000444446f, -0.9994625758058275f, +0.0327804753409776f, -0.6745062666468870f,
			-0.7382691218343610f, +0.1226813796500722f, -0.9924461089082646f, +0.8339780641890598f,
			-0.5517975973592748f, +0.9613949601033843f, +0.2751721837101493f, +0.4157257040026583f,
			+0.9094900433932711f, -0.4209989726203348f, +0.9070611142875780f, -0.9629763390922247f,
			+0.2695859238694348f, -0.8307604078465821f, -0.5566301687427484f, -0.1169174144996730f,
			-0.9931416405461567f, +0.6787811074228051f, -0.7343406622310046f, +0.9992554159724470f,
			+0.0385825562881973f, +0.6201369341201711f, +0.7844935837468874f, -0.1931481494214682f,
			+0.9811696042861612f, -0.8712074932224428f, +0.4909149659086258f, -0.9393222007870077f,
			-0.3430361542296271f, -0.3498042060103595f, -0.9368228314134226f, +0.4846166400948296f,
			-0.8747266499559725f, +0.9797505510481769f, -0.2002220210685972f, +0.7889473022428521f,
			+0.6144608647291752f, +0.0457909354721791f, +0.9989510449609544f, -0.7294243101497431f,
			+0.6840615292227530f, -0.9939593229024027f, -0.1097490975607407f, -0.5626094146025390f,
			-0.8267228354174018f, +0.2626312687452330f, -0.9648962724963078f, +0.9040001019019392f,
			-0.4275322394408211f, +0.9124657316291773f, +0.4091531358824348f, +0.2821012513235693f,
			+0.9593846381935018f, -0.5457662881946498f, +0.8379374431723614f, -0.9915351626845509f,
			+0.1298384425357957f, -0.7431163048326799f, -0.6691622803863227f, +0.0255687442062853f,
			-0.9996730662170076f, +0.7763527553119807f, -0.6302986588273021f, +0.9836012681423212f,
			+0.1803567168386515f, +0.5022166799422209f, +0.8647418148718223f, -0.3307768791887710f,
			+0.9437089891455613f, -0.9321888864830543f, +0.3619722087639923f, -0.8809623252471085f,
			-0.4731864130500873f, -0.2129616324856343f, -0.9770605626515961f, +0.6041364985661350f,
			-0.7968808512571063f, +0.9982701582127194f, -0.0587936324949578f, +0.6935008202914851f,
			+0.7204558364362367f, -0.0967982092968079f, +0.9953040272584711f, -0.8193274492343137f,
			+0.5733258505694586f, -0.9682340024187017f, -0.2500458289199430f, -0.4392662937408502f,
			-0.8983569018954422f, +0.3972379338845546f, -0.9177156552457467f, +0.9556302892322005f,
			-0.2945687530984589f, +0.8449724198323217f, +0.5348098818484104f, +0.1427374585755972f,
			+0.9897605861618151f, -0.6594300077680133f, +0.7517659641504648f, -0.9999212381512442f,
			-0.0125505973595986f, -0.6403535266476091f, -0.7680803088935230f, +0.1675347077076747f,
			-0.9858661784001437f, +0.8581295336101056f, -0.5134332513054668f, +0.9479357869928937f,
			+0.3184615263075951f, +0.3740788450165170f, +0.9273969040875156f, -0.4616759649446430f,
			+0.8870486477034012f, -0.9742049295269273f, +0.2256651397213017f, -0.8046793020829978f,
			-0.5937097108850584f, -0.0717863620135296f, -0.9974200309943962f, +0.7113652211526822f,
			-0.7028225395748172f, +0.9964799940037152f, +0.0838309104707540f, +0.5839450884626246f,
			+0.8117931594072332f, -0.2374179978909748f, +0.9714075840127259f, -0.8925614000865144f,
			+0.4509258775847768f, -0.9228099950981292f, -0.3852553866553855f, -0.3069863155319683f,
			-0.9517139286971200f, +0.5237628071845146f, -0.8518641451605984f, +0.9878182118285335f,
			-0.1556122758007173f, +0.7602881737752754f, +0.6495859395164404f, +0.0004696772366984f,
			+0.9999998897016406f, -0.7596776469502666f, +0.6502998329417794f, -0.9879639510809196f,
			-0.1546842957917130f, -0.5245627784110601f, -0.8513717704420726f, +0.3060921834538644f,
			-0.9520018777441807f, +0.9224476966294768f, -0.3861220622846781f, +0.8929845854878761f,
			+0.4500872471877493f, +0.2383303891026603f, +0.9711841358002995f, -0.5831822693781987f,
			+0.8123413326200348f, -0.9964008074312266f, +0.0847669213219385f, -0.7120251067268070f,
			-0.7021540054650968f, +0.0708493994771745f, -0.9974870237721009f, +0.8041212432524677f,
			-0.5944653279629567f, +0.9744164792492415f, +0.2247499165016809f, +0.4625090142797330f,
			+0.8866145790082576f, };
	private static final float[] GRAD_3D = { -0.448549002408981f, +1.174316525459290f, +0.000000000000001f, +0.0f,
			+0.000000000000001f, +1.069324374198914f, +0.660878777503967f, +0.0f, +0.448549002408981f,
			+1.174316525459290f, +0.000000000000001f, +0.0f, +0.000000000000001f, +1.069324374198914f,
			-0.660878777503967f, +0.0f, -0.725767493247986f, +0.725767493247986f, -0.725767493247986f, +0.0f,
			-1.069324374198914f, +0.660878777503967f, +0.000000000000001f, +0.0f, -0.725767493247986f,
			+0.725767493247986f, +0.725767493247986f, +0.0f, +0.725767493247986f, +0.725767493247986f,
			+0.725767493247986f, +0.0f, +1.069324374198914f, +0.660878777503967f, +0.000000000000000f, +0.0f,
			+0.725767493247986f, +0.725767493247986f, -0.725767493247986f, +0.0f, -0.660878777503967f,
			+0.000000000000003f, -1.069324374198914f, +0.0f, -1.174316525459290f, +0.000000000000003f,
			-0.448549002408981f, +0.0f, +0.000000000000000f, +0.448549002408981f, -1.174316525459290f, +0.0f,
			-0.660878777503967f, +0.000000000000001f, +1.069324374198914f, +0.0f, +0.000000000000001f,
			+0.448549002408981f, +1.174316525459290f, +0.0f, -1.174316525459290f, +0.000000000000001f,
			+0.448549002408981f, +0.0f, +0.660878777503967f, +0.000000000000001f, +1.069324374198914f, +0.0f,
			+1.174316525459290f, +0.000000000000001f, +0.448549002408981f, +0.0f, +0.660878777503967f,
			+0.000000000000001f, -1.069324374198914f, +0.0f, +1.174316525459290f, +0.000000000000001f,
			-0.448549002408981f, +0.0f, -0.725767493247986f, -0.725767493247986f, -0.725767493247986f, +0.0f,
			-1.069324374198914f, -0.660878777503967f, -0.000000000000001f, +0.0f, -0.000000000000001f,
			-0.448549002408981f, -1.174316525459290f, +0.0f, -0.000000000000001f, -0.448549002408981f,
			+1.174316525459290f, +0.0f, -0.725767493247986f, -0.725767493247986f, +0.725767493247986f, +0.0f,
			+0.725767493247986f, -0.725767493247986f, +0.725767493247986f, +0.0f, +1.069324374198914f,
			-0.660878777503967f, +0.000000000000001f, +0.0f, +0.725767493247986f, -0.725767493247986f,
			-0.725767493247986f, +0.0f, -0.000000000000004f, -1.069324374198914f, -0.660878777503967f, +0.0f,
			-0.448549002408981f, -1.174316525459290f, -0.000000000000003f, +0.0f, -0.000000000000003f,
			-1.069324374198914f, +0.660878777503967f, +0.0f, +0.448549002408981f, -1.174316525459290f,
			+0.000000000000003f, +0.0f, };

	private static final Float2[] CELL_2D = { new Float2(-0.4313539279f, 0.1281943404f),
			new Float2(-0.1733316799f, 0.415278375f), new Float2(-0.2821957395f, -0.3505218461f),
			new Float2(-0.2806473808f, 0.3517627718f), new Float2(0.3125508975f, -0.3237467165f),
			new Float2(0.3383018443f, -0.2967353402f), new Float2(-0.4393982022f, -0.09710417025f),
			new Float2(-0.4460443703f, -0.05953502905f), new Float2(-0.302223039f, 0.3334085102f),
			new Float2(-0.212681052f, -0.3965687458f), new Float2(-0.2991156529f, 0.3361990872f),
			new Float2(0.2293323691f, 0.3871778202f), new Float2(0.4475439151f, -0.04695150755f),
			new Float2(0.1777518f, 0.41340573f), new Float2(0.1688522499f, -0.4171197882f),
			new Float2(-0.0976597166f, 0.4392750616f), new Float2(0.08450188373f, 0.4419948321f),
			new Float2(-0.4098760448f, -0.1857461384f), new Float2(0.3476585782f, -0.2857157906f),
			new Float2(-0.3350670039f, -0.30038326f), new Float2(0.2298190031f, -0.3868891648f),
			new Float2(-0.01069924099f, 0.449872789f), new Float2(-0.4460141246f, -0.05976119672f),
			new Float2(0.3650293864f, 0.2631606867f), new Float2(-0.349479423f, 0.2834856838f),
			new Float2(-0.4122720642f, 0.1803655873f), new Float2(-0.267327811f, 0.3619887311f),
			new Float2(0.322124041f, -0.3142230135f), new Float2(0.2880445931f, -0.3457315612f),
			new Float2(0.3892170926f, -0.2258540565f), new Float2(0.4492085018f, -0.02667811596f),
			new Float2(-0.4497724772f, 0.01430799601f), new Float2(0.1278175387f, -0.4314657307f),
			new Float2(-0.03572100503f, 0.4485799926f), new Float2(-0.4297407068f, -0.1335025276f),
			new Float2(-0.3217817723f, 0.3145735065f), new Float2(-0.3057158873f, 0.3302087162f),
			new Float2(-0.414503978f, 0.1751754899f), new Float2(-0.3738139881f, 0.2505256519f),
			new Float2(0.2236891408f, -0.3904653228f), new Float2(0.002967775577f, -0.4499902136f),
			new Float2(0.1747128327f, -0.4146991995f), new Float2(-0.4423772489f, -0.08247647938f),
			new Float2(-0.2763960987f, -0.355112935f), new Float2(-0.4019385906f, -0.2023496216f),
			new Float2(0.3871414161f, -0.2293938184f), new Float2(-0.430008727f, 0.1326367019f),
			new Float2(-0.03037574274f, -0.4489736231f), new Float2(-0.3486181573f, 0.2845441624f),
			new Float2(0.04553517144f, -0.4476902368f), new Float2(-0.0375802926f, 0.4484280562f),
			new Float2(0.3266408905f, 0.3095250049f), new Float2(0.06540017593f, -0.4452222108f),
			new Float2(0.03409025829f, 0.448706869f), new Float2(-0.4449193635f, 0.06742966669f),
			new Float2(-0.4255936157f, -0.1461850686f), new Float2(0.449917292f, 0.008627302568f),
			new Float2(0.05242606404f, 0.4469356864f), new Float2(-0.4495305179f, -0.02055026661f),
			new Float2(-0.1204775703f, 0.4335725488f), new Float2(-0.341986385f, -0.2924813028f),
			new Float2(0.3865320182f, 0.2304191809f), new Float2(0.04506097811f, -0.447738214f),
			new Float2(-0.06283465979f, 0.4455915232f), new Float2(0.3932600341f, -0.2187385324f),
			new Float2(0.4472261803f, -0.04988730975f), new Float2(0.3753571011f, -0.2482076684f),
			new Float2(-0.273662295f, 0.357223947f), new Float2(0.1700461538f, 0.4166344988f),
			new Float2(0.4102692229f, 0.1848760794f), new Float2(0.323227187f, -0.3130881435f),
			new Float2(-0.2882310238f, -0.3455761521f), new Float2(0.2050972664f, 0.4005435199f),
			new Float2(0.4414085979f, -0.08751256895f), new Float2(-0.1684700334f, 0.4172743077f),
			new Float2(-0.003978032396f, 0.4499824166f), new Float2(-0.2055133639f, 0.4003301853f),
			new Float2(-0.006095674897f, -0.4499587123f), new Float2(-0.1196228124f, -0.4338091548f),
			new Float2(0.3901528491f, -0.2242337048f), new Float2(0.01723531752f, 0.4496698165f),
			new Float2(-0.3015070339f, 0.3340561458f), new Float2(-0.01514262423f, -0.4497451511f),
			new Float2(-0.4142574071f, -0.1757577897f), new Float2(-0.1916377265f, -0.4071547394f),
			new Float2(0.3749248747f, 0.2488600778f), new Float2(-0.2237774255f, 0.3904147331f),
			new Float2(-0.4166343106f, -0.1700466149f), new Float2(0.3619171625f, 0.267424695f),
			new Float2(0.1891126846f, -0.4083336779f), new Float2(-0.3127425077f, 0.323561623f),
			new Float2(-0.3281807787f, 0.307891826f), new Float2(-0.2294806661f, 0.3870899429f),
			new Float2(-0.3445266136f, 0.2894847362f), new Float2(-0.4167095422f, -0.1698621719f),
			new Float2(-0.257890321f, -0.3687717212f), new Float2(-0.3612037825f, 0.2683874578f),
			new Float2(0.2267996491f, 0.3886668486f), new Float2(0.207157062f, 0.3994821043f),
			new Float2(0.08355176718f, -0.4421754202f), new Float2(-0.4312233307f, 0.1286329626f),
			new Float2(0.3257055497f, 0.3105090899f), new Float2(0.177701095f, -0.4134275279f),
			new Float2(-0.445182522f, 0.06566979625f), new Float2(0.3955143435f, 0.2146355146f),
			new Float2(-0.4264613988f, 0.1436338239f), new Float2(-0.3793799665f, -0.2420141339f),
			new Float2(0.04617599081f, -0.4476245948f), new Float2(-0.371405428f, -0.2540826796f),
			new Float2(0.2563570295f, -0.3698392535f), new Float2(0.03476646309f, 0.4486549822f),
			new Float2(-0.3065454405f, 0.3294387544f), new Float2(-0.2256979823f, 0.3893076172f),
			new Float2(0.4116448463f, -0.1817925206f), new Float2(-0.2907745828f, -0.3434387019f),
			new Float2(0.2842278468f, -0.348876097f), new Float2(0.3114589359f, -0.3247973695f),
			new Float2(0.4464155859f, -0.0566844308f), new Float2(-0.3037334033f, -0.3320331606f),
			new Float2(0.4079607166f, 0.1899159123f), new Float2(-0.3486948919f, -0.2844501228f),
			new Float2(0.3264821436f, 0.3096924441f), new Float2(0.3211142406f, 0.3152548881f),
			new Float2(0.01183382662f, 0.4498443737f), new Float2(0.4333844092f, 0.1211526057f),
			new Float2(0.3118668416f, 0.324405723f), new Float2(-0.272753471f, 0.3579183483f),
			new Float2(-0.422228622f, -0.1556373694f), new Float2(-0.1009700099f, -0.4385260051f),
			new Float2(-0.2741171231f, -0.3568750521f), new Float2(-0.1465125133f, 0.4254810025f),
			new Float2(0.2302279044f, -0.3866459777f), new Float2(-0.3699435608f, 0.2562064828f),
			new Float2(0.105700352f, -0.4374099171f), new Float2(-0.2646713633f, 0.3639355292f),
			new Float2(0.3521828122f, 0.2801200935f), new Float2(-0.1864187807f, -0.4095705534f),
			new Float2(0.1994492955f, -0.4033856449f), new Float2(0.3937065066f, 0.2179339044f),
			new Float2(-0.3226158377f, 0.3137180602f), new Float2(0.3796235338f, 0.2416318948f),
			new Float2(0.1482921929f, 0.4248640083f), new Float2(-0.407400394f, 0.1911149365f),
			new Float2(0.4212853031f, 0.1581729856f), new Float2(-0.2621297173f, 0.3657704353f),
			new Float2(-0.2536986953f, -0.3716678248f), new Float2(-0.2100236383f, 0.3979825013f),
			new Float2(0.3624152444f, 0.2667493029f), new Float2(-0.3645038479f, -0.2638881295f),
			new Float2(0.2318486784f, 0.3856762766f), new Float2(-0.3260457004f, 0.3101519002f),
			new Float2(-0.2130045332f, -0.3963950918f), new Float2(0.3814998766f, -0.2386584257f),
			new Float2(-0.342977305f, 0.2913186713f), new Float2(-0.4355865605f, 0.1129794154f),
			new Float2(-0.2104679605f, 0.3977477059f), new Float2(0.3348364681f, -0.3006402163f),
			new Float2(0.3430468811f, 0.2912367377f), new Float2(-0.2291836801f, -0.3872658529f),
			new Float2(0.2547707298f, -0.3709337882f), new Float2(0.4236174945f, -0.151816397f),
			new Float2(-0.15387742f, 0.4228731957f), new Float2(-0.4407449312f, 0.09079595574f),
			new Float2(-0.06805276192f, -0.444824484f), new Float2(0.4453517192f, -0.06451237284f),
			new Float2(0.2562464609f, -0.3699158705f), new Float2(0.3278198355f, -0.3082761026f),
			new Float2(-0.4122774207f, -0.1803533432f), new Float2(0.3354090914f, -0.3000012356f),
			new Float2(0.446632869f, -0.05494615882f), new Float2(-0.1608953296f, 0.4202531296f),
			new Float2(-0.09463954939f, 0.4399356268f), new Float2(-0.02637688324f, -0.4492262904f),
			new Float2(0.447102804f, -0.05098119915f), new Float2(-0.4365670908f, 0.1091291678f),
			new Float2(-0.3959858651f, 0.2137643437f), new Float2(-0.4240048207f, -0.1507312575f),
			new Float2(-0.3882794568f, 0.2274622243f), new Float2(-0.4283652566f, -0.1378521198f),
			new Float2(0.3303888091f, 0.305521251f), new Float2(0.3321434919f, -0.3036127481f),
			new Float2(-0.413021046f, -0.1786438231f), new Float2(0.08403060337f, -0.4420846725f),
			new Float2(-0.3822882919f, 0.2373934748f), new Float2(-0.3712395594f, -0.2543249683f),
			new Float2(0.4472363971f, -0.04979563372f), new Float2(-0.4466591209f, 0.05473234629f),
			new Float2(0.0486272539f, -0.4473649407f), new Float2(-0.4203101295f, -0.1607463688f),
			new Float2(0.2205360833f, 0.39225481f), new Float2(-0.3624900666f, 0.2666476169f),
			new Float2(-0.4036086833f, -0.1989975647f), new Float2(0.2152727807f, 0.3951678503f),
			new Float2(-0.4359392962f, -0.1116106179f), new Float2(0.4178354266f, 0.1670735057f),
			new Float2(0.2007630161f, 0.4027334247f), new Float2(-0.07278067175f, -0.4440754146f),
			new Float2(0.3644748615f, -0.2639281632f), new Float2(-0.4317451775f, 0.126870413f),
			new Float2(-0.297436456f, 0.3376855855f), new Float2(-0.2998672222f, 0.3355289094f),
			new Float2(-0.2673674124f, 0.3619594822f), new Float2(0.2808423357f, 0.3516071423f),
			new Float2(0.3498946567f, 0.2829730186f), new Float2(-0.2229685561f, 0.390877248f),
			new Float2(0.3305823267f, 0.3053118493f), new Float2(-0.2436681211f, -0.3783197679f),
			new Float2(-0.03402776529f, 0.4487116125f), new Float2(-0.319358823f, 0.3170330301f),
			new Float2(0.4454633477f, -0.06373700535f), new Float2(0.4483504221f, 0.03849544189f),
			new Float2(-0.4427358436f, -0.08052932871f), new Float2(0.05452298565f, 0.4466847255f),
			new Float2(-0.2812560807f, 0.3512762688f), new Float2(0.1266696921f, 0.4318041097f),
			new Float2(-0.3735981243f, 0.2508474468f), new Float2(0.2959708351f, -0.3389708908f),
			new Float2(-0.3714377181f, 0.254035473f), new Float2(-0.404467102f, -0.1972469604f),
			new Float2(0.1636165687f, -0.419201167f), new Float2(0.3289185495f, -0.3071035458f),
			new Float2(-0.2494824991f, -0.3745109914f), new Float2(0.03283133272f, 0.4488007393f),
			new Float2(-0.166306057f, -0.4181414777f), new Float2(-0.106833179f, 0.4371346153f),
			new Float2(0.06440260376f, -0.4453676062f), new Float2(-0.4483230967f, 0.03881238203f),
			new Float2(-0.421377757f, -0.1579265206f), new Float2(0.05097920662f, -0.4471030312f),
			new Float2(0.2050584153f, -0.4005634111f), new Float2(0.4178098529f, -0.167137449f),
			new Float2(-0.3565189504f, -0.2745801121f), new Float2(0.4478398129f, 0.04403977727f),
			new Float2(-0.3399999602f, -0.2947881053f), new Float2(0.3767121994f, 0.2461461331f),
			new Float2(-0.3138934434f, 0.3224451987f), new Float2(-0.1462001792f, -0.4255884251f),
			new Float2(0.3970290489f, -0.2118205239f), new Float2(0.4459149305f, -0.06049689889f),
			new Float2(-0.4104889426f, -0.1843877112f), new Float2(0.1475103971f, -0.4251360756f),
			new Float2(0.09258030352f, 0.4403735771f), new Float2(-0.1589664637f, -0.4209865359f),
			new Float2(0.2482445008f, 0.3753327428f), new Float2(0.4383624232f, -0.1016778537f),
			new Float2(0.06242802956f, 0.4456486745f), new Float2(0.2846591015f, -0.3485243118f),
			new Float2(-0.344202744f, -0.2898697484f), new Float2(0.1198188883f, -0.4337550392f),
			new Float2(-0.243590703f, 0.3783696201f), new Float2(0.2958191174f, -0.3391033025f),
			new Float2(-0.1164007991f, 0.4346847754f), new Float2(0.1274037151f, -0.4315881062f),
			new Float2(0.368047306f, 0.2589231171f), new Float2(0.2451436949f, 0.3773652989f),
			new Float2(-0.4314509715f, 0.12786735f), };

	private static final Float3[] CELL_3D = { new Float3(0.1453787434f, -0.4149781685f, -0.0956981749f),
			new Float3(-0.01242829687f, -0.1457918398f, -0.4255470325f),
			new Float3(0.2877979582f, -0.02606483451f, -0.3449535616f),
			new Float3(-0.07732986802f, 0.2377094325f, 0.3741848704f),
			new Float3(0.1107205875f, -0.3552302079f, -0.2530858567f),
			new Float3(0.2755209141f, 0.2640521179f, -0.238463215f),
			new Float3(0.294168941f, 0.1526064594f, 0.3044271714f),
			new Float3(0.4000921098f, -0.2034056362f, 0.03244149937f),
			new Float3(-0.1697304074f, 0.3970864695f, -0.1265461359f),
			new Float3(-0.1483224484f, -0.3859694688f, 0.1775613147f),
			new Float3(0.2623596946f, -0.2354852944f, 0.2796677792f),
			new Float3(-0.2709003183f, 0.3505271138f, -0.07901746678f),
			new Float3(-0.03516550699f, 0.3885234328f, 0.2243054374f),
			new Float3(-0.1267712655f, 0.1920044036f, 0.3867342179f),
			new Float3(0.02952021915f, 0.4409685861f, 0.08470692262f),
			new Float3(-0.2806854217f, -0.266996757f, 0.2289725438f),
			new Float3(-0.171159547f, 0.2141185563f, 0.3568720405f),
			new Float3(0.2113227183f, 0.3902405947f, -0.07453178509f),
			new Float3(-0.1024352839f, 0.2128044156f, -0.3830421561f),
			new Float3(-0.3304249877f, -0.1566986703f, 0.2622305365f),
			new Float3(0.2091111325f, 0.3133278055f, -0.2461670583f),
			new Float3(0.344678154f, -0.1944240454f, -0.2142341261f),
			new Float3(0.1984478035f, -0.3214342325f, -0.2445373252f),
			new Float3(-0.2929008603f, 0.2262915116f, 0.2559320961f),
			new Float3(-0.1617332831f, 0.006314769776f, -0.4198838754f),
			new Float3(-0.3582060271f, -0.148303178f, -0.2284613961f),
			new Float3(-0.1852067326f, -0.3454119342f, -0.2211087107f),
			new Float3(0.3046301062f, 0.1026310383f, 0.314908508f),
			new Float3(-0.03816768434f, -0.2551766358f, -0.3686842991f),
			new Float3(-0.4084952196f, 0.1805950793f, 0.05492788837f),
			new Float3(-0.02687443361f, -0.2749741471f, 0.3551999201f),
			new Float3(-0.03801098351f, 0.3277859044f, 0.3059600725f),
			new Float3(0.2371120802f, 0.2900386767f, -0.2493099024f),
			new Float3(0.4447660503f, 0.03946930643f, 0.05590469027f),
			new Float3(0.01985147278f, -0.01503183293f, -0.4493105419f),
			new Float3(0.4274339143f, 0.03345994256f, -0.1366772882f),
			new Float3(-0.2072988631f, 0.2871414597f, -0.2776273824f),
			new Float3(-0.3791240978f, 0.1281177671f, 0.2057929936f),
			new Float3(-0.2098721267f, -0.1007087278f, -0.3851122467f),
			new Float3(0.01582798878f, 0.4263894424f, 0.1429738373f),
			new Float3(-0.1888129464f, -0.3160996813f, -0.2587096108f),
			new Float3(0.1612988974f, -0.1974805082f, -0.3707885038f),
			new Float3(-0.08974491322f, 0.229148752f, -0.3767448739f),
			new Float3(0.07041229526f, 0.4150230285f, -0.1590534329f),
			new Float3(-0.1082925611f, -0.1586061639f, 0.4069604477f),
			new Float3(0.2474100658f, -0.3309414609f, 0.1782302128f),
			new Float3(-0.1068836661f, -0.2701644537f, -0.3436379634f),
			new Float3(0.2396452163f, 0.06803600538f, -0.3747549496f),
			new Float3(-0.3063886072f, 0.2597428179f, 0.2028785103f),
			new Float3(0.1593342891f, -0.3114350249f, -0.2830561951f),
			new Float3(0.2709690528f, 0.1412648683f, -0.3303331794f),
			new Float3(-0.1519780427f, 0.3623355133f, 0.2193527988f),
			new Float3(0.1699773681f, 0.3456012883f, 0.2327390037f),
			new Float3(-0.1986155616f, 0.3836276443f, -0.1260225743f),
			new Float3(-0.1887482106f, -0.2050154888f, -0.353330953f),
			new Float3(0.2659103394f, 0.3015631259f, -0.2021172246f),
			new Float3(-0.08838976154f, -0.4288819642f, -0.1036702021f),
			new Float3(-0.04201869311f, 0.3099592485f, 0.3235115047f),
			new Float3(-0.3230334656f, 0.201549922f, -0.2398478873f),
			new Float3(0.2612720941f, 0.2759854499f, -0.2409749453f),
			new Float3(0.385713046f, 0.2193460345f, 0.07491837764f),
			new Float3(0.07654967953f, 0.3721732183f, 0.241095919f),
			new Float3(0.4317038818f, -0.02577753072f, 0.1243675091f),
			new Float3(-0.2890436293f, -0.3418179959f, -0.04598084447f),
			new Float3(-0.2201947582f, 0.383023377f, -0.08548310451f),
			new Float3(0.4161322773f, -0.1669634289f, -0.03817251927f),
			new Float3(0.2204718095f, 0.02654238946f, -0.391391981f),
			new Float3(-0.1040307469f, 0.3890079625f, -0.2008741118f),
			new Float3(-0.1432122615f, 0.371614387f, -0.2095065525f),
			new Float3(0.3978380468f, -0.06206669342f, 0.2009293758f),
			new Float3(-0.2599274663f, 0.2616724959f, -0.2578084893f),
			new Float3(0.4032618332f, -0.1124593585f, 0.1650235939f),
			new Float3(-0.08953470255f, -0.3048244735f, 0.3186935478f),
			new Float3(0.118937202f, -0.2875221847f, 0.325092195f),
			new Float3(0.02167047076f, -0.03284630549f, -0.4482761547f),
			new Float3(-0.3411343612f, 0.2500031105f, 0.1537068389f),
			new Float3(0.3162964612f, 0.3082064153f, -0.08640228117f),
			new Float3(0.2355138889f, -0.3439334267f, -0.1695376245f),
			new Float3(-0.02874541518f, -0.3955933019f, 0.2125550295f),
			new Float3(-0.2461455173f, 0.02020282325f, -0.3761704803f),
			new Float3(0.04208029445f, -0.4470439576f, 0.02968078139f),
			new Float3(0.2727458746f, 0.2288471896f, -0.2752065618f),
			new Float3(-0.1347522818f, -0.02720848277f, -0.4284874806f),
			new Float3(0.3829624424f, 0.1231931484f, -0.2016512234f),
			new Float3(-0.3547613644f, 0.1271702173f, 0.2459107769f),
			new Float3(0.2305790207f, 0.3063895591f, 0.2354968222f),
			new Float3(-0.08323845599f, -0.1922245118f, 0.3982726409f),
			new Float3(0.2993663085f, -0.2619918095f, -0.2103333191f),
			new Float3(-0.2154865723f, 0.2706747713f, 0.287751117f),
			new Float3(0.01683355354f, -0.2680655787f, -0.3610505186f),
			new Float3(0.05240429123f, 0.4335128183f, -0.1087217856f),
			new Float3(0.00940104872f, -0.4472890582f, 0.04841609928f),
			new Float3(0.3465688735f, 0.01141914583f, -0.2868093776f),
			new Float3(-0.3706867948f, -0.2551104378f, 0.003156692623f),
			new Float3(0.2741169781f, 0.2139972417f, -0.2855959784f),
			new Float3(0.06413433865f, 0.1708718512f, 0.4113266307f),
			new Float3(-0.388187972f, -0.03973280434f, -0.2241236325f),
			new Float3(0.06419469312f, -0.2803682491f, 0.3460819069f),
			new Float3(-0.1986120739f, -0.3391173584f, 0.2192091725f),
			new Float3(-0.203203009f, -0.3871641506f, 0.1063600375f),
			new Float3(-0.1389736354f, -0.2775901578f, -0.3257760473f),
			new Float3(-0.06555641638f, 0.342253257f, -0.2847192729f),
			new Float3(-0.2529246486f, -0.2904227915f, 0.2327739768f),
			new Float3(0.1444476522f, 0.1069184044f, 0.4125570634f),
			new Float3(-0.3643780054f, -0.2447099973f, -0.09922543227f),
			new Float3(0.4286142488f, -0.1358496089f, -0.01829506817f),
			new Float3(0.165872923f, -0.3136808464f, -0.2767498872f),
			new Float3(0.2219610524f, -0.3658139958f, 0.1393320198f),
			new Float3(0.04322940318f, -0.3832730794f, 0.2318037215f),
			new Float3(-0.08481269795f, -0.4404869674f, -0.03574965489f),
			new Float3(0.1822082075f, -0.3953259299f, 0.1140946023f),
			new Float3(-0.3269323334f, 0.3036542563f, 0.05838957105f),
			new Float3(-0.4080485344f, 0.04227858267f, -0.184956522f),
			new Float3(0.2676025294f, -0.01299671652f, 0.36155217f),
			new Float3(0.3024892441f, -0.1009990293f, -0.3174892964f),
			new Float3(0.1448494052f, 0.425921681f, -0.0104580805f),
			new Float3(0.4198402157f, 0.08062320474f, 0.1404780841f),
			new Float3(-0.3008872161f, -0.333040905f, -0.03241355801f),
			new Float3(0.3639310428f, -0.1291284382f, -0.2310412139f),
			new Float3(0.3295806598f, 0.0184175994f, -0.3058388149f),
			new Float3(0.2776259487f, -0.2974929052f, -0.1921504723f),
			new Float3(0.4149000507f, -0.144793182f, -0.09691688386f),
			new Float3(0.145016715f, -0.0398992945f, 0.4241205002f),
			new Float3(0.09299023471f, -0.299732164f, -0.3225111565f),
			new Float3(0.1028907093f, -0.361266869f, 0.247789732f),
			new Float3(0.2683057049f, -0.07076041213f, -0.3542668666f),
			new Float3(-0.4227307273f, -0.07933161816f, -0.1323073187f),
			new Float3(-0.1781224702f, 0.1806857196f, -0.3716517945f),
			new Float3(0.4390788626f, -0.02841848598f, -0.09435116353f),
			new Float3(0.2972583585f, 0.2382799621f, -0.2394997452f),
			new Float3(-0.1707002821f, 0.2215845691f, 0.3525077196f),
			new Float3(0.3806686614f, 0.1471852559f, -0.1895464869f),
			new Float3(-0.1751445661f, -0.274887877f, 0.3102596268f),
			new Float3(-0.2227237566f, -0.2316778837f, 0.3149912482f),
			new Float3(0.1369633021f, 0.1341343041f, -0.4071228836f),
			new Float3(-0.3529503428f, -0.2472893463f, -0.129514612f),
			new Float3(-0.2590744185f, -0.2985577559f, -0.2150435121f),
			new Float3(-0.3784019401f, 0.2199816631f, -0.1044989934f),
			new Float3(-0.05635805671f, 0.1485737441f, 0.4210102279f),
			new Float3(0.3251428613f, 0.09666046873f, -0.2957006485f),
			new Float3(-0.4190995804f, 0.1406751354f, -0.08405978803f),
			new Float3(-0.3253150961f, -0.3080335042f, -0.04225456877f),
			new Float3(0.2857945863f, -0.05796152095f, 0.3427271751f),
			new Float3(-0.2733604046f, 0.1973770973f, -0.2980207554f),
			new Float3(0.219003657f, 0.2410037886f, -0.3105713639f),
			new Float3(0.3182767252f, -0.271342949f, 0.1660509868f),
			new Float3(-0.03222023115f, -0.3331161506f, -0.300824678f),
			new Float3(-0.3087780231f, 0.1992794134f, -0.2596995338f),
			new Float3(-0.06487611647f, -0.4311322747f, 0.1114273361f),
			new Float3(0.3921171432f, -0.06294284106f, -0.2116183942f),
			new Float3(-0.1606404506f, -0.358928121f, -0.2187812825f),
			new Float3(-0.03767771199f, -0.2290351443f, 0.3855169162f),
			new Float3(0.1394866832f, -0.3602213994f, 0.2308332918f),
			new Float3(-0.4345093872f, 0.005751117145f, 0.1169124335f),
			new Float3(-0.1044637494f, 0.4168128432f, -0.1336202785f),
			new Float3(0.2658727501f, 0.2551943237f, 0.2582393035f),
			new Float3(0.2051461999f, 0.1975390727f, 0.3484154868f),
			new Float3(-0.266085566f, 0.23483312f, 0.2766800993f),
			new Float3(0.07849405464f, -0.3300346342f, -0.2956616708f),
			new Float3(-0.2160686338f, 0.05376451292f, -0.3910546287f),
			new Float3(-0.185779186f, 0.2148499206f, 0.3490352499f),
			new Float3(0.02492421743f, -0.3229954284f, -0.3123343347f),
			new Float3(-0.120167831f, 0.4017266681f, 0.1633259825f),
			new Float3(-0.02160084693f, -0.06885389554f, 0.4441762538f),
			new Float3(0.2597670064f, 0.3096300784f, 0.1978643903f),
			new Float3(-0.1611553854f, -0.09823036005f, 0.4085091653f),
			new Float3(-0.3278896792f, 0.1461670309f, 0.2713366126f),
			new Float3(0.2822734956f, 0.03754421121f, -0.3484423997f),
			new Float3(0.03169341113f, 0.347405252f, -0.2842624114f),
			new Float3(0.2202613604f, -0.3460788041f, -0.1849713341f),
			new Float3(0.2933396046f, 0.3031973659f, 0.1565989581f),
			new Float3(-0.3194922995f, 0.2453752201f, -0.200538455f),
			new Float3(-0.3441586045f, -0.1698856132f, -0.2349334659f),
			new Float3(0.2703645948f, -0.3574277231f, 0.04060059933f),
			new Float3(0.2298568861f, 0.3744156221f, 0.0973588921f),
			new Float3(0.09326603877f, -0.3170108894f, 0.3054595587f),
			new Float3(-0.1116165319f, -0.2985018719f, 0.3177080142f),
			new Float3(0.2172907365f, -0.3460005203f, -0.1885958001f),
			new Float3(0.1991339479f, 0.3820341668f, -0.1299829458f),
			new Float3(-0.0541918155f, -0.2103145071f, 0.39412061f),
			new Float3(0.08871336998f, 0.2012117383f, 0.3926114802f),
			new Float3(0.2787673278f, 0.3505404674f, 0.04370535101f),
			new Float3(-0.322166438f, 0.3067213525f, 0.06804996813f),
			new Float3(-0.4277366384f, 0.132066775f, 0.04582286686f),
			new Float3(0.240131882f, -0.1612516055f, 0.344723946f),
			new Float3(0.1448607981f, -0.2387819045f, 0.3528435224f),
			new Float3(-0.3837065682f, -0.2206398454f, 0.08116235683f),
			new Float3(-0.4382627882f, -0.09082753406f, -0.04664855374f),
			new Float3(-0.37728353f, 0.05445141085f, 0.2391488697f),
			new Float3(0.1259579313f, 0.348394558f, 0.2554522098f),
			new Float3(-0.1406285511f, -0.270877371f, -0.3306796947f),
			new Float3(-0.1580694418f, 0.4162931958f, -0.06491553533f),
			new Float3(0.2477612106f, -0.2927867412f, -0.2353514536f),
			new Float3(0.2916132853f, 0.3312535401f, 0.08793624968f),
			new Float3(0.07365265219f, -0.1666159848f, 0.411478311f),
			new Float3(-0.26126526f, -0.2422237692f, 0.2748965434f),
			new Float3(-0.3721862032f, 0.252790166f, 0.008634938242f),
			new Float3(-0.3691191571f, -0.255281188f, 0.03290232422f),
			new Float3(0.2278441737f, -0.3358364886f, 0.1944244981f),
			new Float3(0.363398169f, -0.2310190248f, 0.1306597909f),
			new Float3(-0.304231482f, -0.2698452035f, 0.1926830856f),
			new Float3(-0.3199312232f, 0.316332536f, -0.008816977938f),
			new Float3(0.2874852279f, 0.1642275508f, -0.304764754f),
			new Float3(-0.1451096801f, 0.3277541114f, -0.2720669462f),
			new Float3(0.3220090754f, 0.0511344108f, 0.3101538769f),
			new Float3(-0.1247400865f, -0.04333605335f, -0.4301882115f),
			new Float3(-0.2829555867f, -0.3056190617f, -0.1703910946f),
			new Float3(0.1069384374f, 0.3491024667f, -0.2630430352f),
			new Float3(-0.1420661144f, -0.3055376754f, -0.2982682484f),
			new Float3(-0.250548338f, 0.3156466809f, -0.2002316239f),
			new Float3(0.3265787872f, 0.1871229129f, 0.2466400438f),
			new Float3(0.07646097258f, -0.3026690852f, 0.324106687f),
			new Float3(0.3451771584f, 0.2757120714f, -0.0856480183f),
			new Float3(0.298137964f, 0.2852657134f, 0.179547284f),
			new Float3(0.2812250376f, 0.3466716415f, 0.05684409612f),
			new Float3(0.4390345476f, -0.09790429955f, -0.01278335452f),
			new Float3(0.2148373234f, 0.1850172527f, 0.3494474791f),
			new Float3(0.2595421179f, -0.07946825393f, 0.3589187731f),
			new Float3(0.3182823114f, -0.307355516f, -0.08203022006f),
			new Float3(-0.4089859285f, -0.04647718411f, 0.1818526372f),
			new Float3(-0.2826749061f, 0.07417482322f, 0.3421885344f),
			new Float3(0.3483864637f, 0.225442246f, -0.1740766085f),
			new Float3(-0.3226415069f, -0.1420585388f, -0.2796816575f),
			new Float3(0.4330734858f, -0.118868561f, -0.02859407492f),
			new Float3(-0.08717822568f, -0.3909896417f, -0.2050050172f),
			new Float3(-0.2149678299f, 0.3939973956f, -0.03247898316f),
			new Float3(-0.2687330705f, 0.322686276f, -0.1617284888f),
			new Float3(0.2105665099f, -0.1961317136f, -0.3459683451f),
			new Float3(0.4361845915f, -0.1105517485f, 0.004616608544f),
			new Float3(0.05333333359f, -0.313639498f, -0.3182543336f),
			new Float3(-0.05986216652f, 0.1361029153f, -0.4247264031f),
			new Float3(0.3664988455f, 0.2550543014f, -0.05590974511f),
			new Float3(-0.2341015558f, -0.182405731f, 0.3382670703f),
			new Float3(-0.04730947785f, -0.4222150243f, -0.1483114513f),
			new Float3(-0.2391566239f, -0.2577696514f, -0.2808182972f),
			new Float3(-0.1242081035f, 0.4256953395f, -0.07652336246f),
			new Float3(0.2614832715f, -0.3650179274f, 0.02980623099f),
			new Float3(-0.2728794681f, -0.3499628774f, 0.07458404908f),
			new Float3(0.007892900508f, -0.1672771315f, 0.4176793787f),
			new Float3(-0.01730330376f, 0.2978486637f, -0.3368779738f),
			new Float3(0.2054835762f, -0.3252600376f, -0.2334146693f),
			new Float3(-0.3231994983f, 0.1564282844f, -0.2712420987f),
			new Float3(-0.2669545963f, 0.2599343665f, -0.2523278991f),
			new Float3(-0.05554372779f, 0.3170813944f, -0.3144428146f),
			new Float3(-0.2083935713f, -0.310922837f, -0.2497981362f),
			new Float3(0.06989323478f, -0.3156141536f, 0.3130537363f),
			new Float3(0.3847566193f, -0.1605309138f, -0.1693876312f),
			new Float3(-0.3026215288f, -0.3001537679f, -0.1443188342f),
			new Float3(0.3450735512f, 0.08611519592f, 0.2756962409f),
			new Float3(0.1814473292f, -0.2788782453f, -0.3029914042f),
			new Float3(-0.03855010448f, 0.09795110726f, 0.4375151083f),
			new Float3(0.3533670318f, 0.2665752752f, 0.08105160988f),
			new Float3(-0.007945601311f, 0.140359426f, -0.4274764309f),
			new Float3(0.4063099273f, -0.1491768253f, -0.1231199324f),
			new Float3(-0.2016773589f, 0.008816271194f, -0.4021797064f),
			new Float3(-0.07527055435f, -0.425643481f, -0.1251477955f), };
}
