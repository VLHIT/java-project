package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
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

	public Server() {
		try {
			listener = new ServerSocket(SERVER);
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	@Override
	public void run() {
		System.out.println("server started");
		try {
			while (true) {
				client = listener.accept();
				HandleRequest<Request<String>> request = new HandleRequest<Request<String>>(client, new CacheUnitController<Request<String>>());
				System.out.println("Server started.");
				thread = new Thread(request);
				thread.start();
			}
		} catch (IOException e) {
			System.out.println("Cannot connect.");
		} catch (Exception e) {
			System.out.println("Some error occured.");
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		SERVER_STATUS = (String) evt.getNewValue();
	}

}