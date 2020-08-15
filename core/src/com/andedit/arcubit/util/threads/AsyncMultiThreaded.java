package com.andedit.arcubit.util.threads;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.async.AsyncExecutor;

public abstract class AsyncMultiThreaded<Task extends TaskWorker<?>> implements Disposable
{
	protected final int numsThreads;
	protected final AsyncExecutor exe;
	
	protected final Array<Task> usedTasks, doneTasks, deadTasks;
	
	/** Task pool. */
	protected final Task[] tasks;
	
	public AsyncMultiThreaded(final int numsThreads, final String name, final Task[] tasks) 
	{
		this.numsThreads = numsThreads;
		exe = new AsyncExecutor(numsThreads, name);
		
		usedTasks = new Array<Task>(false, numsThreads);
		doneTasks = new Array<Task>(false, numsThreads);
		deadTasks = new Array<Task>(false, numsThreads);
		
		this.tasks = tasks;
	}
	
	/** This is a temporally array - DO NOT USE IT! */
	public Array<Task> getFinishedTasks() {
		doneTasks.clear();
		for (int i = 0; i < usedTasks.size; i++) {
			if (usedTasks.get(i).isDone()) {
				doneTasks.add(usedTasks.removeIndex(i--));
			}
		}
		return doneTasks;
	}
	
	/** Is any task being used. */
	public void putDeadTask(Task task) {
		deadTasks.add(task);
	}
	
	/** Get and remove the unused task. */
	public Task getNewTask() {
		return deadTasks.isEmpty() ? null : deadTasks.pop();
	}
	
	/** Is any task being used. */
	public boolean isTaskRunning() {
		return usedTasks.notEmpty();
	}
	
	/** Clear anything regardless of the thread is finish. */
	public void clear() {
	}
	
	@Override
	public void dispose() {
		clear();
		exe.dispose();
		for (int i = 0; i < numsThreads; i++) {
			tasks[i].dispose();
		}
	}
}
