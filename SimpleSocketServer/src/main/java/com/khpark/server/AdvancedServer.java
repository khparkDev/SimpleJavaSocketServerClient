package com.khpark.server;

import java.io.File;
import java.io.IOException;

import com.khpark.pool.buffer.ByteBufferPoolImpl;
import com.khpark.pool.buffer.ByteBufferPool;
import com.khpark.pool.manager.PoolManager;
import com.khpark.pool.selector.AcceptSelectorPool;
import com.khpark.pool.selector.RequestSelectorPool;
import com.khpark.pool.selector.SelectorPool;
import com.khpark.pool.thread.ThreadPoolImpl;
import com.khpark.pool.thread.ThreadPool;
import com.khpark.queue.BlockingEventQueue;
import com.khpark.queue.Queue;

public class AdvancedServer {
	private int port;
	private int osType;
	private Queue queue = null;
	private SelectorPool acceptSelectorPool = null;
	private SelectorPool requestSelectorPool = null;
	private ByteBufferPool byteBufferPool = null;
	ThreadPool acceptThreadPool = null;
	ThreadPool readWriteThreadPool = null;

	public AdvancedServer(int port, int osType) {
		try {
			this.port = port;
			this.osType = osType;
			initResource();
			startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initResource() throws IOException {
		String filename = (osType == 1) ? "C:/Buffer.tmp" : "Buffer.tmp";
		File bufferFile = new File(filename);

		if (!bufferFile.exists()) {
			bufferFile.createNewFile();
		}

		bufferFile.deleteOnExit();
		byteBufferPool = new ByteBufferPoolImpl(20 * 1024, 40 * 2048, bufferFile);
		queue = BlockingEventQueue.getInstance();
		PoolManager.registByteBufferPool(byteBufferPool);

		acceptThreadPool = new ThreadPoolImpl(queue, "com.khpark.pool.thread.processor.AcceptProcessor");
		readWriteThreadPool = new ThreadPoolImpl(queue, "com.khpark.pool.thread.processor.ReadWriteProcessor");
		acceptSelectorPool = new AcceptSelectorPool(queue, port);
		requestSelectorPool = new RequestSelectorPool(queue);

		PoolManager.registAcceptSelectorPool(acceptSelectorPool);
		PoolManager.registRequestSelectorPool(requestSelectorPool);
	}

	private void startServer() {
		acceptThreadPool.startAll();
		readWriteThreadPool.startAll();

		acceptSelectorPool.startAll();
		requestSelectorPool.startAll();
	}
}
