package com.andedit.arcubit.world;

import static com.badlogic.gdx.Gdx.gl;

import java.util.ArrayList;
import java.util.ListIterator;

import com.andedit.arcubit.graphic.MeshVert;
import com.andedit.arcubit.graphic.mesh.MeshBuilder;
import com.andedit.arcubit.graphic.mesh.VertConsumer;
import com.andedit.arcubit.graphic.vertex.Vertex;
import com.andedit.arcubit.util.BlockPos;
import com.andedit.arcubit.util.Util;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public final class Chunk implements Disposable {
	public static final int SIZE = 16;
	public static final int MASK = SIZE-1;
	
	private final ArrayList<Vertex> vertices;
	private final Renderer render;
	private final short x, y, z;
	private int count;
	
	public Chunk(Renderer render, int x, int y, int z) {
		this.render = render;
		vertices = new ArrayList<>(2);
		this.x = (short) x;
		this.y = (short) y;
		this.z = (short) z;
	}
	
	/** @return is empty after build. */
	public boolean build(World world, MeshBuilder builder) {
		int xPos = x<<4;
		int yPos = y<<4;
		int zPos = z<<4;
		var pos = new BlockPos();
		for (int x = 0; x < SIZE; x++)
		for (int y = 0; y < SIZE; y++)
		for (int z = 0; z < SIZE; z++) {
			pos.set(x+xPos, y+yPos, z+zPos);
			world.getBlock(pos).getRender().build(builder, world, pos);
		}
		
		// this looks bad.
		boolean isEmpty = builder.size() == 0;
		build(builder);
		return isEmpty;
	}
	
	public boolean isVisible(final Plane[] planes) {
		final int s = planes.length;
		final float x, y, z;
		x = (this.x<<4)+8;
		y = (this.y<<4)+8;
		z = (this.z<<4)+8;

		for (int i = 2; i < s; i++) {
			final Plane plane = planes[i];
			final Vector3 normal = plane.normal;
			final float dist = normal.dot(x, y, z) + plane.d;
			
			final float radius = 
			8f * Math.abs(normal.x) +
			8f * Math.abs(normal.y) +
			8f * Math.abs(normal.z);

			if (dist < radius && dist < -radius) {
				return false;
			}
		}
		return true;
	}
	
	public void render() {
		for (var vertex : vertices) {
			vertex.bind();
			gl.glDrawElements(GL20.GL_TRIANGLES, (vertex.size() / MeshVert.byteSize) * 6, GL20.GL_UNSIGNED_SHORT, 0);
			if (!Util.isGL30()) {
				vertex.unbind();
			}
		}
	}
	
	public void build(VertConsumer consumer) {
		var it = vertices.listIterator(consumer.build(vertices, () -> Vertex.newVbo(MeshVert.context, GL20.GL_STREAM_DRAW)));
        while (it.hasNext()) {
            it.next().dispose();
            it.remove();
        }
	}
	
	public boolean isEmpty() {
		return vertices.isEmpty();
	}
	
	public boolean pass(GridPoint3 pos, int offset) {
		final int rad = Renderer.RADIUS + offset;
		return x < (-rad)+pos.x || y < (-rad)+pos.y || z < (-rad)+pos.z || x > rad+pos.x || y > rad+pos.y || z > rad+pos.z;
	}

	@Override
	public void dispose() {
		vertices.forEach(Vertex::dispose);
		if (!isEmpty()) setDirty(true);
	}
	
	public boolean equals(short x, short y, short z) {
		return this.x == x && this.y == y && this.z == z;
	}
	
	@Override
	public int hashCode() {
		return 29 * z + 1721 * x + 95713 * y;
	}
	
	void setDirty(boolean isDirty) {
		render.setDirt(isDirty, x, y, z);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true; 
		if (obj == null) return false;
		if (obj.getClass() == Chunk.class) {
			var c = (Chunk)obj;
			return equals(c.x, c.y, c.z);
		}
		return false;
	}
}
