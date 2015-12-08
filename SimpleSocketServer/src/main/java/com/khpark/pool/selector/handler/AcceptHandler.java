package com.khpark.pool.selector.handler;

import static com.khpark.common.Constants.ACCEPT_EVENT;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.khpark.common.SessionMap;
import com.khpark.queue.BlockingMessageQueue;

public class AcceptHandler implements Runnable {
	private BlockingMessageQueue queue = null;
	private Selector selector = null;
	private int port;
	private String name = "";

	public AcceptHandler(BlockingMessageQueue queue, Selector selector, int port, int index) {
		this.queue = queue;
		this.selector = selector;
		this.port = port;
		this.name = ":: AcceptHandler[" + index + "]";
		init();
	}

	private void init() {
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), port);
			ssc.socket().bind(address);
			System.out.println(this.name + "가 초기화 되었습니다.");
			ssc.register(this.selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while (true) {
				selector.select();
				acceptPendingConnections();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void acceptPendingConnections() throws Exception {
		Iterator<SelectionKey> it = selector.selectedKeys().iterator();

		while (it.hasNext()) {
			SelectionKey key = (SelectionKey) it.next();
			it.remove();

			if (key != null) {
				ServerSocketChannel readyChannel = (ServerSocketChannel) key.channel();
				SocketChannel sc = readyChannel.accept();

				if (sc != null) {
					pushMyJob(sc);
					System.out.println(sc.socket().getInetAddress() + "클라이언트가 접속되었습니다.");
				}
			}
		}
	}

	private void pushMyJob(SocketChannel sc) {
		Map<String, SocketChannel> session = new ConcurrentHashMap<String, SocketChannel>();
		session.put("SocketChannel", sc);
		SessionMap job = new SessionMap(ACCEPT_EVENT, session);
		queue.push(job);
	}

}
