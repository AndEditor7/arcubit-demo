package com.andedit.arcubit.glutils;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicIntegerArray;

import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;

public class ArrayBuffer implements Disposable
{
	private final ByteBuffer buffers[];
	private final AtomicIntegerArray counts;
	
	/** The size of the buffer bytes. */
	public final int bufferSize;
	/** The total size of all buffers. */
	public final int totalSize;
	public final int arrayLenght;
	/** The current size of the array. Cashes it when in the "for loop". */
	public volatile int size;
	
	public ArrayBuffer(final int bufferSize, final int arrayLenght) {
		buffers = new ByteBuffer [arrayLenght];
		counts = new AtomicIntegerArray(arrayLenght);
		this.bufferSize = bufferSize;
		this.arrayLenght = arrayLenght;
		totalSize = bufferSize*arrayLenght;
		
		for (int i = 0; i < arrayLenght; i++) {
			final ByteBuffer buffer = BufferUtils.newUnsafeByteBuffer(bufferSize);
			buffer.position(0);
			buffer.flip();
			buffers[i] = buffer;
		}
	}
	
	public void setVertices(final float[] vertices, final int count, final int index) {
		final ByteBuffer buffer = buffers[index];
		BufferUtils.copy(vertices, buffer, count, 0);
		buffer.limit(count*Float.BYTES);
		counts.set(index, count);
	}
	
	public ByteBuffer getBuffer(int index) {
		return buffers[index];
	}
	
	/** Count of number of floats. */
	public int getCount(final int index) {
		return counts.get(index);
	}
	
	@Override
	public void dispose() {
		for (int i = 0; i < arrayLenght; i++) {
			BufferUtils.disposeUnsafeByteBuffer(buffers[i]);
		}
	}
	
	public static ArrayBuffer create(final int trueSize, final int arrayLenght) {
		return new ArrayBuffer(trueSize/arrayLenght, arrayLenght);
	}
}
