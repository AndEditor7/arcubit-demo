package com.andedit.arcubit.world.gen.features;

import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.util.math.FastNoise;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.math.MathUtils;

public class Tree 
{
	public static FastNoise noise = new FastNoise(MathUtils.random.nextInt());
	
	public static void create2(World world, int height, int size, int x, int z)
	{
		byte leave;
		byte log;
		for (int y = ChunkRegion.HEIGHT-1; y > 20; y--)
		{
			byte blockData = world.getBlock(x, y, z);
			if (blockData == Blocks.GRASS || blockData == Blocks.SNOWGRASS || blockData == Blocks.SAND)
			{
				if (blockData == Blocks.SAND) {
					log = Blocks.CACTUS;
					leave = Blocks.AIR;
				} else {
					log = Blocks.LOG;
					leave = Blocks.LEAVES;
				}
				//leaves(x, y, z, height);
				if (leave != 0)
				for (int x1 = -size; x1 < size; x1++)
				{
					for (int y1 = -size; y1 < size; y1++)
					{
						for (int z1 = -size; z1 < size; z1++)
						{
							float sqrt = (float)Math.sqrt((x1*x1)+(y1*y1)+(z1*z1));
							sqrt -= size*0.7f;
							if (noise.GetPerlin((x1+x)/3f, (y1+y)/3f, (z1+z)/3f) > sqrt) {
								world.setBlock(x+x1, y+height+y1+(size/10), z+z1, leave);
							}
						}
					}
				}
				
				int a = 0;
				if (blockData == Blocks.SAND) {
					a = 1;
					height -= 3;
				}
				for (int i = a; i < height; i++) {
					world.setBlock(x, y+i, z, log);
				}
				return;
			}
		}
	}
}
