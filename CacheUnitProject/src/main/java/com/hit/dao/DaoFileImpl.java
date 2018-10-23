package com.hit.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hit.dm.DataModel;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {
	private static final int READ = 0;
	private static final int WRITE = 1;
	private static final int DEFAULT_CAPACITY = 100;

	private final String filePath;

	private static ArrayList<Long> Ids;
	private int capacity;
	private DataModel<T> dataModel = null;
	private FileInputStream inputStream = null;
	private FileOutputStream outputStream = null;
	private Map<Long, T> FileContent = new HashMap<Long, T>();
	private int totalNumberOfDataModels;

	public DaoFileImpl(String filePath) {
		this.filePath = filePath;
		this.capacity = DEFAULT_CAPACITY;
		this.totalNumberOfDataModels = 0;
	}

	public DaoFileImpl(String filePath, int capacity) {
		this.filePath = filePath;
		this.capacity = capacity;
	}

	public void save(DataModel<T> t) {
		try {
			// ReadWriteFile(FileContent, filePath, READ);
			if (FileContent.containsKey(t.getDataModelId())) {
				FileContent.replace(t.getDataModelId(), t.getContent());
			} else {
				FileContent.put(t.getDataModelId(), t.getContent());
				capacity--;
			}
			ReadWriteFile(FileContent, filePath, WRITE);
		} catch (FileNotFoundException ex) {
			System.out.println("Error, cannot find the file.");
		} catch (IOException ex) {
			System.out.println("Error, cannot read " + filePath + " file, " + "or file is empty");
		} catch (Exception ex) {
			System.out.println("Error occured.");
		}
	}

	public void delete(DataModel<T> t) {
		try {
			ReadWriteFile(FileContent, filePath, READ);
			if (FileContent.containsKey(t.getDataModelId())) {
				FileContent.remove(t.getDataModelId());
				capacity++;
			}
			ReadWriteFile(FileContent, filePath, WRITE);
		} catch (FileNotFoundException ex) {
			System.out.println("Error, cannot find the file.");
		} catch (IOException ex) {
			System.out.println("Error, cannot read " + filePath + "file.");
		} catch (Exception ex) {
			System.out.println("Error occured.");
		}
	}

	public DataModel<T> find(Long id) {
		try {
			ReadWriteFile(FileContent, filePath, READ);
			if (FileContent.containsKey(id)) {
				dataModel = new DataModel<T>(id, FileContent.get(id));
			} else
				dataModel = null;
		} catch (FileNotFoundException ex) {
			System.out.println("Error, cannot find the file.");
		} catch (IOException ex) {
			System.out.println("Error, cannot read " + filePath + "file.");
		} catch (Exception ex) {
			System.out.println("Error occured.");
		}
		return dataModel;
	}

	public int countAll() {
		totalNumberOfDataModels = 0;
		try {
			ReadWriteFile(FileContent, filePath, READ);
			for (Long key : FileContent.keySet()) {
				totalNumberOfDataModels++;
			}
		} catch (FileNotFoundException ex) {
			System.out.println("Error, cannot find the file.");
		} catch (IOException ex) {
			System.out.println("Error, cannot read " + filePath + "file.");
		} catch (Exception ex) {
			System.out.println("Error occured.");
		}
		return totalNumberOfDataModels;
	}

	private <K, V> void ReadWriteFile(Map<K, V> map, String filePath, int IO) throws IOException {
		boolean ReadWrite = false;
		try {
			if (IO == READ) {
				ReadWrite = false;
				inputStream = new FileInputStream(filePath);
				ObjectInputStream ObjectInputStream = new ObjectInputStream(inputStream);
				FileContent = (Map<Long, T>) ObjectInputStream.readObject();
				ObjectInputStream.close();
			} else if (IO == WRITE) {
				ReadWrite = true;
				outputStream = new FileOutputStream(filePath);
				ObjectOutputStream ObjectOutputStream = new ObjectOutputStream(outputStream);
				ObjectOutputStream.writeObject(FileContent);
				ObjectOutputStream.close();
			}
		} catch (FileNotFoundException ex) {
			System.out.println("Error, cannot find the file.");
		} catch (IOException ex) {
			System.out.println("Error, cannot read " + filePath + "file.");
		} catch (Exception ex) {
			System.out.println("Error occured.");
		} finally {
			if (ReadWrite == false) {
				inputStream.close();
			} else
				outputStream.close();
		}
	}
}
