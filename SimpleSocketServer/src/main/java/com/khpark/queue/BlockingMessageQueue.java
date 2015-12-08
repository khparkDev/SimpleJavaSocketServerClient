package com.khpark.queue;

import static com.khpark.common.Constants.ACCEPT_EVENT;
import static com.khpark.common.Constants.READ_EVENT;

import java.util.ArrayList;
import java.util.List;

import com.khpark.common.SessionMap;

public enum BlockingMessageQueue {
	INSTANCE;
	private final Object acceptMonitor = new Object();
	private final Object readMonitor = new Object();
	private final List<SessionMap> acceptList = new ArrayList<SessionMap>();
	private final List<SessionMap> readList = new ArrayList<SessionMap>();

	public static BlockingMessageQueue getInstance() {
		return INSTANCE;
	}

	private BlockingMessageQueue() {
	}

	public SessionMap pop(int eventType) {
		switch (eventType) {
			case ACCEPT_EVENT :
				return getAcceptJob();
			case READ_EVENT :
				return getReadJob();
			default :
				throw new IllegalArgumentException("Illegal EventType..");
		}
	}

	private SessionMap getAcceptJob() {
		synchronized (acceptMonitor) {

			if (acceptList.isEmpty()) {

				try {
					acceptMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (acceptList != null && acceptList.size() > 0) {
				return acceptList.remove(0);
			}

			return null;
		}
	}

	private SessionMap getReadJob() {
		synchronized (readMonitor) {

			if (readList.isEmpty()) {

				try {
					readMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (readList != null && readList.size() > 0) {
				return readList.remove(0);
			}

			return null;
		}
	}

	public void push(SessionMap job) {
		if (job != null) {
			int eventType = job.getEventType();

			switch (eventType) {
				case ACCEPT_EVENT :
					putAcceptJob(job);
					break;
				case READ_EVENT :
					putReadJob(job);
					break;
				default :
					throw new IllegalArgumentException("Illegal EventType..");
			}
		}
	}

	private void putAcceptJob(SessionMap job) {
		synchronized (acceptMonitor) {
			acceptList.add(job);
			acceptMonitor.notify();
		}
	}

	private void putReadJob(SessionMap job) {
		synchronized (readMonitor) {
			readList.add(job);
			readMonitor.notify();
		}
	}

}
