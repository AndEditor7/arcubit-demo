package com.andedit.arcubit.graphic.vertex;

import static com.andedit.arcubit.graphic.vertex.VertBuf.buffer;
import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.gl30;

import java.nio.IntBuffer;

import com.andedit.arcubit.graphic.QuadIndex;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

/** Vertex Buffer Object with Vertex Array Object (VBO with VAO)
 *  Will always binds QuadIndex. */
public class VAO implements Vertex {
	private static final IntBuffer intBuf = BufferUtils.newIntBuffer(1);

	private int glDraw, size;
	private int handle, vao;
	private boolean isBound;
	
	VAO(VertContext context, int glDraw) {
		handle = gl30.glGenBuffer();
		this.glDraw = glDraw;
		
		intBuf.clear();
		gl30.glGenVertexArrays(1, intBuf);
		vao = intBuf.get();
		
		gl30.glBindVertexArray(vao);
		gl30.glBindBuffer(GL20.GL_ARRAY_BUFFER, handle);
		QuadIndex.bind();
		context.setVertexAttributes(null);
		gl30.glBindVertexArray(0);
	}
	
	@Override
	public void setVertices(float[] array, int size, int offset) {
		this.size = size;
		BufferUtils.copy(array, buffer, size, offset);
		if (!isBound) {
			gl30.glBindVertexArray(vao);
		}
		gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, handle);
		gl.glBufferData(GL20.GL_ARRAY_BUFFER, buffer.remaining(), buffer, glDraw);
		if (!isBound) {
			gl30.glBindVertexArray(0);
		}
	}

	@Override
	public void bind() {
		gl30.glBindVertexArray(vao);
		isBound = true;
	}

	@Override
	public void unbind() {
		gl30.glBindVertexArray(0);
		isBound = false;
	}
	
	@Override
	public void setDraw(int glDraw) {
		this.glDraw = glDraw;
	}

	@Override
	public int getDraw() {
		return glDraw;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public void dispose() {
		intBuf.clear();
		intBuf.put(vao);
		intBuf.flip();
		gl30.glDeleteVertexArrays(1, intBuf);
		gl30.glDeleteBuffer(handle);
	}
}
