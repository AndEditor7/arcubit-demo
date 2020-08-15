package com.andedit.arcubit.util.threads;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;

public abstract class TaskWorker<Packet> implements AsyncTask<Packet>, Disposable
{
	protected AsyncResult<Packet> result;
	
	/** The call thread to start after submitted. */
	public abstract Packet call() throws Exception;
	
	/** Get the packet after the thread is finished executing. */
	public Packet get() {
		return result == null ? null : result.get();
	}
	
	/** Check the thread is finished executing. Will return true even the AsyncResult are null. */
	public boolean isDone() {
		return result == null ? true : result.isDone();
	}
	
	public void dispose() {}
}
