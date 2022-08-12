package org.ibm.service.configuration.configurationprovider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.ibm.exceptions.ConfigurationProviderArgumentError;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationProvider {
	public String getConfigurationFileContents(String fileName) throws ConfigurationProviderArgumentError {
		try {
			return new String(this.getClass().getClassLoader().getResourceAsStream(fileName).readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new ConfigurationProviderArgumentError("File " + fileName + " did not produce results.");
		}
	}
	
}
