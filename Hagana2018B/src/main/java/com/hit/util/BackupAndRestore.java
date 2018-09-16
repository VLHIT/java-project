package com.hit.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.hit.algorithm.IAlgoCache;
import com.hit.dm.DataModel;

public class BackupAndRestore<T> {
	private static final int READ = 0;
	private static final int WRITE = 1;
	private static BackupAndRestore instance = null;

	private String filePath;

	private FileInputStream inputStream = null;
	private FileOutputStream outputStream = null;
	private Map<Long, T> FileContent = new HashMap<Long, T>();

	private BackupAndRestore() {

	}

	public static BackupAndRestore getInstance() {
		if (instance == null) {
			instance = new BackupAndRestore();
		}
		return instance;
	}

	public void backup(IAlgoCache<Long, DataModel<T>> algo, String filePathBackup) {
		this.filePath = filePathBackup;
		try {
			ReadWriteFile(FileContent, filePath, WRITE);
		} catch (FileNotFoundException ex) {
			System.out.println("Error, cannot find the file.");
		} catch (IOException ex) {
			System.out.println("Error, cannot read " + filePath + "file.");
		} catch (Exception ex) {
			System.out.println("Error occured.");
		}
	}

	public HashMap<Long, T> restore(String filePathRestore) {
		try {
			ReadWriteFile(FileContent, filePath, READ);
		} catch (FileNotFoundException ex) {
			System.out.println("Error, cannot find the file.");
		} catch (IOException ex) {
			System.out.println("Error, cannot read " + filePath + "file.");
		} catch (Exception ex) {
			System.out.println("Error occured.");
		}
		
		return (HashMap<Long, T>)FileContent;
	}

	private <K, V> void ReadWriteFile(Map<Long, T> map, String filePath, int IO) throws IOException {
		boolean ReadWrite = false;
		try {
			if (IO == READ) {
				ReadWrite = false;
				inputStream = new FileInputStream(filePath);
				ObjectInputStream ObjectInputStream = new ObjectInputStream(inputStream);
				FileContent = (HashMap<Long, T>) ObjectInputStream.readObject();
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
