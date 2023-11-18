package org.ibm.exceptions.reposervice;

import lombok.ToString;

@ToString
public class RepoServicePersistenceError extends Exception {
	String message;
	public RepoServicePersistenceError(String str) {
		this.message = str;
	}
	
}
