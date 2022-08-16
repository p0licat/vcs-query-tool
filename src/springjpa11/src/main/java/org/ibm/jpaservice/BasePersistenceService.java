package org.ibm.jpaservice;

import javax.persistence.EntityManager;

import org.ibm.exceptions.persistence.PersistenceServiceContextError;
import org.springframework.beans.factory.annotation.Autowired;

public class BasePersistenceService {

	@Autowired
	protected EntityManager em;
	
	public void testPersistenceConnection() throws PersistenceServiceContextError {
		if (! this.em.isOpen() ) {
			throw new PersistenceServiceContextError("Problem with persistence context.");
		}
	}
}
