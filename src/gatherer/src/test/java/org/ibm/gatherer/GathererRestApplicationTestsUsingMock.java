package org.ibm.gatherer;

import org.ibm.model.deserializers.GetReposOfUserDeserializerFromEndpointReply;
import org.ibm.shared.model.vcsmanager.dto.GetUserRepositoriesDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
public class GathererRestApplicationTestsUsingMock {
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Test
	void testGetGitGathererServiceEndpoint_UsingMock() throws Exception {
		String addr = "127.0.0.1:8080";
		String uri = "/scanReposOfUserOffline?username=p0licat";
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("http://" + addr + uri);
		ResultMatcher okMatcher = MockMvcResultMatchers.status().isOk();
		
		final String response;
		try {
			mockMvc.perform(builder).andExpect(okMatcher); // an assertion (throws AssertionError)
			MvcResult result = mockMvc.perform(builder).andReturn();
			//result.wait();
			response = result.getResponse().getContentAsString();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} catch (AssertionError e) {
			throw e;
		}
		
		ObjectMapper mapper = this.getMapperFor__getReposOfUserDeserializerFromEndpoint();
		GetUserRepositoriesDTO dto_res = mapper.readValue(response, GetUserRepositoriesDTO.class);
		Assertions.assertTrue(dto_res != null);
		Assertions.assertTrue(dto_res.getRepositories().size() > 0);
	}
	
	private ObjectMapper getMapperFor__getReposOfUserDeserializerFromEndpoint() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserRepositoriesDTO.class, new GetReposOfUserDeserializerFromEndpointReply());
		mapper.registerModule(module);
		return mapper;
	}
}
