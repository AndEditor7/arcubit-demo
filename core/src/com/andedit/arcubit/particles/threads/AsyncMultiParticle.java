package com.andedit.arcubit.particles.threads;

import com.andedit.arcubit.util.threads.AsyncMultiThreaded;
import com.badlogic.gdx.utils.Array;

public class AsyncMultiParticle extends AsyncMultiThreaded<ParticleTask> 
{	
	public AsyncMultiParticle(int numsThreads, String name) {
		super(numsThreads, name, new ParticleTask[numsThreads]);
		for (int i = 0; i < numsThreads; i++) {
			final ParticleTask task = new ParticleTask(i);
			deadTasks.add(task);
			tasks[i] = task;
		}
	}

	public void start(Array<ParticleTask> newTasks) {
		for (int i = 0; i < newTasks.size; i++) {
			final ParticleTask task = newTasks.get(i);
			task.start(exe);
			usedTasks.add(task);
		}
	}
	
	public void start(ParticleTask newTask) {
		usedTasks.add(newTask);
		newTask.start(exe);
	}

	public int getSize() {
		int n = 0;
		for (int i = 0; i < numsThreads; i++) {
			n += tasks[i].getSize();
		}
		return n;
	}
}
