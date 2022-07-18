package org.ibm.contentscanner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class ContentGathererApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	void testGetContentsFromRoute() throws IOException {
		String userName = "p0licat";
		String repoName = "distnet";
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("keyValue.txt");
		
		String keyValue = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		String addr = "127.0.0.1:8081";
		String uri = "/getContentsOfRepo?username=" + userName + "&reponame=" + repoName + "&key=" + keyValue;
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("http://" + addr + uri).header("Authorization", keyValue);
		
	}
}
