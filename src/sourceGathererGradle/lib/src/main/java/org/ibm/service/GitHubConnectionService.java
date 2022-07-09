package org.ibm.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;

import org.ibm.model.deserializers.GetReposOfUserDeserializer;
import org.ibm.model.dto.GetUserDetailsDTO;

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
	
	public String sendGETRequest(String URI) {
		String path = URL + '/' + URI;
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

	public String sendPOSTRequest(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	// use Nullable/optional wrapper / facade
	public Optional<GetUserDetailsDTO> getUserDetails(String userName) {
		String response = this.sendGETRequest(userName); // todo analyze API.
		GetUserDetailsDTO responseDTO = null; // or use builder pattern.
		
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserDetailsDTO.class, new GetReposOfUserDeserializer());
		mapper.registerModule(module);
		
		try {
			responseDTO = mapper.readValue(response, GetUserDetailsDTO.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		Optional<GetUserDetailsDTO> returnValue = Optional.ofNullable((GetUserDetailsDTO) responseDTO);
		return returnValue;
	}

	public String getRepositoriesOfUser(String userName) {
		return null;
	}

	public String getCommitsOfRepository(String userName, String repositoryName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRepositoryContentsAtPath(String userName, String repositoryName, String path) {
		// TODO Auto-generated method stub
		return null;
	}
}
