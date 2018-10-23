package com.hit.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

public class HandleRequest<T> implements Runnable {

	private Socket socket;
	private CacheUnitController<T> cacheUnitController;
	private Type ref;
	private String req;
	private Request<DataModel<T>[]> request;
	private String action;
	private Map<String, String> headers;
	private DataModel<T>[] body;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private StringBuilder stringToReturn;
	private boolean answer;
	private String algorithm;
	private int index;
	private int index2;
	private int capacity;
	private int requests;
	private int datamodelRequests;
	private int datamodelSwaps;

	public HandleRequest(Socket s, CacheUnitController<T> controller) {
		try {
			this.socket = s;
			this.cacheUnitController = controller;
			this.headers = new HashMap<String, String>();
			this.stringToReturn = new StringBuilder();
		} catch (Exception ex) {

		}
	}

	@Override
	public synchronized void run() {
		try {
			this.objectInputStream = new ObjectInputStream(socket.getInputStream());
			this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			req = (String) objectInputStream.readObject();
			if (!req.equals("GET_STATS")) {
				this.ref = new TypeToken<Request<DataModel<T>[]>>() {
				}.getType();
				request = new Gson().fromJson(req, ref);
				this.headers = new HashMap<String, String>(request.getHeaders());
				this.body = request.getBody();

				if (headers.containsValue("UPDATE")) {
					answer = cacheUnitController.update(request.getBody());
					if (answer == true) {
						stringToReturn.append("Successfully updated.\n");
					} else {
						stringToReturn.append("Cannot update.\n");
					}

				} else if (headers.containsValue("GET")) {
					body = cacheUnitController.get(request.getBody());
					for(DataModel<T> dm : body) {
						if(dm.getContent() != null) {
							stringToReturn.append(dm.toString() + "\n");
						}
						else {
							stringToReturn.append("ID: " + dm.getDataModelId() + " is not exist.\n");
						}
					}
				} else if (headers.containsValue("DELETE")) {
					answer = cacheUnitController.delete(request.getBody());
					if (answer == true) {
						stringToReturn.append("Successfully deleted.\n");
					} else {
						stringToReturn.append("Cannot delete.\n");
					}
				} else {

				}
			} else {
				stringToReturn.append(cacheUnitController.getStats());
			}
		} catch (Exception e) {
			System.out.println("Error");
		}finally {
			try {
				objectOutputStream.writeObject(stringToReturn.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*private String statsUpdate(String stats) {
		String statsCopy = null;
		String tempLine = null;
		String tempLineSubstring = null;
		while (stats.length() > 0) {
			statsCopy = stats;
			index = statsCopy.indexOf("\n");
			tempLine = statsCopy.substring(0, index);
			// tempLineSubstring = tempLine.substring(index, stats.length());
			if (tempLine.contains("Capacity: ")) {
				index2 = tempLine.indexOf(" ");
				tempLineSubstring = tempLine.substring(index2 + 1, tempLine.length());
				capacity += Integer.parseInt(tempLineSubstring);
			}
			if (tempLine.contains("Algorithm: ")) {
				index2 = tempLine.indexOf(" ");
				tempLineSubstring = tempLine.substring(index2 + 1, tempLine.length());
				algorithm = tempLineSubstring;
			}
			if (tempLine.contains("Total_number_of_requests: ")) {
				index2 = tempLine.indexOf(" ");
				tempLineSubstring = tempLine.substring(index2 + 1, tempLine.length());
				requests += Integer.parseInt(tempLineSubstring);
			}
			if (tempLine.contains("Total_number_of_data_models: ")) {
				index2 = tempLine.indexOf(" ");
				tempLineSubstring = tempLine.substring(index2 + 1, tempLine.length());
				datamodelRequests += Integer.parseInt(tempLineSubstring);
			}
			if (tempLine.contains("Total_number_of_swaps: ")) {
				index2 = tempLine.indexOf(" ");
				tempLineSubstring = tempLine.substring(index2 + 1, tempLine.length());
				datamodelSwaps += Integer.parseInt(tempLineSubstring);
			}
			stats = stats.substring(index + 1, stats.length());
		}
		stats = String.format(
				"Capacity: %d\n" + "Algorithm: LRU\n" + "Total number of requests: %d\n"
						+ "Total number of data models: %d\n" + "Total number of swaps: %d\n",
				capacity, requests, datamodelRequests, datamodelSwaps);

		return stats;
	}*/
}
