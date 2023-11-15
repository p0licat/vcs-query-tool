package org.ibm.repository.implementation;

import java.util.List;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import org.ibm.model.repohub.GitRepository;
import org.ibm.repository.GitRepoRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public class GitRepoRepositoryImplementation implements GitRepoRepositoryCustom {
	
	private EntityManager entityManager;

	@Override // might also use Non-Primitive arguments and construct/deserialize here... by injecting Deserialization service. # todo
	@Transactional
	public List<GitRepository> findByFirstName(String name) {
		entityManager.isOpen();
		return null;
	}
	

}