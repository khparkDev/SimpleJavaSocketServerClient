package com.khpark.queue;

import java.nio.channels.SocketChannel;
import java.util.Vector;

@SuppressWarnings("serial")
public class ChatChannel extends Vector<SocketChannel> {
	private static ChatChannel instance = new ChatChannel();

	public static ChatChannel getInstance() {
		if (instance == null) {

			synchronized (ChatChannel.class) {
				instance = new ChatChannel();
			}
		}

		return instance;
	}
}
