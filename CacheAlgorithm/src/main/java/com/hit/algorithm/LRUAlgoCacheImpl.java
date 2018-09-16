package com.hit.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUAlgoCacheImpl<K,V> extends AbstractAlgoCache<K,V> {
	private Map<K,V> entries ;
	private LinkedList<K> orderQue ;
	
	public LRUAlgoCacheImpl(int capacity) {
		super(capacity);
		entries = new HashMap<K,V>() ;
		orderQue = new LinkedList<K>() ;
	}

	@Override
	public V getElement(K key) {
		V value = entries.get(key) ;
		if (value != null) {
			orderQue.removeLast() ;
			orderQue.push(key);
		}
		return value ;
	}

	@Override
	public V putElement(K key, V value) {
		V result = null ;
		if (entries.size() >= capacity) {
			if (!entries.containsKey(key)) {
				K temp = orderQue.getLast() ;
				orderQue.removeLast() ;
				result = entries.get(temp);
				entries.remove(temp) ;
			}
			else {
				orderQue.remove(key) ;	
			}
		}
		else {
			if (entries.containsKey(key)) {
					orderQue.remove(key) ;
			}
		}
		orderQue.push(key);
		entries.put(key, value) ;
		return result;
	}

	@Override
	public void removeElement(K key) {
		entries.remove(key) ;
		orderQue.remove(key) ;
	}

	
	void printAll() {
		System.out.println(orderQue.toString()) ;
		System.out.println(entries.toString()) ;
		
	}
}