package com.hit.memory;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;

public class CacheUnitTest {
	private static final int LENGTH = 2;

	private static DataModel<String>[] dataModelArr;
	private static DataModel<String>[] dataModelArrBack;
	private static IAlgoCache<Long, DataModel<String>> algo;
	private static CacheUnit<String> cacheUnit;
	private static DaoFileImpl<String> DaoFileImpl;

	private Long[] ids;
	private Long id;
	private String content;

	@Before
	public void setUp() {
		algo = new LRUAlgoCacheImpl<>(LENGTH);
		cacheUnit = new CacheUnit<String>(algo);
		dataModelArr = new DataModel[LENGTH];
		dataModelArrBack = new DataModel[LENGTH];
		DaoFileImpl = new DaoFileImpl<String>(
				"C:\\Users\\vadik\\eclipse-workspace\\CacheUnitProject\\src\\main\\resources\\datasource.txt", LENGTH);

		ids = new Long[LENGTH];
		id = (long) 1;
		content = "DataModel Content";

		for (int i = 0; i < LENGTH; i++) {
			ids[i] = id++;
			dataModelArr[i] = new DataModel<String>((long) 0, null);
			dataModelArrBack[i] = new DataModel<String>((long) 0, null);
			dataModelArr[i].setDataModelId(ids[i]);
			dataModelArr[i].setContent(content);
			DaoFileImpl.save(dataModelArr[i]);
		}
	}

	@Test
	public void putAndGetDataModelsTest() {
		dataModelArr = cacheUnit.putDataModels(dataModelArr);
		dataModelArrBack = cacheUnit.getDataModels(ids);
		assertArrayEquals(dataModelArr, dataModelArrBack);
	}

	@Test
	public void removeDataModelsTest() {
		cacheUnit.removeDataModels(ids);
		for (int i = 0; i < LENGTH; i++) {
			dataModelArr[i] = null;
		}
		dataModelArrBack = cacheUnit.getDataModels(ids);
		assertArrayEquals(dataModelArr, dataModelArrBack);
	}

	@Test
	public void daoFileFindTest() {
		int i = 0;
		for (long id : ids) {
			dataModelArrBack[i++] = DaoFileImpl.find(id);
		}
		assertArrayEquals(dataModelArr, dataModelArrBack);
	}

	@Test
	public void daoFileDeleteTest() {
		int counter = 0;
		for (int i = 0; i < dataModelArr.length; i++) {
			if ((i % 2) != 0) {
				DaoFileImpl.delete(dataModelArr[i]);
				dataModelArr[i] = null;
			}
		}
		for (long id : ids) {
			dataModelArrBack[counter++] = DaoFileImpl.find(id);
		}
		assertArrayEquals(dataModelArr, dataModelArrBack);
	}
}
