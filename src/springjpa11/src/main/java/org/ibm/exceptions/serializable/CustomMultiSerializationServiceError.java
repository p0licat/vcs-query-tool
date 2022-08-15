package org.ibm.exceptions.serializable;

import lombok.ToString;

@SuppressWarnings("serial")
@ToString
public class CustomMultiSerializationServiceError extends Exception {
	String message;
	public CustomMultiSerializationServiceError(String str) {
		this.message = str;
	}
	
}
