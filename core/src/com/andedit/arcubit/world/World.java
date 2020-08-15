package com.andedit.arcubit.world;

import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.particles.batchs.IParticleSystem;
import com.andedit.arcubit.particles.threads.ParticleMultiThreaded;
import com.andedit.arcubit.util.BlockPos;
import com.andedit.arcubit.util.Camera;
import com.andedit.arcubit.util.Util;
import com.andedit.arcubit.world.gen.FlatGen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public class World implements Disposable
{
	public static World world;
	
	public static final int defaultSize = 64; // 64
	public static final int LENGHT = defaultSize*Chunk.SIZE;
	public static final int CENTER = LENGHT/2;
	
	public final ChunkRegion[][] regions;
	
	private WorldRenderer render;
	public IParticleSystem parts;
	
	public final BlockEdit editable = new BlockEdit(this);
	
	public World(boolean gen) {
		world = this;
		regions = new ChunkRegion[defaultSize][defaultSize];
		for (int x = 0; x < defaultSize; x++)
		for (int z = 0; z < defaultSize; z++) {
			regions[x][z] = new ChunkRegion(this, x, z);
		}
		if (gen) {
			new FlatGen().gen(this);
			/* TODO: height lighting disabled.
			for (int x = 0; x < defaultSize; x++)
			{
				for (int z = 0; z < defaultSize; z++)
				{
					regions[x][z].reLighting();;
				}
			} */
		}
		
	}
	
	public void intsRender(Camera cam, int maxDis) {
		render = new WorldRenderer(this, maxDis);
		parts = new ParticleMultiThreaded(cam);
	}
	
	public void render(Camera cam) {
		WorldRenderer.indices.bind();
		render.render(cam);
		parts.render();
		WorldRenderer.indices.unbind();
	}
	
	public ChunkRegion getChunkRegion(int x, int z) {
		if (x < 0 || z < 0 || x >= defaultSize || z >= defaultSize) return null;
		return regions[x][z];
	}
	
	public byte getBlock(int x, int y, int z) {
		if (y < 0 || y >= ChunkRegion.HEIGHT) return Blocks.AIR;
		ChunkRegion region = getChunkRegion(x>>4, z>>4);
		return region == null ? Blocks.AIR : region.getBlock(x, y, z);
	}
	
	public void setBlock(int x, int y, int z, byte id) {
		if (y < 0 || y >= ChunkRegion.HEIGHT) return;
		ChunkRegion region = getChunkRegion(x>>4, z>>4);
		if (region == null) return;
		region.setBlock(x, y, z, id);
	}
	
	/** */
	public void forceDirty() {
		render.forceDirty = true;
	}

	public byte getBlock(float x, float y, float z) {
		return getBlock(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z));
	}

	public void setBlock(BlockPos pos, byte block) {
		setBlock(pos.x, pos.y, pos.z, block);
	}

	public short getLight(int x, int z) {
		ChunkRegion region = getChunkRegion(x>>4, z>>4);
		return region == null ? 0 : region.getLight(x, z);
	}
	
	public Chunk getChunk(int x, int y, int z) {
		ChunkRegion region = getChunkRegion(x, z);
		return region == null ? null : region.getChunk(y);
	}
	
	public Chunk getChunkAt(int x, int y, int z) {
		return getChunk(x>>4, y>>4, z>>4);
	}
	
	@Override
	public void dispose() {
		Util.disposes(render, parts);
	}
}
