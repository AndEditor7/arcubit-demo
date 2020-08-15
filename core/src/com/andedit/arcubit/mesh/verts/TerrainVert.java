package com.andedit.arcubit.mesh.verts;

import static com.badlogic.gdx.graphics.glutils.ShaderProgram.*;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

// Needs update comments after attribute change.
/** Data[sideLight&Ambiant, hightLight, isLeaves, nothing] */
public final class TerrainVert 
{
	/** 3 Position, 1 Data and 2 TextureCoordinates [x,y,z,d,u,v] */
	public static final VertexAttributes attributes = new VertexAttributes(
			 	new VertexAttribute(Usage.Position, 3, POSITION_ATTRIBUTE),
				new VertexAttribute(Usage.Generic, 1, "a_light"),
				new VertexAttribute(Usage.TextureCoordinates, 2, TEXCOORD_ATTRIBUTE)
			);
	
	/** 24 bytes in a single vertex with 6 float components. */ 
	public static final int byteSize = attributes.vertexSize;
	
	/** 6 floats in a single vertex. */ 
	public static final int floatSize = byteSize/Float.BYTES;
}
