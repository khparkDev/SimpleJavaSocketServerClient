package com.khpark.pool.selector;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Iterator;

import com.khpark.pool.selector.handler.AcceptHandler;
import com.khpark.queue.BlockingMessageQueue;

public class AcceptSelectorPool extends AbstractSelectorPool {
	private BlockingMessageQueue queue = null;
	private int port;

	public AcceptSelectorPool(BlockingMessageQueue queue, int port) {
		this(queue, 1, port);
	}

	public AcceptSelectorPool(BlockingMessageQueue queue, int size, int port) {
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

	protected Runnable createHandler(int index) {
		Selector selector = null;

		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Runnable r = new AcceptHandler(queue, selector, port, index);
		executors().submit(r);

		return r;
	}

	public void startAll() {
		for (Iterator<Runnable> it = pool.iterator(); it.hasNext();) {
			executors().submit(it.next());
		}
	}

	public void stopAll() {
	}
}
