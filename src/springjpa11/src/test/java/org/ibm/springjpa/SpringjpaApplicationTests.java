package org.ibm.springjpa;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.ibm.model.deserializers.GetReposOfUserDeserializer;
import org.ibm.rest.dto.GetUserRepositoriesDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class SpringjpaApplicationTests {

	
	// warn! this design can only work if the RestService is Mocked as well
	// cannot use localhost or loopback to request application running as a different process
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Test
	void contextLoads() {
	}

	//@Test
	void testGetGitGathererServiceEndpoint_UsingMock() throws Exception {
		String addr = "127.0.0.1:8080";
		String uri = "/scanReposOfUser?username=p0licat";
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("http://" + addr + uri);
		ResultMatcher okMatcher = MockMvcResultMatchers.status().isOk();
		try {
			mockMvc.perform(builder).andExpect(okMatcher);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} catch (AssertionError e) {
			throw e;
		}
	}
	
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
	
	private ObjectMapper getMapperFor__getReposOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserRepositoriesDTO.class, new GetReposOfUserDeserializer());
		mapper.registerModule(module);
		return mapper;
	}
	
	@Test
	void testGetGitGathererServiceEndpoint_UsingHttpClient() throws Exception {
		String url = "http://127.0.0.1:8080/scanReposOfUser?username=p0licat";
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		HttpResponse<String> response = null; // not a string, but a DTO
		try {
			response = httpClient.send(request, BodyHandlers.ofString());
		} catch(IOException e) {
			throw e;
		}
		
		Assertions.assertTrue(response.statusCode() != 404);
	}
	
	private HttpResponse<String> makeRequest(String url) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.GET()
				.build();

		return httpClient.send(request, BodyHandlers.ofString());
		
	}
}
