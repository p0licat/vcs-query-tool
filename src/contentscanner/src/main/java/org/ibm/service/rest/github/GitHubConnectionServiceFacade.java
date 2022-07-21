package org.ibm.service.rest.github;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubConnectionServiceFacade {

	@Autowired
	GitHubConnectionService gitHubConnectionService;
	
	public HttpResponse<String> getResponseFromEndpoint_repoContents(String username, String repoName,
			String authKey) {
		this.gitHubConnectionService.URL = "https://api.github.com";
		HttpResponse<String> response = this.gitHubConnectionService.getRawContentsOfRepository(username, repoName, authKey);
		return response;
	}

	public HttpResponse<String> getResponseFromEndpoint_repoContentsAtPath(String username, String repoName,
			String authKey, String resourcePath) {
		this.gitHubConnectionService.URL = "https://api.github.com";
		HttpResponse<String> response = this.gitHubConnectionService.getRawContentsOfRepositoryAtPath(username, repoName, authKey,
				resourcePath);
		return response;
	}

	public HttpResponse<String> getResponseFromEndpoint_repoContentsAtContentsUrl(String username, String contentsUrl,
			String apiKey) throws IOException, InterruptedException {
		this.gitHubConnectionService.URL = contentsUrl;
		HttpResponse<String> response = this.gitHubConnectionService.getRawResponseFromMainUrl(apiKey);
		return response;
	}
	
}
