package com.khpark.pool.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.khpark.common.ClassLoader;
import com.khpark.queue.BlockingMessageQueue;

public class ThreadPool {
	private ExecutorService executors = Executors.newCachedThreadPool();
	private int max = 10;
	private int min = 2;
	private int current = 0;

	private final Object monitor = new Object();
	private final List<Runnable> pool = new ArrayList<Runnable>();

	private BlockingMessageQueue queue = null;
	private String type = null;

	public ThreadPool(BlockingMessageQueue queue, String type) {
		this(queue, type, 2, 10);
	}

	public ThreadPool(BlockingMessageQueue queue, String type, int min, int max) {
		this.queue = queue;
		this.type = type;
		this.min = min;
		this.max = max;
		init();
	}

	private void init() {
		for (int i = 0; i < min; i++) {
			pool.add(createThread());
		}
	}

	private synchronized Runnable createThread() {
		Runnable runnable = null;

		try {
			runnable = (Runnable) ClassLoader.createInstance(type, queue);
		} catch (Exception e) {
			e.printStackTrace();
		}

		current++;
		return runnable;
	}

	public void startAll() {
		synchronized (monitor) {
			for (Iterator<Runnable> it = pool.iterator(); it.hasNext();) {
				executors.submit(it.next());
			}
		}
	}

	public void stopAll() {
		//TODO 구현
	}

	public void addThread() {
		synchronized (monitor) {
			if (current < max) {
				Runnable r = createThread();
				executors.submit(r);
				pool.add(r);
			}
		}
	}

	public void removeThread() {
		synchronized (monitor) {
			if (current > min) {
				pool.remove(0);
			}
		}
	}

}
