package org.ibm.service.persistence.applicationuser;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.ibm.model.applicationuser.ApplicationUser;
import org.ibm.repository.ApplicationUserRepository;
import org.ibm.rest.dto.GetUserDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceService {
	
	@Autowired
	private EntityManager em;
	
	@Autowired 
	private ApplicationUserRepository userRepository;

	@Transactional
	public void saveUserDetails(GetUserDetailsDTO u) {
		ApplicationUser newUser = new ApplicationUser();
		newUser.setFullName(u.getFullName());
		newUser.setId((int)(u.getId()));
		newUser.setUsername(u.getUserLogin());
		newUser.setUrl(u.getReposUrl());
		newUser.setReposUrl(u.getReposUrl());
		newUser.setGitId(u.getNodeId());
		
		em.persist(newUser);
		this.userRepository.save(newUser);
	}

	public ApplicationUser findUserByName(String username) throws Exception {
		ApplicationUser exampleUser = new ApplicationUser();
		exampleUser.setUsername(username);
		ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().caseSensitive());
		Example<ApplicationUser> example = Example.of(exampleUser, matcher);
		var foundAll = this.userRepository.findAll(example);
		if (foundAll.size() != 1) {
			throw new Exception("Custom exception.");
		}
		
		return foundAll.get(0);
	}
}
