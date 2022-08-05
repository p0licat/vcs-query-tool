package org.ibm.service.persistence.reposervice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.ibm.exceptions.reposervice.RepoServicePersistenceError;
import org.ibm.model.applicationuser.ApplicationUser;
import org.ibm.model.repohub.GitRepository;
import org.ibm.model.repohub.RepoHub;
import org.ibm.repository.ApplicationUserRepository;
import org.ibm.repository.GitRepoRepository;
import org.ibm.repository.RepoHubRepository;
import org.ibm.rest.dto.RequestUserRepositoriesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoPersistenceService {
	
	@Autowired
	private EntityManager em;
	
	@Autowired 
	private RepoHubRepository repoHubRepo;
	
	@Autowired
	private ApplicationUserRepository userRepo;
	
	@Autowired 
	private GitRepoRepository repoRepo;

	// handle fetch type and optimize 
	public List<GitRepository> getReposOfUser(ApplicationUser user) {
		var repoList = this.repoRepo.findAll().stream().filter(e -> e.getMasterRepoHub().getHubOwner().getId() == (user.getId())).collect(Collectors.toList());
		return repoList;
	}

	@Transactional
	public void persistReposForUser(String username, RequestUserRepositoriesDTO rs) throws RepoServicePersistenceError {
		var matchList = userRepo.findAll().stream().filter(e -> e.getUsername().compareTo(username)==0).collect(Collectors.toList());
		if (matchList.size() != 1) {
			throw new RepoServicePersistenceError(""); // todo
		}
		var foundUser = matchList.get(0);
		// todo find all repohubs of user (should be max 1)
		var repoHub = repoHubRepo.findAll().stream().filter(e -> e.getHubOwner().getId() == foundUser.getId()).findFirst().orElse(null);
		if (repoHub == null) {
			em.persist(foundUser);
			var newHub = new RepoHub();
			newHub.setHubOwner(foundUser);
			newHub.setUrl("");
			em.persist(newHub);
			
			List<GitRepository> newRepos = new ArrayList<>();
			rs.repositories.forEach(r -> {
				GitRepository newRepo = new GitRepository();
				newRepo.setContentsUrl(r.getContentsUrl());
				newRepo.setDescription(r.getDescription());
				newRepo.setHtmlUrl(r.getContentsUrl());
				newRepo.setMasterRepoHub(newHub);
				newRepo.setName(r.getName());
				newRepo.setNodeId(r.getNodeId());
				newRepo.setUrl(r.getContentsUrl());
				newRepos.add(newRepo);
			});
			
			for (GitRepository g : newRepos) {
				em.persist(g);
				this.repoRepo.save(g);
			}
			
			newHub.setRepositories(newRepos.stream().collect(Collectors.toSet()));
			this.repoHubRepo.save(newHub);
		}
	}
	
	
	
}
