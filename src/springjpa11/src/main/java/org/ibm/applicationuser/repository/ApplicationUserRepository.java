package org.ibm.applicationuser.repository;

import org.springframework.stereotype.Repository;
import org.ibm.model.applicationuser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {

}
