package com.hit.memory;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.NRUAlgoCacheImpl;
import com.hit.algorithm.RandomAlgoCacheImpl;
import com.hit.dm.DataModel;

public class CacheUnitTest {
	private static final int LENGTH = 2;

	private static DataModel<String>[] dataModelArr;
	private static DataModel<String>[] dataModelArrBack;
	private static IAlgoCache<Long, DataModel<String>> algo1;
	private static IAlgoCache<Long, DataModel<String>> algo2;
	private static IAlgoCache<Long, DataModel<String>> algo3;
	private static CacheUnit<String> cacheUnit1;
	private static CacheUnit<String> cacheUnit2;
	private static CacheUnit<String> cacheUnit3;

	private Long[] ids;
	private Long id;
	private String content;

	@Before
	public void setUp() {
		algo1 = new LRUAlgoCacheImpl<>(LENGTH);
		algo2 = new NRUAlgoCacheImpl<>(LENGTH);
		algo3 = new RandomAlgoCacheImpl<>(LENGTH);
		cacheUnit1 = new CacheUnit<String>(algo1,
				"C:\\Users\\vadik\\eclipse-workspace\\Hagana2018B\\src\\main\\resources\\backup.txt");
		cacheUnit2 = new CacheUnit<String>(algo2,
				"C:\\Users\\vadik\\eclipse-workspace\\Hagana2018B\\src\\main\\resources\\backup.txt");
		cacheUnit3 = new CacheUnit<String>(algo3,
				"C:\\Users\\vadik\\eclipse-workspace\\Hagana2018B\\src\\main\\resources\\backup.txt");
		dataModelArr = new DataModel[LENGTH];
		dataModelArrBack = new DataModel[LENGTH];

		ids = new Long[LENGTH];
		id = (long) 1;
		content = "DataModel Content";

		for (int i = 0; i < LENGTH; i++) {
			ids[i] = id++;
			dataModelArr[i] = new DataModel<String>((long) 0, null);
			dataModelArrBack[i] = new DataModel<String>((long) 0, null);
			dataModelArr[i].setDataModelId(ids[i]);
			dataModelArr[i].setContent(content);
		}
	}

	@Test
	public void BackupDataModelsTest1() throws IOException {
		dataModelArr = cacheUnit1.putDataModels(dataModelArr);
		cacheUnit1.restoreCacheUnit("C:\\Users\\vadik\\eclipse-workspace\\Hagana2018B\\src\\main\\resources\\backup.txt");
		dataModelArrBack = cacheUnit1.getDataModels(ids);
		assertArrayEquals(dataModelArr, dataModelArrBack);
	}
	
	@Test
	public void BackupDataModelsTest2() throws IOException {
		dataModelArr = cacheUnit2.putDataModels(dataModelArr);
		cacheUnit1.restoreCacheUnit("C:\\Users\\vadik\\eclipse-workspace\\Hagana2018B\\src\\main\\resources\\backup.txt");
		dataModelArrBack = cacheUnit2.getDataModels(ids);
		assertArrayEquals(dataModelArr, dataModelArrBack);
	}
	
	@Test
	public void BackupDataModelsTest3() throws IOException {
		dataModelArr = cacheUnit3.putDataModels(dataModelArr);
		cacheUnit1.restoreCacheUnit("C:\\Users\\vadik\\eclipse-workspace\\Hagana2018B\\src\\main\\resources\\backup.txt");
		dataModelArrBack = cacheUnit3.getDataModels(ids);
		assertArrayEquals(dataModelArr, dataModelArrBack);
	}

	
}
