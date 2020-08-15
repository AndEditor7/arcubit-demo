package com.andedit.arcubit.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderUtil 
{
	public static ShaderProgram getShader(String path) {		
		return new ShaderProgram(Gdx.files.internal(path + ".vert"), Gdx.files.internal(path + ".frag"));		
	}
	
	public static int[] locateAttributes(final ShaderProgram shader, final VertexAttributes attributes) {
		final int s = attributes.size();
		final int[] locations = new int[s];
		for (int i = 0; i < s; i++) {
			final VertexAttribute attribute = attributes.get(i);
			locations[i] = shader.getAttributeLocation(attribute.alias);
		}
		return locations;
	}
	
	public static void getSouce(ShaderProgram shader) {
		System.out.println("Vertex:\n" + shader.getVertexShaderSource() + "\nFragment:\n" + shader.getFragmentShaderSource());
	}
}
