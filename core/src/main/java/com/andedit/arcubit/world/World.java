package com.andedit.arcubit.world;

import com.andedit.arcubit.block.Block;
import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.util.BlockPos;
import com.badlogic.gdx.utils.Disposable;

public class World {
	public static final int SIZE = 256;
	public static final int HEIGHT = 128;
	public static final int LENGHT = SIZE * HEIGHT * SIZE;
	
	private final byte[] data = new byte[LENGHT];

	public Block getBlock(BlockPos pos) {
		return getBlock(pos.x, pos.y, pos.z);
	}
	
	public Block getBlock(int x, int y, int z) {
		return Blocks.get(getData(x, y, z));
	}
	
	public void setBlock(Block block, BlockPos pos) {
		setBlock(block, pos.x, pos.y, pos.z);
	}
	
	public void setBlock(Block block, int x, int y, int z) {
		setData(block.getId(), x, y, z);
	}
	
	public byte getData(int i) {
		return data[i];
	}
	
	public int getData(int x, int y, int z) {
		if (y >= HEIGHT) {
			return Blocks.AIR.getId();
		}
		if (y < 0 || x < 0 || z < 0 || x >= SIZE || z >= SIZE) {
			return Blocks.AIR.getId(); // TODO: barrier block
		}
		return data[getIndex(x, y, z)] & 0xFF;
	}
	
	public void setData(int data, int x, int y, int z) {
		if (isOutBound(x, y, z)) return;
		this.data[getIndex(x, y, z)] = (byte)data;
	}
	
	public static int getIndex(int x, int y, int z) {
		return x + (y * SIZE) + (z * SIZE  * HEIGHT);
	}
	
	public static boolean isOutBound(int x, int y, int z) {
		return x < 0 || y < 0 || z < 0 || x >= SIZE || y >= HEIGHT || z >= SIZE;
	}
	
	public static boolean isOutBound(BlockPos pos) {
		return isOutBound(pos.x, pos.y, pos.z);
	}
}
