package org.ibm.springjpa;

import org.junit.jupiter.api.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
class SpringjpaApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Test
	void contextLoads() {
	}

	@Test
	void testGetGitGathererServiceEndpoint() throws Exception {
		String addr = "127.0.0.1:8080";
		String uri = "/getUserRepos?user=p0licat";
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("http://" + addr + uri);
		ResultMatcher okMatcher = MockMvcResultMatchers.status().isOk();
		try {
			mockMvc.perform(builder).andExpect(okMatcher);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
