package com.andedit.arcubit.handle;

import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.renderer.TexLib;
import com.andedit.arcubit.util.Util;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import static com.andedit.arcubit.block.Blocks.blocks;

import com.andedit.arcubit.PartiBench;

/** Render the GUI. */
public final class GUI 
{
	public static int blockPick = 1;
	
	public static void ints() 
	{
		InputHolder.holder.add(new InputAdapter() {
			public boolean scrolled (int amount) {
				if (amount == 1) { // Left
					if (blockPick-1 < 1) return true;
					blockPick--;
				} if (amount == -1) { // Right
					if (blockPick+1 > Blocks.size-1) return true;
					blockPick++;
				}
				return false;
			}
		});
	}
	
	public static void render(SpriteBatch batch)
	{
		if (PartiBench.isBench) return;
		final int height = Util.world.h;
		final int id = blockPick;
		if (id-1 > 0) {
			batch.draw(blocks[id-1].textures.side, 26, height-72, 48, 48);
		}
		if (id+1 < Blocks.size) {
			batch.draw(blocks[id+1].textures.side, 38+96, height-72, 48, 48);
		}
		batch.draw(blocks[id].textures.side, 32+40, height-80, 64, 64);
		batch.draw(TexLib.cross, (Util.world.w/2f)-16f, (Util.world.h/2f)-16f, 32f, 32f);
	}
}
