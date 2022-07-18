package org.ibm;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.stream.Collectors;

import org.ibm.model.deserializers.contentservice.GetRepoContentsDeserializerFromGithubReply;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromGithubReplyDTO;
import org.ibm.model.repohub.GitRepository;
import org.ibm.repository.GitRepoRepository;
import org.ibm.rest.dto.RequestUserDetailsDTO;
import org.ibm.rest.dto.RequestUserRepositoriesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SpringBootApplication
@PropertySources({ @PropertySource({ "classpath:application.properties" }) })
@RestController
@ComponentScan(basePackages = "{org.ibm.*}")
@EntityScan("org.ibm.*")
public class SpringjpaApplication {
	
	@Autowired
	private GitRepoRepository gitRepoRepository;
	
	private ObjectMapper getMapperFor__getRepoContentsDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RepoContentsFromGithubReplyDTO.class, new GetRepoContentsDeserializerFromGithubReply());
		mapper.registerModule(module);
		return mapper;
	}

	private HttpResponse<String> makeRequest(String url) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.GET().build();

		return httpClient.send(request, BodyHandlers.ofString());
	}
	
	@PostMapping("/populateUserRepositories")
	public RequestUserRepositoriesDTO requestUserRepositoryData(String username, String repoName) throws IOException, InterruptedException {
		
		List<GitRepository> repos = gitRepoRepository.findAll().stream().filter(e -> e.getName().contains(repoName)).collect(Collectors.toList());
		String foundName = repos.get(0).getName();
		
		String response = this.makeRequest("http://127.0.0.1:8081/getContentsOfRepo?username=" + username + "&repoName=" + repoName.toString()).body();
		
		ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializer();
		var lombokDeserialized = mapper.readValue(response, RepoContentsFromGithubReplyDTO.class);
		lombokDeserialized.getNodes().forEach(e -> {
			if (e.getType().equals("dir")) {
				// either use recursion or a queue of requests
				// must also create a new route in the contentscanner /getContentsOfRepoAtContentsUrl?...
			}
		});
		
		return null;
	}
	
	@PostMapping("/populateUserDetails")
	public RequestUserDetailsDTO requestUserDetailsData(String username) {
		return null;
		
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringjpaApplication.class, args);
	}
}
