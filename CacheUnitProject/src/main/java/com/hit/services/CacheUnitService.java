package com.hit.services;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitService<T> {
	private static final int CAPACITY = 2;

	private LRUAlgoCacheImpl<Long, DataModel<T>> algo = new LRUAlgoCacheImpl<Long, DataModel<T>>(CAPACITY);
	private CacheUnit<T> Cache = new CacheUnit<T>(algo);
	private IDao<Long, T> IDao;
	private Long[] fromCache = new Long[CAPACITY];

	public boolean update(DataModel<T>[] dataModels) {
		return true;
	}

	public boolean delete(DataModel<T>[] dataModels) {
		return true;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		return Cache.getDataModels(fromCache);
	}

	private boolean findInCache(DataModel<T>[] dataModels) {
		boolean isFound = false;
		return isFound;
	}
}
