package com.andedit.arcubit.util.math;

import com.badlogic.gdx.math.MathUtils;

public class PerlinNoise {
	
	private int MAX_VERTICES = 256;
	private int MAX_VERTICES_MASK = MAX_VERTICES -1;
	
	public float amplitude = 1;

	private float[] r = new float[MAX_VERTICES];
	
	public PerlinNoise(int vertces, float amplitude) 
	{
		this.MAX_VERTICES = vertces;
		this.MAX_VERTICES_MASK = vertces-1;
		
		this.amplitude = amplitude;
		
		this.r = new float[MAX_VERTICES];
		
		for (int i = 0; i < MAX_VERTICES; ++i)
			this.r[i] = MathUtils.random();
	}

	/** Change Vertices. The seed will be changed. */
	public void changeVertices(int vert)
	{
		this.MAX_VERTICES = vert;
		this.MAX_VERTICES_MASK = vert-1;
		
		this.r = new float[this.MAX_VERTICES];
		
		for (int i = 0; i < this.MAX_VERTICES; ++i) 
			this.r[i] = MathUtils.random();
	}
	
	/** Get vales. */
	public float getData(float num)
	{
        int xFloor = MathUtils.floor(num);
        float t = num - xFloor;
        float tRemapSmoothstep = t * t * (3f - 2f * t);

        int xMin = xFloor & this.MAX_VERTICES_MASK;
        int xMax = (xMin + 1) & this.MAX_VERTICES_MASK;

        float y = lerp(this.r[xMin], this.r[xMax], tRemapSmoothstep);

        return (y * this.amplitude) - (this.amplitude/2f);
    }
	
	private float lerp(float a, float b, float t) 
	{
        return a * (1f - t) + b * t;
    }
}
