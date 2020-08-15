package com.andedit.arcubit.particles.threads;

import static com.andedit.arcubit.particles.threads.ParticleMultiThreaded.*;
import static com.badlogic.gdx.Gdx.gl30;
import static com.andedit.arcubit.particles.batchs.ParticleGeometry.*;

import com.andedit.arcubit.glutils.ArrayBuffer;
import com.andedit.arcubit.glutils.ModernShader;
import com.andedit.arcubit.glutils.VAOwithoutBuffer;
import com.andedit.arcubit.util.Util;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class MultiParticleDataGeomety implements Disposable 
{
	private final ModernShader shader;
	private final Array<VAOwithoutBuffer[]> verticesArray;
	private final int[] sizes;
	
	private final Camera cam;
	
	public MultiParticleDataGeomety(Camera cam) {
		this.cam = cam;
		shader = new ModernShader(Util.getFile("shaders/particleGeo.vert"), Util.getFile("shaders/particleGeo.geom"), Util.getFile("shaders/particleGeo.frag"));
		
		verticesArray = new Array<VAOwithoutBuffer[]>(thraedsNum);
		sizes = new int[thraedsNum];
		
		for (int n = 0; n < thraedsNum; n++) 
		{
			VAOwithoutBuffer[] vaos = new VAOwithoutBuffer[buffersArrays];
			verticesArray.add(vaos);
			for (int b = 0; b < buffersArrays; b++) 
			{
				vaos[b] = new VAOwithoutBuffer();
				vaos[b].bindShader(shader, attributes);
			}
		}
	}
	
	public void update(Array<ParticleTask> array) {
		for (int i = 0; i < array.size; i++) {
			ArrayBuffer buffers = array.get(i).get();
			final int id = array.get(i).id;
			VAOwithoutBuffer[] vaos = verticesArray.get(id);
			final int s = buffers.size;
			sizes[id] = s;
			for (int j = 0; j < s; j++) {
				vaos[j].bind();
				vaos[j].uploadVertices(buffers.getBuffer(j), buffers.getCount(j));
			}
		}
		gl30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
	}
	
	public void render() {
		final Camera cam = this.cam;
		shader.bind();
		gl30.glUniformMatrix4fv(shader.fetchUniformLocation("u_projTrans"), 1, false, cam.combined.val, 0);
		gl30.glUniform3f(shader.fetchUniformLocation("u_camPos"), cam.position.x, cam.position.y, cam.position.z);
		gl30.glUniform3f(shader.fetchUniformLocation("up"), cam.up.x, cam.up.y, cam.up.z);
		
		for (int i = 0; i < thraedsNum; i++) {
			VAOwithoutBuffer[] vaos = verticesArray.get(i);
			final int s = sizes[i];
			for (int j = 0; j < s; j++) {
				VAOwithoutBuffer vao = vaos[j];
				vao.bind();
				gl30.glDrawArrays(GL30.GL_POINTS, 0, vao.count/floatSize);
			}
		}
		
		gl30.glBindVertexArray(0);
		gl30.glUseProgram(0);
	}

	@Override
	public void dispose() {
		for (int n = 0; n < thraedsNum; n++) {
			VAOwithoutBuffer[] vaos = verticesArray.get(n);
			for (int b = 0; b < buffersArrays; b++) {
				vaos[b].dispose();
			}
		}
	}
}
