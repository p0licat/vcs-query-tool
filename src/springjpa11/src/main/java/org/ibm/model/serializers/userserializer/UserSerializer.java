package org.ibm.model.serializers.userserializer;

import java.util.ArrayList;
import java.util.List;

import org.ibm.model.applicationuser.ApplicationUser;
import org.ibm.rest.dto.endpointresponse.UserDTO;

public class UserSerializer {
	public static List<UserDTO> serialize(List<ApplicationUser> users) {
		var result = new ArrayList<UserDTO>();
		users.forEach(e -> {
			UserDTO newUser = new UserDTO(
				e.getId(),
				e.getFullName(),
				e.getUrl(),
				e.getReposUrl(),
				e.getFollowers_url(),
				e.getNodeId(),
				e.getGitId()
			);
			result.add(newUser);
		});
		return result;
	}
}
