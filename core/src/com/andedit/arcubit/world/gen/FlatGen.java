package com.andedit.arcubit.world.gen;

import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.util.math.FastNoise;
import com.andedit.arcubit.world.World;
import com.andedit.arcubit.world.gen.features.Lands;
import com.andedit.arcubit.world.gen.features.OrePatch;
import com.andedit.arcubit.world.gen.features.PlentPatch;
import com.andedit.arcubit.world.gen.features.Tree;
import com.andedit.arcubit.world.gen.structure.Structure;
import com.andedit.arcubit.world.gen.structure.StrutBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;

public class FlatGen implements Generator 
{
	private final RandomXS128 rand;
	private final FastNoise lands, biomes;
	
	public FlatGen() {
		rand = new RandomXS128();
		
		lands = new FastNoise(rand.nextInt());
		lands.SetFractalOctaves(5);
		lands.SetFractalGain(0.5f);
		lands.SetFrequency(0.005f);
		
		biomes = new FastNoise(rand.nextInt());
		biomes.SetFractalOctaves(3);
		biomes.SetFractalGain(0.4f);
		biomes.SetFrequency(0.02f);
	}
	
	@Override
	public void gen(World world) {
		for (int x = 0; x < World.defaultSize; x++)
		{
			for (int z = 0; z < World.defaultSize; z++)
			{
				final ChunkRegion region = world.regions[x][z];
				genRegion(region);
				dec(region);
			}
		}
		
		worldGen(world);
	}
	
	private void genRegion(ChunkRegion region)
	{
		for (int x = 0; x < Chunk.SIZE; x++)
		{
			float realX = x + (region.xR*Chunk.SIZE);
			for (int z = 0; z < Chunk.SIZE; z++)
			{
				float value = lands.GetPerlinFractal(realX, z + (region.zR*Chunk.SIZE));
				value *= 38f; // 35
				for (int y = 0; y < ChunkRegion.HEIGHT; y++)
				{				
					if (y-46 < value) { // 30
						region.setBlockChunk(x, y, z, Blocks.STONE);
					}
				}
			}
		}
	}
	
	private void dec(ChunkRegion region)
	{
		final int size = Chunk.SIZE;
		
		Lands.fastGen(region, biomes);
		
		for (int i = 0; i < 4; i++) {
			OrePatch.create(region, 0.4f, Blocks.STONE, Blocks.GOLDORE, 2, MathUtils.random(1, 14), MathUtils.random(1, 35), MathUtils.random(1, 14));
		}
		for (int i = 0; i < 6; i++) {
			OrePatch.create(region, 0.6f, Blocks.STONE, Blocks.COPPERORE, 2, MathUtils.random(1, 14), MathUtils.random(1, 45), MathUtils.random(1, 14));
		}
		for (int i = 0; i < 8; i++) {
			OrePatch.create(region, 0.8f, Blocks.STONE, Blocks.COALORE, 2, MathUtils.random(1, 14), MathUtils.random(1, 55), MathUtils.random(1, 14));
		}
		
		Chunk chunk = region.chunks[0];
		byte bedrock = Blocks.BEDROCK;
		for (int x = 0; x < size; x++)
		{
			for (int z = 0; z < size; z++)
			{
				chunk.blocks[x][0][z] = bedrock;
				if (randBool(0.8f)) chunk.blocks[x][1][z] = bedrock;
				if (randBool(0.5f)) chunk.blocks[x][2][z] = bedrock;
				if (randBool(0.2f)) chunk.blocks[x][3][z] = bedrock;		
			}
		}
	}
	
	private void worldGen(World world)
	{		
		final float size = World.defaultSize*World.defaultSize;
		
		StrutBuilder build = new StrutBuilder();
		Structure house = getHouse(build);
		house.fillGround = true;
		house.findGenAt(world, 0, World.CENTER, World.CENTER);
		
		Structure well = getWell(build);
		int x1 = 0; //bSize/2;
		int z1 = 0; //bSize/2;
		int loop = floor(size*0.01f); // 0.1
		for (int i = 0; i < loop; i++) {
			well.findGenAt(world, -4, rand.nextInt(World.LENGHT)-x1, rand.nextInt(World.LENGHT)-z1, 22, 46);
		}
		
		build.begin(5, 6, 5);
		build.set(Blocks.STONEBRICK, 0, 5, 0);
		build.set(Blocks.STONEBRICK, 2, 5, 0);
		build.set(Blocks.STONEBRICK, 0, 5, 2);
		build.set(Blocks.STONEBRICK, 2, 5, 4);
		build.set(Blocks.STONEBRICK, 4, 5, 2);
		build.set(Blocks.STONEBRICK, 4, 5, 0);
		build.set(Blocks.STONEBRICK, 0, 5, 4);
		build.set(Blocks.STONEBRICK, 4, 5, 4);
		build.fill(Blocks.STONEBRICK, 0, 0, 0, 4, 4, 4);
		build.fill(Blocks.AIR, 1, 1, 1, 3, 3, 3);
		build.fill(Blocks.AIR, 2, 1, 0, 2, 2, 0);
		build.fill(Blocks.AIR, 0, 1, 2, 0, 2, 2);
		build.fill(Blocks.AIR, 2, 1, 4, 2, 2, 4);
		build.fill(Blocks.AIR, 4, 1, 2, 4, 2, 2);
		build.fill(Blocks.LOG, 0, 0, 0, 0, 4, 0);
		build.fill(Blocks.LOG, 4, 0, 0, 4, 4, 0);
		build.fill(Blocks.LOG, 0, 0, 4, 0, 4, 4);
		build.fill(Blocks.LOG, 4, 0, 4, 4, 4, 4);
		Structure base = build.end();
		base.fillGround = true;
		
		build.begin(7, 10, 7);
		build.fill(Blocks.STONEBRICK, 2, 5, 2, 4, 5, 4);
		build.fill(Blocks.STONEMOSS, 2, 6, 2, 4, 8, 4);
		build.fill(Blocks.STONEBRICK, 0, 0, 0, 6, 4, 6);
		build.fill(Blocks.AIR, 3, 3, 3, 3, 8, 3);
		build.fill(Blocks.AIR, 2, 9, 2, 4, 9, 4);
		build.fill(Blocks.AIR, 1, 1, 1, 5, 3, 5);
		build.set(Blocks.COPPER, 1, 1, 1);
		build.set(Blocks.COPPER, 1, 2, 1);
		build.set(Blocks.COPPER, 1, 1, 2);
		build.set(Blocks.GOLD, 5, 1, 5);
		build.set(Blocks.GOLD, 4, 1, 5);
		Structure dungon = build.end();
		
		loop = floor(size*0.01f); // 0.1
		for (int i = 0; i < loop; i++) {
			int x = rand.nextInt(World.LENGHT)-x1;
			int z = rand.nextInt(World.LENGHT)-z1;
			dungon.findGenAt(world, -8, x, z, 23, 35);
		}
		
		loop = floor(size*0.02f); // 0.1
		for (int i = 0; i < loop; i++) {
			int x = rand.nextInt(World.LENGHT)-x1;
			int z = rand.nextInt(World.LENGHT)-z1;
			float num = biomes.GetPerlinFractal(x, z);
			if (num < -0.48d) {
				base.findGenAt(world, 0, x, z, 30, 70);
			} else base.findGenAt(world, 0, x, z, 45, 70);
		}
		
		
		loop = floor(size*3.2f); // 3.2f
		for (int i = 0; i < loop; i++) {
			PlentPatch.create(world, 0.09f, 4, rand.nextInt(5)+4, rand.nextInt(World.LENGHT)-x1, rand.nextInt(World.LENGHT)-z1);
		}
		
		loop = floor(size*0.9f); // 2000
		for (int i = 0; i < loop; i++) {
			PlentPatch.createSrub(world, 4, rand.nextInt(5)+4, rand.nextInt(World.LENGHT)-x1, rand.nextInt(World.LENGHT)-z1);
		}
		
		loop = floor(size*0.35f); // 0.42
		for (int i = 0; i < loop; i++) {
			Tree.create2(world, 6 + rand.nextInt(3), 5, rand.nextInt(World.LENGHT)-x1, rand.nextInt(World.LENGHT)-z1);
		}
	}
	
	private static int floor(float a) {
		return MathUtils.floor(a);
	}
	
	static Structure getHouse(StrutBuilder build) {
		build.begin(7, 7, 7);
		build.fill(Blocks.WOOD, 0, 0, 0, 6, 4, 6);
		build.fill(Blocks.AIR, 1, 1, 1, 5, 3, 5);
		build.fill(Blocks.COBSTONE, 0, 1, 0, 0, 5, 0);
		build.fill(Blocks.COBSTONE, 6, 1, 0, 6, 5, 0);
		build.fill(Blocks.COBSTONE, 0, 1, 6, 0, 5, 6);
		build.fill(Blocks.COBSTONE, 6, 1, 6, 6, 5, 6);
		build.fill(Blocks.BRICK, 0, 5, 0, 6, 5, 6);
		build.fill(Blocks.BRICK, 1, 6, 1, 5, 6, 5);
		build.set(Blocks.AIR, 3, 2, 0);
		build.set(Blocks.AIR, 3, 1, 0);
		build.set(Blocks.AIR, 0, 2, 3);
		build.set(Blocks.AIR, 0, 1, 3);
		build.set(Blocks.AIR, 6, 2, 3);
		build.set(Blocks.AIR, 6, 1, 3);
		build.set(Blocks.AIR, 3, 2, 6);
		build.set(Blocks.AIR, 3, 1, 6);
		return build.end();
	}
	
	static Structure getWell(StrutBuilder build) {
		build.begin(4, 10, 4);
		build.fill(Blocks.COBSTONE, 0, 8, 0, 3, 8, 3);
		build.fill(Blocks.COBSTONE, 1, 9, 1, 2, 9, 2);
		build.fill(Blocks.COBSTONE, 0, 0, 0, 3, 5, 3);
		build.fill(Blocks.LOG, 0, 5, 0, 0, 7, 0);
		build.fill(Blocks.LOG, 3, 5, 0, 3, 7, 0);
		build.fill(Blocks.LOG, 0, 5, 3, 0, 7, 3);
		build.fill(Blocks.LOG, 3, 5, 3, 3, 7, 3);
		build.fill(Blocks.WATER, 1, 1, 1, 2, 4, 2);
		build.fill(Blocks.AIR, 1, 5, 1, 2, 5, 2);
		return build.end();
	}
	
	/** Returns true if a random value between 0 and 1 is less than the specified value. */
	private boolean randBool(float chance) {
		return rand.nextFloat() < chance;
	}
}
