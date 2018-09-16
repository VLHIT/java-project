package com.hit.memory;

import com.hit.algorithm.*;
import com.hit.dm.DataModel;

public class CacheUnit<T> {

	IAlgoCache<Long, DataModel<T>> algo = null;

	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {

		this.algo = algo;
	}

	public DataModel<T>[] getDataModels(Long[] ids) {
		DataModel<T> temp;
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
		for (DataModel<T> data : datamodels) {
			algo.putElement(data.getDataModelId(), (DataModel<T>) data);
		}
		return datamodels;
	}

	public void removeDataModels(Long[] ids) {
		for (long id : ids) {
			algo.removeElement(id);
		}
	}

}
