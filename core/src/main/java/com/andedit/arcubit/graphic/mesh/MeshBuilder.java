package com.andedit.arcubit.graphic.mesh;

import java.util.ArrayList;

import com.andedit.arcubit.block.model.BlockModel.Quad;

/** An extension of MeshComsumer for block mesh builder. */
public class MeshBuilder extends MeshConsumer {
	/** A cached ArrayList instance for storing quads temporary. */
	public final ArrayList<Quad> quads;
	
	public MeshBuilder() {
		quads = new ArrayList<>();
	}
}
