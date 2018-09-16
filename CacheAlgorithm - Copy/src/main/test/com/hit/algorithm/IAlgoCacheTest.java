package com.hit.algorithm;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public final class IAlgoCacheTest {
	private static final int CAPACITY = 3;
	
	private int returned;

	LRUAlgoCacheImpl<Integer, Integer> LRU = new LRUAlgoCacheImpl<Integer, Integer>(CAPACITY);
	NRUAlgoCacheImpl<Integer, Integer> NRU = new NRUAlgoCacheImpl<Integer, Integer>(CAPACITY);
	RandomAlgoCacheImpl<Integer, Integer> Random = new RandomAlgoCacheImpl<Integer, Integer>(CAPACITY);

	@Before
	public void setUp() throws Exception {
		for (int i = 0; i < CAPACITY; i++) {
			LRU.putElement(i, -i);
			NRU.putElement(i, -i);
			Random.putElement(i, -i);
		}
	}

	@Test
	public void LRUTest() {
		returned = LRU.putElement(CAPACITY + 1, CAPACITY + 1);
		System.out.println("LRU Returned number: " + returned);
		assertEquals(0, returned);
	}

//	@Test
////	public void NRUTest() {
////		NRU.putElement(CAPACITY + 1, CAPACITY + 1);
////	}
	
	@Test
	public void RandomTest() {
		returned = Random.putElement(CAPACITY + 1, CAPACITY + 1);
		System.out.println("Random Returned number: " + returned);
		assertEquals(0, returned);
	}
}
