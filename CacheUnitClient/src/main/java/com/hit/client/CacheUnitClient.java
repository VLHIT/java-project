package com.hit.client;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CacheUnitClient {
	private static final String LOAD = "load";
	private static final String SHOW_STATS = "statistics";

	private InetAddress address;
	private Socket myServerSocket;
	private String request;
	private String answer;
	private String fileToString;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	public CacheUnitClient() {
		try {
			this.address = InetAddress.getLocalHost();

		} catch (Exception e) {
			System.out.println("Error, cannot get IP address");
		}
	}

	/*
	 * Method send string request directly to the socket and waiting for answer
	 * returns answer
	 */
	public String send(String request) {
		try {
			myServerSocket = new Socket(address, 12345);
			if (!request.equals("GET_STATS")) {
				fileToString = new String(Files.readAllBytes(Paths.get(request))); // reads whole file and saves into
																					// string
				objectOutputStream = new ObjectOutputStream(myServerSocket.getOutputStream());
				objectOutputStream.writeObject(fileToString);
				objectInputStream = null;
				//while (true) {
					answer = null;
					objectInputStream = new ObjectInputStream(myServerSocket.getInputStream());
					answer = (String) objectInputStream.readObject();
					if (objectInputStream != null) {
						//break;
					}
				//}
			} else {
				objectOutputStream = new ObjectOutputStream(myServerSocket.getOutputStream());
				objectOutputStream.writeObject(request);
				objectInputStream = null;
				//while (true) {
					answer = null;
					objectInputStream = new ObjectInputStream(myServerSocket.getInputStream());
					answer = (String) objectInputStream.readObject();
					if (objectInputStream != null) {
						//break;
					}
				//}
			}
		} catch (Exception e) {
			System.out.println("Error\n");
		}
		return answer;

	}
}
