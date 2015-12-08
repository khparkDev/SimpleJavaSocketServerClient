package com.khpark.common;

import java.util.Map;

public class SessionMap {
	private int eventType;
	private Map<?, ?> session = null;

    public SessionMap() {
	}

	public SessionMap(int eventType, Map<?, ?> session) {
		this.eventType = eventType;
		this.session = session;
	}

	public Map<?, ?> getSession() {
		return session;
	}

	public void setSession(Map<?, ?> session) {
		this.session = session;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
}
