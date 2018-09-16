package com.hit.algorithm;

public abstract class AbstractAlgoCache<K,V> implements IAlgoCache<K,V> {
	Integer capacity ;
	public AbstractAlgoCache(int capacity) {
		this.capacity = capacity ;
	}
}