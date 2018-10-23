package com.hit.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileSystemView;

public class CacheUnitView {
	private static final String LOAD = "load";
	private static final String SHOW_STATS = "show";
	private static String FILE_PATH;
	private static String PRESSED_BUTTON;
	private static boolean PRESSED = false;
	private JTextArea txtArea;
	private JScrollPane scroll;

	private class CachUnitPanel extends JPanel implements ActionListener {
		private JButton buttonLoad;
		private JButton buttonShow;

		public CachUnitPanel() {
			buttonLoad = new JButton("Load a Request");
			buttonLoad.addActionListener(this);
			buttonShow = new JButton("Show Statistics");
			buttonShow.addActionListener(this);
			txtArea = new JTextArea(17, 25);
			txtArea.setEditable(false);
			// txtArea.setMaximumSize(new Dimension(100,100));
			scroll = new JScrollPane(txtArea);
			scroll.setMaximumSize(new Dimension(15, 20));
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			add(buttonLoad);
			add(buttonShow);
			// add(txtArea);
			add(scroll);
		}

		public void actionPerformed(ActionEvent event) {
			Object button = event.getSource();
			if (button.equals(buttonShow)) {
				PRESSED_BUTTON = SHOW_STATS;
				PRESSED = true;
			} else if (button.equals(buttonLoad)) {
				PRESSED_BUTTON = LOAD;
				PRESSED = true;
			}
		}
	}

	private PropertyChangeSupport support;

	public CacheUnitView() {
		support = new PropertyChangeSupport(this);
	}

	public void start() {
		JFrame frame = new JFrame("Cache Unit View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CachUnitPanel CachUnitPanel = new CachUnitPanel();
		CachUnitPanel.setOpaque(true);
		frame.setContentPane(CachUnitPanel);
		frame.setPreferredSize(new Dimension(300, 350));
		// frame.setMaximumSize(new Dimension(300,350));
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
		while (true) {
			System.out.println("UI: waiting");
			if (PRESSED) {
				System.out.println("");
				if (PRESSED_BUTTON.equals(LOAD)) {
					/*
					 * new file selection window only for request
					 */
					JFileChooser chooserUI = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					int returnedValue = chooserUI.showOpenDialog(null);
					File file = chooserUI.getSelectedFile();
					if (!(returnedValue == JFileChooser.CANCEL_OPTION)) {
						FILE_PATH = file.getAbsolutePath();
						support.firePropertyChange(LOAD, null, FILE_PATH);
						PRESSED = false;
					}
					PRESSED = false;
				} else if (PRESSED_BUTTON.equals(SHOW_STATS)) {
					support.firePropertyChange(SHOW_STATS, null, SHOW_STATS);
					PRESSED = false;
				}

			}
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}

	public <T> void updateUIData(T t) {
		txtArea.setText((String) t);
	}
}
