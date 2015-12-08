package com.khpark.pool.thread.processor;

import java.nio.channels.SocketChannel;
import static com.khpark.common.Constants.ACCEPT_EVENT;
import com.khpark.common.SessionMap;
import com.khpark.pool.manager.PoolManager;
import com.khpark.pool.selector.handler.RequestHandler;
import com.khpark.queue.BlockingMessageQueue;

public class AcceptProcessor implements Runnable {
	private BlockingMessageQueue queue = null;

	public AcceptProcessor(BlockingMessageQueue queue) {
		this.queue = queue;
	}

	public void run() {
		try {

			while (true) {
				SessionMap job = queue.pop(ACCEPT_EVENT);

				if (job != null) {
					SocketChannel sc = (SocketChannel) job.getSession().get("SocketChannel");
					sc.configureBlocking(false);
					RequestHandler handler = (RequestHandler) PoolManager.getRequestSelectorPool().get();
					handler.addClient(sc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
