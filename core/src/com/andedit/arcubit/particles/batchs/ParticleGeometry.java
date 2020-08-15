package com.andedit.arcubit.particles.batchs;

import com.andedit.arcubit.particles.bits.Particle;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/** The geometry shader particle renderer */
public class ParticleGeometry implements IParticleRenderer 
{
	/** [x,y,z,u,v,u2,v2] */
	public static final VertexAttributes attributes = new VertexAttributes(
			new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.TextureCoordinates, 4, ShaderProgram.TEXCOORD_ATTRIBUTE)
	);
	
	/** 28 bytes. 28 bytes per particle. */
	public static final int byteSize = attributes.vertexSize;
	/** 7 floats. */
	public static final int floatSize = byteSize/Float.BYTES;

	/** Vertices/Particles. */
	public static final int maxVertex = 2000;
	
	// TODO Rebuild this particle geometry renderer.
	public ParticleGeometry() {
		
	}
	
	@Override
	public void begin() {
		
	}
	
	@Override
	public void flush() {
		
	}
	
	@Override
	public void end() {
		
	}
	
	@Override
	public void draw(Particle particle) {
		
	}
	
	@Override
	public void dispose() {
		
	}
}
