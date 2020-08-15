package com.andedit.arcubit.world.gen.features;

import static com.andedit.arcubit.block.Blocks.blocks;

import com.andedit.arcubit.block.BlockType;
import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.FlushablePool;

public class PlentPatch 
{	
	private static FlushablePool<GridPoint2> POOL = new FlushablePool<GridPoint2>(64, 64) {
		@Override
		protected GridPoint2 newObject() {
			return new GridPoint2();
		}
	};
	
	public static void create (World world, float flowerChange, int size, int loops, int x, int z) 
	{
		GridPoint2[] targets = new GridPoint2[loops];
		
		for (int i = 0; i < loops; i++) {
			int randX = MathUtils.random(-size, size);
			int randZ = MathUtils.random(-size, size);
			for (int j = 0; j < loops; j++) 
			{
				if (targets[j] == null) {
					targets[j] = POOL.obtain().set(randX, randZ);
					break;
				} else if (targets[j].x == randX && targets[j].y == randZ) {
					i--;
					continue;
				}
			}
		}
		
		boolean flowerMode = MathUtils.randomBoolean(flowerChange);
		
		byte f = 0;
		if (MathUtils.randomBoolean(0.5f)) {
			f = Blocks.FLOWER;
		} else {
			f = Blocks.ROSE;
		}
		
		for (int i = 0; i < loops; i++) 
		{
			if (targets[i] == null) continue;
			for (int y = ChunkRegion.HEIGHT-1; y > -1; y--) 
			{				
				byte blockData = world.getBlock(targets[i].x+x, y, targets[i].y+z);
				if (blocks[blockData].type == BlockType.SOIL && world.getBlock(targets[i].x+x, y+1, targets[i].y+z) == Blocks.AIR) {
				//if (blockData != Blocks.AIR) {
					if (flowerMode) {
						world.setBlock(targets[i].x+x, y+1, targets[i].y+z, f);
					} else {
						world.setBlock(targets[i].x+x, y+1, targets[i].y+z, Blocks.TALLGRASS);
					}
					break;
				} else if (blocks[blockData].type == BlockType.PLANT) {
					break;
				}
			}
		}
		
		POOL.flush();
	}
	
	public static void createSrub (World world, int size, int loops, int x, int z) 
	{
		GridPoint2[] targets = new GridPoint2[loops];
		
		for (int i = 0; i < loops; i++) {
			int randX = MathUtils.random(-size, size);
			int randZ = MathUtils.random(-size, size);
			for (int j = 0; j < loops; j++) 
			{
				if (targets[j] == null) {
					targets[j] = POOL.obtain().set(randX, randZ);
					break;
				} else if (targets[j].x == randX && targets[j].y == randZ) {
					i--;
					continue;
				}
			}
		}
		
		for (int i = 0; i < loops; i++) 
		{
			if (targets[i] == null) continue;
			for (int y = ChunkRegion.HEIGHT-1; y > 20; y--) 
			{				
				byte blockData = world.getBlock(targets[i].x+x, y, targets[i].y+z);
				if (blocks[blockData].type == BlockType.SAND && world.getBlock(targets[i].x+x, y+1, targets[i].y+z) == Blocks.AIR) {
						world.setBlock(targets[i].x+x, y+1, targets[i].y+z, Blocks.SHRUB);
					break;
				} else if (blocks[blockData].type == BlockType.PLANT) {
					break;
				}
			}
		}
		
		POOL.flush();
	}
}
