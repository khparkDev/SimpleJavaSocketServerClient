package com.khpark.pool.thread;

public interface ThreadPool {
	public void addThread();

	public void removeThread();

	public void startAll();

	public void stopAll();

}
