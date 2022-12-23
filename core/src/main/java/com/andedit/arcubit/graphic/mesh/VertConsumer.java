package com.andedit.arcubit.graphic.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.andedit.arcubit.graphic.vertex.Vertex;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface VertConsumer {
	/** Append position. */
	void pos(float x, float y, float z);
	/** Append position. */
	default void pos(Vector3 pos) {
		pos(pos.x, pos.y, pos.z);
	}
	/** Append texture coordinate (UV). */
	void uv(float u, float v);
	/** Append texture coordinate (UV). */
	default void uv(Vector2 uv) {
		uv(uv.x, uv.y);
	}
	/** Append attribute value. */
	void val(float val);
	
	/** @return vertex size of floats appended. */
	int size();
	
	/** Build vertex and clear/reset the consumer. */
	void build(Vertex vertex);
	
	/** Build vertex and clear/reset the consumer. */
	int build(List<Vertex> vertices, Supplier<Vertex> supplier);
}
