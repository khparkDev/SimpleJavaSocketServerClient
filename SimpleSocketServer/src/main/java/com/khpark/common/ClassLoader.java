package com.khpark.common;

import java.lang.reflect.Constructor;

import com.khpark.queue.BlockingMessageQueue;

public class ClassLoader {

	public static Object createInstance(String type, BlockingMessageQueue queue) throws Exception {
		Object obj = null;

		if (type != null) {
			Class<?>[] paramType = new Class[] { BlockingMessageQueue.class };
			Constructor<?> con = Class.forName(type).getConstructor(paramType);
			Object[] params = new Object[] { queue };
			obj = con.newInstance(params);
		}

		return obj;
	}
}