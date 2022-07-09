package org.ibm.service.tests;

import org.ibm.model.dto.IGitDto;
import org.ibm.service.GitHubConnectionService;
import org.ibm.service.IGitConnectionService;

public class HttpResponseLoggerGitHubTest {

	public static void main(String[] args) {
		IGitConnectionService service = new GitHubConnectionService("https://api.github.com/users");
		IGitDto response = service.getUserDetails("p0licat");
		
	}

}
