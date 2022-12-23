package com.andedit.arcubit.block.model;

import java.util.List;

import com.andedit.arcubit.block.model.BlockModel.Quad;
import com.andedit.arcubit.graphic.mesh.MeshBuilder;
import com.andedit.arcubit.graphic.mesh.MeshConsumer;
import com.andedit.arcubit.util.BlockPos;
import com.andedit.arcubit.util.Facing;
import com.andedit.arcubit.util.MatrixStack;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.math.collision.BoundingBox;

public interface BlockRender {
	
	static float getShade(Facing face) {
		return switch (face) {
		case UP -> 1.0f;
		case NORTH, SOUTH -> 0.86f;
		case EAST, WEST -> 0.75f;
		case DOWN -> 0.7f;
		default -> 1.0f;
		};
	}
	
	void build(MeshBuilder consumer, World world, BlockPos pos);
	void build(MeshConsumer consumer, MatrixStack stack);
	void getQuads(List<Quad> list);
	void getBoxes(List<BoundingBox> list);
}
