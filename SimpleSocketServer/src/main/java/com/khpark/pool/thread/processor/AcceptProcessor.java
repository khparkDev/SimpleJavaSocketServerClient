package com.khpark.pool.thread.processor;

import java.nio.channels.SocketChannel;

import com.khpark.event.Job;
import com.khpark.event.NIOEvent;
import com.khpark.pool.manager.PoolManager;
import com.khpark.pool.selector.handler.HandlerAdaptor;
import com.khpark.queue.Queue;

public class AcceptProcessor extends Thread {
	private Queue queue = null;

	public AcceptProcessor(Queue queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Job job = queue.pop(NIOEvent.ACCEPT_EVENT);
				SocketChannel sc = (SocketChannel) job.getSession().get("SocketChannel");
				sc.configureBlocking(false);
				HandlerAdaptor handler = (HandlerAdaptor) PoolManager.getRequestSelectorPool().get();
				handler.addClient(sc);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

}
