package com.khpark.pool.selector.handler;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.khpark.event.Job;
import com.khpark.event.NIOEvent;
import com.khpark.queue.ChattingRoom;
import com.khpark.queue.Queue;

public class RequestHandler extends HandlerAdaptor {
	private Queue queue = null;
	private Selector selector = null;
	private String name = "RequestHandler-";

	private Vector<SocketChannel> newClients = new Vector<SocketChannel>();

	public RequestHandler(Queue queue, Selector selector, int index) {
		this.queue = queue;
		this.selector = selector;
		setName(name + index);
	}

	public void run() {
		try {
			
			while (!Thread.currentThread().isInterrupted()) {
				processNewConnection();
				int keysReady = selector.select(1000);
				System.out.println("@RequestHandler(" + getName() + ") selected : " + keysReady);
				
				if (keysReady > 0) {
					processRequest();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void processNewConnection() throws ClosedChannelException {
		Iterator<SocketChannel> it = newClients.iterator();
		
		while (it.hasNext()) {
			SocketChannel sc = it.next();
			sc.register(selector, SelectionKey.OP_READ);
			ChattingRoom.getInstance().add(sc);
			System.out.println("@RequestHandler(" + getName() + ") success regist : " + sc.toString());
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
		Map<String, SelectionKey> session = new HashMap<String, SelectionKey>();
		session.put("SelectionKey", key);
		Job job = new Job(NIOEvent.READ_EVENT, session);
		queue.push(job);
	}

	public void addClient(SocketChannel sc) {
		newClients.add(sc);
	}

}
