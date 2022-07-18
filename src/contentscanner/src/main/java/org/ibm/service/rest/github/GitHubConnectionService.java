package org.ibm.service.rest.github;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;

import org.ibm.model.contentscanner.dto.RepoContentsDTO;
import org.ibm.model.deserializers.GetRepoContentsDeserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class GitHubConnectionService {
	public String URL;
	public HttpClient httpClient;
	
	public GitHubConnectionService(String URL) {
		this.URL = URL;
		this.httpClient = HttpClient.newBuilder()
				.build();
	}
	
	public HttpResponse<String> sendGETRequest(String URI) {
		String path = URL + '/' + URI;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(java.net.URI.create(path))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		
		HttpResponse<String> response = null;
		try {
			response = this.httpClient.send(request, BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return response;
	}

	
	public HttpResponse<String> getRawRepositoriesOfUser(String userName) {
		String getRepositoriesOfUsersURI = "users" + '/' + userName + '/' + "repos";
		HttpResponse<String> response = this.sendGETRequest(getRepositoriesOfUsersURI);
		return response;
	}

	public Optional<RepoContentsDTO> getRepoContents(String userName, String repoName, String apiKey) {
		String response = this.getRawRepositoriesOfUser(userName).body();
		RepoContentsDTO responseDTO = null;
		
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RepoContentsDTO.class, new GetRepoContentsDeserializer());
		mapper.registerModule(module);
		
		try {
			responseDTO = mapper.readValue(response, RepoContentsDTO.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		Optional<RepoContentsDTO> returnValue = Optional.ofNullable((RepoContentsDTO) responseDTO);
		return returnValue;
	}

}
