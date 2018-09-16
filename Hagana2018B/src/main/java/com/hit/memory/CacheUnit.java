package com.hit.memory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.hit.algorithm.IAlgoCache;
import com.hit.dm.DataModel;
import com.hit.util.BackupAndRestore;

public class CacheUnit<T> {

	private Timer timer;
	private BackupAndRestore<T> BK;
	private IAlgoCache<Long, DataModel<T>> algo;
	private FileInputStream inputStream;
	private Map<Long, T> FileContent = new HashMap<Long, T>();

	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo, String filePathBackup) {
		BK = BK.getInstance();
		this.algo = algo;
		BK.backup(algo, filePathBackup);
		TimerTask backupTask = new TimerTask() {
			public void run() {
				BK.backup(algo, filePathBackup);
			}
		};
		timer = new Timer("BackupTimer");
		long delay = 1000L;
		long period = 5000L;
		timer.scheduleAtFixedRate(backupTask, delay, period);
	}

	public void restoreCacheUnit(String filePathRestore) throws IOException {
		try {
			inputStream = new FileInputStream(filePathRestore);
			ObjectInputStream ObjectInputStream = new ObjectInputStream(inputStream);
			FileContent = (HashMap<Long, T>) ObjectInputStream.readObject();
			ObjectInputStream.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Error, cannot find the file.");
		} catch (IOException ex) {
			System.out.println("Error, cannot read " + filePathRestore + "file.");
		} catch (Exception ex) {
			System.out.println("Error occured.");
		} finally {
			inputStream.close();
		}
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
