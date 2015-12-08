package com.khpark.pool.manager;

import static com.khpark.common.Constants.ASP;
import static com.khpark.common.Constants.BBP;
import static com.khpark.common.Constants.RSP;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.khpark.pool.buffer.ByteBufferPool;
import com.khpark.pool.selector.AbstractSelectorPool;

public enum PoolManager {
	INSTANCE;
	private static Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	public static void registAcceptSelectorPool(AbstractSelectorPool selectorPool) {
		map.put(ASP, selectorPool);
	}

	public static void registRequestSelectorPool(AbstractSelectorPool selectorPool) {
		map.put(RSP, selectorPool);
	}

	public static AbstractSelectorPool getAcceptSelectorPool() {
		return (AbstractSelectorPool) map.get(ASP);
	}

	public static AbstractSelectorPool getRequestSelectorPool() {
		return (AbstractSelectorPool) map.get(RSP);
	}

	public static void registByteBufferPool(ByteBufferPool byteBufferPool) {
		map.put(BBP, byteBufferPool);
	}

	public static ByteBufferPool getByteBufferPool() {
		return (ByteBufferPool) map.get(BBP);
	}

}
