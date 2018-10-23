package com.hit.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.hit.view.CacheUnitView;

public class CacheUnitClientObserver implements PropertyChangeListener {
	private String path;
	private CacheUnitClient client;
	private CacheUnitView ui;

	public CacheUnitClientObserver() {
		this.client = new CacheUnitClient();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof CacheUnitView) {
			ui = (CacheUnitView) evt.getSource();
			if (evt.getPropertyName().equals("show")) {
				ui.updateUIData(client.send("GET_STATS"));
			} else {
				ui.updateUIData(client.send((String) evt.getNewValue()));
			}
		}
	}
}
