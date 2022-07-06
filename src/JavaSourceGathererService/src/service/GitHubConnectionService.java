package service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;

public class GitHubConnectionService implements IGitConnectionService {

	public String URL;
	public HttpClient httpClient;
	
	public GitHubConnectionService(String URL) {
		this.URL = URL;
		this.httpClient = HttpClient.newBuilder()
				.build();
	}
	
	@Override
	public byte[] sendGETRequest(String URI) {
		String path = URL + '/' + URI;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(java.net.URI.create(URI))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		return null;
	}

	@Override
	public byte[] sendPOSTRequest(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

}
