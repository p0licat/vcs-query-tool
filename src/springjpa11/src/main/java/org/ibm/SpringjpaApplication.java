package org.ibm;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.ibm.config.servicemesh.ServiceMeshResourceManager;
import org.ibm.exceptions.ConfigurationProviderArgumentError;
import org.ibm.exceptions.reposervice.RepoServicePersistenceError;
import org.ibm.jpaservice.contentsgatherer.ContentsGathererService;
import org.ibm.model.deserializers.GetDetailsOfUserDeserializer;
import org.ibm.model.deserializers.ScanReposOfUserDeserializerFromEndpointReply;
import org.ibm.model.deserializers.contentservice.model.ContentNode;
import org.ibm.model.repohub.GitRepository;
import org.ibm.model.serializers.reposerializer.RepoSerializer;
import org.ibm.model.serializers.userserializer.UserSerializer;
import org.ibm.repository.ApplicationUserRepository;
import org.ibm.repository.GitRepoRepository;
import org.ibm.rest.dto.FileContentsDTO;
import org.ibm.rest.dto.GetReposDTO;
import org.ibm.rest.dto.GetUserDetailsDTO;
import org.ibm.rest.dto.RefreshAllRepoContentsDTO;
import org.ibm.rest.dto.RepositoryDTO;
import org.ibm.rest.dto.RequestUserRepositoriesDTO;
import org.ibm.rest.dto.SearchCodeDTO;
import org.ibm.rest.dto.endpointresponse.GetUsersDTO;
import org.ibm.rest.dto.endpointresponse.PopulateUserRepositoriesEndpointResponseDTO;
import org.ibm.service.contentscanner.contentscannerservice.ContentsScannerService;
import org.ibm.service.persistence.applicationuser.UserPersistenceService;
import org.ibm.service.persistence.contentsfilesservice.ContentsFilesService;
import org.ibm.service.persistence.reposervice.RepoPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.web.client.HttpClientErrorException;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;

@SpringBootApplication
@PropertySources({ @PropertySource({ "classpath:application.properties" }), @PropertySource({"classpath:application_password.properties"}) })
@RestController
@ComponentScan(basePackages = { "org.ibm.jpaservice", "org.ibm.service.persistence.applicationuser", "org.ibm.service.persistence.reposervice", "org.ibm.service.persistence.*", "org.ibm.service.requests.*", "org.ibm.config.servicemesh", "org.ibm.service.contentscanner.contentscannerservice" })
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
	
	@Autowired
	private ContentsFilesService fileService;
	
	@Autowired
	private ServiceMeshResourceManager meshResources;
	
	@Autowired 
	private ContentsScannerService contentsScannerService;

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

	private HttpResponse<String> makeRequestToMicroservice(String url) throws IOException, InterruptedException, HttpClientErrorException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.GET().build();
		var value = httpClient.send(request, BodyHandlers.ofString());
		if (value.statusCode() == 404) {
			throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "Query to microservice failed.");
		}
		return value;
	}

	@GetMapping("/getUsers")
	public GetUsersDTO getUsers() {
		var allUsers = userRepository.findAll();
		var usersList = UserSerializer.serialize(allUsers);
        return new GetUsersDTO(usersList);
	}

	@PostMapping("/populateUserRepositories")
/*	@Operation(summary = ""
			+ "Accepts an username of an existing user and a repository name that existed at the time the user was inserted."
			+ "Creates a new entry node for the repository contents.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the list of users.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDetailsDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Failure accessing db.", content = @Content), })*/
	public PopulateUserRepositoriesEndpointResponseDTO requestUserRepositoryData(String username, String repoName)
			throws IOException, InterruptedException, ConfigurationProviderArgumentError {

		List<GitRepository> repos = gitRepoRepository.findAll().stream().filter(e -> e.getName().contains(repoName))
				.collect(Collectors.toList()); // todo optimize query by creating a custom query within repo

		List<ContentNode> nodeList = this.contentsScannerService.recursiveContentNodeScan(repos, username);
		contentsGathererService.persistContentNodes(nodeList, repoName, username);
		return new PopulateUserRepositoriesEndpointResponseDTO(nodeList, Set.of(""));
	}

	@PostMapping("/refreshContents")
	public RefreshAllRepoContentsDTO refreshContents() throws IOException, InterruptedException, ConfigurationProviderArgumentError {
		
		var allRepoNames = this.repoService.getAllRepoNames();
		for ( Pair<String, String> repoPairs : allRepoNames) {
			var username = repoPairs.getFirst();
			var repoName = repoPairs.getSecond();
			

			List<GitRepository> repos = gitRepoRepository.findAll().stream().filter(e -> e.getName().contains(repoName))
					.collect(Collectors.toList()); 
			
			
			List<ContentNode> nodeList;
			nodeList = this.contentsScannerService.recursiveContentNodeScan(repos, username);
			
			contentsGathererService.persistContentNodes(nodeList, repoName, username);
		}
		
		return new RefreshAllRepoContentsDTO("OK");
		
	}
	
	@PostMapping("/refreshFileContents")
	public RefreshAllRepoContentsDTO refreshFileContents() throws IOException, InterruptedException {
		
		var allFiles = this.fileService.getAllFiles();
		if (this.fileService.gatherAllContents(allFiles)) {
			return new RefreshAllRepoContentsDTO("OK");
		}
		return new RefreshAllRepoContentsDTO("FAIL");
	}
	
	@PostMapping("/requestUserDetailsData")
/*	@Operation(summary = ""
			+ "Searches for an existing GitHub user, and if the request is successful it adds it to the database.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found a GitHub match and added to db.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDetailsDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "User not found or db persistence error.", content = @Content), })*/
	public GetUserDetailsDTO requestUserDetailsData(String username) throws IOException, InterruptedException, ConfigurationProviderArgumentError {
		String searchForUserUrl = "http://"  + this.meshResources.getResourceValue("networkAddr") + ":" + this.meshResources.getResourceValue("contentsGathererPort") + "/getDetailsOfUser?username=" + username;
		String response = this.makeRequestToMicroservice(searchForUserUrl).body(); // should be a service instance, not a
																		// RestController method
		ObjectMapper mapper = this.getMapperFor__getUserDetailsDeserializer(); // should be a service call to do the
																				// whole deserialization part
		var deserializedUserDetails = mapper.readValue(response, GetUserDetailsDTO.class);
		this.userService.saveUserDetails(deserializedUserDetails);
		return deserializedUserDetails;
	}

	@PostMapping("/scanRepos")
/*	@Operation(summary = "Initiates a scan of the GitHub username for existing public repos. If there is a match, the repositories are inserted in the database.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found username and gathered list of repos.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetReposDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "User not found or db persistence error.", content = @Content), })*/
	public GetReposDTO scanRepos(String username) throws IOException, InterruptedException, ConfigurationProviderArgumentError {
		String scanReposUrl = "http://" + this.meshResources.getResourceValue("networkAddr") + ":" + "8080" + "/scanReposOfUser?username=" + username;
		String response = this.makeRequestToMicroservice(scanReposUrl).body();
		ObjectMapper mapper = this.getMapperFor__scanReposOfUserDeserializer();
		var deserializedResponse = mapper.readValue(response, RequestUserRepositoriesDTO.class);
		try {
			this.repoService.persistReposForUser(username, deserializedResponse);
		} catch (RepoServicePersistenceError e) {
			return new GetReposDTO(); // change to HttpResponse 400
		}
		return new GetReposDTO(deserializedResponse.repositories); // not ideal especially without shared libraries
	}
	
	@GetMapping("/searchCode")
/*	@Operation(summary = "Locally scans all files for contents matching the pattern.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found code substring match.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetReposDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Db persistence error.", content = @Content), })*/
	public SearchCodeDTO searchCode(String search) throws IOException, InterruptedException {
		var allFiles = this.fileService.findAllContainingSubstring(search);
		var dtolist = new ArrayList<FileContentsDTO>();
		for (var file : allFiles) {
			FileContentsDTO newContents = new FileContentsDTO(file.getContents(), file.getFileName());
			dtolist.add(newContents);
		}
		SearchCodeDTO result = new SearchCodeDTO(dtolist);
		return result;
	}
	
	
	
	@PostMapping("/getRepos")
/*	@Operation(summary = "Returns repos from the db.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Optional list of repos from db.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetReposDTO.class)) }), })*/
	public GetReposDTO getRepos(String username) throws Exception {
		var user = this.userService.findUserByName(username);
		var repositories = this.repoService.getReposOfUser(user); // optional
		// use RepoSerializer
		// todo custom exception
		
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
