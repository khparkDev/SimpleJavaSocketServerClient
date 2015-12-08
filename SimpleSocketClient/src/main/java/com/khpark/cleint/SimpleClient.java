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
	private String id;
	private String hostIp;
	private int port;

	public SimpleClient(String id, String hostIp, int port) {
		this.id = id;
		this.hostIp = hostIp;
		this.port = port;
	}

	public SimpleClient initClient() throws IOException {
		selector = Selector.open();
		socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, port));
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);

		readSocket = new ReadSocket(selector, id);
		writeSocket = new WriteSocket(socketChannel);

		return this;
	}

	public SimpleClient startClient() {
		writeSocket.start(id);
		readSocket.start(id);
		return this;
	}
}
