package com.khpark.pool.selector;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Iterator;

import com.khpark.pool.selector.handler.AcceptHandler;
import com.khpark.queue.Queue;

public class AcceptSelectorPool extends SelectorPoolAdaptorImpl {
	private int port;
	private Queue queue = null;

	public AcceptSelectorPool(Queue queue, int port) {
		this(queue, 1, port);
	}

	public AcceptSelectorPool(Queue queue, int size, int port) {
		super.size = size;
		this.queue = queue;
		this.port = port;
		
		init();
	}

	private void init() {
		for (int i = 0; i < size; i++) {
			pool.add(createHandler(i));
		}
	}

	protected Thread createHandler(int index) {
		Selector selector = null;
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread handler = new AcceptHandler(queue, selector, port, index);

		return handler;
	}

	public void startAll() {
		Iterator<Thread> iter = pool.iterator();
		while (iter.hasNext()) {
			Thread handler = (Thread) iter.next();
			handler.start();
		}
	}

	public void stopAll() {
		Iterator<Thread> iter = pool.iterator();
		while (iter.hasNext()) {
			Thread handler = (Thread) iter.next();
			handler.interrupt();
			handler = null;
		}
	}

}
