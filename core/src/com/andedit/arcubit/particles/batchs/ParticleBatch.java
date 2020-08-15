package com.andedit.arcubit.particles.batchs;

import static com.andedit.arcubit.Options.*;

import java.nio.ShortBuffer;

import com.andedit.arcubit.particles.bits.Particle;
import com.andedit.arcubit.util.ShaderUtil;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexArray;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class ParticleBatch implements IParticleRenderer 
{
	/** [x,y,z,u,v] */
	private static final VertexAttributes attributes = new VertexAttributes(
			new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE)
	);
	
	/** 20 bytes. 80 bytes per particle. */ 
	private static final int byteSize = attributes.vertexSize;
	/** 5 floats. */ @SuppressWarnings("unused")
	private static final int floatSize = byteSize/Float.BYTES;
	
	// Shader.
	private final ShaderProgram shader;
	
	// Vertices.
	private final int maxSize;
	private final float[] verts;
	private final VertexData vertex;
	private int idx = 0;
	
	// Mathematics.
	private final Vector3 tmp = new Vector3();
	private final Matrix4 mat = new Matrix4();
	private final Camera cam;
	
	// Indices.
	private final IndexData index;
	private boolean bindIndex = true;
	
	public ParticleBatch(Camera cam, IndexData index) {
		this.index = index;
		this.cam = cam;
		shader = ShaderUtil.getShader("shaders/particle");
		
		final int size = 1000; // 1000 quad/particles.
		final int vertexs = size*4; // 4 corners of the quad (vertices).
		
		maxSize = size*byteSize; // Max size/length of floats.
		verts = new float[maxSize]; // Allocate the array of floats.
		
		if (VBO) {
			if (GL3) vertex = new VertexBufferObjectWithVAO(false, vertexs, attributes);
			else vertex = new VertexBufferObject(false, vertexs, attributes);
		} else {
			// TODO: Adds VertexArrayWithVAO.
			vertex = new VertexArray(vertexs, attributes);
		}
	}
	
	
	@Override
	public void begin() {
		shader.bind();
		shader.setUniformMatrix("u_projTrans", cam.combined);
		if (GL3) {
			if (bindIndex) {
				bindIndex = false;
				vertex.bind(shader);
				index.bind();
				vertex.unbind(shader);
			}
		} else {
			vertex.bind(shader);
		}
		idx = 0;
	}
	
	@Override
	public void end() {
		flush();
		if (!GL3) vertex.unbind(shader);
	}
	
	@Override
	public void flush() {
		if (idx == 0) return;
		vertex.setVertices(verts, 0, idx);
		if (GL3) vertex.bind(shader);
		final int count = (idx/byteSize)*6;
		if (VBO) {
			Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, count, GL20.GL_UNSIGNED_SHORT, 0);
		} else {
			final ShortBuffer buffer = index.getBuffer();
			buffer.limit(count);
			Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, count, GL20.GL_UNSIGNED_SHORT, buffer);
		}
		if (GL3) vertex.unbind(shader);
		idx = 0;
	}
	
	@Override
	public void draw(final Particle particle) {
		if (idx == maxSize) 
			flush();
		
		final int i = idx;
		mat.setToTranslation(particle.pos); 
		mat.scl(0.25f);
		mat.rotateTowardDirection(tmp.set(cam.position).sub(particle.pos), cam.up); // Face the particle toward the camera.
		
		final TextureRegion reg = particle.region;
		
		tmp.set(-1f, -1f, 0f).mul(mat);
		verts[i]    = tmp.x;
		verts[i+1]  = tmp.y;
		verts[i+2]  = tmp.z;
		verts[i+3]  = reg.getU2();
		verts[i+4]  = reg.getV2();

		tmp.set(-1f, 1f, 0f).mul(mat);
		verts[i+5]  = tmp.x;
		verts[i+6]  = tmp.y;
		verts[i+7]  = tmp.z;
		verts[i+8]  = reg.getU2();
		verts[i+9]  = reg.getV();

		tmp.set(1f, 1f, 0f).mul(mat);
		verts[i+10] = tmp.x;
		verts[i+11] = tmp.y;
		verts[i+12] = tmp.z;
		verts[i+13] = reg.getU();
		verts[i+14] = reg.getV();

		tmp.set(1f, -1f, 0f).mul(mat);
		verts[i+15] = tmp.x;
		verts[i+16] = tmp.y;
		verts[i+17] = tmp.z;
		verts[i+18] = reg.getU();
		verts[i+19] = reg.getV2();
		this.idx = i + 20;
	}
	
	@Override
	public void dispose() {
		shader.dispose();
		vertex.dispose();
	}
}
