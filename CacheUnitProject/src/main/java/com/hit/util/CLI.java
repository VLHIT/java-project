package com.hit.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

import com.hit.server.Server;

public class CLI extends Observable implements Runnable {

	private static final int LENGTH = 2;
	private static final String START = "start";
	private static final String SHUTDOWN = "shutdown";

	private static String SERVER_STATUS = "not running";

	private Scanner scan;
	private OutputStream out;
	private String[] legalOperations;
	private PropertyChangeSupport support;
	private InetAddress address;
	private ArrayList<PropertyChangeListener> listener;

	public CLI(InputStream in, OutputStream out) {
		try {
			scan = new Scanner(in);
			this.out = out;
			this.legalOperations = new String[LENGTH];
			this.address = InetAddress.getLocalHost();
			this.listener = new ArrayList<PropertyChangeListener>();
			support = new PropertyChangeSupport(this);
			legalOperations[0] = START;
			legalOperations[1] = SHUTDOWN;
			write("Hello, legal operations are: ");
			for (int i = 0; i < LENGTH; i++) {
				write(legalOperations[i] + ", ");
			}
			System.out.println();
		} catch (Exception e) {
			System.out.println("Error");
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
		listener.add(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
		listener.add(pcl);
	}

	public void write(String string) {
		try {
			out.write(string.getBytes());
		} catch (IOException e) {
			System.out.println("Error, CLI Cannot write");
		}
	}

	@Override
	public void run() {
		while (true) {
			String tempString = null;
			write("CLI: Please enter your command: ");
			tempString = scan.next();
			if (isLegalOperation(tempString)) {
				if (!tempString.equals(SHUTDOWN)) {
					try {
						if (tempString.equals(START) && !SERVER_STATUS.equals("start")) {
							SERVER_STATUS = START;
							//Socket mySocket = new Socket(address, 12345);
							for (PropertyChangeListener listener : listener) {
								if (listener instanceof Server) {
									support.firePropertyChange(SERVER_STATUS, "not started", START);
									new Thread((Server) listener).start();
								}
							}
							write("CLI: Strarting server...\n");
						}
					} catch (Exception e) {
						System.out.println("CLI: Failed to start\n");
					}
				} else {
					for (PropertyChangeListener listener : listener) {
						if (listener instanceof Server) {
							support.firePropertyChange(SERVER_STATUS, SERVER_STATUS, SHUTDOWN);
						}
					}
					write("CLI: Shutdown server\n");
					SERVER_STATUS = SHUTDOWN;
				}
			} else {
				write("CLI: Not valid command\n");
			}
		}
	}

	private boolean isLegalOperation(String operation) {
		boolean isLegal = false;
		for (String legal : legalOperations) {
			if (operation.equals(legal)) {
				isLegal = true;
				break;
			}
		}
		return isLegal;
	}
}
