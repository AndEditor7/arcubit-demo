package com.andedit.arcubit.handle;

import static com.andedit.arcubit.block.Blocks.blocks;
import static com.andedit.arcubit.world.World.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/** Collision handling. */
public class Collision 
{
	/** Fast but bad particle collision. */
	public static boolean pointCol(Vector3 pos) {
		boolean onGound = false;
		
		float setX = pos.x;
		float setY = pos.y;
		float setZ = pos.z;
		
		// down
		if (getBF(pos.x, pos.y,pos.z)) {
			setY = MathUtils.ceil(pos.y)+0.002f;
			onGound = true;
		}
		
		pos.set(setX, setY, setZ);
		
		// left
		if (getBF(pos.x+0.1f, pos.y, pos.z)) {
			setX = MathUtils.round(pos.x)-0.1f;
		} else		
		// right
		if (getBF(pos.x-0.1f, pos.y, pos.z)) {
			setX = MathUtils.round(pos.x)+0.1f;
		}
		// right z
		if (getBF(pos.x, pos.y, pos.z+0.1f)) {
			setZ = MathUtils.round(pos.z) - 0.1f;
		} else		
		// left z
		if (getBF(pos.x, pos.y, pos.z-0.1f)) {
			setZ = MathUtils.round(pos.z) + 0.1f;
		}
		
		pos.set(setX, setY, setZ);
		return onGound;
	}
	
	private static boolean getBF(float x, float y, float z) {
		return blocks[world.getBlock(x, y, z)].collision;
	}
}
