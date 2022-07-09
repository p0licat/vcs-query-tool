package org.ibm.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import org.ibm.model.dto.GetReposOfUserDTO;
import org.ibm.model.dto.IGitDto;


public class GitHubConnectionService implements IGitConnectionService {
	public String URL;
	public HttpClient httpClient;
	
	public GitHubConnectionService(String URL) {
		this.URL = URL;
		this.httpClient = HttpClient.newBuilder()
				.build();
	}
	
	@Override
	public String sendGETRequest(String URI) {
		String path = URL + URI;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(java.net.URI.create(path))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		
		String response = "";
		try {
			response = this.httpClient.send(request, BodyHandlers.ofString()).body();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}

	@Override
	public String sendPOSTRequest(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGitDto getUserDetails(String userName) {
		String response = this.sendGETRequest(userName); // todo analyze API.
		GetReposOfUserDTO responseDTO = new GetReposOfUserDTO(response); // or use builder pattern.
		responseDTO.id = 1;
		return null;
	}

	@Override
	public String getRepositoriesOfUser(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommitsOfRepository(String userName, String repositoryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRepositoryContentsAtPath(String userName, String repositoryName, String path) {
		// TODO Auto-generated method stub
		return null;
	}
}
