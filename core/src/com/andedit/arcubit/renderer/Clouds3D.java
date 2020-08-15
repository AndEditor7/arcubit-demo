package com.andedit.arcubit.renderer;

import com.andedit.arcubit.util.Camera;
import com.andedit.arcubit.util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx.graphics.glutils.VertexArray;
import com.badlogic.gdx.math.Vector3;

import static com.andedit.arcubit.Options.VBO;

import java.nio.ShortBuffer;

public class Clouds3D implements Clouds
{
	/** [x,y,z,l] */
	static final VertexAttributes attributes = new VertexAttributes(
			new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.Generic, 1, "a_light")
	);
	
	/** 16 bytes. */
	static final int byteSize = attributes.vertexSize;
	/** 4 floats */
	static final int floatSize = byteSize/4;
	
	private final float[] verts;
	private int idx = 0;
	private final int maxSize;
	
	private final VertexData vertex;
	private ShaderProgram shader;
	
	public Clouds3D() {
		shader = new ShaderProgram(Util.getFile("shaders/clouds.vert"), Util.getFile("shaders/clouds.frag"));
		
		final int size = 500; // 500 quad.
		final int vertexs = size*4; // 4 corners of the quad (vertices).
		
		maxSize = size*byteSize; // Max size/length of floats.
		verts = new float[maxSize]; // Allocate the array of floats.
		
		// Allocate the vertex array.
		if (VBO) {
			vertex = new VertexBufferObjectWithVAO(false, vertexs, attributes);
		} else {
			vertex = new VertexArray(vertexs, attributes);
		}
	}
	
	public float cloudPos = -FAR;
	
	private final Vector3 fixPos = new Vector3();
	
	@Override
	public void render(Camera cam, IndexData indices) {
		cloudPos += SPEED;
		if (cloudPos > FAR) {
			cloudPos = -FAR;
		}
		
		final float x1 = Math.round((cam.position.x+cloudPos)/SIZE);
		final float z1 = Math.round(cam.position.z/SIZE);
		
		fixPos.set(cam.position).add(-(SIZE/2f), 0f, SIZE/2f);
		final boolean isAbove = fixPos.y > HEIGHT+1.5f;
			
		for (float x = -DIST+x1; x < DIST+x1; x++)
		{
			float xFix = (x-(cloudPos/SIZE))*SIZE;
			for (float z = -DIST+z1; z < DIST+z1; z++)
			{				
				float zFix = (float)(z*SIZE);
				if (Clouds.getPerlin(x/SCALE, z/SCALE) > DENSE) { // 0.4f
					box(xFix, HEIGHT, zFix, 3f, isAbove);
					//rect(xFix, height, zFix, sizer);
				}
			}
		}
		
		// render
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		if (idx == 0) return;
		vertex.setVertices(verts, 0, idx);
		final int count = (idx / byteSize) * 6;
		
		shader.bind();
		shader.setUniformMatrix("u_projTrans", cam.combined);
		
		vertex.bind(shader);
		indices.bind();
		if (VBO) {
			Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, count, GL20.GL_UNSIGNED_SHORT, 0);
		} else {
			final ShortBuffer buffer = indices.getBuffer();
			buffer.limit(count);
			Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, count, GL20.GL_UNSIGNED_SHORT, buffer);
		}
		vertex.unbind(shader);
		
		// clear
		idx = 0;
	}
	
	@Override
	public void dispose() {
		vertex.dispose();
		shader.dispose();
	}
	
	public void box (float x, float y, float z, float height, boolean isAbove) {
		if (idx+96 >= maxSize) {
			Gdx.app.log("Clouds3D", "Out of vertexs!");
			return;
		}

		final float depth = -SIZE;
		final float width = SIZE;
		
		final float 
		lightHigh = 1.0f, // 1.0f;
		lightMed = 0.90f, // 0.86f
		lightLow = 0.80f, // 0.68f
		lightDim = 0.75f; // 0.6f
		
		if (z < fixPos.z) {
			// Front
			vertex(x, y, z);
			light(lightMed);
			vertex(x + width, y, z);
			light(lightMed);
			vertex(x + width, y + height, z);
			light(lightMed);
			vertex(x, y + height, z);
			light(lightMed);
		} else {
			// Back
			vertex(x+width, y, z + depth);
			light(lightMed);
			vertex(x, y, z + depth);
			light(lightMed);
			vertex(x, y+height, z + depth);
			light(lightMed);
			vertex(x+width, y+height, z + depth);
			light(lightMed);
		}

		if (x > fixPos.x) {
			// Left
			vertex(x, y, z + depth);
			light(lightLow);
			vertex(x, y, z);
			light(lightLow);
			vertex(x, y + height, z);
			light(lightLow);
			vertex(x, y + height, z + depth);
			light(lightLow);
		} else {
			// Right
			vertex(x + width, y, z);
			light(lightLow);
			vertex(x + width, y, z + depth);
			light(lightLow);
			vertex(x + width, y + height, z + depth);
			light(lightLow);
			vertex(x + width, y + height, z);
			light(lightLow);
		}		

		if (isAbove) {
			// Top
			vertex(x, y + height, z);
			light(lightHigh);
			vertex(x + width, y + height, z);
			light(lightHigh);
			vertex(x + width, y + height, z + depth);
			light(lightHigh);
			vertex(x, y + height, z + depth);
			light(lightHigh);
		} else {
			// Bottom
			vertex(x, y, z + depth);
			light(lightDim);
			vertex(x + width, y, z + depth);
			light(lightDim);
			vertex(x + width, y, z);
			light(lightDim);
			vertex(x, y, z);
			light(lightDim);
		}
	}
	
	public void rect (float x, float y, float z, float size) {
		if (idx+16 >= maxSize) {
			//Gdx.app.log("Clouds", "Out of vertexs!");
			return;
		}
		
		final float depth = -size;
		final float width = size;
		
		final float lightMed = 0.95f;
		
		// Bottom
		vertex(x, y, z + depth);
		light(lightMed);
		vertex(x + width, y, z + depth);
		light(lightMed);
		vertex(x + width, y, z);
		light(lightMed);
		vertex(x, y, z);
		light(lightMed);
	}

	private void light(float light) {
		verts[idx++] = light;
	};
	
	private void vertex(float x, float y, float z) {
		verts[idx] = x;
		verts[idx+1] = y;
		verts[idx+2] = z;
		idx += 3;
	}
}
