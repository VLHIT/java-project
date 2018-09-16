package com.hit.algorithm;

import java.util.HashMap;
import java.util.TreeMap;

public class NRUAlgoCacheImpl<K,V> extends AbstractAlgoCache<K,V> {
	TreeMap<HashMap.Entry<K, V>, Integer> counters ;
	
	public NRUAlgoCacheImpl(int capacity) {
		super(capacity);
		this.capacity = capacity ;
		counters = new TreeMap<HashMap.Entry<K, V>, Integer>();
	}

	@Override
	public V getElement(K key) {
		
		return null;
	}

	@Override
	public V putElement(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeElement(K key) {
		// TODO Auto-generated method stub
		
	}

}
