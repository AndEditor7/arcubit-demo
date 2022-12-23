package com.andedit.arcubit.graphic.mesh;

import java.util.List;
import java.util.function.Supplier;

import com.andedit.arcubit.graphic.MeshVert;
import com.andedit.arcubit.graphic.QuadIndex;
import com.andedit.arcubit.graphic.vertex.Vertex;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;

// v3-----v2
// |       |
// |       |
// v4-----v1
/** pos,uv,lit */
public class MeshConsumer implements VertConsumer {
	private final FloatArray array = new FloatArray(512);
	
	/** light */
	protected float dat = Color.toFloatBits(1f, 0, 0, 0);
	
	private final TextureRegion defReg = new TextureRegion();
	protected TextureRegion region = defReg;
	
	public void setReg(TextureRegion region) {
		this.region = region;
	}
	
	public void setLight(float light) {
		dat = toData(light);
	}
	
	public TextureRegion region() {
		return region = defReg;
	}
	
	public void vert1(float x, float y, float z) {
		array.add(x, y, z);
		array.add(region.getU2(), region.getV2(), dat);
	}
	
	public void vert2(float x, float y, float z) {
		array.add(x, y, z);
		array.add(region.getU2(), region.getV(), dat);
	}
	
	public void vert3(float x, float y, float z) {
		array.add(x, y, z);
		array.add(region.getU(), region.getV(), dat);
	}
	
	public void vert4(float x, float y, float z) {
		array.add(x, y, z);
		array.add(region.getU(), region.getV2(), dat);
	}
	
	public void vert(Vector3 pos, Vector2 uv) {
		array.add(pos.x, pos.y, pos.z);
		array.add(uv.x, uv.y, dat);
	}
	
	public void vert(float x, float y, float z, float u, float v) {
		array.add(x, y, z);
		array.add(u, v, dat);
	}
	
	public void vert(float x, float y, float z, float u, float v, float dat) {
		array.add(x, y, z);
		array.add(u, v, dat);
	}

	@Override
	public void pos(float x, float y, float z) {
		array.add(x, y, z);
	}

	@Override
	public void uv(float u, float v) {
		array.add(u, v);
	}
	
	public void lit(float lit) {
		array.add(toData(lit));
	}
	
	public void dat(float dat) {
		array.add(dat);
	}

	@Override
	public void val(float val) {
		lit(val);
	}
	
	@Override
	public int size() {
		return array.size;
	}

	@Override
	public void build(Vertex vertex) {
		vertex.setVertices(array.items, size(), 0);
		array.clear();
	}
	
	@Override
	public int build(List<Vertex> list, Supplier<Vertex> supplier) {
		int size = size();
		int max = QuadIndex.maxVertex * MeshVert.floatSize;
		int off = 0;
		int itr = 0;
		while (size != 0) {
			final Vertex vertex;
			if (itr >= list.size()) {
				vertex = supplier.get();
				list.add(vertex);
			} else vertex = list.get(itr);
			
			int min = Math.min(size, max);
			vertex.setVertices(array.items, min, off);
			size -= min;
			off += min;
			itr++;
		}
		array.clear();
		return itr;
	}
	
	public static float toData(float light) {
		return Color.toFloatBits(light, 0, 0, 0);
	}
}
