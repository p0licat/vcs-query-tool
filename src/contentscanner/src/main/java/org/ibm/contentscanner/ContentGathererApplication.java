package org.ibm.contentscanner;

import java.net.http.HttpResponse;

import org.ibm.model.contentscanner.dto.RepoContentsFromGithubReplyDTO;
import org.ibm.model.deserializers.GetRepoContentsDeserializerFromGithubReply;
import org.ibm.service.rest.github.GitHubConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SpringBootApplication
@RestController
public class ContentGathererApplication {

	@Autowired
	GitHubConnectionService gitHubConnectionService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(ContentGathererApplication.class, args);
	}
	
	private HttpResponse<String> getResponseFromEndpoint_repoContents(String username, String repoName, String authKey) {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		HttpResponse<String> response = service.getRawContentsOfRepository(username, repoName, authKey);
		return response;
	}


	private ObjectMapper getMapperFor__getRepoContentsDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RepoContentsFromGithubReplyDTO.class, new GetRepoContentsDeserializerFromGithubReply());
		mapper.registerModule(module);
		return mapper;
	}
	
	@GetMapping("/getContentsOfRepo")
	public RepoContentsFromGithubReplyDTO getContentsOfRepo(String username, String repoName, String authKey) throws JsonMappingException, JsonProcessingException {
		HttpResponse<String> response = this.getResponseFromEndpoint_repoContents(username, repoName, authKey);
		
		ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializer();
		RepoContentsFromGithubReplyDTO dto = mapper.readValue(response.body(), RepoContentsFromGithubReplyDTO.class);
		return new RepoContentsFromGithubReplyDTO();
	}
	
}
