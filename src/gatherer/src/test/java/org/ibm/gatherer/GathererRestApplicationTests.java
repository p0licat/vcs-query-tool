package org.ibm.gatherer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.ibm.model.RepositoryDTO;
import org.ibm.model.deserializers.GetDetailsOfUserDeserializer;
import org.ibm.model.deserializers.GetReposOfUserDeserializer;
import org.ibm.model.dto.GetUserDetailsDTO;
import org.ibm.model.dto.GetUserRepositoriesDTO;
import org.ibm.service.rest.github.GitHubConnectionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GathererRestApplicationTests {

	@Test
	void contextLoads() {
	}

	@Order(2)
	@Test
	void testDtoConverterRepositoriesOfUser() throws IOException {
		String contents = this.getResponseFromResouces("response2.txt");
		ObjectMapper mapper = this.getMapperFor__getReposOfUserDeserializer();
		try {
			GetUserRepositoriesDTO dto;
			dto = mapper.readValue(contents, GetUserRepositoriesDTO.class);
			Assertions.assertTrue(dto.toString().length() > 0);
		} catch (JsonProcessingException e) {
			Assertions.fail();
		}
	}

	@Order(1)
	@Test
	void testDtoConverterDetailsOfUser() throws IOException, JsonMappingException, JsonProcessingException {

		String contents = this.getResponseFromResouces("response1.txt");
		ObjectMapper mapper = this.getMapperFor__getDetailsOfUserDeserializer();

		try {
			GetUserDetailsDTO dto = mapper.readValue(contents, GetUserDetailsDTO.class);
			Assertions.assertTrue(dto.toString().length() > 0);
		} catch (JsonProcessingException e) {
			Assertions.fail();
		}
	}

	@Order(1)
	@Test
	void testDtoConverterDetailsOfUserAsList() throws IOException, JsonMappingException, JsonProcessingException {

		String contents = this.getResponseFromResouces("responseList.txt");
		ObjectMapper mapper = this.getMapperFor__getDetailsOfUserDeserializer();

		try {
			GetUserDetailsDTO dto = mapper.readValue(contents, GetUserDetailsDTO.class);
			Assertions.assertTrue(dto.toString().length() > 0);
		} catch (JsonProcessingException e) {
			Assertions.fail();
		}
	}

	// make this a service: Singleton Bean?
	// todo: refactor into class
	private ObjectMapper getMapperFor__getDetailsOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserDetailsDTO.class, new GetDetailsOfUserDeserializer());
		mapper.registerModule(module);
		return mapper;
	}

	private ObjectMapper getMapperFor__getReposOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserRepositoriesDTO.class, new GetReposOfUserDeserializer());
		mapper.registerModule(module);
		return mapper;
	}

	// alternatively use REST mock.
	private String getResponseFromEndpoint_userDetails() {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		String response = service.getRawUserDetails("p0licat").body();
		return response;
	}

	private String getResponseFromEndpoint_userRepos() {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		String response = service.getRawRepositoriesOfUser("p0licat").body();
		return response;
	}

	@Order(3)
	@Test
	void getResponseFromEndpointTest__userRepos() throws Exception {
		String compareTo = null;
		try {
			compareTo = this.getResponseFromResouces("response2.txt");
		} catch (Exception e) {
			Assertions.fail();
		}

		String result = this.getResponseFromEndpoint_userRepos();
		ObjectMapper mapper = this.getMapperFor__getReposOfUserDeserializer();

		final GetUserRepositoriesDTO dto_web;
		final GetUserRepositoriesDTO dto_res;

		try {
			dto_web = mapper.readValue(result, GetUserRepositoriesDTO.class);
			dto_res = mapper.readValue(compareTo, GetUserRepositoriesDTO.class);
		} catch (Exception e) {
			Assertions.fail();
			throw e;
		}

		Assertions.assertTrue(dto_web.toString().compareTo(dto_res.toString()) == 0);
		List<RepositoryDTO> matches = dto_web.getRepositories().stream()
				.filter(e -> e.getName().compareTo("Algorithms") == 0).collect(Collectors.toList());
		// no duplicates check
		Assertions.assertTrue(matches.stream().filter(
				e -> dto_res.repositories.stream().filter(f -> f.getId().compareTo(e.getId()) == 0).count() == 1)
				.count() == 1);
		Assertions.assertTrue(matches.size() > 0);

	}

	@Order(3)
	@Test
	void getResponseFromEndpointTest__userDetails() {
		String compareTo = null;
		try {
			compareTo = this.getResponseFromResouces("response1.txt");
		} catch (Exception e) {
			Assertions.fail();
		}

		String result = this.getResponseFromEndpoint_userDetails();
		ObjectMapper mapper = this.getMapperFor__getDetailsOfUserDeserializer();

		GetUserDetailsDTO dto_web = null;
		GetUserDetailsDTO dto_res = null;
		try {
			dto_web = mapper.readValue(result, GetUserDetailsDTO.class);
			dto_res = mapper.readValue(compareTo, GetUserDetailsDTO.class);
		} catch (Exception e) {
			Assertions.fail();
		}

		Assertions.assertTrue(dto_web.getReposUrl().compareTo(dto_res.getReposUrl()) == 0);
		Assertions.assertTrue(dto_web.getNodeId().equals(dto_res.getNodeId()));
	}

	private String getResponseFromResouces(String resourceName) throws IOException {
		File file = new File(ClassLoader.getSystemResource("./test_data/" + resourceName).getPath().toString());
		FileReader input = new FileReader(file);
		BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
		char[] buffer = new char[65535]; // seach const
		reader.read(buffer, 0, 65534);
		reader.close();
		input.close();

		String contents = new String(buffer);
		return contents;
	}

}
