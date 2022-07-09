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
	
	public GetReposOfUserDTO(long id, String reposUrl) {
		this.id = id;
		this.reposUrl = reposUrl;
	}
	
	public String toString() {
		String returnValue = "";
		returnValue += "{";
		returnValue += "id=" + this.id + ",";
		returnValue += "reposUrl=" + this.reposUrl;
		//returnValue += ",";
		returnValue += "}";
		return returnValue;
	}
}
