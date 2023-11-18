package org.ibm.config.servicemesh;

import org.ibm.exceptions.ConfigurationProviderArgumentError;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

@Service
@PropertySources({ @PropertySource({ "classpath:application_connection_urls.properties" }) })
public class ServiceMeshResourceManager {

	Logger logger = org.slf4j.LoggerFactory.getLogger(ServiceMeshResourceManager.class);
	@Value("${mesh.CONTENTS_GATHERER_URL}")
	private String contentsGathererUrl;
	@Value("${mesh.CONTENTS_GATHERER_PORT}")
	private String contentsGathererPort;
	@Value("${mesh.CONTENTS_SCANNER_URL}")
	private String contentsScannerUrl;
	@Value("${mesh.CONTENTS_SCANNER_PORT}")
	private String contentsScannerPort;
	@Value("${mesh.NETWORK_ADDR}")
	private String networkAddr;
	/*
	 * Gets a string from a configuration.
	 */
	public String getResourceValue(String keyName) throws ConfigurationProviderArgumentError {
		var fields = this.getClass().getDeclaredFields();
		for (var i : fields) {
			if (i.getName().compareTo(keyName) == 0) {
					i.setAccessible(true);
					try {
						return (String) i.get(this);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						logger.error(e.toString());
						throw new ConfigurationProviderArgumentError("Arg " + keyName + " did not produce results.");
					}
            }
		}
		
		throw new ConfigurationProviderArgumentError("No match for arg");
	}
}
