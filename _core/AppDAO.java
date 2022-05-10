package com.shopperStore._core;

/**
 * @param <T> generic
 */
public interface AppDAO<T> {
	public T create(T payload);
	public T get(int id);
	public T update(T payload);
	public boolean delete(int id);
}

