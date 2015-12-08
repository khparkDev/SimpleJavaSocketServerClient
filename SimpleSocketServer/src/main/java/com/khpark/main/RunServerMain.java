package com.khpark.main;

import java.net.UnknownHostException;

import com.khpark.server.Server;

/**
 * new AdvancedServer(띄울포트번호, OS타입)
 * new AdvancedServer(서버아이피, 띄울포트번호, OS타입)
 * 
 * @author khpark
 *
 */
public class RunServerMain {

	public static void main(String[] args) throws UnknownHostException {
		new Server(12000, 1);
	}
}
