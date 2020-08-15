package com.andedit.arcubit.glutils;

import static com.badlogic.gdx.Gdx.gl30;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;

public class VAOwithoutBuffer implements Disposable 
{
	private final static IntBuffer tmpHandle = BufferUtils.newIntBuffer(1);
	
	public final int bufferHandle, vaoHandle;
	
	/** Numbers of floats. */
	public int count;
	
	public VAOwithoutBuffer() {
		tmpHandle.clear();
		gl30.glGenVertexArrays(1, tmpHandle);
		vaoHandle = tmpHandle.get();
		bufferHandle = gl30.glGenBuffer();
	}
	
	public void bindShader(ModernShader shader, VertexAttributes attributes) {
		bind();
		gl30.glBindBuffer(GL30.GL_ARRAY_BUFFER, bufferHandle);
		final int numAttributes = attributes.size();
		for (int i = 0; i < numAttributes; i++) {
		final VertexAttribute attribute = attributes.get(i);
		final int location = shader.fetchAttributeLocation(attribute.alias);
		gl30.glEnableVertexAttribArray(location);
		
		gl30.glVertexAttribPointer(location, attribute.numComponents, attribute.type, 
				attribute.normalized, attributes.vertexSize, attribute.offset);
		}
		unbind();
	}
	
	public void bind() {
		gl30.glBindVertexArray(vaoHandle);
		//gl30.glBindBuffer(GL30.GL_ARRAY_BUFFER, bufferHandle);
	}
	
	public void uploadVertices(final ByteBuffer buffer, final int count) {
		gl30.glBindBuffer(GL30.GL_ARRAY_BUFFER, bufferHandle);
		gl30.glBufferData(GL30.GL_ARRAY_BUFFER, count*Float.BYTES, buffer, GL30.GL_DYNAMIC_DRAW);
		this.count = count;
	}
	
	public void unbind() {
		gl30.glBindVertexArray(0);
	}

	@Override
	public void dispose() {
		gl30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		gl30.glDeleteBuffer(bufferHandle);
		
		tmpHandle.clear();
		tmpHandle.put(vaoHandle);
		tmpHandle.flip();
		gl30.glDeleteVertexArrays(1, tmpHandle);
	}
}
