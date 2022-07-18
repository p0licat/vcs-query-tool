package org.ibm;

import org.ibm.rest.dto.RequestUserDetailsDTO;
import org.ibm.rest.dto.RequestUserRepositoriesDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@PropertySources({ @PropertySource({ "classpath:application.properties" }) })
@RestController
@ComponentScan(basePackages = "{org.ibm.*}")
@EntityScan("org.ibm.*")
public class SpringjpaApplication {

	
	@PostMapping("/populateUserRepositories")
	public RequestUserRepositoriesDTO requestUserRepositoryData(String username, String repoName) {
		return null;
		
	}
	
	@PostMapping("/populateUserDetails")
	public RequestUserDetailsDTO requestUserDetailsData(String username) {
		return null;
		
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringjpaApplication.class, args);
	}
}
