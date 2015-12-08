package com.khpark.pool.thread.processor;

import static com.khpark.common.Constants.READ_EVENT;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;

import com.khpark.common.SessionMap;
import com.khpark.pool.buffer.ByteBufferPool;
import com.khpark.pool.manager.PoolManager;
import com.khpark.queue.BlockingMessageQueue;
import com.khpark.queue.ChatChannel;

public class RequestProcessor implements Runnable {
	private BlockingMessageQueue queue = null;

	public RequestProcessor(BlockingMessageQueue queue) {
		this.queue = queue;
	}

	public void run() {
		try {

			while (true) {
				SessionMap job = queue.pop(READ_EVENT);

				if (job != null) {
					Map<?, ?> map = job.getSession();

					if (map != null) {
						SelectionKey key = (SelectionKey) map.get("SelectionKey");
						SocketChannel sc = (SocketChannel) key.channel();

						try {
							broadcast(sc);
						} catch (IOException e) {
							closeChannel(sc);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void broadcast(SocketChannel sc) throws IOException {
		ByteBufferPool bufferPool = PoolManager.getByteBufferPool();
		ByteBuffer buffer = null;

		try {
			buffer = bufferPool.getMemoryBuffer();

			for (int i = 0; i < 2; i++) {
				sc.read(buffer);
			}

			buffer.flip();

			for (Iterator<SocketChannel> it = ChatChannel.getInstance().iterator(); it.hasNext();) {
				SocketChannel member = (SocketChannel) it.next();

				if (member != null && member.isConnected()) {

					while (buffer.hasRemaining()) {
						member.write(buffer);
					}

					buffer.rewind();
				}
			}
		} finally {
			bufferPool.putBuffer(buffer);
		}
	}

	private void closeChannel(SocketChannel sc) {
		try {
			sc.close();
			ChatChannel.getInstance().remove(sc);
		} catch (IOException e) {
		}
	}

}
