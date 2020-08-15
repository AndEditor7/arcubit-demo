package com.andedit.arcubit.chunk.loader;

import com.andedit.arcubit.block.Block;
import com.andedit.arcubit.block.BlockType;
import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.mesh.ChunkMesh;
import com.andedit.arcubit.mesh.builders.PlantBuilder;
import com.andedit.arcubit.mesh.builders.TerrainBuilder;
import com.andedit.arcubit.mesh.builders.WaterBuilder;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.utils.Disposable;

import static com.andedit.arcubit.block.Blocks.canAddFace;

public class ChunkBuilderThreaded
{
	private final World world;
	
	private final TerrainBuilder tBuild = new TerrainBuilder();
	private final PlantBuilder   pBuild = new PlantBuilder();
	private final WaterBuilder   wBuild = new WaterBuilder();
	
	private final VolatileMeshPacket packet = new VolatileMeshPacket();
	
	public ChunkBuilderThreaded(final World world) {
		this.world = world;
	}
	
	public VolatileMeshPacket all(final Chunk chunk, VolatileMeshPacket packet)
	{
		final ChunkRegion tempNorth = world.getChunkRegion(chunk.x, chunk.z+1);
		final ChunkRegion tempSouth = world.getChunkRegion(chunk.x, chunk.z-1);
		final ChunkRegion tempEast = world.getChunkRegion(chunk.x+1, chunk.z);
		final ChunkRegion tempWest = world.getChunkRegion(chunk.x-1, chunk.z);
		final ChunkRegion temp = world.getChunkRegion(chunk.x, chunk.z);
		
		final int size = Chunk.SIZE;
		final int maskSize = size-1;
		final int sizeX = chunk.x*size;
		final int sizeY = chunk.y*size;
		final int sizeZ = chunk.z*size;
		final byte[][][] blocks = chunk.blocks;
		
		for (int x = 0; x < size; x++)
		{
			for (int y = 0; y < size; y++)
			{
				for (int z = 0; z < size; z++)
				{
					final byte id = blocks[x][y][z];
					if (id == Blocks.AIR) continue;
					final Block block = Blocks.blocks[id];
					
					if (block.type == BlockType.PLANT) {
						pBuild.build(block, x+sizeX, y+sizeY, z+sizeZ);
						continue;
					}
					
					final boolean isWater = id == Blocks.WATER;
					
					// check south Z-
					if (z-1 == -1)
					{
						if (tempSouth != null) {
							if (canAddFace(block, tempSouth.chunks[chunk.y].blocks[x][y][z+maskSize])) {
								if (isWater) {
									wBuild.bSouth(block, x+sizeX, y+sizeY, z+sizeZ);
								} else {
									tBuild.bSouth(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
								}
							}
						}
					}  else {
						if (canAddFace(block, blocks[x][y][z-1])) {
							if (isWater) {
								wBuild.bSouth(block, x+sizeX, y+sizeY, z+sizeZ);
							} else {
								tBuild.bSouth(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
							}
						}
					}
					
					// check north Z+
					if (z+1 == size)
					{
						if (tempNorth != null) {
							if (canAddFace(block, tempNorth.chunks[chunk.y].blocks[x][y][z-maskSize])) {
								if (isWater) {
									wBuild.bNorth(block, x+sizeX, y+sizeY, z+sizeZ);
								} else {
									tBuild.bNorth(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
								}
							}
						}
					} else if (canAddFace(block, blocks[x][y][z+1])) {
						if (isWater) {
							wBuild.bNorth(block, x+sizeX, y+sizeY, z+sizeZ);
						} else {
							tBuild.bNorth(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
						}
					}
					
					// check west X-
					if (x-1 == -1)
					{
						if (tempWest != null) {
							if (canAddFace(block, tempWest.chunks[chunk.y].blocks[x+maskSize][y][z])) {
								if (isWater) {
									wBuild.bWest(block, x+sizeX, y+sizeY, z+sizeZ);
								} else {
									tBuild.bWest(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
								}
							}
						}
					} else if (canAddFace(block, blocks[x-1][y][z])) {
						if (isWater) {
							wBuild.bWest(block, x+sizeX, y+sizeY, z+sizeZ);
						} else {
							tBuild.bWest(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
						}
					}
					
					// check east X+
					if (x+1 == size)							
					{
						if (tempEast != null)	{
							if (canAddFace(block, tempEast.chunks[chunk.y].blocks[x-maskSize][y][z])) {
								if (isWater) {
									wBuild.bEast(block, x+sizeX, y+sizeY, z+sizeZ);
								} else {
									tBuild.bEast(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
								}
							}
						}
					} else if (canAddFace(block, blocks[x+1][y][z])) {
						if (isWater) {
							wBuild.bEast(block, x+sizeX, y+sizeY, z+sizeZ);
						} else {
							tBuild.bEast(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
						}
					}				
					
					// check up Y+
					if (y+1 == size)
					{
						Chunk chunk1 = temp.getChunk(chunk.y+1);
						if (chunk1 != null) {
							if (canAddFace(block, chunk1.blocks[x][y-maskSize][z])) {
								if (isWater) {
									wBuild.bTop(block, x+sizeX, y+sizeY, z+sizeZ);
								} else {
									tBuild.bTop(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
								}
							}								
						}
					} else if (canAddFace(block, blocks[x][y+1][z])) {
						if (isWater) {
							wBuild.bTop(block, x+sizeX, y+sizeY, z+sizeZ);
						} else {
							tBuild.bTop(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
						}
					}
					
					// check down Y-
					if (y-1 == -1)
					{
						Chunk chunk1 = temp.getChunk(chunk.y-1);
						if (chunk1 != null) {
							if (canAddFace(block, chunk1.blocks[x][y+maskSize][z])) {
								if (isWater) {
									wBuild.bBottem(block, x+sizeX, y+sizeY, z+sizeZ);
								} else {
									tBuild.bBottem(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
								}
							}
						}
					} else if (canAddFace(block, blocks[x][y-1][z])) {
						if (isWater) {
							wBuild.bBottem(block, x+sizeX, y+sizeY, z+sizeZ);
						} else {
							tBuild.bBottem(block, chunk, x+sizeX, y+sizeY, z+sizeZ);
						}
					}
				}
			}
		}
		
		packet = packet == null ? this.packet : packet;
		packet.terrain = tBuild.create(chunk);
		packet.plant = pBuild.create(chunk);
		packet.water = wBuild.create(chunk);
		packet.chunk = chunk;
		return packet;
	}
	
	public static class VolatileMeshPacket implements Disposable
	{
		public volatile ChunkMesh terrain, plant, water;
		public volatile Chunk chunk;
		
		public boolean isEmpty() {
			return terrain == null && plant == null && water == null;
		}

		@Override
		public void dispose() {
			if (terrain != null) terrain.dispose();
			if (plant != null) plant.dispose();
			if (water != null) water.dispose();
			if (chunk != null) {
				chunk.setNewChunk(true);
			}
		}
	}
}
