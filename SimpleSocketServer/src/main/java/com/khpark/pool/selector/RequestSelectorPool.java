package com.khpark.pool.selector;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Iterator;

import com.khpark.pool.selector.handler.RequestHandler;
import com.khpark.queue.BlockingMessageQueue;

public class RequestSelectorPool extends AbstractSelectorPool {
	private BlockingMessageQueue queue = null;

	public RequestSelectorPool(BlockingMessageQueue queue) {
		this(queue, 2);
	}

	public RequestSelectorPool(BlockingMessageQueue queue, int size) {
		super.size = size;
		this.queue = queue;
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

		Runnable r = new RequestHandler(queue, selector, index);
		executors().submit(r);

		return r;
	}

	public void startAll() {
		for (Iterator<Runnable> it = pool.iterator(); it.hasNext();) {
			executors().submit(it.next());
		}
	}

	public void stopAll() {
		//TODO 종료 처리
	}

}
