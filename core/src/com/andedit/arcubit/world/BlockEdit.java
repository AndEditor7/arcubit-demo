package com.andedit.arcubit.world;

import com.andedit.arcubit.block.Block;
import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.util.BlockPos;

public final class BlockEdit 
{
	private final World world;

	public BlockEdit(final World world) {
		this.world = world;
	}

	public void breakBlock(final BlockPos in,  final Block block) {
		final Chunk chunk = world.getChunkAt(in.x, in.y, in.z);
		if (chunk != null) {
			chunk.editBlock(in.x, in.y, in.z, block);
			world.forceDirty();
		}
	}

	public void placeBlock(final BlockPos out, final Block block) {
		final Chunk chunk = world.getChunkAt(out.x, out.y, out.z);
		if (chunk != null) {
			chunk.editBlock(out.x, out.y, out.z, block);
			world.forceDirty();
		}
	}
}
