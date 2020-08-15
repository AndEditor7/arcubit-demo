package com.andedit.arcubit.world.gen.features;

import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.util.math.FastNoise;
import com.andedit.arcubit.world.gen.HillGen;

public final class Lands 
{
	public static void fastGen(ChunkRegion region, FastNoise biomes) {
		final int height = 40; // 23
		final int water = height-1; // 23
		for (int x = 0; x < Chunk.SIZE; x++) {
		for (int z = 0; z < Chunk.SIZE; z++) {
			for (int y = ChunkRegion.HEIGHT-1; y > 0; y--) {
				byte id = region.getBlockChunkf(x, y, z);
				if (id == Blocks.AIR && water > y) {
					region.setBlockChunkf(x, y, z, Blocks.WATER);
				}
				if (id == Blocks.STONE) {
					if (height > y) {
						for (int i = 0; i < 3; i++) {
							region.setBlockChunk(x, y-i, z, Blocks.SAND);
						}
					} else {
						region.setBlockChunkf(x, y, z, Blocks.GRASS);
						for (int i = 1; i < 4; i++) {
							region.setBlockChunk(x, y-i, z, Blocks.DIRT);
						}
					}
					break;
				}
			}
		}}
	}
	
	public static void slowGen(ChunkRegion region, FastNoise biomes) {
		final int height = (int)HillGen.height; // 60
		for (int x = 0; x < Chunk.SIZE; x++) {
		for (int z = 0; z < Chunk.SIZE; z++) {
			boolean clear = true;
			for (int y = ChunkRegion.HEIGHT-1; y > 0; y--) {
				byte id = region.getBlock(x, y, z);
				if (id == Blocks.AIR) {
					clear = true;
					if (y<height-1) region.setBlock(x, y, z, Blocks.WATER);
				}
				if (clear && id == Blocks.STONE) {
					boolean beach = y<height;
					byte topBlock    = beach ? Blocks.SAND : Blocks.GRASS;
					byte middleBlock = beach ? Blocks.SAND : Blocks.DIRT;
					region.setBlock(x, y, z, topBlock); // grass
					clear = false;
					for (int i = 0; i < 3; i++) {
						y--;
						if (y < 0) break;
						byte b = region.getBlock(x, y, z);
						if (b == Blocks.STONE) region.setBlock(x, y, z, middleBlock);
						else if (b == Blocks.AIR) {
							y++;
							break;
						}
					}
					y++;
				}
			}
		}}
	}
}
