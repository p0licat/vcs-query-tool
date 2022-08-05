package org.ibm.service.persistence.reposervice;

import java.util.List;
import java.util.stream.Collectors;

import org.ibm.model.applicationuser.ApplicationUser;
import org.ibm.model.repohub.GitRepository;
import org.ibm.repository.GitRepoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RepoPersistenceService {
//	
//	@Autowired
//	private EntityManager em;
	
//	@Autowired 
//	private RepoHubRepository repoHubRepo;
	
	@Autowired 
	private GitRepoRepository repoRepo;

	// handle fetch type and optimize 
	public List<GitRepository> getReposOfUser(ApplicationUser user) {
		var repoList = this.repoRepo.findAll().stream().filter(e -> e.getMasterRepoHub().getHubOwner().getId() == (user.getId())).collect(Collectors.toList());
		return repoList;
	}
	
	
	
}
