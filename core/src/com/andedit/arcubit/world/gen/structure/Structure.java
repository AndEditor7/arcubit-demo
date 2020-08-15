package com.andedit.arcubit.world.gen.structure;

import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.world.World;

public class Structure 
{
	public int xSize;
	public int ySize;
	public int zSize;
	
	public boolean xCenter;
	public boolean yCenter;
	public boolean zCenter;
	
	public boolean fillGround;
	
	protected byte[][][] data;
	
	public Structure(int xSize, int ySize, int zSize) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		data = new byte[xSize][ySize][zSize];
		//Arrays.fill(data, Blocks.Air);
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				for (int z = 0; z < xSize; z++) {
					data[x][y][z] = -1;
				}
			}
		}
		xCenter = true;
		yCenter = false;
		zCenter = true;
	}
	
	protected Structure(int xSize, int ySize, int zSize, byte[][][] data) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.data = data.clone();
		xCenter = true;
		yCenter = false;
		zCenter = true;
	}
	
	public void findGenAt(World world, int height ,int xPos, int zPos) {
		findGenAt(world, height, xPos, zPos, 0, ChunkRegion.LENGTH*Chunk.SIZE);
	}
	
	public void findGenAt(World world, int height ,int xPos, int zPos, int yMin, int yMax) 
	{
		yMin--; yMax--;
		int x = xPos;
		int z = zPos;
		if (!xCenter) {
			x -= (xSize/2);
		}
		if (!zCenter) {
			z -= (zSize/2);
		}
		
		for (int y = yMax; y > yMin; y--) {
			if (world.getBlock(x, y, z) != 0) {
				if (yMax == y) return;
				genAt(world, xPos, y+height, zPos);
				return;
			}
		}
	}
	
	public void genAt(World world, int xPos, int yPos, int zPos) 
	{		
		if (xCenter) {
			xPos -= (xSize/2);
		}
		if (yCenter) {
			yPos -= (ySize/2);
		}
		if (zCenter) {
			zPos -= (zSize/2);
		}
		
		for (int x = 0; x < xSize; x++) 
		{
			for (int z = 0; z < xSize; z++) 
			{
				for (int y = 0; y < ySize; y++) 
				{
					byte b = data[x][y][z];
					if (b == -1) continue;
					world.setBlock(x+xPos, y+yPos, z+zPos, b);
					if (fillGround && y == 0) {
						for (int i = yPos-1; i > -1; i--) {
							if (world.getBlock(x+xPos, i, z+zPos) == 0) {
								world.setBlock(x+xPos, i, z+zPos, b);
							} else break;
						}
					}
				}
			}
		}
	}
}
