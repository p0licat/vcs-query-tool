package org.ibm.repository.implementation;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.ibm.repository.GitRepoRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class GitRepoRepositoryImplementation<T, ID extends Serializable> implements GitRepoRepository<T, ID> {
	private EntityManager entityManager;
	
	public GitRepoRepositoryImplementation(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}
}