package org.ibm.contentscanner;

import org.ibm.model.contentscanner.dto.RepoContentsDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class ContentGathererApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentGathererApplication.class, args);
	}

	@GetMapping("/getContentsOfRepo")
	public RepoContentsDTO getContentsOfRepo(String username, String repoName, String authKey) {
		return new RepoContentsDTO();
	}
	
}
