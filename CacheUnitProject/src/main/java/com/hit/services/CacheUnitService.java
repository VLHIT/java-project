package com.hit.services;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitService<T> {
	private final static int CAPACITY = 5;
	private static boolean inCache = false;

	private LRUAlgoCacheImpl<Long, DataModel<T>> algo = new LRUAlgoCacheImpl<Long, DataModel<T>>(CAPACITY);
	private CacheUnit<T> cache = new CacheUnit<T>(algo);
	private IDao<Long, DataModel<T>> idao;
	private DataModel<T>[] fromCache;
	private Long[] ids;
	private int swaps;
	private int requestsCounter;
	private int dataModelsCounter;

	public CacheUnitService(CacheUnit<T> cacheUnit, IDao<Long, DataModel<T>> iDao) {
		this.cache = cacheUnit;
		this.idao = iDao;
		this.requestsCounter = 0;
		this.swaps = 0;
		this.dataModelsCounter = idao.countAll();
	}

	public boolean update(DataModel<T>[] dataModels) {
		requestsCounter++;
		int iterator = 0;
		boolean success = false;
		DataModel<T>[] dataModelsArray = new DataModel[dataModels.length];
		DataModel<T>[] dataModelsCacheUpdate = new DataModel[dataModels.length];
		ids = new Long[dataModels.length];

		for (DataModel<T> dm : dataModels) {
			ids[iterator++] = dm.getDataModelId();
		}
		fromCache = findInCache(ids);
		if (inCache) {
			/*
			 * not all in cache.
			 */
			if (fromCache.length != dataModels.length) {
				iterator = 0;
				for (DataModel<T> dm : dataModels) {
					for (DataModel<T> dmInCache : fromCache) {
						if (dm.getDataModelId().equals(dmInCache.getDataModelId())) {
							dataModelsCacheUpdate[iterator++] = dm;
							idao.save(dm);
						} else {
							dataModelsCacheUpdate[iterator++] = dm;
							idao.save(dm);
							swaps++;
						}
					}
					if (!inCache) {
						dataModelsArray[iterator++] = idao.find(dm.getDataModelId());
						swaps++;
					}
				}
				cache.putDataModels(dataModelsCacheUpdate);
			} 
			/*
			 * all NOT in cache.
			 */
			else {
				iterator = 0;
				for (DataModel<T> dm : dataModels) {
					dataModelsCacheUpdate[iterator++] = dm;
					idao.save(dm);
					swaps++;
				}
				cache.putDataModels(dataModelsCacheUpdate);
			}
		} else {
			iterator = 0;
			for (DataModel<T> dm : dataModels) {
				dataModelsArray[iterator++] = idao.find(dm.getDataModelId());
				idao.save(dm);
				swaps++;
			}
			success = true;
			cache.putDataModels(dataModels);
		}
		inCache = false;
		dataModelsCounter = idao.countAll();
		return success;
	}

	public boolean delete(DataModel<T>[] dataModels) {
		requestsCounter++;
		boolean success = false;
		int iterator = 0;
		ids = new Long[dataModels.length];
		for (DataModel<T> dm : dataModels) {
			ids[iterator++] = dm.getDataModelId();
			idao.delete(dm);
			success = true;
		}
		cache.removeDataModels(ids);
		dataModelsCounter = idao.countAll();
		return success;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		requestsCounter++;
		boolean found = false;
		DataModel<T> tempDM = new DataModel((long) 0, null);
		int iterator = 0;
		DataModel<T>[] dataModelsArray = new DataModel[dataModels.length];
		ids = new Long[dataModels.length];

		for (DataModel<T> dm : dataModels) {
			ids[iterator++] = dm.getDataModelId();
		}
		fromCache = findInCache(ids);
		if (inCache) {
			if (fromCache.length != dataModels.length) {
				for (DataModel<T> dm : dataModels) {
					for (DataModel<T> dmInCache : fromCache) {
						if (dm.getDataModelId().equals(dmInCache.getDataModelId())) {
							continue;
						}
					}
					if (!inCache) {
						tempDM = idao.find(dm.getDataModelId());
						if (tempDM != null) {
							dataModelsArray[iterator++] = tempDM;
							swaps++;
							found = true;
						} else {
							tempDM.setDataModelId(dm.getDataModelId());
							dataModelsArray[iterator++] = tempDM;
						}
					}
				}
			} else {
				return fromCache;
			}
		} else {
			iterator = 0;
			for (DataModel<T> dm : dataModels) {
				tempDM = idao.find(dm.getDataModelId());
				if (tempDM != null) {
					dataModelsArray[iterator++] = tempDM;
					swaps++;
					found = true;
				} else {
					tempDM = new DataModel(dm.getDataModelId(), null);
					dataModelsArray[iterator++] = tempDM;
				}
			}
			if (found) {
				cache.putDataModels(dataModelsArray);
			}
		}
		inCache = false;
		return dataModelsArray;

	}

	private DataModel<T>[] findInCache(Long[] dataModels) {
		int counter = 0;
		DataModel<T>[] temp = new DataModel[dataModels.length];
		temp = cache.getDataModels(dataModels);
		for (DataModel<T> dm : temp) {
			if (!(dm == null)) {
				counter++;
				inCache = true;
			}
		}
		if (counter > 0) {
			fromCache = new DataModel[counter];
			counter = 0;
			for (DataModel<T> dm : temp) {
				if (!(dm == null)) {
					fromCache[counter++] = dm;
				}
			}
		}
		return fromCache;
	}

	public String getStats() {
		return String.format(
				"Capacity: %d\n" + "Algorithm: LRU\n" + "Total_number_of_requests: %d\n"
						+ "Total_number_of_data_models: %d\n" + "Total_number_of_swaps: %d\n",
				CAPACITY, requestsCounter, dataModelsCounter, swaps);
	}
}
