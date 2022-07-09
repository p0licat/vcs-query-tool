package org.ibm.model.dto;

// in the future, look to use ObjectMapper
// or JsonBuilder
// or Serialization options
public class GetReposOfUserDTO {
	String userLogin;
	long id;
	String nodeId;
	String subscriptionsUrl;
	String reposUrl;
}
