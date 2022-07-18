package org.ibm.service.rest;

import org.ibm.service.rest.github.GitHubConnectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.ibm.service.rest.github*")
public class RestServiceConfigurationFactory {

	@Bean
	public GitHubConnectionService gitHubConnectionService() {
		return new GitHubConnectionService("https://api.github.com");
	}
}
