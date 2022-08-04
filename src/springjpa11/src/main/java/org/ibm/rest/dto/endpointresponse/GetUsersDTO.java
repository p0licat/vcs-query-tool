package org.ibm.rest.dto.endpointresponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUsersDTO {
	public List<UserDTO> userList;
}
