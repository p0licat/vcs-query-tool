package org.ibm.gatherer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.ibm.model.RepositoryDTO;
import org.ibm.model.dto.GetUserRepositoriesDTO;
import org.ibm.service.rest.github.GitHubConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"org.ibm.service.*", "org.ibm.*"})
@RestController
//@ComponentScan(basePackages = "{org.ibm.service.rest.*}")
public class GathererRestApplication {

	Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	GitHubConnectionService gitHubConnectionService;
	
	public static void main(String[] args) {
		SpringApplication.run(GathererRestApplication.class, args);
	}

	@GetMapping("/scanReposOfUser")
	// return implements Serializable
	public List<String> scanReposOfUser(String username) {
		JacksonJsonParser p = new JacksonJsonParser();
		List<String> res = new ArrayList<String>();
		res.add("rando");
		try {
			List<String> objects = p.parseList("{username:"+username+"}").stream().map(s -> {return s.toString();}).collect(Collectors.toList());
			objects.forEach(e -> {res.add(e);});
		} catch (Exception io) {
			logger.info(io.toString());
		}
		return res;
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
			ArrayList<RepositoryDTO> results = new ArrayList<>();
			dto.repositories.forEach(e -> {results.add(e);});
			return results;
		}
	}
	
	
}
