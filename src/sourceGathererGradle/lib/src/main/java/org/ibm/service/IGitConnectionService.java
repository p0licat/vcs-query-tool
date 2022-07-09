package org.ibm.service;

import java.util.Optional;

import org.ibm.model.dto.IGitDto;

public interface IGitConnectionService {
	
	public String sendGETRequest(String URI);
	public String sendPOSTRequest(String URI);
	
	public Optional<IGitDto> getUserDetails(String userName);
	public String getRepositoriesOfUser(String userName);
	public String getCommitsOfRepository(String userName, String repositoryName);
	public String getRepositoryContentsAtPath(String userName, String repositoryName, String path);
	
}
