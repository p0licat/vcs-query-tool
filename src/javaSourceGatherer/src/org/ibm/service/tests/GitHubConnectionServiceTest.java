package org.ibm.service.tests;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.ibm.service.GitHubConnectionService;
import org.ibm.service.IGitConnectionService;
import org.ibm.tests.fixtures.model.HttpServerFixtureHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;

class GitHubConnectionServiceTest {
	
	HttpServer server;

	@BeforeEach
	void setupServer() {
		InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 8080);
		try {
			this.server = HttpServer.create(addr, 1);
			this.server.createContext("/testcase/one", new HttpServerFixtureHandler());
			this.server.setExecutor(null);
			this.server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	void testSendGETRequest() {
		IGitConnectionService service = new GitHubConnectionService("http://127.0.0.1:8080");
		String response = service.sendGETRequest("/testcase/one");
		Assertions.assertEquals(response, "Response.".toString());
	}

	@Test
	void testSendGETRequestMultiple() {
		IGitConnectionService service = new GitHubConnectionService("http://127.0.0.1:8080");
		String response = service.sendGETRequest("/testcase/one");
		Assertions.assertEquals(response, "Response.".toString());
		
		response = service.sendGETRequest("/testcase/one");
		Assertions.assertEquals(response, "Response.".toString());
	}
	
	@Test
	void testSendPOSTRequest() {
		fail("Not yet implemented");
	}

}
