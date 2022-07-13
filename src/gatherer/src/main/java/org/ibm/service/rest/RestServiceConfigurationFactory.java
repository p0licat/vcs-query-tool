package org.ibm.service.rest;

import java.util.logging.Logger;

import org.ibm.service.rest.github.GitHubConnectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan("org.ibm.service.rest.*")
public class RestServiceConfigurationFactory {

	Logger logger = Logger.getLogger(getClass().getName());
	
	@Bean
	public GitHubConnectionService gitHubConnectionService() {
		logger.info("Creating bean");
		return new GitHubConnectionService("https://api.github.com");
	}
}
