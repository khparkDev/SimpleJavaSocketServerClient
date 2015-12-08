package com.khpark.pool.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractSelectorPool {
	private ExecutorService executors = Executors.newCachedThreadPool();
	protected int size = 2;
	private int roundRobinIndex = 0;
	private final Object monitor = new Object();
	protected final List<Runnable> pool = new ArrayList<Runnable>();

	protected abstract Runnable createHandler(int index);

	public abstract void startAll();

	public abstract void stopAll();

	public Runnable get() {
		synchronized (monitor) {
			return (Runnable) pool.get(roundRobinIndex++ % size);
		}
	}

	public void put(Runnable handler) {
		synchronized (monitor) {

			if (handler != null) {
				pool.add(handler);
			}

			monitor.notify();
		}
	}

	public int size() {
		synchronized (monitor) {
			return pool.size();
		}
	}

	public boolean isEmpty() {
		synchronized (monitor) {
			return pool.isEmpty();
		}
	}

	public ExecutorService executors() {
		return this.executors;
	}
}
