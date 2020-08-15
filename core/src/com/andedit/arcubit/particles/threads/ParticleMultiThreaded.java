package com.andedit.arcubit.particles.threads;

import com.andedit.arcubit.particles.batchs.IParticleSystem;
import com.andedit.arcubit.particles.bits.Particle;
import com.andedit.arcubit.util.Camera;
import com.badlogic.gdx.utils.Array;

public class ParticleMultiThreaded implements IParticleSystem 
{
	/** Max Particles or Vertices size. */
	public static final int maxParticles = 3000000;
	/** Number of threads it using. */
	public static final int thraedsNum = 1;
	/** Total vertex/particle size for each threads. */
	public static final int vertexSize = maxParticles/thraedsNum;
	/** Number of buffers for each threads. */
	public static final int buffersArrays = 300;
	
	private final AsyncMultiParticle async;
	
	private final Array<Particle> newParts = new Array<Particle>(64);
	
	private final MultiParticleDataGeomety batch;

	public ParticleMultiThreaded(Camera cam) {
		async = new AsyncMultiParticle(thraedsNum, "Hyper-Particles");
		batch = new MultiParticleDataGeomety(cam);
	}

	@Override
	public void render() {
		if (async.isTaskRunning()) {
			Array<ParticleTask> array = async.getFinishedTasks();
			if (array.notEmpty()) {
				
				batch.update(array); // update the vertices.
				
				for (int i = 0; i < array.size; i++) {
					ParticleTask task = array.get(i);
					task.cleanDeadParts();
					if (task.needUpdate) {
						if (newParts.notEmpty()) {
							final int size = Math.min(task.getReqiureSize(), newParts.size);
							for (int j = 0; j < size; j++) {
								task.newParts.add(newParts.pop());
							}
						}
						async.start(task);
					} else {
						if (newParts.notEmpty()) {
							final int size = Math.min(task.getReqiureSize(), newParts.size);
							for (int j = 0; j < size; j++) {
								task.newParts.add(newParts.pop());
							}
							async.start(task);
						} else {
							async.putDeadTask(task);
						}
					}
				}
			}
		}
		if (newParts.notEmpty()) {
			ParticleTask task = async.getNewTask();
			if (task != null) {
				final int size = Math.min(task.getReqiureSize(), newParts.size);
				for (int j = 0; j < size; j++) {
					task.newParts.add(newParts.pop());
				}
				async.start(task);
			}
		}
		
		// render the particles
		batch.render();
	}

	@Override
	public void add(Particle part) {
		newParts.add(part);
	}

	@Override
	public int getSize() {
		return async.getSize();
	}
	
	@Override
	public void dispose() {
		async.dispose();
		batch.dispose();
	}
}
