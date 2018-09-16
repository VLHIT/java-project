package com.hit.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomAlgoCacheImpl<K,V> extends AbstractAlgoCache<K,V> {
	private HashMap<K, V> entries ;
	private ArrayList<K> randomChoices ;
	
	public RandomAlgoCacheImpl(int capacity) {
		super(capacity);
		entries = new HashMap<K, V>() ;
		randomChoices = new ArrayList<K>() ; 
	}

	@Override
	public V getElement(K key) {
		return entries.get(key) ;
	}

	@Override
	public V putElement(K key, V value) {
		Random randomGenerator = new Random() ;
		V result = null ;
		if (!entries.containsKey(key)) {
			if (entries.size() >= capacity) {
				int index = randomGenerator.nextInt(randomChoices.size()) ;
				result = entries.get(randomChoices.get(index)) ;
				entries.remove(randomChoices.get(index)) ;
				randomChoices.remove(index) ;
			}
			entries.put(key, value) ;
			randomChoices.add(key) ;
		}
		else {
			entries.put(key, value) ;
		}
		return result;
	}

	@Override
	public void removeElement(K key) {
		entries.remove(key) ;
		randomChoices.remove(key) ;
	}
	
	public void printAll( ) {
		System.out.println(entries);
		System.out.println(randomChoices);
	}

}
