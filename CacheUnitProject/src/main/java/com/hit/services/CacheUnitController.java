package com.hit.services;

import com.hit.dm.DataModel;

public class CacheUnitController<T>{
	
	private CacheUnitService<T> cacheUnitService = new CacheUnitService<T>();
	
	
	public boolean update(DataModel<T>[] dataModels) {
		if(cacheUnitService.update(dataModels)) {
			return true;
		}
		return false;
	}

	public boolean delete(DataModel<T>[] dataModels) {
		if(cacheUnitService.delete(dataModels)) {
			return true;
		}
		return false;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		return cacheUnitService.get(dataModels);
	}
}
