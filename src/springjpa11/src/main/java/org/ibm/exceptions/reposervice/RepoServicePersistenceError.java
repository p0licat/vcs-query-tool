package org.ibm.exceptions.reposervice;

import lombok.ToString;

@SuppressWarnings("serial")
@ToString
public class RepoServicePersistenceError extends Exception {
	String message;
	public RepoServicePersistenceError(String str) {
		this.message = str;
	}
	
}
