package com.andedit.arcubit.util;

import java.util.concurrent.atomic.AtomicReferenceArray;

/** A volatile array list using {@link AtomicReferenceArray}. */
public class VolatileFixedArray<T>
{
	/** {@link AtomicReferenceArray} */
	protected final AtomicReferenceArray<T> items;
	/** The max length that should not pass. */
	public final int length;
	/** The current size. Try to cache this variable in a 'for (Loop) {}' since it in volatile memory. */
	public volatile int size = 0;
	
	public VolatileFixedArray(final int length) {
		this.length = length;
		items = new AtomicReferenceArray<T>(length);
	}
	
	public T get(final int index) {
		return items.get(index);
	}
	
	public void set(final int index, final T value) {
		items.set(index, value);
	}
	
	public void add(final T value) {
		items.set(size++, value);
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public void clear() {
		size = 0;
	}
}
