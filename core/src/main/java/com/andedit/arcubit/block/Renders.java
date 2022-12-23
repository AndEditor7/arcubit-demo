package com.andedit.arcubit.block;

import static com.andedit.arcubit.block.Blocks.*;

import com.andedit.arcubit.block.model.BlockModel;
import com.andedit.arcubit.block.model.BlockModel.Cube;
import com.andedit.arcubit.block.model.BlockModel.Quad;
import com.andedit.arcubit.util.Facing;
import com.andedit.arcubit.util.Pair;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** Block renderer */
public class Renders {
	private final Texture texture;
	
	private static Pair<BlockModel, Cube> cube() {
		var model = new BlockModel();
		return new Pair<>(model, model.cube(0, 0, 0, 16, 16, 16));
	}
	
	private TextureRegion reg(int x, int y) {
		return new TextureRegion(texture, x<<4, y<<4, 16, 16);
	}
	
	/** initialize block renderer */
	public Renders(Texture texture) {
		this.texture = texture;
		Pair<BlockModel, Cube> pair;
		BlockModel model;
		Cube cube;
		Quad quad;
		
		pair = cube();
		pair.right.regAll(reg(1,0));
		STONE.render = pair.left;
	}
}
