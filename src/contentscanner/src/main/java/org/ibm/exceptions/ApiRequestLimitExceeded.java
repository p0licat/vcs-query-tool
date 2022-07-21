package org.ibm.exceptions;

import lombok.ToString;

@SuppressWarnings("serial")
@ToString
public class ApiRequestLimitExceeded extends Exception {
	String message;
	public ApiRequestLimitExceeded(String str) {
		this.message = str;
	}
	
}
