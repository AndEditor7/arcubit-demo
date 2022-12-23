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

public class AirRender implements BlockRender {
	public static final AirRender INSTANCE = new AirRender();
	
	@Override
	public void build(MeshBuilder consumer, World world, BlockPos pos) {
		
	}

	@Override
	public void build(MeshConsumer consumer, MatrixStack stack) {

	}

	@Override
	public void getQuads(List<Quad> list) {
		
	}

	@Override
	public void getBoxes(List<BoundingBox> list) {
		
	}
}
