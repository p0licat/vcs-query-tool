package org.ibm.contentscanner;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.ibm.exceptions.ApiRequestLimitExceeded;
import org.ibm.exceptions.ConfigurationProviderArgumentError;
import org.ibm.service.deserializer.DeserializerFactoryService;
import org.ibm.service.rest.github.GitHubConnectionServiceFacade;
import org.ibm.shared.model.vcsmanager.dto.RepoContentsFromGithubReplyDTO;
import org.ibm.shared.model.vcsmanager.dto.RepoFileFromGitHubReplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication(scanBasePackages = { "org.ibm.service.*", "org.ibm.*" })
@RestController
public class ContentGathererApplication {

	@Autowired
	DeserializerFactoryService deserializerService;
	
	@Autowired
	GitHubConnectionServiceFacade gitHubConnectionServiceFacade;
	
	@Autowired
	ContentsGathererConfigurationProvider configurationProvider;

	public static void main(String[] args) {
		SpringApplication.run(ContentGathererApplication.class, args);
	}

	@GetMapping("/getContentsOfRepo")
	public RepoContentsFromGithubReplyDTO getContentsOfRepo(String username, String repoName)
			throws IOException, ApiRequestLimitExceeded, ConfigurationProviderArgumentError {
		String authKey = this.configurationProvider.getConfigurationFileContents("keyValue.txt");
		HttpResponse<String> response = this.gitHubConnectionServiceFacade.getResponseFromEndpoint_repoContents(username, repoName, authKey);
		if (response.statusCode() == 403) {
			if (response.body().contains("limit")) {
				throw new ApiRequestLimitExceeded("Request limit exceeded for this api key.");
			}
		}
		ObjectMapper mapper = this.deserializerService.getMapperFor__getRepoContentsDeserializer(); // alternatively inject multiple deserializers, which is sane
		RepoContentsFromGithubReplyDTO dto = mapper.readValue(response.body(), RepoContentsFromGithubReplyDTO.class);
		return dto;
	}

	@GetMapping("/getContentsOfRepoAtFilePath")
	public RepoFileFromGitHubReplyDTO getContentsOfRepoAtFilePath(String username, String repoName, String resourcePath,
			String resourceType) throws Exception {
		String authKey = this.configurationProvider.getConfigurationFileContents("keyValue.txt");
		HttpResponse<String> response = this.gitHubConnectionServiceFacade.getResponseFromEndpoint_repoContentsAtPath(username, repoName, authKey,
				resourcePath);

		if (resourceType.compareTo("dir") == 0) {
			throw new Exception("Query must contain a file resource type.");
		} else {
			ObjectMapper mapper = this.deserializerService.getMapperFor__getRepoContentsDeserializerSingleFile();
			RepoFileFromGitHubReplyDTO dto = mapper.readValue(response.body(), RepoFileFromGitHubReplyDTO.class);
			return dto;
		}
	}

	@GetMapping("/getContentsOfRepoAtDirPath")
	public RepoContentsFromGithubReplyDTO getContentsOfRepoAtDirPath(String username, String repoName,
			String resourcePath, String resourceType) throws Exception {
		String authKey = this.configurationProvider.getConfigurationFileContents("keyValue.txt");
		HttpResponse<String> response = this.gitHubConnectionServiceFacade.getResponseFromEndpoint_repoContentsAtPath(username, repoName, authKey,
				resourcePath);

		if (resourceType.compareTo("dir") == 0) {
			ObjectMapper mapper = this.deserializerService.getMapperFor__getRepoContentsDeserializer();
			RepoContentsFromGithubReplyDTO dto = mapper.readValue(response.body(),
					RepoContentsFromGithubReplyDTO.class);
			return dto;
		} else {
			throw new Exception("Query must contain a directory resource type.");
		}
	}

	@GetMapping("/getContentsOfRepoAtContentsUrlOfDirectory")
	public RepoContentsFromGithubReplyDTO getContentsOfRepoAtContentsUrlOfDirectory(String username, String contentsUrl) throws IOException, InterruptedException, ConfigurationProviderArgumentError {
		String authKey = this.configurationProvider.getConfigurationFileContents("keyValue.txt");
		HttpResponse<String> response = this.gitHubConnectionServiceFacade.getResponseFromEndpoint_repoContentsAtContentsUrl(username, contentsUrl, authKey);
		ObjectMapper mapper = this.deserializerService.getMapperFor__getRepoContentsDeserializer();
		var dto = mapper.readValue(response.body(), RepoContentsFromGithubReplyDTO.class);
		return dto;
	}
}
