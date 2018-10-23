package com.hit.services;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitController<T> {

	private CacheUnitService<T> cacheUnitService;
	private CacheUnit<T> cacheUnit;
	public int capacity = 5;
	public String path;
	private IDao<Long, DataModel<T>> idao;
	public CacheUnitService<T> service;

	public CacheUnitController() {
		this.path = "datasource.txt";
		this.idao = null;
		this.cacheUnit = new CacheUnit<T>(new LRUAlgoCacheImpl<Long, DataModel<T>>(capacity));
		this.idao = new DaoFileImpl<>(path);
		this.cacheUnitService = new CacheUnitService<T>(cacheUnit, idao);
	}

	public boolean update(DataModel<T>[] dataModels) {
		if (cacheUnitService.update(dataModels)) {
			return true;
		}
		return false;
	}

	public boolean delete(DataModel<T>[] dataModels) {
		if (cacheUnitService.delete(dataModels)) {
			return true;
		}
		return false;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		return cacheUnitService.get(dataModels);
	}

	public String getStats() {
		return cacheUnitService.getStats();
	}
}
