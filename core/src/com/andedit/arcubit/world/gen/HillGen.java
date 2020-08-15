package com.andedit.arcubit.world.gen;

import static com.badlogic.gdx.math.MathUtils.floor;

import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.util.math.OpenSimplex2S;
import com.andedit.arcubit.world.World;
import com.andedit.arcubit.world.gen.features.Lands;
import com.andedit.arcubit.world.gen.features.Tree;
import com.andedit.arcubit.world.gen.structure.Structure;
import com.andedit.arcubit.world.gen.structure.StrutBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;

public class HillGen implements Generator 
{
	private final RandomXS128 rand;
	private final OpenSimplex2S hill;
	private final OpenSimplex2S base;
	private final OpenSimplex2S rock;
	private final OpenSimplex2S flat;
	
	public HillGen() {
		rand = new RandomXS128();
		
		base = new OpenSimplex2S(rand.nextLong());
		hill = new OpenSimplex2S(rand.nextLong());
		rock = new OpenSimplex2S(rand.nextLong());
		flat = new OpenSimplex2S(rand.nextLong());
	}
	
	@Override
	public void gen(World world) {
		for (int x = 0; x < World.defaultSize; x++)
		{
			for (int z = 0; z < World.defaultSize; z++)
			{
				final ChunkRegion region = world.regions[x][z];
				genRegion(region);
				Lands.slowGen(region, null);
			}
		}
		
		worldGen(world);
	}

	private void worldGen(World world) {
		final float size = World.defaultSize*World.defaultSize;
		
		StrutBuilder build = new StrutBuilder();
		Structure house = FlatGen.getHouse(build);
		house.fillGround = true;
		house.findGenAt(world, 0, World.CENTER, World.CENTER);
		
		Structure well = FlatGen.getWell(build);
		int x1 = 0; //bSize/2;
		int z1 = 0; //bSize/2;
		int loop = floor(size*0.02f); // 0.1
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
		
		loop = floor(size*0.014f); // 0.1
		for (int i = 0; i < loop; i++) {
			int x = rand.nextInt(World.LENGHT)-x1;
			int z = rand.nextInt(World.LENGHT)-z1;
			dungon.findGenAt(world, -8, x, z, 23, 35);
		}
		
		
		 /*
		loop = floor(size*3.2f); // 3.2f
		for (int i = 0; i < loop; i++) {
			PlentPatch.create(world, 0.09f, 4, rand.nextInt(5)+4, rand.nextInt(World.LENGHT)-x1, rand.nextInt(World.LENGHT)-z1);
		}
		
		loop = floor(size*0.9f); // 2000
		for (int i = 0; i < loop; i++) {
			PlentPatch.createSrub(world, 4, rand.nextInt(5)+4, rand.nextInt(World.LENGHT)-x1, rand.nextInt(World.LENGHT)-z1);
		} */
		
		loop = floor(size*0.35f); // 0.42
		for (int i = 0; i < loop; i++) {
			Tree.create2(world, 6 + rand.nextInt(3), 5, rand.nextInt(World.LENGHT)-x1, rand.nextInt(World.LENGHT)-z1);
		}
	}
	
	private static final double size = 150.0; // 128.0
	private static final double scale = 100.0; // 50.0
	private static final double heightScl = 0.5; // 0.8
	private static final double depth = 16.0; // 16.0
	public static final double height = 32.0; // 46.0

	private void genRegion(ChunkRegion region) {
		final Chunk[] chunks = region.chunks;
		final int xOffset = region.xR*Chunk.SIZE;
		final int zOffset = region.zR*Chunk.SIZE;
		for (int x = 0; x < Chunk.SIZE; x++) {
			int realX = x+xOffset;
		for (int z = 0; z < Chunk.SIZE; z++) {
			int realZ = z+zOffset;
			double lump = rock.noise2(realX/32d, realZ/32d)*4d;
			double raw = base.noise2(realX/size, realZ/size);
			double preScl = ((flat.noise2(realX/size, realZ/size)+1d)*0.5)*MathUtils.clamp(raw, 0d, 1d);
			double scl = preScl*scale;
			for (int y = 0; y < ChunkRegion.HEIGHT; y++) {
				//double noise3d = scl < 0.01 ? 0d : fast.GetPerlin(realX/26f, y/18f, realZ/26f);
				double noise3d = scl < 0.01 ? 0d : hill.noise3_XZBeforeY(realX/32d, y/24d, realZ/32d);
				noise3d *= scl;
				noise3d += lump+height+(scl*heightScl)+(MathUtils.clamp(raw, -1d, 0d)*depth);
				if (noise3d < y) continue;
				chunks[y>>>4].blocks[x][y&15][z] = Blocks.STONE;
			}
		}}
	}
}
