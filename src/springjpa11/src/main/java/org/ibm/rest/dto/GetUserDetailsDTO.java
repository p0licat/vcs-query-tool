package org.ibm.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// in the future, look to use ObjectMapper
// or JsonBuilder
// or Serialization options
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetUserDetailsDTO {
	public String userLogin;
	public long id;
	public String nodeId;
	public String subscriptionsUrl;
	public String reposUrl;
	public String fullName;
}
