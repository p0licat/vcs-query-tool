package org.ibm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.persistence.PersistenceException;

import org.ibm.config.servicemesh.ServiceMeshResourceManager;
import org.ibm.exceptions.ConfigurationProviderArgumentError;
import org.ibm.exceptions.persistence.PersistenceServiceContextError;
import org.ibm.exceptions.reposervice.RepoServicePersistenceError;
import org.ibm.exceptions.serializable.CustomMultiSerializationServiceError;
import org.ibm.jpaservice.contentsgatherer.ContentsGathererService;
import org.ibm.model.applicationuser.ApplicationUser;
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
import org.ibm.service.rest.SimpleRestMessagingService;
import org.ibm.service.serialization.DTOSerializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@SpringBootApplication
@PropertySources({ @PropertySource({ "classpath:application.properties" }),
		@PropertySource({ "classpath:application_password.properties" }) })
@RestController
@ComponentScan(basePackages = { "org.ibm.jpaservice", "org.ibm.service.persistence.applicationuser",
		"org.ibm.service.persistence.reposervice", "org.ibm.service.persistence.*", "org.ibm.service.requests.*",
		"org.ibm.config.servicemesh", "org.ibm.service.contentscanner.contentscannerservice",
		"org.ibm.service.serialization", "org.ibm.service.rest" })
@EntityScan("org.ibm.*")
@CrossOrigin
public class SpringjpaApplication {

	Logger logger = Logger.getLogger(getClass().getName());

	/*
	 * Can directly access database for Repository objects.
	 */
	@Autowired
	private GitRepoRepository gitRepoRepository;

	/*
	 * Can directly access database for ApplicationUser objects.
	 */
	@Autowired
	private ApplicationUserRepository userRepository;

	/*
	 * Makes HTTP requests to contentsGatherer microservice
	 */
	@Autowired
	private ContentsGathererService contentsGathererService;

	/*
	 * Persistence service which stores data to database.
	 */
	@Autowired
	private RepoPersistenceService repoService;

	/*
	 * Persistence service which stores data to database.
	 */
	@Autowired
	private UserPersistenceService userService;

	/*
	 * Persistence manager that stores file contents. Can also perform requests to
	 * obtain file contents from the other microservice.
	 */
	@Autowired
	private ContentsFilesService fileService;

	@Autowired
	private ServiceMeshResourceManager meshResources;

	/*
	 * Makes HTTP requests to contentsScanner microservice
	 */
	@Autowired
	private ContentsScannerService contentsScannerService;

	/*
	 * Provides deserialization using a lookup by the DTO's class attributes.
	 */
	@Autowired
	private DTOSerializationService serializationService;

	/*
	 * Make simple requests between microservices.
	 */
	@Autowired
	private SimpleRestMessagingService simpleRestService;

	@GetMapping("/getUsers")
	public ResponseEntity<GetUsersDTO> getUsers() {
		var allUsers = userRepository.findAll();
		var usersList = UserSerializer.serialize(allUsers);
		GetUsersDTO result = new GetUsersDTO(usersList);

		ResponseEntity<GetUsersDTO> response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	@PostMapping("/populateUserRepositories")
	@Operation(summary = ""
			+ "Accepts an username of an existing user and a repository name that existed at the time the user was inserted."
			+ "Creates a new entry node for the repository contents.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the list of users.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDetailsDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Failure with database persistence context or during recursive scan.", content = @Content), })
	public ResponseEntity<PopulateUserRepositoriesEndpointResponseDTO> requestUserRepositoryData(String username,
			String repoName) {

		List<GitRepository> repos;
		try {
			this.repoService.testPersistenceConnection();
			repos = gitRepoRepository.findAll().stream().filter(e -> e.getName().contains(repoName))
					.collect(Collectors.toList()); // todo optimize query by creating a custom query within repo
		} catch (PersistenceServiceContextError e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			this.contentsGathererService.testPersistenceConnection();
		} catch (PersistenceServiceContextError e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<ContentNode> nodeList;
		try {
			nodeList = this.contentsScannerService.recursiveContentNodeScan(repos, username);
		} catch (IOException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ConfigurationProviderArgumentError e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try {
			contentsGathererService.persistContentNodes(nodeList, repoName, username);
		} catch (PersistenceException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		var result = new PopulateUserRepositoriesEndpointResponseDTO(nodeList, Set.of(""));
		var response = new ResponseEntity<PopulateUserRepositoriesEndpointResponseDTO>(result, HttpStatus.OK);
		return response;
	}

	@PostMapping("/refreshContents")
	public ResponseEntity<RefreshAllRepoContentsDTO> refreshContents() {

		var allRepoNames = this.repoService.getAllRepoNames();
		for (Pair<String, String> repoPairs : allRepoNames) {
			var username = repoPairs.getFirst();
			var repoName = repoPairs.getSecond();

			List<GitRepository> repos = gitRepoRepository.findAll().stream().filter(e -> e.getName().contains(repoName))
					.collect(Collectors.toList());

			List<ContentNode> nodeList = new ArrayList<>();
			try {
				nodeList = this.contentsScannerService.recursiveContentNodeScan(repos, username);
			} catch (IOException e1) {
				e1.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (ConfigurationProviderArgumentError e1) {
				e1.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			contentsGathererService.persistContentNodes(nodeList, repoName, username);
		}

		return new ResponseEntity<>(new RefreshAllRepoContentsDTO(), HttpStatus.OK);

	}

	@PostMapping("/refreshFileContents")
	public ResponseEntity<RefreshAllRepoContentsDTO> refreshFileContents() {

		var allFiles = this.fileService.getAllFiles();
		try {
			if (this.fileService.gatherAllContents(allFiles)) {
				return new ResponseEntity<>(new RefreshAllRepoContentsDTO(), HttpStatus.OK);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<RefreshAllRepoContentsDTO>(new RefreshAllRepoContentsDTO(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/requestUserDetailsData")
	@Operation(summary = ""
			+ "Searches for an existing GitHub user, and if the request is successful it adds it to the database.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found a GitHub match and added to db.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDetailsDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "User not found or db persistence error.", content = @Content),
			@ApiResponse(responseCode = "504", description = "Error when contacting external microservice.", content = @Content), })
	public ResponseEntity<GetUserDetailsDTO> requestUserDetailsData(String username) {
		String searchForUserUrl;
		try {
			searchForUserUrl = "http://" + this.meshResources.getResourceValue("networkAddr") + ":" + "8080"
					+ "/getDetailsOfUser?username=" + username.toString();
		} catch (ConfigurationProviderArgumentError e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String response;
		try {
			response = this.simpleRestService.makeRequest(searchForUserUrl).body();
		} catch (IOException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}

		GetUserDetailsDTO deserializedUserDetails;
		try {
			deserializedUserDetails = (GetUserDetailsDTO) this.serializationService.deserializeClass(response,
					GetUserDetailsDTO.class.getName());
		} catch (CustomMultiSerializationServiceError e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		this.userService.saveUserDetails(deserializedUserDetails);
		return new ResponseEntity<>(deserializedUserDetails, HttpStatus.OK);
	}

	@PostMapping("/scanRepos")
	@Operation(summary = "Initiates a scan of the GitHub username for existing public repos. If there is a match, the repositories are inserted in the database.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found username and gathered list of repos.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetReposDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "User not found or db persistence error.", content = @Content), })
	public ResponseEntity<GetReposDTO> scanRepos(String username) {
		String scanReposUrl;
		try {
			scanReposUrl = "http://" + this.meshResources.getResourceValue("networkAddr") + ":" + "8080"
					+ "/scanReposOfUser?username=" + username.toString();
		} catch (ConfigurationProviderArgumentError e2) {
			e2.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String response;
		try {
			response = this.simpleRestService.makeRequest(scanReposUrl).body();
		} catch (IOException e2) {
			e2.printStackTrace();
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}
		RequestUserRepositoriesDTO deserializedResponse;
		try {
			deserializedResponse = (RequestUserRepositoriesDTO) this.serializationService.deserializeClass(response,
					RequestUserRepositoriesDTO.class.getName());
		} catch (CustomMultiSerializationServiceError e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(new GetReposDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			this.repoService.persistReposForUser(username, deserializedResponse);
		} catch (RepoServicePersistenceError e) {
			return new ResponseEntity<>(new GetReposDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new GetReposDTO(deserializedResponse.repositories), HttpStatus.OK);
	}

	@GetMapping("/searchCode")
	@Operation(summary = "Locally scans all files for contents matching the pattern.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found code substring match.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetReposDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Db persistence error.", content = @Content), })
	public ResponseEntity<SearchCodeDTO> searchCode(String search) {
		var allFiles = this.fileService.findAllContainingSubstring(search);
		var dtolist = new ArrayList<FileContentsDTO>();
		for (var file : allFiles) {
			FileContentsDTO newContents = new FileContentsDTO(file.getContents(), file.getFileName());
			dtolist.add(newContents);
		}
		SearchCodeDTO result = new SearchCodeDTO(dtolist);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/getRepos")
	@Operation(summary = "Returns repos from the db.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Optional list of repos from db.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetReposDTO.class)) }), })
	public ResponseEntity<GetReposDTO> getRepos(String username) {
		ApplicationUser user;
		try {
			user = this.userService.findUserByName(username);
		} catch (Exception e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		var repositories = this.repoService.getReposOfUser(user);

		var result = new ArrayList<RepositoryDTO>();
		repositories.forEach(e -> {
			var newDTO = RepoSerializer.fromGitRepository(e);
			result.add(newDTO);
		});
		return new ResponseEntity<>(new GetReposDTO(result), HttpStatus.OK);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringjpaApplication.class, args);
	}
}
