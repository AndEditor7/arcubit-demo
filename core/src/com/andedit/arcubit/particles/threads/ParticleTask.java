package com.andedit.arcubit.particles.threads;

import static com.andedit.arcubit.particles.batchs.ParticleGeometry.*;
import static com.andedit.arcubit.particles.threads.ParticleMultiThreaded.*;

import com.andedit.arcubit.glutils.ArrayBuffer;
import com.andedit.arcubit.particles.BitPools;
import com.andedit.arcubit.particles.bits.Fragment;
import com.andedit.arcubit.particles.bits.Particle;
import com.andedit.arcubit.util.VolatileArray;
import com.andedit.arcubit.util.threads.TaskWorker;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.async.AsyncExecutor;

public class ParticleTask extends TaskWorker<ArrayBuffer> 
{
	// TODO: Replace ArrayMap with ObjectMap if Particle class getting populated.
	private final ArrayMap<Class<?>, Pool<Particle>> map = new ArrayMap<Class<?>, Pool<Particle>>();
	
	private void intsPools() {
		Fragment.intsPool(map);
	}
	
	private final Array<Particle> parts;
	private final ArrayBuffer buffers;
	
	public final VolatileArray<Particle> newParts  = new VolatileArray<Particle>(256);
	
	// Vertex
	private final int maxSize;
	private final float[] verts;
	private int idx;
	
	// Thread
	public final int id;
	
	/** If the particles's array has one or more items. */
	public volatile boolean needUpdate = false;
	/** The real particles size. */
	public volatile int realSize;
	
	public final int maxParticles;
		
	public ParticleTask(final int id) {
		intsPools();
		this.id = id;
		maxParticles = vertexSize;
		maxSize = (vertexSize*floatSize)/buffersArrays; // Max size/length of floats.
		verts = new float[maxSize]; // Allocate the array of floats.
		parts = new Array<Particle>(false, maxParticles, Particle.class);
		buffers = ArrayBuffer.create(vertexSize*byteSize, buffersArrays);
	}

	public void start(AsyncExecutor exe) {
		buffers.size = 0;
		result = exe.submit(this);
	}

	@Override
	public ArrayBuffer call() throws Exception {
		final World world = World.world;
		buffers.size = 0;
		idx = 0;
		//deadParts.clear();
		
		for (int i = 0, s = newParts.size; i < s; i++) {
			if (parts.size >= maxParticles) {
				break;
			}
			parts.add(newParts.getAndNull(i));
		}
		newParts.clear();
		
		realSize = parts.size;
		final Particle[] fast = parts.items;
		for (int i = 0, s = parts.size; i < s; ++i) {
			final Particle p = fast[i];
			p.update(world);
			if (p.isDead) {
				//deadParts.add(parts.removeIndex(i--));
				BitPools.free(parts.removeIndex(i--));
				//parts.removeIndex(i--);
				--s; continue;
			}
			draw(p);
		}
		
		realSize = parts.size;
		needUpdate = parts.notEmpty();
		realSize = realSize;
		needUpdate = needUpdate;
		
		if (idx != 0) flush();
		return buffers;
	}
	
	private void draw(Particle particle) {
		if (idx == maxSize) 
			flush();
		
		final Vector3 pos = particle.pos;
		final TextureRegion reg = particle.region;
		
		final int i = idx;
		verts[i]    = pos.x;
		verts[i+1]  = pos.y;
		verts[i+2]  = pos.z;
		verts[i+3]  = reg.getU();
		verts[i+4]  = reg.getV();
		verts[i+5]  = reg.getU2();
		verts[i+6]  = reg.getV2();
		this.idx = i + 7;
	}
	
	public void cleanDeadParts() {
		/*
		final int s = deadParts.size;
		for (int i = 0; i < s; i++) {
			BitPools.free(deadParts.pop());
		}
		//deadParts.clear();
		deadParts.size = 0; */
	}
	
	private void flush() {
		//buffers.setVertices(verts, idx, buffers.size++);
		idx = 0;
	}
	
	public int getSize() {
		return realSize;
	}
	
	public int getReqiureSize() {
		return maxParticles - realSize;
	}
	
	@Override
	public void dispose() {
		buffers.dispose();
	}
}
