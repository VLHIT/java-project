package com.hit.memory;

import com.hit.algorithm.*;
import com.hit.dm.DataModel;

public class CacheUnit<T> {

	IAlgoCache<Long, DataModel<T>> algo = null;

	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {

		this.algo = algo;
	}

	public DataModel<T>[] getDataModels(Long[] ids) {
		DataModel<T> temp = new DataModel((long) 0, null);
		int counter = 0;
		DataModel<T>[] DataModelArray = new DataModel[ids.length];
		for (long id : ids) {
			temp = (DataModel<T>) algo.getElement(id);
			if (temp != null) {
				DataModelArray[counter++] = temp;
			}
		}
		return DataModelArray;

	}

	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {
		DataModel<T> temp = new DataModel((long) 0, null);
		DataModel<T>[] DataModelArray = new DataModel[datamodels.length];
		int counter = 0;
		for (DataModel<T> data : datamodels) {
			temp = algo.putElement(data.getDataModelId(), (DataModel<T>) data);
			DataModelArray[counter++] = temp;
		}
		return DataModelArray;
	}

	public void removeDataModels(Long[] ids) {
		for (long id : ids) {
			algo.removeElement(id);
		}
	}

}
