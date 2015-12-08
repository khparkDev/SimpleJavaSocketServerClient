package com.khpark.cleint;

import java.net.InetAddress;

import javax.swing.JFrame;

/**
 * setHostInfo에 "사용할이름", "연결할 서버아이피", 서버포트번호를 입력한 뒤 실행시킵니다.
 *
 */
@SuppressWarnings("serial")
public class RunClientMain extends JFrame {

	public static void main(String... args) throws Exception {
		String hostName = InetAddress.getLocalHost().getHostName();
		WindowPanelControl wpc = new WindowPanelControl(hostName);
		wpc.showPanel();
		SimpleClient.INSTANCE.setHostInfo("10.5.220.124", 20000).addPanel(wpc).initClient().startClient();
	}
}
