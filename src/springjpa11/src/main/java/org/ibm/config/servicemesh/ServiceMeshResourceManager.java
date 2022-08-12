package org.ibm.config.servicemesh;

import org.ibm.exceptions.ConfigurationProviderArgumentError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceMeshResourceManager {
	
	@Value("${mesh.CONTENTS_GATHERER_URL}")
	private String contentsGathererUrl;
	@Value("${mesh.CONTENTS_GATHERER_PORT}")
	private String contentsGathererPortl;
	@Value("${mesh.CONTENTS_SCANNER_URL}")
	private String contentsScannerUrl;
	@Value("${mesh.CONTENTS_SCANNER_PORT}")
	private String contentsScannerPort;
	/*
	 * Gets a string from a configuration.
	 */
	public String getResourceValue(String fileName, String keyName) throws ConfigurationProviderArgumentError {
		var fields = this.getClass().getDeclaredFields();
		for (var i : fields) {
			if (i.getName().compareTo(keyName) == 0) {
					i.setAccessible(true);
					try {
						return (String) i.get(this);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						throw new ConfigurationProviderArgumentError("File " + fileName + " did not produce results.");
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						throw new ConfigurationProviderArgumentError("File " + fileName + " did not produce results.");
					} finally {
					}
				}
		}
		
		throw new ConfigurationProviderArgumentError("No match for arg");
	}
}
