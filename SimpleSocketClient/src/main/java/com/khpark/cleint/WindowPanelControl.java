package com.khpark.cleint;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowPanelControl {
	private Frame mainFrame;
	private Panel controlPanel;
	private TextField idtf;
	private TextArea textArea = new TextArea("", 20, 75);

	public String getId() {
		return idtf.getText();
	}

	public WindowPanelControl(String id) {
		textArea.setEditable(false);
		idtf = new TextField(50);
		idtf.setColumns(10);
		idtf.setText(id);
		prepareGUI();
	}

	public void setMessage(String message) {
		mainFrame.toFront();
		textArea.append("\n");
		textArea.append(message);
	}

	private void prepareGUI() {
		mainFrame = new Frame("SimpleCSocketClient");
		mainFrame.setSize(600, 420);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		controlPanel = new Panel();
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(controlPanel);
	}

	public void showPanel() {
		Label idLabel = new Label("Id", Label.RIGHT);
		TextField tf = new TextField(50);
		tf.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						SimpleClient.INSTANCE.sendMessage(tf.getText());
						tf.setText("");
						tf.setCursor(new Cursor(0));
						tf.setFocusable(true);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent paramKeyEvent) {
			}
		});

		tf.setFocusable(true);
		controlPanel.add(idLabel);
		controlPanel.add(idtf);
		controlPanel.add(tf);
		controlPanel.add(textArea);
		mainFrame.setVisible(true);
	}
}