package org.ibm.model.dto;

// in the future, look to use ObjectMapper
// or JsonBuilder
// or Serialization options
public class GetReposOfUserDTO {
	public String userLogin;
	public long id;
	public String nodeId;
	public String subscriptionsUrl;
	public String reposUrl;
	
	public GetReposOfUserDTO(long id) {
		this.id = id;
	}
}
