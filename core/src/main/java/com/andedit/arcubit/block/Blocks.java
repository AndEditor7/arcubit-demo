package com.andedit.arcubit.block;

import com.andedit.arcubit.util.Registry;
import com.badlogic.gdx.utils.Array;

public class Blocks {
	private static Array<Block> BLOCKS = new Array<>();
	
	public static final Block
	AIR = register("air", new AirBlock()),
	STONE = register("stone", new Block(Block.newSettings()));
	
	public static Block register(String id, Block block) {
		Registry.BLOCKS.add(id, block);
		block.id = BLOCKS.size;
		BLOCKS.add(block);
		return block;
	}
	
	public static Block get(int data) {
		return BLOCKS.get(data & 0xFF);
	}
}
