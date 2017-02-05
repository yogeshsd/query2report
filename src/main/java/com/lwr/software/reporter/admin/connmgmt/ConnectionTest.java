package com.lwr.software.reporter.admin.connmgmt;

import java.sql.Connection;

public class ConnectionTest {

	public static void main(String[] args) {
		
		for (int i = 0; i < 4 ; i++) {
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					Connection connection = ConnectionPool.getInstance().getConnection("default");
					ConnectionPool.getInstance().releaseConnection(connection, "default");
				}
			});
			t1.start();
		}
	}
}
