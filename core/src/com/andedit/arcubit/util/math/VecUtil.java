package com.andedit.arcubit.util.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/** Add more utilities for Vectors. */
public class VecUtil
{	
	public static Vector2 newVec2()
	{
		return new Vector2(0.0f, 0.0f);	
	}
	
	/** Clamp the vector length to given min and max values, but with Y and X axis option. */
	public static void clamp(Vector2 vec, float minX, float maxX, float minY, float maxY)
	{
		vec.set(MathUtils.clamp(vec.x, minX, maxX), MathUtils.clamp(vec.y, minY, maxY));
	}
	
	public static void clamp(Vector3 vec, float minX, float maxX, float minZ, float maxZ)
	{
		vec.set(MathUtils.clamp(vec.x, minX, maxX), vec.y, MathUtils.clamp(vec.z, minZ, maxZ));
	}
	
	/** Whoosh movement effect, glides a object to the target position. */
	public static void whoosh(Vector2 vec, Vector2 target, float speed)
	{
		vec.add(-(vec.x - target.x) * speed, -(vec.y - target.y) * speed);
	}
	
	/** Add random x and y axis with positive and negative value. */
	public static void addRand(Vector2 vec, float sizeX, float sizeY)
	{
		vec.add(MathUtils.random(-sizeX, sizeX), MathUtils.random(-sizeY, sizeY));
	}
	
	public static void drag(Vector3 vec, float power, float space, boolean isMoving)
	{
		if (isMoving)
		{
			if (vec.z > space)
				vec.z -= power;
			if (vec.z < -space)
				vec.z += power;
			if (vec.x > space)
				vec.x -= power;
			if (vec.x < -space)
				vec.x += power;
		} else 
		{
			if (vec.z < space && vec.z > -space) {
				vec.z = 0.0f;
			} else {
				if (vec.z > space)
					vec.z /= power;
				if (vec.z < -space)
					vec.z /= power;
			}
			
			if (vec.x < space && vec.x > -space) {
				vec.x = 0.0f;
			} else {
				if (vec.x > space)
					vec.x /= power;
				if (vec.x < -space)
					vec.x /= power;
			}
		}
	}
}
