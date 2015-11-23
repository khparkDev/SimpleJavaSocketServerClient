package com.khpark.queue;

import com.khpark.event.Job;

public interface Queue {
	public Job pop(int eventType);

	public void push(Job job);

}
