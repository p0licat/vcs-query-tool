package org.ibm.service.requests.contentsrequesterservice;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.stereotype.Service;

@Service
public class ContentsRequesterService {

	private HttpResponse<String> makeRequest(String url) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.GET().build();

		return httpClient.send(request, BodyHandlers.ofString());

	}
	
	
	
	public String requestContentsOfDownloadUrl(String downloadUrl) throws IOException, InterruptedException {
		var response = this.makeRequest(requestsEndpointUrl + "getContentsAtDownloadUrl?url=" + downloadUrl.toString());
		return response.body();
	}
	
	
	// to be configured
	private String requestsEndpointUrl = "http://127.0.0.1:8081/"; 

}
