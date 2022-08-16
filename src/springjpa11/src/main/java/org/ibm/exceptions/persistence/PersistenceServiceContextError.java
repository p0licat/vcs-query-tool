package org.ibm.exceptions.persistence;

import lombok.ToString;

@SuppressWarnings("serial")
@ToString
public class PersistenceServiceContextError extends Exception {
	String message;
	public PersistenceServiceContextError(String str) {
		this.message = str;
	}
}
