package org.ibm.contentscanner;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.ibm.exceptions.ConfigurationProviderArgumentError;
import org.ibm.model.contentscanner.dto.FileContentsFromGithubReplyDTO;
import org.ibm.model.contentscanner.dto.RepoContentsFromGithubReplyDTO;
import org.ibm.model.contentscanner.dto.RepoFileFromGitHubReplyDTO;
import org.ibm.service.deserializer.DeserializerFactoryService;
import org.ibm.service.rest.github.GitHubConnectionServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
	public ResponseEntity<RepoContentsFromGithubReplyDTO> getContentsOfRepo(String username, String repoName) {
		String authKey;
		try {
			authKey = this.configurationProvider.getConfigurationFileContents("keyValue.txt");
		} catch (ConfigurationProviderArgumentError e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		HttpResponse<String> response = this.gitHubConnectionServiceFacade
				.getResponseFromEndpoint_repoContents(username, repoName, authKey);
		if (response.statusCode() == 403) {
			if (response.body().contains("limit")) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		ObjectMapper mapper = this.deserializerService.getMapperFor__getRepoContentsDeserializer();
		RepoContentsFromGithubReplyDTO dto;
		try {
			dto = mapper.readValue(response.body(), RepoContentsFromGithubReplyDTO.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<RepoContentsFromGithubReplyDTO>(dto, HttpStatus.OK);
	}

	@GetMapping("/getContentsOfRepoAtFilePath")
	public ResponseEntity<RepoFileFromGitHubReplyDTO> getContentsOfRepoAtFilePath(String username, String repoName,
			String resourcePath, String resourceType) {
		String authKey;
		try {
			authKey = this.configurationProvider.getConfigurationFileContents("keyValue.txt");
		} catch (ConfigurationProviderArgumentError e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		HttpResponse<String> response = this.gitHubConnectionServiceFacade
				.getResponseFromEndpoint_repoContentsAtPath(username, repoName, authKey, resourcePath);

		if (resourceType.compareTo("dir") == 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			ObjectMapper mapper = this.deserializerService.getMapperFor__getRepoContentsDeserializerSingleFile();
			RepoFileFromGitHubReplyDTO dto;
			try {
				dto = mapper.readValue(response.body(), RepoFileFromGitHubReplyDTO.class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<RepoFileFromGitHubReplyDTO>(dto, HttpStatus.OK);
		}
	}

	@GetMapping("/getContentsOfRepoAtDirPath")
	public ResponseEntity<RepoContentsFromGithubReplyDTO> getContentsOfRepoAtDirPath(String username, String repoName,
			String resourcePath, String resourceType) {
		String authKey;
		try {
			authKey = this.configurationProvider.getConfigurationFileContents("keyValue.txt");
		} catch (ConfigurationProviderArgumentError e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		HttpResponse<String> response = this.gitHubConnectionServiceFacade
				.getResponseFromEndpoint_repoContentsAtPath(username, repoName, authKey, resourcePath);

		if (resourceType.compareTo("dir") == 0) {
			ObjectMapper mapper = this.deserializerService.getMapperFor__getRepoContentsDeserializer();
			RepoContentsFromGithubReplyDTO dto;
			try {
				dto = mapper.readValue(response.body(), RepoContentsFromGithubReplyDTO.class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<RepoContentsFromGithubReplyDTO>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getContentsOfRepoAtContentsUrlOfDirectory")
	public ResponseEntity<RepoContentsFromGithubReplyDTO> getContentsOfRepoAtContentsUrlOfDirectory(String username,
			String contentsUrl) {
		String authKey;
		try {
			authKey = this.configurationProvider.getConfigurationFileContents("keyValue.txt");
		} catch (ConfigurationProviderArgumentError e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		HttpResponse<String> response;
		try {
			response = this.gitHubConnectionServiceFacade.getResponseFromEndpoint_repoContentsAtContentsUrl(username,
					contentsUrl, authKey);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		ObjectMapper mapper = this.deserializerService.getMapperFor__getRepoContentsDeserializer();
		RepoContentsFromGithubReplyDTO dto;
		try {
			dto = mapper.readValue(response.body(), RepoContentsFromGithubReplyDTO.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<RepoContentsFromGithubReplyDTO>(dto, HttpStatus.OK);
	}

	@GetMapping("/getContentsAtDownloadUrl")
	public ResponseEntity<FileContentsFromGithubReplyDTO> getContentsAtDownloadUrl(String downloadUrl) {
		String authKey;
		try {
			authKey = this.configurationProvider.getConfigurationFileContents("keyValue.txt");
		} catch (ConfigurationProviderArgumentError e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		HttpResponse<String> response;
		try {
			response = this.gitHubConnectionServiceFacade
					.getResponseFromEndpoint_directDownloadUrl(downloadUrl, authKey);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String rawFileContents = response.body();
		var contentsDTO = new FileContentsFromGithubReplyDTO(rawFileContents, "", downloadUrl);
		return new ResponseEntity<FileContentsFromGithubReplyDTO>(contentsDTO, HttpStatus.OK);
	}
}
