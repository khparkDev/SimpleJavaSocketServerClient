package com.khpark.pool.buffer;

import java.nio.ByteBuffer;

public interface ByteBufferPool {
	public ByteBuffer getMemoryBuffer();

	public ByteBuffer getFileBuffer();

	public void putBuffer(ByteBuffer buffer);

	public void setWait(boolean wait);

	public boolean isWait();

}
