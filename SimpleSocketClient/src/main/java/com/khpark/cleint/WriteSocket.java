package com.khpark.cleint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WriteSocket {
	private String id;
	private SocketChannel socketChannel = null;
	private ExecutorService executor = Executors.newFixedThreadPool(1);

	public WriteSocket(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public void start(String id) {
		this.id = id;
		executor.submit(new WriteSocketThread(socketChannel));
	}

	private class WriteSocketThread implements Runnable {
		private SocketChannel socketChannel = null;

		public WriteSocketThread(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
		}

		public void run() {
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

			try {
				System.out.print("# " + id + "(나) ] ");

				while (!Thread.currentThread().isInterrupted()) {
					buffer.clear();
					BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
					String message = in.readLine();

					if (message.equals("exit") || message.equals("quit") || message.equals("shutdown")) {
						System.exit(0);
					}

					String sendMessage = id + Constants.DELIMETER + message;
					buffer.put(sendMessage.getBytes());
					buffer.flip();

					socketChannel.write(buffer);
					System.out.println();
					System.out.print("# " + id + "(나) ] ");
				}
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
