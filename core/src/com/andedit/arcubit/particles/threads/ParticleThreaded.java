package com.andedit.arcubit.particles.threads;

import static com.andedit.arcubit.particles.batchs.ParticleGeometry.*;

import com.andedit.arcubit.glutils.ArrayBuffer;
import com.andedit.arcubit.particles.batchs.IParticleSystem;
import com.andedit.arcubit.particles.bits.Particle;
import com.andedit.arcubit.util.VolatileArray;
import com.badlogic.gdx.graphics.Camera;

public class ParticleThreaded implements IParticleSystem
{
	public static final int maxParticles = 1500000;
	
	private final AsyncParticle async;
	private final ParticleDataGeometry batch;
	
	private final VolatileArray<Particle> newParts = new VolatileArray<Particle>();
	
	private boolean flip;
	private final ArrayBuffer buffers1;
	private final ArrayBuffer buffers2;
	
	public ParticleThreaded(Camera cam) {
		async = new AsyncParticle();
		batch = new ParticleDataGeometry(cam);
		
		buffers1 = new ArrayBuffer(byteSize*maxVertex, maxParticles/maxVertex);
		buffers2 = new ArrayBuffer(byteSize*maxVertex, maxParticles/maxVertex);
	}
	
	public void render() {
		if (async.isDone()) {
			final ArrayBuffer done = async.get();
			if (done != null) {
				batch.render(done);
			}
			async.cleanDeadParts();
			if (flip) {
				async.start(buffers1, newParts);
			} else {
				async.start(buffers2, newParts);
			}
			flip = !flip;
			return;
		}
		ArrayBuffer buffers;
		if (flip) {
			buffers = buffers1;
		} else {
			buffers = buffers2;
		}
		batch.render(buffers);
	}

	@Override
	public void dispose() {
		async.dispose();
	}

	public void add(Particle part) {
		synchronized (newParts) {
			newParts.add(part);
		}
	}

	@Override
	public int getSize() {
		return async.getSize();
	}
}
