package com.andedit.arcubit.glutils;

import static com.badlogic.gdx.Gdx.gl30;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.andedit.arcubit.world.WorldRenderer;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.FloatArray;

public class VAO implements Vertex 
{
	private final static IntBuffer tmpHandle = BufferUtils.newIntBuffer(1);
	
	private final VertContext context;
	private final ByteBuffer buffer;
	private int bufferHandle, vaoHandle;
	private boolean isNew = true;
	private final boolean UseIndex;
	
	public VAO(final FloatArray array, final VertContext context) {
		this.context = context;
		buffer = BufferUtils.newUnsafeByteBuffer(context.getAttrs().vertexSize * (array.size/context.getAttrsSize()));
		buffer.flip();
		
		BufferUtils.copy(array.items, buffer, array.size, 0);
		buffer.limit(array.size*4);
		UseIndex = true;
	}
	
	public VAO(final int size, final VertContext context) {
		this.context = context;
		buffer = BufferUtils.newUnsafeByteBuffer(context.getAttrs().vertexSize * (size/context.getAttrsSize()));
		buffer.flip();
		UseIndex = false;
	}
	
	@Override
	public void bind() {
		if (isNew) {
			isNew = false;
			
			tmpHandle.clear();
			gl30.glGenVertexArrays(1, tmpHandle);
			vaoHandle = tmpHandle.get();
			gl30.glBindVertexArray(vaoHandle);
			
			bufferHandle = gl30.glGenBuffer();
			gl30.glBindBuffer(GL30.GL_ARRAY_BUFFER, bufferHandle);
			
			// Cheeky index bind.
			if (UseIndex) WorldRenderer.indices.bind();
			
			final VertexAttributes attributes = context.getAttrs();
			final ShaderProgram shader = context.getShader();
			final int numAttributes = attributes.size();
			for (int i = 0; i < numAttributes; i++) {
			final VertexAttribute attribute = attributes.get(i);
			final int location = context.getLocation(i);
			shader.enableVertexAttribute(location);
			
			shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, 
					attributes.vertexSize, attribute.offset);
			}
			
			if (UseIndex) gl30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer.limit(), buffer, GL30.GL_STATIC_DRAW);
		} else {
			gl30.glBindVertexArray(vaoHandle);
		}
	}
	
	public void setVertices(float[] vertices, int count, boolean uploadIt) {
		BufferUtils.copy(vertices, buffer, count, 0);
		buffer.limit(count*Float.BYTES);
	}

	@Override
	public void unbind() {
		gl30.glBindVertexArray(0);
	}
	
	@Override
	public void dispose() {
		if (!isNew) {
			gl30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			gl30.glDeleteBuffer(bufferHandle);
			
			tmpHandle.clear();
			tmpHandle.put(vaoHandle);
			tmpHandle.flip();
			gl30.glDeleteVertexArrays(1, tmpHandle);
		}
		BufferUtils.disposeUnsafeByteBuffer(buffer);
	}
}
