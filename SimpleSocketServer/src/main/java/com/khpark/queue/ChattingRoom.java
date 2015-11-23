package com.khpark.queue;

import java.nio.channels.SocketChannel;
import java.util.Vector;

@SuppressWarnings("serial")
public class ChattingRoom extends Vector<SocketChannel> {
	private static ChattingRoom instance = new ChattingRoom();

	public static ChattingRoom getInstance() {
		if (instance == null) {
			
			synchronized (ChattingRoom.class) {
				instance = new ChattingRoom();
			}
		}
		return instance;
	}

	private ChattingRoom() {
	}

}
