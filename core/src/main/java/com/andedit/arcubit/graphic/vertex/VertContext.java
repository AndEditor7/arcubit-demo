package com.andedit.arcubit.graphic.vertex;

import java.nio.Buffer;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Null;

public interface VertContext 
{
	ShaderProgram getShader();
	
	VertexAttributes getAttrs();
	
	default void setVertexAttributes(@Null Buffer buffer) {
		final VertexAttributes attributes = getAttrs();
		final ShaderProgram shader = getShader();
		for (int i = 0; i < attributes.size(); i++) {
			final VertexAttribute attribute = attributes.get(i);
			final int location = shader.getAttributeLocation(attribute.alias);
			if (location < 0) continue;
			shader.enableVertexAttribute(location);

			if (buffer == null) {
				shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized,
						attributes.vertexSize, attribute.offset);
			} else {
				buffer.position(attribute.offset);
				shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized,
						attributes.vertexSize, buffer);
			}
		}
	}
	
	default void unVertexAttributes() {
		final VertexAttributes attributes = getAttrs();
		final ShaderProgram shader = getShader();
		for (int i = 0; i < attributes.size(); i++) {
			shader.disableVertexAttribute(attributes.get(i).alias);
		}
	}
	
	static VertContext of(ShaderProgram shader, VertexAttribute... attributeArray) {
		return of(shader, new VertexAttributes(attributeArray));
	}
	
	static VertContext of(ShaderProgram shader, VertexAttributes attributes) {
		return new VertContext() {
			public ShaderProgram getShader() {
				return shader;
			}
			public VertexAttributes getAttrs() {
				return attributes;
			}
		};
	}
}
