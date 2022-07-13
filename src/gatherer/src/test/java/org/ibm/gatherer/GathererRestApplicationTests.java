package org.ibm.gatherer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.ibm.model.deserializers.GetDetailsOfUserDeserializer;
import org.ibm.model.dto.GetUserDetailsDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application-logging-test.properties")
class GathererRestApplicationTests {

	@Test
	void contextLoads() {
	}

	@Order(1)
	@Test
	void testDtoConverterDetailsOfUser() throws IOException, JsonMappingException, JsonProcessingException {		
		
		String contents = this.getResponseFromResouces(); 
		ObjectMapper mapper = this.getMapperFor__getDetailsOfUserDeserializer();

		try {
		GetUserDetailsDTO dto = mapper.readValue(contents, GetUserDetailsDTO.class);
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
	
	// alternatively use REST mock.
	private String getResponseFromEndpoint() {
		
	}
	
	@Order(2)
	@Test
	void getResponseFromEndpointTest() {
		String compareTo = null;
		try {
			compareTo = this.getResponseFromResouces();
		} catch (Exception e) {
			Assertions.fail();
		}
		
		String result = this.getResponseFromEndpoint();
		ObjectMapper mapper = this.getMapperFor__getDetailsOfUserDeserializer();
		
		GetUserDetailsDTO dto_web = null;
		GetUserDetailsDTO dto_res = null;
		try {
			dto_web = mapper.readValue(result, GetUserDetailsDTO.class);
			dto_res = mapper.readValue(compareTo, GetUserDetailsDTO.class);
		} catch (Exception e) {
			Assertions.fail();
		}
		
		Assertions.assertTrue(dto_web.getReposUrl() == dto_res.getReposUrl());
		Assertions.assertTrue(dto_web.getNodeId().equals(dto_res.getNodeId()));
	}
	
	private String getResponseFromResouces() throws IOException {
		File file = new File(ClassLoader.getSystemResource("./test_data/response1.txt").getPath().toString());
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
