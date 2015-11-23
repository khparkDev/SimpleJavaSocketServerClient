package com.khpark.main;

import com.khpark.server.AdvancedServer;

public class RunServerMain {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("----------- usage --------------");
			System.out.println("RunServerMain [port_num] [osType-> 1:win, 2:linux]");
			System.exit(0);
		}
		
		int portNum = Integer.parseInt(args[0]);
		int osType = Integer.parseInt(args[1]);

		/*
		if (portNum >= 10000 || portNum < 1000) {
			System.out.println("error : port range is 1001 ~ 9999.");
			System.exit(0);
		}
		*/

		if (osType > 2 && osType < 1) {
			System.exit(0);
		}

		// normal execute
		new AdvancedServer(portNum, osType);
	}
}
