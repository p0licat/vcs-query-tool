package org.ibm;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.ibm.model.deserializers.contentservice.GetRepoContentsDeserializerFromGithubReply;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromEndpointResponseDTO;
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

	Logger logger = Logger.getLogger(getClass().getName());
	
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
	public RequestUserRepositoriesDTO requestUserRepositoryData(String username, String repoName)
			throws IOException, InterruptedException {

		List<GitRepository> repos = gitRepoRepository.findAll().stream().filter(e -> e.getName().contains(repoName))
				.collect(Collectors.toList()); // todo optimize query by creating a custom query within repo
		String foundName = repos.get(0).getName();

		String response = this.makeRequest(
				"http://127.0.0.1:8081/getContentsOfRepo?username=" + username + "&repoName=" + repoName.toString())
				.body();

		Set<String> performedRequests = new HashSet<>(); // maybe this container should have versioned persistence or make this method responsible for completeness
		// for example creating a stack of get...ofDirectory and looping while there are changes to the stack. note that the loop should not mutate the stack, but return a new one
		Stack<String> queryQueue = new Stack<>();
		
		ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializer();
		var lombokDeserialized = mapper.readValue(response, RepoContentsFromEndpointResponseDTO.class);
		
		// todo: add while loop
		lombokDeserialized.getNodes().forEach(e -> {
			if (e.getType().equals("file")) {
				// either use recursion or a queue of requests
				// must also create a new route in the contentscanner
				// /getContentsOfRepoAtContentsUrl?...

				// move business logic elsewhere...
				if (!performedRequests.contains(e.getContentsUrl())) {

					try {
						String result = this
								.makeRequest("http://127.0.0.1:8081/getContentsOfRepoAtContentsUrlOfFile?username="
										+ username + "&contentsUrl=" + e.getContentsUrl())
								.body();
						// result.persist()
						performedRequests.add(e.getContentsUrl());
						logger.info("Found a file: " + e.getContentsUrl());
						logger.info("Found a file: " + e.getDownloadsUrl());
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				
			} else {
				// directory request branch
				try {
					String result = this
							.makeRequest("http://127.0.0.1:8081/getContentsOfRepoAtContentsUrlOfDirectory?username="
									+ username + "&contentsUrl=" + e.getContentsUrl())
							.body();
					performedRequests.add(e.getContentsUrl());
					var currentRequestDeserialized = mapper.readValue(result, RepoContentsFromEndpointResponseDTO.class);
					currentRequestDeserialized.getNodes().forEach(node -> {
						logger.info("Sub-file of directory with name: " + e.getName() + " is: " + node.getContentsUrl());
						if (!performedRequests.contains(node.getContentsUrl())) {
							queryQueue.add(node.getContentsUrl());
							logger.info("Directory: added to query queue the url: " + node.getContentsUrl());
						}
					});
					
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
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
