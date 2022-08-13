package org.ibm.service.requests.contentsrequesterservice;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

@Service
@PropertySources({ @PropertySource({ "classpath:application_connection_urls.properties" }) })
public class ContentsRequesterService {

	private HttpResponse<String> makeRequest(String url) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.GET().build();

		return httpClient.send(request, BodyHandlers.ofString());

	}
	
	
	
	public String requestContentsOfDownloadUrl(String downloadUrl) throws IOException, InterruptedException {
		var response = this.makeRequest("http://" + requestsEndpointUrl + ":" + requestsEndpointPort + "/" + "getContentsAtDownloadUrl?downloadUrl=" + downloadUrl.toString());
		return response.body();
	}
	
	@Value("${mesh.NETWORK_ADDR}")
	private String requestsEndpointUrl;
	@Value("${mesh.CONTENTS_SCANNER_PORT}")
	private String requestsEndpointPort; 
}
