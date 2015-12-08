package com.khpark.server;

import java.io.File;
import java.io.IOException;

import com.khpark.pool.buffer.ByteBufferPool;
import com.khpark.pool.manager.PoolManager;
import com.khpark.pool.selector.AbstractSelectorPool;
import com.khpark.pool.selector.AcceptSelectorPool;
import com.khpark.pool.selector.RequestSelectorPool;
import com.khpark.pool.thread.ThreadPool;
import com.khpark.queue.BlockingMessageQueue;

public class Server {
	private int port;
	private int osType;
	private BlockingMessageQueue queue = null;
	private AbstractSelectorPool acceptSelectorPool = null;
	private AbstractSelectorPool requestSelectorPool = null;
	private ByteBufferPool byteBufferPool = null;
	ThreadPool acceptThreadPool = null;
	ThreadPool readWriteThreadPool = null;

	public Server(int port, int osType) {
		this.port = port;
		this.osType = osType;
		try {
			initResource();
			startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initResource() throws IOException {
		String filename = (osType == 1) ? "D:/Buffer.tmp" : "Buffer.tmp";
		File bufferFile = new File(filename);

		if (!bufferFile.exists()) {
			bufferFile.createNewFile();
		}

		bufferFile.deleteOnExit();
		byteBufferPool = new ByteBufferPool(20 * 1024, 40 * 2048, bufferFile);
		queue = BlockingMessageQueue.getInstance();
		PoolManager.registByteBufferPool(byteBufferPool);
		acceptThreadPool = new ThreadPool(queue, "com.khpark.pool.thread.processor.AcceptProcessor");
		readWriteThreadPool = new ThreadPool(queue, "com.khpark.pool.thread.processor.RequestProcessor");
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
