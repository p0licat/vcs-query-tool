package org.ibm;

import java.util.List;

import javax.sql.DataSource;

import org.ibm.applicationuser.repository.ApplicationUserRepository;
import org.ibm.model.applicationuser.ApplicationUser;
import org.ibm.rest.dto.RequestUserDetailsDTO;
import org.ibm.rest.dto.RequestUserRepositoriesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@PropertySources({ @PropertySource({ "classpath:application.properties" }) })
@RestController
@ComponentScan(basePackages = "{org.ibm.*}")
@EntityScan("org.ibm.*")
public class SpringjpaApplication {

	@Autowired
	private ApplicationUserRepository userRepository;

	@GetMapping("/getUsers")
	// this should return a DTO.
	public List<ApplicationUser> getUsers() {
		return this.userRepository.findAll();
	}
	
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
