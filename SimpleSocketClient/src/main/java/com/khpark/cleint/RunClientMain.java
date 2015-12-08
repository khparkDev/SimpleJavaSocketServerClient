package com.khpark.cleint;

/**
 * new SimpleClient("사용할이름", "연결할 서버아이피", 서버포트번호);
 *
 */
public class RunClientMain {
	public static void main(String... args) throws Exception {
		new SimpleClient("테스트2", "10.5.220.124", 12000).initClient().startClient();
	}
}
