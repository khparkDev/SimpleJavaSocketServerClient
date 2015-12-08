package com.khpark.cleint;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public enum SimpleClient {
	INSTANCE;

	private ReadSocket readSocket;
	private WriteSocket writeSocket;
	private Selector selector = null;
	private SocketChannel socketChannel = null;
	private String hostIp;
	private int port;
	private WindowPanelControl wpc;

	public SimpleClient setHostInfo(String hostIp, int port) {
		this.hostIp = hostIp;
		this.port = port;
		return this;
	}

	public SimpleClient initClient() throws IOException {
		selector = Selector.open();
		socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, port));
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);

		readSocket = new ReadSocket(selector, wpc);
		writeSocket = new WriteSocket(socketChannel, wpc);

		return this;
	}

	public SimpleClient startClient() {
		readSocket.start();
		return this;
	}

	public void sendMessage(String message) {
		writeSocket.start(message);
	}

	public SimpleClient addPanel(WindowPanelControl wpc) {
		this.wpc = wpc;
		return this;
	}
}
