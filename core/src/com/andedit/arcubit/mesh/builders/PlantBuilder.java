package com.andedit.arcubit.mesh.builders;

import com.andedit.arcubit.block.Block;
import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.glutils.VertContext;
import com.andedit.arcubit.mesh.ChunkMesh;
import com.andedit.arcubit.mesh.verts.TerrainVert;
import com.andedit.arcubit.mesh.verts.VertInfo;
import com.andedit.arcubit.util.Shaders;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlantBuilder extends MeshBuilder 
{
	private static VertContext context = new VertContext() {
		public ShaderProgram getShader() {
			return Shaders.plant;
		}
		public VertexAttributes getAttrs() {
			return TerrainVert.attributes;
		}
		public int getLocation(int i) {
			return Shaders.locations[i];
		}
	};
	
	private final VertInfo v1, v2, v3, v4;
	
	public PlantBuilder() {
		v1 = new VertInfo();
		v2 = new VertInfo();
		v3 = new VertInfo();
		v4 = new VertInfo();
		
		v1.uv.set(0f, 1f);
		v2.uv.set(1f, 1f);
		v3.uv.set(1f, 0f);
		v4.uv.set(0f, 0f);
		
		v1.lit = 0f;
		v2.lit = 0f;
		v3.lit = 0.15f;
		v4.lit = 0.15f;
	}
	
	private void rect(TextureRegion region) {
		begin();
		setUVRange(region);
		vertex(v1.pos, v1.lit, v1.uv);
		vertex(v2.pos, v2.lit, v2.uv);
		vertex(v3.pos, v3.lit, v3.uv);
		vertex(v4.pos, v4.lit, v4.uv);
	}
	
	private void vertex(final Vector3 pos, final float lit, final Vector2 uv) {
		vertexs.add(pos.x, pos.y, pos.z, lit);
		vertexs.add(uOffset+uScale*uv.x, vOffset+vScale*uv.y);
	}
	
	public ChunkMesh create(Chunk chunk) {
		if (!isBuilding) return null;
		isBuilding = false;
		return new ChunkMesh(chunk, vertexs, context);
	}
	
	public void build(Block block, float x, int y, float z) {
		z += 0.5f;
		v1.pos.set(x, y, z);
		v2.pos.set(x+1f, y, z);
		v3.pos.set(x+1f, y+1, z);
		v4.pos.set(x, y+1, z);
		rect(block.textures.side);
		z -= 0.5f;
		x += 0.5f;
		v1.pos.set(x, y, z+1f);
		v2.pos.set(x, y, z);
		v3.pos.set(x, y+1, z);
		v4.pos.set(x, y+1, z+1f);
		rect(block.textures.side);
	}
}
