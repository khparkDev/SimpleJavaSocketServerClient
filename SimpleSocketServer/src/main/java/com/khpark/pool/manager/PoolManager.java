package com.khpark.pool.manager;

import java.util.HashMap;
import java.util.Map;

import com.khpark.pool.buffer.ByteBufferPool;
import com.khpark.pool.selector.SelectorPool;

public enum PoolManager {
	INSTANCE;

	private static Map<String, Object> map = new HashMap<String, Object>();

	public static void registAcceptSelectorPool(SelectorPool selectorPool) {
		map.put("AcceptSelectorPool", selectorPool);
	}

	public static void registRequestSelectorPool(SelectorPool selectorPool) {
		map.put("RequestSelectorPool", selectorPool);
	}

	public static SelectorPool getAcceptSelectorPool() {
		return (SelectorPool) map.get("AcceptSelectorPool");
	}

	public static SelectorPool getRequestSelectorPool() {
		return (SelectorPool) map.get("RequestSelectorPool");
	}

	public static void registByteBufferPool(ByteBufferPool byteBufferPool) {
		map.put("ByteBufferPool", byteBufferPool);
	}

	public static ByteBufferPool getByteBufferPool() {
		return (ByteBufferPool) map.get("ByteBufferPool");
	}

}
