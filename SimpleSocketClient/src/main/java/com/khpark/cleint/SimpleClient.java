package com.khpark.cleint;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SimpleClient {
	private ReadSocket readSocket;
	private WriteSocket writeSocket;
	private Selector selector = null;
	private SocketChannel socketChannel = null;
	private String hostIp;
	private int port;

	public SimpleClient(String hostIp, int port) {
		this.hostIp = hostIp;
		this.port = port;
	}

	public void initClient() throws IOException {
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, port));
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);

			readSocket = new ReadSocket(selector);
			writeSocket = new WriteSocket(socketChannel);
		} catch (IOException e) {
			throw new IOException();
		}
	}

	public void startClient() {
		writeSocket.start();
		readSocket.start();
	}
}
