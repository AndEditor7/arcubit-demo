package com.andedit.arcubit.glutils;

import static com.badlogic.gdx.Gdx.gl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.FloatArray;

/** An VertexBufferObject for static object. */
public final class VBO implements Vertex
{
	private final VertContext context;
	private final FloatBuffer buffer;
	private final ByteBuffer byteBuffer;
	private int bufferHandle = -1;

	public VBO(final FloatArray array, final VertContext context) {
		this.context = context;
		byteBuffer = BufferUtils.newUnsafeByteBuffer(context.getAttrs().vertexSize * (array.size/context.getAttrsSize()));
		buffer = byteBuffer.asFloatBuffer();
		
		BufferUtils.copy(array.items, byteBuffer, array.size, 0);
		buffer.position(0);
		buffer.limit(array.size);
	}

	@Override
	public void bind() {
		if (isUploaded()) 
		gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, bufferHandle);
		else upload();
		
		final VertexAttributes attributes = context.getAttrs();
		final ShaderProgram shader = context.getShader();
		final int numAttributes = attributes.size();
		for (int i = 0; i < numAttributes; ++i) {
			final VertexAttribute attribute = attributes.get(i);
			final int location = context.getLocation(i);
			shader.enableVertexAttribute(location);

			shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized,
				attributes.vertexSize, attribute.offset);
		}
	}
	
	/** Unbinds this VertexBufferObject.
	 * @param shader the shader */
	@Override
	public void unbind() {
		final VertexAttributes attributes = context.getAttrs();
		final ShaderProgram shader = context.getShader();
		final int numAttributes = attributes.size();
		for (int i = 0; i < numAttributes; ++i) {
			shader.disableVertexAttribute(context.getLocation(i));
		}
		gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
	}

	/** Upload to GPU. */
	private void upload() 
	{
		// Create the handle.
		bufferHandle = gl.glGenBuffer();
		
		// Bind the buffer.
		gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, bufferHandle);
		
		// Upload the data.
		byteBuffer.limit(buffer.limit() * 4);
		gl.glBufferData(GL20.GL_ARRAY_BUFFER, byteBuffer.limit(), byteBuffer, GL20.GL_STATIC_DRAW);
	}
	
	private boolean isUploaded() {
		return bufferHandle != -1;
	}

	/** Disposes of all resources this VertexBufferObject uses. */
	@Override
	public void dispose () {
		if (isUploaded()) gl.glDeleteBuffer(bufferHandle);
		BufferUtils.disposeUnsafeByteBuffer(byteBuffer);
	}
}

