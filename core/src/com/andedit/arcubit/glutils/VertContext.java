package com.andedit.arcubit.glutils;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public interface VertContext 
{
	public ShaderProgram getShader();
	public VertexAttributes getAttrs();
	public int getLocation(int i);
	
	/** @return vertexSize/Float.BYTE */
	public default int getAttrsSize() {
		return getAttrs().vertexSize/Float.BYTES;
	}
}
