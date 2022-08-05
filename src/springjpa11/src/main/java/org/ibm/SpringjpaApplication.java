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

import org.ibm.exceptions.reposervice.RepoServicePersistenceError;
import org.ibm.exceptions.userservice.UserServiceInvalidUserError;
import org.ibm.jpaservice.contentsgatherer.ContentsGathererService;
import org.ibm.model.deserializers.GetDetailsOfUserDeserializer;
import org.ibm.model.deserializers.ScanReposOfUserDeserializerFromEndpointReply;
import org.ibm.model.deserializers.contentservice.GetRepoContentsDeserializerFromGithubReply;
import org.ibm.model.deserializers.contentservice.model.ContentNode;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromEndpointResponseDTO;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromGithubReplyDTO;
import org.ibm.model.repohub.GitRepository;
import org.ibm.model.serializers.reposerializer.RepoSerializer;
import org.ibm.model.serializers.userserializer.UserSerializer;
import org.ibm.repository.ApplicationUserRepository;
import org.ibm.repository.GitRepoRepository;
import org.ibm.rest.dto.GetReposDTO;
import org.ibm.rest.dto.GetUserDetailsDTO;
import org.ibm.rest.dto.RepositoryDTO;
import org.ibm.rest.dto.RequestUserRepositoriesDTO;
import org.ibm.rest.dto.endpointresponse.GetUsersDTO;
import org.ibm.rest.dto.endpointresponse.PopulateUserRepositoriesEndpointResponseDTO;
import org.ibm.service.persistence.applicationuser.UserPersistenceService;
import org.ibm.service.persistence.reposervice.RepoPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@SpringBootApplication
@PropertySources({ @PropertySource({ "classpath:application.properties" }) })
@RestController
@ComponentScan(basePackages = { "org.ibm.jpaservice", "org.ibm.service.persistence.applicationuser", "org.ibm.service.persistence.reposervice" })
@EntityScan("org.ibm.*")
@CrossOrigin
public class SpringjpaApplication {

	Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private GitRepoRepository gitRepoRepository;

	@Autowired
	private ApplicationUserRepository userRepository;

	@Autowired
	private ContentsGathererService contentsGathererService;
	
	@Autowired
	private RepoPersistenceService repoService;

	@Autowired
	private UserPersistenceService userService;

	private ObjectMapper getMapperFor__getRepoContentsDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RepoContentsFromGithubReplyDTO.class, new GetRepoContentsDeserializerFromGithubReply());
		mapper.registerModule(module);
		return mapper;
	}

	// turn these into a service
	private ObjectMapper getMapperFor__getUserDetailsDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserDetailsDTO.class, new GetDetailsOfUserDeserializer());
		mapper.registerModule(module);
		return mapper;
	}
	
	private ObjectMapper getMapperFor__scanReposOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RequestUserRepositoriesDTO.class, new ScanReposOfUserDeserializerFromEndpointReply());
		mapper.registerModule(module);
		return mapper;
	}

	private HttpResponse<String> makeRequest(String url) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.GET().build();

		return httpClient.send(request, BodyHandlers.ofString());
	}

	@GetMapping("/getUsers")
	public GetUsersDTO getUsers() {
		var allUsers = userRepository.findAll();
		var usersList = UserSerializer.serialize(allUsers);
		GetUsersDTO result = new GetUsersDTO(usersList);
		return result;
	}

	@PostMapping("/populateUserRepositories")
	@Operation(summary = ""
			+ "Accepts an username of an existing user and a repository name that existed at the time the user was inserted."
			+ "Creates a new entry node for the repository contents.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the list of users.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDetailsDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Failure accessing db.", content = @Content), })
	public PopulateUserRepositoriesEndpointResponseDTO requestUserRepositoryData(String username, String repoName)
			throws IOException, InterruptedException {

		// int apiLimit = -1;
		int apiLimit = 3;

		List<GitRepository> repos = gitRepoRepository.findAll().stream().filter(e -> e.getName().contains(repoName))
				.collect(Collectors.toList()); // todo optimize query by creating a custom query within repo
		// String foundName = repos.get(0).getName(); // proxy variable guards against
		// JPA NotExists errors

		// possible errors for the above .get()
		// if it doesn't exist, ContentsScanningForInexistentRepoError

		Set<String> performedRequests = new HashSet<>(); // maybe this container should have versioned persistence or
															// make this method responsible for completeness // for
															// example creating a stack of get...ofDirectory and looping
															// while there are changes to the stack. note that the loop
															// should not mutate the stack, but return a new one
		Stack<String> queryQueue = new Stack<>();
		List<String> allFileUrls = new ArrayList<>();
		List<ContentNode> nodeList = new ArrayList<>();

		String[] regexMatch = repos.get(0).getContentsUrl().split("/\\{");
		queryQueue.add(regexMatch[0].toString());

		ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializer();
		while (!queryQueue.empty()) {
			if (performedRequests.size() > apiLimit) {
				break;
			}

			var e = queryQueue.pop();
			String result2 = this
					.makeRequest("http://127.0.0.1:8081/getContentsOfRepoAtContentsUrlOfDirectory?username=" + username
							+ "&contentsUrl=" + e)
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
							// Future<String> future = Future.;
							if (!allFileUrls.contains(r.getDownloadsUrl())) {
								allFileUrls.add(r.getDownloadsUrl());
								nodeList.add(r);
							}
							// fileDownloadUrls.add( )
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
						var currentRequestDeserialized = mapper.readValue(result,
								RepoContentsFromEndpointResponseDTO.class);

						currentRequestDeserialized.getNodes().forEach(node -> {
							if (node.getType().compareTo("file") == 0) {
								logger.info("Sub-file of directory with name: " + r.getName() + " is: "
										+ node.getContentsUrl());
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

	@PostMapping("/requestUserDetailsData")
	@Operation(summary = ""
			+ "Searches for an existing GitHub user, and if the request is successful it adds it to the database.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found a GitHub match and added to db.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDetailsDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "User not found or db persistence error.", content = @Content), })
	public GetUserDetailsDTO requestUserDetailsData(String username) throws IOException, InterruptedException {
		String searchForUserUrl = "http://127.0.0.1:8080/getDetailsOfUser?username=" + username.toString(); // not using
																											// dns....
																											// should be
																											// a service
		String response = this.makeRequest(searchForUserUrl).body(); // should be a service instance, not a
																		// RestController method
		ObjectMapper mapper = this.getMapperFor__getUserDetailsDeserializer(); // should be a service call to do the
																				// whole deserialization part
		var deserializedUserDetails = mapper.readValue(response, GetUserDetailsDTO.class);
		this.userService.saveUserDetails(deserializedUserDetails);
		return deserializedUserDetails;
	}

	@PostMapping("/scanRepos")
	@Operation(summary = "Initiates a scan of the GitHub username for existing public repos. If there is a match, the repositories are inserted in the database.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found username and gathered list of repos.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetReposDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "User not found or db persistence error.", content = @Content), })
	public GetReposDTO scanRepos(String username) throws IOException, InterruptedException {
		String scanReposUrl = "http://127.0.0.1:8080/scanReposOfUser?username=" + username.toString();
		String response = this.makeRequest(scanReposUrl).body();
		ObjectMapper mapper = this.getMapperFor__scanReposOfUserDeserializer();
		var deserializedResponse = mapper.readValue(response, RequestUserRepositoriesDTO.class);
		try {
			this.repoService.persistReposForUser(username, deserializedResponse);
		} catch (RepoServicePersistenceError e) {
			return new GetReposDTO(); // change to HttpResponse 400
		}
		return new GetReposDTO(deserializedResponse.repositories); // not ideal especially without shared libraries
	}
	
	@PostMapping("/getRepos")
	@Operation(summary = "Returns repos from the db.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Optional list of repos from db.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetReposDTO.class)) }), })
	public GetReposDTO getRepos(String username) throws IOException, InterruptedException, UserServiceInvalidUserError {
		var user = this.userService.findUserByName(username);
		var repositories = this.repoService.getReposOfUser(user); // optional
		// use RepoSerializer
		
		var result = new ArrayList<RepositoryDTO>();
		repositories.forEach(e -> {
			var newDTO = RepoSerializer.fromGitRepository(e);
			result.add(newDTO);
		});
		return new GetReposDTO(result); // not ideal especially without shared libraries
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringjpaApplication.class, args);
	}
}
