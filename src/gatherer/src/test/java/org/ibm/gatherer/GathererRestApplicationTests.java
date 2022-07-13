package org.ibm.gatherer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.logging.Logger;

import org.ibm.model.deserializers.GetDetailsOfUserDeserializer;
import org.ibm.model.deserializers.GetReposOfUserDeserializer;
import org.ibm.model.dto.GetUserDetailsDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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

	@Test
	void testDtoConverterDetailsOfUser() throws IOException, JsonMappingException, JsonProcessingException {		
		File file = new File(ClassLoader.getSystemResource("./test_data/response1.txt").getPath().toString());
		FileReader input = new FileReader(file);		
		BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
		char[] buffer = new char[65535]; // seach const
		reader.read(buffer, 0, 65534);
		reader.close();
		input.close();
		
		String contents = new String(buffer);
		
		// make this a service: Singleton Bean?
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserDetailsDTO.class, new GetDetailsOfUserDeserializer());
		mapper.registerModule(module);
		GetUserDetailsDTO dto = mapper.readValue(contents, GetUserDetailsDTO.class);
	}
}
