package org.ibm.repository;

import java.util.List;

import org.ibm.model.repohub.GitRepository;

public interface GitRepoRepositoryCustom {
	public List<GitRepository> findByFirstName(String name);
}
