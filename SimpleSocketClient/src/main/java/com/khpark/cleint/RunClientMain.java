package com.khpark.cleint;


public class RunClientMain {
	public static void main(String... args) throws Exception {
		SimpleClient scc = new SimpleClient("10.10.11.21", 2000);
		scc.initClient();
		scc.startClient();
	}
}
