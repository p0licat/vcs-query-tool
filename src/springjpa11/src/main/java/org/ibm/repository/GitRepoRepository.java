package org.ibm.repository;

import org.ibm.model.repohub.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;

//@NoRepositoryBean
public interface GitRepoRepository extends JpaRepository<GitRepository, Integer> {


}
