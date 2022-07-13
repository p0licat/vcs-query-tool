package org.ibm.gatherer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class GathererRestApplication {

	Logger logger = Logger.getLogger(getClass().getName());
	
	public static void main(String[] args) {
		SpringApplication.run(GathererRestApplication.class, args);
	}

	@GetMapping("/scanReposOfUser")
	public List<String> scanReposOfUser(String username) {
		JacksonJsonParser p = new JacksonJsonParser();
		List<String> res = new ArrayList<String>();
		res.add("{}");
		try {
			res = p.parseList("{"+username+"}").stream().map(s -> {return s.toString();}).collect(Collectors.toList());
		} catch (Exception io) {
			logger.info(io.toString());
		}
		return res;
	}
	
	
}
