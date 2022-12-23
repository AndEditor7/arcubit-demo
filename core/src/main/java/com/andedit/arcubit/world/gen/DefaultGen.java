package com.andedit.arcubit.world.gen;

import java.util.Random;

import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.util.Noise;
import com.andedit.arcubit.world.World;

public class DefaultGen implements Generator {
	private final Noise noise;
	
	{
		var random = new Random();
		noise = new Noise(random.nextInt());
		noise.setFrequency(1/180f);
		noise.setFractalOctaves(2);
	}

	@Override
	public void gen(World world) {
		for (int x = 0; x < World.SIZE; x++)
		for (int z = 0; z < World.SIZE; z++) {
			
			int h = (int)((noise.getConfiguredNoise(x, z) * 10f) + (World.HEIGHT/2));
			for (int y = 0; y < World.HEIGHT; y++) {
				if (h > y) {
					world.setBlock(Blocks.STONE, x, y, z);
				}
			}
		}
	}
}
