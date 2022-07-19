package org.ibm.contentscanner;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.ibm.exceptions.ApiRequestLimitExceeded;
import org.ibm.model.contentscanner.dto.RepoContentsFromGithubReplyDTO;
import org.ibm.model.contentscanner.dto.RepoFileFromGitHubReplyDTO;
import org.ibm.model.deserializers.GetRepoContentsDeserializerFromGithubReply;
import org.ibm.model.deserializers.GetRepoContentsFilePathDeserializerFromGitHubReply;
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
public class ContentGathererApplication {

	@Autowired
	GitHubConnectionService gitHubConnectionService;

	public static void main(String[] args) {
		SpringApplication.run(ContentGathererApplication.class, args);
	}

	private HttpResponse<String> getResponseFromEndpoint_repoContents(String username, String repoName,
			String authKey) {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		HttpResponse<String> response = service.getRawContentsOfRepository(username, repoName, authKey);
		return response;
	}

	private HttpResponse<String> getResponseFromEndpoint_repoContentsAtPath(String username, String repoName,
			String authKey, String resourcePath) {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		HttpResponse<String> response = service.getRawContentsOfRepositoryAtPath(username, repoName, authKey,
				resourcePath);
		return response;
	}

	private HttpResponse<String> getResponseFromEndpoint_repoContentsAtContentsUrl(String username, String contentsUrl,
			String apiKey) throws IOException, InterruptedException {
		GitHubConnectionService service = new GitHubConnectionService(contentsUrl);
		HttpResponse<String> response = service.getRawResponseFromMainUrl(apiKey);
		return response;
	}

	private ObjectMapper getMapperFor__getRepoContentsDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RepoContentsFromGithubReplyDTO.class, new GetRepoContentsDeserializerFromGithubReply());
		mapper.registerModule(module);
		return mapper;
	}

	private ObjectMapper getMapperFor__getRepoContentsDeserializerSingleFile() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RepoFileFromGitHubReplyDTO.class,
				new GetRepoContentsFilePathDeserializerFromGitHubReply());
		mapper.registerModule(module);
		return mapper;
	}

	@GetMapping("/getContentsOfRepo")
	public RepoContentsFromGithubReplyDTO getContentsOfRepo(String username, String repoName)
			throws IOException, ApiRequestLimitExceeded {
		String authKey = new String(this.getClass().getClassLoader().getResourceAsStream("keyValue.txt").readAllBytes(), StandardCharsets.UTF_8);
		HttpResponse<String> response = this.getResponseFromEndpoint_repoContents(username, repoName, authKey);
		if (response.statusCode() == 403) {
			if (response.body().contains("limit")) {
				throw new ApiRequestLimitExceeded("Request limit exceeded for this api key.");
			}
		}
		ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializer();
		RepoContentsFromGithubReplyDTO dto = mapper.readValue(response.body(), RepoContentsFromGithubReplyDTO.class);
		return dto;
	}

	@GetMapping("/getContentsOfRepoAtFilePath")
	public RepoFileFromGitHubReplyDTO getContentsOfRepoAtFilePath(String username, String repoName, String resourcePath,
			String resourceType) throws Exception {
		String authKey = new String(this.getClass().getClassLoader().getResourceAsStream("keyValue.txt").readAllBytes(), StandardCharsets.UTF_8);
		HttpResponse<String> response = this.getResponseFromEndpoint_repoContentsAtPath(username, repoName, authKey,
				resourcePath);

		if (resourceType.compareTo("dir") == 0) {
			throw new Exception("Query must contain a file resource type.");
		} else {
			ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializerSingleFile();
			RepoFileFromGitHubReplyDTO dto = mapper.readValue(response.body(), RepoFileFromGitHubReplyDTO.class);
			return dto;
		}
	}

	@GetMapping("/getContentsOfRepoAtDirPath")
	public RepoContentsFromGithubReplyDTO getContentsOfRepoAtDirPath(String username, String repoName,
			String resourcePath, String resourceType) throws Exception {
		String authKey = new String(this.getClass().getClassLoader().getResourceAsStream("keyValue.txt").readAllBytes(), StandardCharsets.UTF_8);
		HttpResponse<String> response = this.getResponseFromEndpoint_repoContentsAtPath(username, repoName, authKey,
				resourcePath);

		if (resourceType.compareTo("dir") == 0) {
			ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializer();
			RepoContentsFromGithubReplyDTO dto = mapper.readValue(response.body(),
					RepoContentsFromGithubReplyDTO.class);
			return dto;
		} else {
			throw new Exception("Query must contain a directory resource type.");
		}
	}

	@GetMapping("/getContentsOfRepoAtContentsUrlOfDirectory")
	public RepoContentsFromGithubReplyDTO getContentsOfRepoAtContentsUrlOfDirectory(String username, String contentsUrl) throws IOException, InterruptedException {
		String authKey = new String(this.getClass().getClassLoader().getResourceAsStream("keyValue.txt").readAllBytes(), StandardCharsets.UTF_8);
		HttpResponse<String> response = this.getResponseFromEndpoint_repoContentsAtContentsUrl(username, contentsUrl, authKey);
		ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializer();
		var dto = mapper.readValue(response.body(), RepoContentsFromGithubReplyDTO.class);
		return dto;
	}
}
