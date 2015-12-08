package com.khpark.cleint;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WriteSocket {
	private ExecutorService executor = Executors.newFixedThreadPool(1);
	private SocketChannel socketChannel = null;
	private WindowPanelControl wpc;

	public WriteSocket(SocketChannel socketChannel, WindowPanelControl wpc) {
		this.socketChannel = socketChannel;
		this.wpc = wpc;
	}

	public void start(String message) {
		executor.submit(new WriteSocketThread(socketChannel, message));
	}

	private class WriteSocketThread implements Runnable {
		private SocketChannel socketChannel = null;
		private String message;

		public WriteSocketThread(SocketChannel socketChannel, String message) {
			this.socketChannel = socketChannel;
			this.message = message;
		}

		public void run() {
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

			try {
				if (message.equals("exit") || message.equals("quit") || message.equals("shutdown")) {
					socketChannel.close();
					System.exit(0);
				}

				String sendMessage = wpc.getId() + Constants.DELIMETER + message;
				buffer.put(sendMessage.getBytes());
				buffer.flip();
				socketChannel.write(buffer);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (buffer != null) {
					buffer.clear();
					buffer = null;
				}
			}
		}
	}
}
