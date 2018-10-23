package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Struct;
import java.util.ArrayList;

import com.hit.services.CacheUnitController;
import com.hit.util.CLI;

public class Server implements PropertyChangeListener, Runnable {

	private static final int SERVER = 12345;

	private static String SERVER_STATUS;

	private Thread thread;
	private ServerSocket listener;
	private Socket client;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private HandleRequest<String> request;
	private CacheUnitController<String> cacheUnitController;

	public Server() {
		try {
			this.listener = new ServerSocket(SERVER);
			this.cacheUnitController = new CacheUnitController<String>();
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	@Override
	public void run() {
		System.out.println("Server: server started\n");
		try {
			while (true) {
				System.out.println("Server: waititng for request...\n");
				client = listener.accept();
				System.out.println("Server: working\n");
				request = new HandleRequest<String>(client, cacheUnitController);
				thread = new Thread(request);
				thread.start();
				//thread.join();
				if (SERVER_STATUS.equals("shutdown")) {
					break;
				}

			}
		} catch (IOException e) {
			System.out.println("Server: Cannot connect.\n");
		} catch (Exception e) {
			System.out.println("Server: Some error occured.\n");
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		SERVER_STATUS = (String) evt.getNewValue();
	}

}