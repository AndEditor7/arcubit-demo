package com.andedit.arcubit.particles.threads;

import static com.andedit.arcubit.particles.batchs.ParticleGeometry.*;
import static com.badlogic.gdx.Gdx.gl30;

import java.nio.ByteBuffer;

import com.andedit.arcubit.glutils.ArrayBuffer;
import com.andedit.arcubit.glutils.ModernShader;
import com.andedit.arcubit.glutils.VAOwithoutBuffer;
import com.andedit.arcubit.util.Util;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.utils.Disposable;

public class ParticleDataGeometry implements Disposable
{
	private final ModernShader shader;
	private final VAOwithoutBuffer vertex;
	
	private final Camera cam;
	
	public ParticleDataGeometry(Camera cam) {
		this.cam = cam;
		shader = new ModernShader(Util.getFile("shaders/particleGeo.vert"), Util.getFile("shaders/particleGeo.geom"), Util.getFile("shaders/particleGeo.frag"));
		
		vertex = new VAOwithoutBuffer();
		vertex.bindShader(shader, attributes);
	}
	
	public void render(ArrayBuffer buffers) {
		if (buffers.size == 0) return;
		final Camera cam = this.cam;
		shader.bind();
		/* TODO: fixed this later.
		shader.setUniformMatrix("u_projTrans", cam.combined);
		shader.setUniformf("u_camPos", cam.position);
		shader.setUniformf("up", cam.up);
		*/
		vertex.bind();
		
		final int s = buffers.size;
		for (int i = 0; i < s; i++) {
			final ByteBuffer buffer = buffers.getBuffer(i);
			final int count = buffers.getCount(i);
			gl30.glBufferData(GL30.GL_ARRAY_BUFFER, count*Float.BYTES, buffer, GL30.GL_DYNAMIC_DRAW);
			gl30.glDrawArrays(GL30.GL_POINTS, 0, count/floatSize);
		}
		
		vertex.unbind();
		gl30.glUseProgram(0);
	}

	public void dispose() {
		shader.dispose();
		vertex.dispose();
	}
}
