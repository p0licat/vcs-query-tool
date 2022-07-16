package org.ibm.gatherer;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.ibm.model.RepositoryDTO;
import org.ibm.model.deserializers.GetDetailsOfUserDeserializer;
import org.ibm.model.deserializers.GetReposOfUserDeserializer;
import org.ibm.model.dto.GetUserDetailsDTO;
import org.ibm.model.dto.GetUserRepositoriesDTO;
import org.ibm.service.rest.github.GitHubConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SpringBootApplication(scanBasePackages = {"org.ibm.service.*", "org.ibm.*"})
@RestController
public class GathererRestApplication {

	Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	GitHubConnectionService gitHubConnectionService;
	
	public static void main(String[] args) {
		SpringApplication.run(GathererRestApplication.class, args);
	}
	
	// region mappers
	private ObjectMapper getMapperFor__getDetailsOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserDetailsDTO.class, new GetDetailsOfUserDeserializer());
		mapper.registerModule(module);
		return mapper;
	}

	private ObjectMapper getMapperFor__getReposOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserRepositoriesDTO.class, new GetReposOfUserDeserializer());
		mapper.registerModule(module);
		return mapper;
	}
	//endregion
	
	//region GET queries
	private HttpResponse<String> getResponseFromEndpoint_userDetails() {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		HttpResponse<String> response = service.getRawUserDetails("p0licat");
		return response;
	}

	private HttpResponse<String> getResponseFromEndpoint_userRepos() {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		HttpResponse<String> response = service.getRawRepositoriesOfUser("p0licat");
		return response;
	}
	//endregion
	
	@GetMapping("/getDetailsOfUser")
	public GetUserDetailsDTO getDetailsOfUser(String username) throws Exception {
		HttpResponse<String> response = this.getResponseFromEndpoint_userDetails();
		if (response.statusCode() != 200) {
			throw new Exception("Custom HTTP exception. Request failed. Git API unreachable.");
		}
		
		ObjectMapper mapper = this.getMapperFor__getDetailsOfUserDeserializer();
		GetUserDetailsDTO dto = mapper.readValue(response.body(), GetUserDetailsDTO.class);
		return dto;
	}
	
	@GetMapping("/scanReposOfUser")
	public GetUserRepositoriesDTO scanReposOfUser(String username) throws Exception {
		HttpResponse<String> response = this.getResponseFromEndpoint_userRepos();
		if (response.statusCode() != 200) {
			throw new Exception("Custom HTTP exception. Request failed. Git API unreachable.");
		}
		
		ObjectMapper mapper = this.getMapperFor__getReposOfUserDeserializer();
		GetUserRepositoriesDTO dto = mapper.readValue(response.body(), GetUserRepositoriesDTO.class);
		return dto;
	}


	@GetMapping("/getReposOfUser")
	public List<RepositoryDTO> getReposOfUser(String username, String repositoryName) {
		GetUserRepositoriesDTO dto = null;
		if (repositoryName.compareTo("github") == 0) {
			dto = this.gitHubConnectionService.getRepositoriesOfUser(username).orElse(null);
		}
		if (dto == null) {
			return new ArrayList<>();
		} else {
			ArrayList<RepositoryDTO> results = new ArrayList<>(); // not an actual DTO
			dto.repositories.forEach(e -> {results.add(e);});
			return results;
		}
	}
	
	
}
