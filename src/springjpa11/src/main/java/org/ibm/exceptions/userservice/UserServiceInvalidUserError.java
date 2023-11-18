package org.ibm.exceptions.userservice;

import lombok.ToString;

@ToString
public class UserServiceInvalidUserError extends Exception {
	String message;
	public UserServiceInvalidUserError(String str) {
		this.message = str;
	}
	
}
