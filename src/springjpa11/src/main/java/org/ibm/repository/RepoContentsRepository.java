package org.ibm.repository;

import org.ibm.model.repohub.RepoContents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoContentsRepository extends JpaRepository<RepoContents, Integer> {

}
