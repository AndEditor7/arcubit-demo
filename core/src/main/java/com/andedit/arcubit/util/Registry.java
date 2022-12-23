package com.andedit.arcubit.util;

import com.andedit.arcubit.block.Block;
import com.badlogic.gdx.utils.OrderedMap;

public class Registry<T> {
	
	public static final Registry<Block> BLOCKS = new Registry<>();
	
	private final OrderedMap<String, T> idToObj = new OrderedMap<>();
	private final OrderedMap<T, String> objToId = new OrderedMap<>();
	
	public String getId(T obj) {
		return objToId.get(obj);
	}
	
	public T getObj(String id) {
		return idToObj.get(id);
	}
	
	public void add(String id, T Obj) {
		idToObj.put(id, Obj);
		objToId.put(Obj, id);
	}
	
	public Iterable<String> idIterator() {
		return idToObj.orderedKeys();
	}
	
	public Iterable<T> objIterator() {
		return objToId.orderedKeys();
	}
}
