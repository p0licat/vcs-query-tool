package org.ibm.model.serializers.userserializer;

import java.util.ArrayList;
import java.util.List;

import org.ibm.model.applicationuser.ApplicationUser;
import org.ibm.rest.dto.endpointresponse.UserDTO;

public class UserSerializer {
	public static List<UserDTO> serialize(List<ApplicationUser> users) {
		var result = new ArrayList<UserDTO>();
		users.forEach(e -> {
			UserDTO newUser = new UserDTO();
			newUser.setFullName(e.getFullName());
			newUser.setUserName(e.getUsername());
			newUser.setFollowers_url(e.getFollowers_url());
			newUser.setGitId(e.getGitId());
			newUser.setNodeId(e.getNodeId());
			newUser.setReposUrl(e.getUrl());
			newUser.setUrl(e.getUrl());
			result.add(newUser);
		});
		return result;
	}
}
