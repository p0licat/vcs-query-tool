package org.ibm.gatherer;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.ibm.model.RepositoryDTO;
import org.ibm.model.deserializers.GetDetailsOfUserDeserializer;
import org.ibm.model.deserializers.GetReposOfUserDeserializerFromGitReply;
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

@SpringBootApplication(scanBasePackages = { "org.ibm.service.*", "org.ibm.*" })
@RestController
public class GathererRestApplication {

	@Autowired
	GitHubConnectionService gitHubConnectionService;

	public static void main(String[] args) {
		SpringApplication.run(GathererRestApplication.class, args);
	}

	private final ArrayList<String> configFilePaths = new ArrayList<>();

	{
		configFilePaths.add("src/main/resources/test_data/response2.txt");
	}

	// region mappers
	private ObjectMapper getMapperFor__getDetailsOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserDetailsDTO.class, new GetDetailsOfUserDeserializer());
		mapper.registerModule(module);
		return mapper;
	}

	private ObjectMapper getMapperFor__getReposOfUserDeserializerFromGitReply() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserRepositoriesDTO.class, new GetReposOfUserDeserializerFromGitReply());
		mapper.registerModule(module);
		return mapper;
	}
	// endregion

	// region GET queries
	private HttpResponse<String> getResponseFromEndpoint_userDetails() {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		HttpResponse<String> response = service.getRawUserDetails("p0licat"); // todo: move to args
		if (response.statusCode() != 200) {
			throw new RuntimeException("Custom HTTP exception. Request failed. Git API unreachable.");
		}
		return response;
	}

	private HttpResponse<String> getResponseFromEndpoint_userRepos() {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		HttpResponse<String> response = service.getRawRepositoriesOfUser("p0licat");
		if (response.statusCode() != 200) {
			throw new RuntimeException("Custom HTTP exception. Request failed. Git API unreachable.");
		}
		return response;
	}
	// endregion

	// region cached queries
	private String getResponseFromResources(String resourceName) throws Exception {
		String contents;
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourceName)) {
            assert is != null;
            contents = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IOException("Could not read file: " + resourceName);
		} catch (NullPointerException e) {
			throw new NullPointerException("Could not find file: " + resourceName);
		} catch (AssertionError | Exception e) {
			throw new Exception("Could not read file or is null: " + resourceName);
		}
        return contents;
	}
	// endregion

	@GetMapping("/getDetailsOfUser")
	public GetUserDetailsDTO getDetailsOfUser(String username) throws Exception {
		HttpResponse<String> response = this.getResponseFromEndpoint_userDetails();
		if (response.statusCode() != 200) {
			throw new Exception("Custom HTTP exception. Request failed. Git API unreachable.");
		}

		ObjectMapper mapper = this.getMapperFor__getDetailsOfUserDeserializer();
		GetUserDetailsDTO dto = mapper.readValue(response.body(), GetUserDetailsDTO.class);
		if (dto.getClass() == GetUserDetailsDTO.class) {
			System.out.println("dto is of type GetUserRepositoriesDTO");
		} else {
			System.out.println("dto is not of type GetUserRepositoriesDTO");
		}
		return dto;
	}

	@GetMapping("/scanReposOfUser")
	public GetUserRepositoriesDTO scanReposOfUser(String username) throws Exception {
		HttpResponse<String> response = this.getResponseFromEndpoint_userRepos();
		if (response.statusCode() != 200) {
			throw new Exception("Custom HTTP exception. Request failed. Git API unreachable.");
		}

		ObjectMapper mapper = this.getMapperFor__getReposOfUserDeserializerFromGitReply();
		GetUserRepositoriesDTO dto = mapper.readValue(response.body(), GetUserRepositoriesDTO.class);
		if (dto.getClass() == GetUserRepositoriesDTO.class) {
			System.out.println("dto is of type GetUserRepositoriesDTO");
		} else {
			System.out.println("dto is not of type GetUserRepositoriesDTO");
		}
		return dto;
	}

	// used to avoid rate limit
	@GetMapping("/scanReposOfUserOffline")
	public GetUserRepositoriesDTO scanReposOfUserOffline(String username) throws Exception {
		String response = this.getResponseFromResources(configFilePaths.get(0));

		ObjectMapper mapper = this.getMapperFor__getReposOfUserDeserializerFromGitReply();
		GetUserRepositoriesDTO dto = mapper.readValue(response, GetUserRepositoriesDTO.class);

		// todo: additional security checks needed when deserializing and defining DTOs
		if (dto.getClass() == GetUserRepositoriesDTO.class) {
			System.out.println("dto is of type GetUserRepositoriesDTO");
		} else {
			System.out.println("dto is not of type GetUserRepositoriesDTO");
		}
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
			dto.repositories.forEach(e -> {
				results.add(e);
			});
			return results;
		}
	}

}
