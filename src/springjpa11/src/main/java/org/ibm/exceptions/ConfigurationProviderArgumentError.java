package org.ibm.exceptions;

import lombok.ToString;

@ToString
public class ConfigurationProviderArgumentError extends Exception {
	String message;
	public ConfigurationProviderArgumentError(String str) {
		this.message = str;
	}
	
}
