package com.andedit.arcubit.util;

import java.lang.reflect.ParameterizedType;

public interface NewInstance<T> {
	public T newObject();
	
	public default Class<?> getObjClass() { 
		return (Class<?>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
