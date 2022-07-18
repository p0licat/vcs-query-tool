package org.ibm.springjpa;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.ibm.model.deserializers.GetReposOfUserDeserializerFromEndpointReply;
import org.ibm.rest.dto.GetUserRepositoriesDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
public class SpringWebMvcTestsWithHttpClient {
	
	@Test
	void testGetGitGathererServiceEndpoint_getRepositories() {
		String url = "http://127.0.0.1:8080/scanReposOfUser?username=p0licat";
		try {
			HttpResponse<String> response = this.makeRequest(url);
			Assertions.assertTrue(response.statusCode() == 200);
			ObjectMapper mapper = this.getMapperFor__getReposOfUserDeserializer();
			GetUserRepositoriesDTO dto;
			try {
				dto = mapper.readValue(response.body(), GetUserRepositoriesDTO.class);
			} catch (NullPointerException e) {
				throw e; // should be custom exception from Deserializer.
				// otherwise refactor deserializers as a sort of external module
			}
			Assertions.assertTrue(dto.toString().length() > 0);

		} catch (IOException e) {
			Assertions.fail();
		} catch (InterruptedException e) {
			Assertions.fail();
		}

	}
	
	@Test
	void testGetGitGathererServiceEndpoint_UsingHttpClient() throws Exception {
		String url = "http://127.0.0.1:8080/scanReposOfUser?username=p0licat";
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.GET().build();
		HttpResponse<String> response = null; // not a string, but a DTO
		try {
			response = httpClient.send(request, BodyHandlers.ofString());
		} catch (IOException e) {
			throw e;
		}

		Assertions.assertTrue(response.statusCode() != 404);
	}
	
	private ObjectMapper getMapperFor__getReposOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserRepositoriesDTO.class, new GetReposOfUserDeserializerFromEndpointReply());
		mapper.registerModule(module);
		return mapper;
	}



	private HttpResponse<String> makeRequest(String url) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.GET().build();

		return httpClient.send(request, BodyHandlers.ofString());

	}
}
