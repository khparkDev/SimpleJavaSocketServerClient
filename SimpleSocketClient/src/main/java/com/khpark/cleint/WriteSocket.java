package com.khpark.cleint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriteSocket {
	private SocketChannel socketChannel = null;

	public WriteSocket(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public void start() {
		Thread t = new WorkerThread(socketChannel);
		t.start();
	}

	private class WorkerThread extends Thread {
		private SocketChannel socketChannel = null;

		public WorkerThread(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
		}

		public void run() {
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

			try {
				while (!Thread.currentThread().isInterrupted()) {
					buffer.clear();
					BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
					String message = in.readLine();

					if (message.equals("exit") || message.equals("quit") || message.equals("shutdown")) {
						System.exit(0);
					}

					buffer.put(message.getBytes());
					buffer.flip();

					socketChannel.write(buffer);
				}
			} catch (Exception e) {
				System.out.println("MyThread.run()");
			} finally {
				if (buffer != null) {
					buffer.clear();
					buffer = null;
				}
			}
		}
	}

}
