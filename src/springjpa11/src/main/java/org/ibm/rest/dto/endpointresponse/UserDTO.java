package org.ibm.rest.dto.endpointresponse;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	@Id
	private int id;
	private String fullName;
	private String url;
	private String reposUrl;
	private String followers_url;
	private String nodeId;
	private String gitId;
	private String userName;
}
