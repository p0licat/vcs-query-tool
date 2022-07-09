package org.ibm.service.tests;

import java.io.IOException;

import com.sun.net.httpserver.HttpServer;

import junit.framework.TestCase;

public class HttpClientTestCaseTest extends TestCase {
	HttpServer server;
	
	protected void setUP() {
		try {
			this.server = HttpServer.create();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
