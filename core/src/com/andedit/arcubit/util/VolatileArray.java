package com.andedit.arcubit.util;

import java.util.concurrent.atomic.AtomicReferenceArray;

/** A resisable volatile array list using {@link AtomicReferenceArray}. */
public class VolatileArray<T>
{
	public volatile AtomicReferenceArray<T> items;
	public volatile int size;
	
	/** Creates an ordered array with a capacity of 16. */
	public VolatileArray() {
		this(16);
	}

	/** Creates an array with the specified capacity. */
	public VolatileArray(final int capacity) {
		items = new AtomicReferenceArray<T>(capacity);
	}
	
	public void add (final T value) {
		AtomicReferenceArray<T> items = this.items;
		if (size == items.length()) items = resize(Math.max(8, (int)(size * 1.75f)));
		items.set(size++, value);
	}
	
	public T get (int index) {
		if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
		return items.get(index);
	}
	
	public T getAndNull (int index) {
		if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
		return items.getAndSet(index, null);
	}
	
	public void remove (int index) {
		if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
		AtomicReferenceArray<T> items = this.items;
		size--;
		items.set(index, items.get(size));
		items.set(size, null);
	}

	protected AtomicReferenceArray<T> resize(final int newSize) {
		final AtomicReferenceArray<T> items = this.items;
		final AtomicReferenceArray<T> newItems = new AtomicReferenceArray<T>(newSize);
		final int max = Math.min(size, newItems.length());
		for (int i = 0; i < max; i++) {
			newItems.set(i, items.get(i));
		}
		this.items = newItems;
		return newItems;
	}
	
	/** Returns true if the array has one or more items. */
	public boolean notEmpty () {
		return size > 0;
	}

	/** Returns true if the array is empty. */
	public boolean isEmpty () {
		return size == 0;
	}

	/** Sets the. */
	public void clear () {
		size = 0;
	}
}
