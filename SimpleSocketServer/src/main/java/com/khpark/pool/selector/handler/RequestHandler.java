package com.khpark.pool.selector.handler;

import static com.khpark.common.Constants.READ_EVENT;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.khpark.common.SessionMap;
import com.khpark.queue.BlockingMessageQueue;
import com.khpark.queue.ChatChannel;

public class RequestHandler implements Runnable {
	private BlockingMessageQueue queue = null;
	private Selector selector = null;
	private String name = "@@ RequestHandler[";

	private Vector<SocketChannel> newClients = new Vector<SocketChannel>();

	public RequestHandler(BlockingMessageQueue queue, Selector selector, int index) {
		this.queue = queue;
		this.selector = selector;
		this.name = name + index + "]";
	}

	public void run() {
		try {

			while (true) {
				processNewConnection();
				int keysReady = selector.select(1000);

				if (keysReady > 0) {
					processRequest();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void processNewConnection() throws ClosedChannelException {
		for (Iterator<SocketChannel> it = newClients.iterator(); it.hasNext();) {
			SocketChannel sc = it.next();
			sc.register(selector, SelectionKey.OP_READ);
			ChatChannel.getInstance().add(sc);
			System.out.println(name + " 클라이언트의 요청을 처리할 준비가 되었습니다.");
		}

		newClients.clear();
	}

	private void processRequest() {
		Iterator<SelectionKey> it = selector.selectedKeys().iterator();

		while (it.hasNext()) {
			SelectionKey key = it.next();
			it.remove();
			pushMyJob(key);
		}
	}

	private void pushMyJob(SelectionKey key) {
		Map<String, SelectionKey> session = new ConcurrentHashMap<String, SelectionKey>();
		session.put("SelectionKey", key);
		SessionMap job = new SessionMap(READ_EVENT, session);
		queue.push(job);
	}

	public void addClient(SocketChannel sc) {
		newClients.add(sc);
	}

}
