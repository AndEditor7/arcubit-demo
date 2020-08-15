package com.andedit.arcubit.chunk;

import static com.andedit.arcubit.block.Blocks.blocks;
import static com.andedit.arcubit.chunk.Chunk.SIZE;

import com.andedit.arcubit.world.World;

public class ChunkRegion 
{
	public static final int LENGTH = 6;
	public static final int HEIGHT = LENGTH*SIZE;
	
	final World world;
	
	public final int xR;
	public final int zR;
	
	public final Chunk[] chunks;
	
	public final byte[][] lightMap;
	public boolean needUpdate;
	public byte loopDown;
	
	public ChunkRegion(World world, int x, int z)
	{
		this.world = world;
		xR = x;
		zR = z;
		loopDown = -1;
		chunks = new Chunk[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			chunks[i] = new Chunk(this, x, i, z);
		}
		lightMap = new byte[SIZE][SIZE];
		needUpdate = false;
	}
	
	public short getLightMap(int x, int z) {
		if (needUpdate) reLighting();
		return lightMap[x][z];		
	}
	
	public void reLighting() 
	{
		byte h = (byte)HEIGHT-1;
		for (int x = 0; x < SIZE; x++)
		{
			for (int z = 0; z < SIZE; z++)
			{
				for (byte y = h; y > -1; y--)
				{
					if (blocks[getBlockChunkf(x, y, z)].isSoild) {
						lightMap[x][z] = y;
						break;
					}
				}
			}
		}
		needUpdate = false;
	}
	
	public byte getBlock(int x, int y, int z) {
		return chunks[y>>>4].blocks[x&15][y&15][z&15];
	}
	
	public void setBlock(int x, int y, int z, byte ID) {
		chunks[y>>>4].blocks[x&15][y&15][z&15] = ID;
	}
	
	public byte getBlockChunk(int x, int y, int z) {
		if (isUnsafe(y>>>4)) return 0;
		return chunks[y>>>4].getBlock(x, y&15, z);
	}
	
	public void setBlockChunk(int x, int y, int z, byte ID) {
		if (isUnsafe(y>>>4)) return;
		chunks[y>>>4].setBlock(x, y&15, z, ID);
	}
	
	public byte getBlockChunkf(int x, int y, int z) {
		return chunks[y>>>4].blocks[x][y&15][z];
	}
	
	public void setBlockChunkf(int x, int y, int z, byte ID) {
		chunks[y>>>4].blocks[x][y&15][z] = ID;
	}
	
	public boolean matches(int x, int z) {
		return x == xR && z == zR;
	}
	
	private boolean isUnsafe(int y) {
		return y < 0 || y >= LENGTH;
	}
	
	public Chunk getChunk(int y) {
		return isUnsafe(y) ? null : chunks[y];		
	}
	
	public void reLighting(int x, int z, boolean needCheck) 
	{
		x &= 15;
		z &= 15;
		if (needCheck) {
			for (byte y = (byte)(HEIGHT-1); y > -1; y--)
			{
				if (blocks[getBlockChunkf(x, y, z)].isSoild) {
					lightMap[x][z] = y;
					return;
				}
			}
		}
		
		boolean mode = true;
		for (byte y = (byte)HEIGHT-1; y > -1; y--)
		{
			if (mode) {
				if (blocks[getBlockChunkf(x, y, z)].isSoild) {
					lightMap[x][z] = y;
					mode = false;
					//return;
				}
			} else {
				if (blocks[getBlockChunkf(x, y, z)].isSoild) {
					loopDown = (byte)((y>>>4)-1);
					return;
				}
			}
		}
		loopDown = -1;
	}
	
	public byte getLight(int x, int z)	{
		return lightMap[x&15][z&15];
	}
}
