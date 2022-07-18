package org.ibm.contentscanner;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class ContentGathererApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	void testGetContentsFromRoute() throws Exception {
		String userName = "p0licat";
		String repoName = "distnet";
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("keyValue.txt");
		
		String keyValue = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		String addr = "127.0.0.1:8081";
		String uri = "/getContentsOfRepo?username=" + userName + "&repoName=" + repoName + "&authKey=" + keyValue;
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("http://" + addr + uri).header("Authorization", keyValue);
		ResultMatcher okMatcher = MockMvcResultMatchers.status().isOk();
		
		final String response;
		try {
			mockMvc.perform(builder).andExpect(okMatcher); // an assertion (throws AssertionError)
			MvcResult result = mockMvc.perform(builder).andReturn();
			//result.wait();
			response = result.getResponse().getContentAsString();
		} catch (Exception e) {
			throw e;
		} catch (AssertionError e) {
			throw e;
		}
		
	}
}
