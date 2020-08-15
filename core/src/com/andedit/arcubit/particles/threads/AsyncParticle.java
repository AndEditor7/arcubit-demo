package com.andedit.arcubit.particles.threads;

import static com.andedit.arcubit.particles.batchs.ParticleGeometry.*;

import com.andedit.arcubit.glutils.ArrayBuffer;
import com.andedit.arcubit.particles.BitPools;
import com.andedit.arcubit.particles.bits.Particle;
import com.andedit.arcubit.util.VolatileArray;
import com.andedit.arcubit.util.threads.AsyncThreaded;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncResult;

/** A threaded particle. */
class AsyncParticle extends AsyncThreaded<ArrayBuffer>
{	
	private final Array<Particle> parts;
	private volatile ArrayBuffer buffers;
	
	private AsyncResult<ArrayBuffer> result;
	private volatile VolatileArray<Particle> newParts;
	
	private final VolatileArray<Particle> deadParts = new VolatileArray<Particle>(64);
	
	// Vertex
	private final int maxSize;
	private final float[] verts;
	private int idx; 
	
	public AsyncParticle() {
		super("Particle");
		this.parts = new Array<Particle>(false, 128);
		
		final int vertexs = maxVertex; // Vertices/Particles.
		maxSize = vertexs*floatSize; // Max size/length of floats.
		verts = new float[maxSize]; // Allocate the array of floats.
		
		// buffers = new ArrayBuffer(byteSize*vertexs, maxParticles/vertexs);
	}
	
	public void start(ArrayBuffer buffers, VolatileArray<Particle> newParts) {
		buffers.size = 0;
		this.buffers = buffers;
		this.newParts = newParts;
		result = exe.submit(this);
	}

	@Override
	public ArrayBuffer call() throws Exception {
		final World world = World.world;
		synchronized (newParts) {
			for (int i = 0, s = newParts.size; i < s; i++) {
				if (parts.size >= ParticleThreaded.maxParticles) {
					break;
				}
				parts.add(newParts.get(i));
			}
			newParts.clear();
		}
		idx = 0;
		deadParts.clear();
		for (int i = 0; i < parts.size; ++i) {
			final Particle p = parts.get(i);
			p.update(world);
			if (p.isDead) {
				deadParts.add(parts.removeIndex(i--));
				continue;
			}
			draw(p);
		}
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
		final int s = deadParts.size;
		for (int i = 0; i < s; i++) {
			BitPools.free(deadParts.get(i));
		}
		deadParts.clear();
	}
	
	private void flush() {
		buffers.setVertices(verts, idx, buffers.size++);
		idx = 0;
	}

	@Override
	public ArrayBuffer get() {
		return result == null ? null : result.get();
	}

	@Override
	public boolean isDone() {
		return result == null ? true : result.isDone();
	}
	
	public int getSize() {
		return parts.size;
	}
}
