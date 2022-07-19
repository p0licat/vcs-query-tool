package org.ibm;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.ibm.jpaservice.contentsgatherer.ContentsGathererService;
import org.ibm.model.deserializers.contentservice.GetRepoContentsDeserializerFromGithubReply;
import org.ibm.model.deserializers.contentservice.model.ContentNode;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromEndpointResponseDTO;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromGithubReplyDTO;
import org.ibm.model.repohub.GitRepository;
import org.ibm.repository.GitRepoRepository;
import org.ibm.rest.dto.RequestUserDetailsDTO;
import org.ibm.rest.dto.endpointresponse.PopulateUserRepositoriesEndpointResponseDTO;
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
	
	@Autowired
	private ContentsGathererService contentsGathererService;

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
	public PopulateUserRepositoriesEndpointResponseDTO requestUserRepositoryData(String username, String repoName)
			throws IOException, InterruptedException {

		List<GitRepository> repos = gitRepoRepository.findAll().stream().filter(e -> e.getName().contains(repoName))
				.collect(Collectors.toList()); // todo optimize query by creating a custom query within repo
		String foundName = repos.get(0).getName(); // proxy variable guards against JPA NotExists errors

		String response = this.makeRequest(
				"http://127.0.0.1:8081/getContentsOfRepo?username=" + username + "&repoName=" + foundName.toString())
				.body();

		
		Set<String> performedRequests = new HashSet<>(); // maybe this container should have versioned persistence or make this method responsible for completeness // for example creating a stack of get...ofDirectory and looping while there are changes to the stack. note that the loop should not mutate the stack, but return a new one
		Stack<String> queryQueue = new Stack<>();
		List<String> allFileUrls = new ArrayList<>();
		List<ContentNode> nodeList = new ArrayList<>();
		
		ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializer();
		var lombokDeserialized = mapper.readValue(response, RepoContentsFromEndpointResponseDTO.class);
		
		//List<CompletableFuture<String>> fileDownloadUrls = new ArrayList<CompletableFuture<String>>();
		//ExecutorService s = Executors.newSingleThreadExecutor();
		//s.
		
		// todo: add while loop
		// instead of recursive enumeration running synchronously, could have recursive generation of CompletableFuture and iterative synchronous requests with exception handling
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
						logger.info("Response for contentsRequest:" + result);
						//Future<String> future = Future.;
						if (!allFileUrls.contains(e.getDownloadsUrl())) {
							allFileUrls.add(e.getDownloadsUrl());
							nodeList.add(e);
						}
						//fileDownloadUrls.add(  )
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
						if (node.getType().compareTo("file") == 0) {
							logger.info("Sub-file of directory with name: " + e.getName() + " is: " + node.getContentsUrl());
							if (!allFileUrls.contains(node.getDownloadsUrl())) {
								allFileUrls.add(node.getDownloadsUrl());	
								nodeList.add(node);
							}
						} else if (node.getType().compareTo("dir") == 0) {
							if (!performedRequests.contains(node.getContentsUrl())) {
								queryQueue.add(node.getContentsUrl());
								logger.info("Directory: added to query queue the url: " + node.getContentsUrl());
								nodeList.add(node);
							}
						}
					});
					
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// refactoring day is 21st
		while (!queryQueue.empty()) {
			var e = queryQueue.pop();
			String result2 = this
					.makeRequest("http://127.0.0.1:8081/getContentsOfRepoAtContentsUrlOfDirectory?username="
							+ username + "&contentsUrl=" + e)
					.body();
			performedRequests.add(e);
			var currentRequestDeserialized2 = mapper.readValue(result2, RepoContentsFromEndpointResponseDTO.class);
			
			currentRequestDeserialized2.getNodes().forEach(r -> {
				if (r.getType().equals("file")) {
					// either use recursion or a queue of requests
					// must also create a new route in the contentscanner
					// /getContentsOfRepoAtContentsUrl?...

					// move business logic elsewhere...
					if (!performedRequests.contains(r.getContentsUrl())) {

						try {
							String result = this
									.makeRequest("http://127.0.0.1:8081/getContentsOfRepoAtContentsUrlOfFile?username="
											+ username + "&contentsUrl=" + r.getContentsUrl())
									.body();
							// result.persist()
							performedRequests.add(r.getContentsUrl());
							logger.info("Found a file: " + r.getContentsUrl());
							logger.info("Found a file: " + r.getDownloadsUrl());
							logger.info("Response for contentsRequest:" + result);
							//Future<String> future = Future.;
							if (!allFileUrls.contains(r.getDownloadsUrl())) {
								allFileUrls.add(r.getDownloadsUrl());
								nodeList.add(r);
							}
							//fileDownloadUrls.add(  )
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
										+ username + "&contentsUrl=" + r.getContentsUrl())
								.body();
						performedRequests.add(r.getContentsUrl());
						var currentRequestDeserialized = mapper.readValue(result, RepoContentsFromEndpointResponseDTO.class);
						
						currentRequestDeserialized.getNodes().forEach(node -> {
							if (node.getType().compareTo("file") == 0) {
								logger.info("Sub-file of directory with name: " + r.getName() + " is: " + node.getContentsUrl());
								if (!allFileUrls.contains(node.getDownloadsUrl())) {
									allFileUrls.add(node.getDownloadsUrl());
									nodeList.add(node);
								}
							} else if (node.getType().compareTo("dir") == 0) {
								if (!performedRequests.contains(node.getContentsUrl())) {
									queryQueue.add(node.getContentsUrl());
									logger.info("Directory: added to query queue the url: " + node.getContentsUrl());
									nodeList.add(node);
								}
							}
						});
						
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			});
		}

		
		contentsGathererService.persistContentNodes(nodeList, repoName);
		return new PopulateUserRepositoriesEndpointResponseDTO(nodeList, performedRequests);
	}

	@PostMapping("/populateUserDetails")
	public RequestUserDetailsDTO requestUserDetailsData(String username) {
		return null;

	}

	public static void main(String[] args) {
		SpringApplication.run(SpringjpaApplication.class, args);
	}
}
