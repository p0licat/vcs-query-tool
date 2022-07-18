package org.ibm.springjpa;

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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
public class SpringWebMvcTestWithMockMvc {
	// warn! this design can only work if the RestService is Mocked as well
	// cannot use localhost or loopback to request application running as a
	// different process
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
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

}
