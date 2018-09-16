package com.hit.services;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitService<T> {
	private static final int CAPACITY = 2;

	private LRUAlgoCacheImpl<Long, DataModel<T>> algo = new LRUAlgoCacheImpl<Long, DataModel<T>>(CAPACITY);
	private CacheUnit<T> cache = new CacheUnit<T>(algo);
	private IDao<Long, T> idao;
	private DataModel<T>[] fromCache = new DataModel[CAPACITY];

	public boolean update(DataModel<T>[] dataModels) {
		
		return false;
	}

	public boolean delete(DataModel<T>[] dataModels) {
		return true;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		return fromCache;
	}

	private boolean findInCache(Long[] dataModels) {
		boolean isFound = false;
		fromCache = cache.getDataModels(dataModels);

		return isFound;
	}
}
