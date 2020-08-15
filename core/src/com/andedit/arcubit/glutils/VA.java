package com.andedit.arcubit.glutils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.FloatArray;

public final class VA implements Vertex 
{
	private final VertContext context;
	private final FloatBuffer buffer;
	private final ByteBuffer byteBuffer;
	
	public VA(FloatArray array, final VertContext context) {
		this.context = context;
		byteBuffer = BufferUtils.newUnsafeByteBuffer(context.getAttrs().vertexSize * (array.size/context.getAttrsSize()));
		buffer = byteBuffer.asFloatBuffer();
		buffer.flip();
		byteBuffer.flip();
		
		BufferUtils.copy(array.items, byteBuffer, array.size, 0);
		buffer.position(0);
		buffer.limit(array.size);
	}

	@Override
	public void bind() {
		byteBuffer.limit(buffer.limit() * Float.BYTES);
		final VertexAttributes attributes = context.getAttrs();
		final ShaderProgram shader = context.getShader();
		final int numAttributes = context.getAttrs().size();
		for (int i = 0; i < numAttributes; i++) {
			final VertexAttribute attribute = attributes.get(i);
			final int location = context.getLocation(i);
			shader.enableVertexAttribute(location);

			if (attribute.type == GL20.GL_FLOAT) {
				buffer.position(attribute.offset / Float.BYTES);
				shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized,
					attributes.vertexSize, buffer);
			} else {
				byteBuffer.position(attribute.offset);
				shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized,
					attributes.vertexSize, byteBuffer);
			}
		}
	}

	@Override
	public void unbind() {
		final VertexAttributes attributes = context.getAttrs();
		final ShaderProgram shader = context.getShader();
		final int numAttributes = attributes.size();
		for (int i = 0; i < numAttributes; i++) {
			final int location = context.getLocation(i);
			if (location >= 0) shader.disableVertexAttribute(location);
		}
	}
	
	@Override
	public void dispose() {
		BufferUtils.disposeUnsafeByteBuffer(byteBuffer);
	}
}
