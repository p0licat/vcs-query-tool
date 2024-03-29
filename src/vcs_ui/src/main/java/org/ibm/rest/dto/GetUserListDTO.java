package org.ibm.rest.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetUserListDTO {
	public List<GetUserDetailsDTO> userList; // replace DTO with model objects where needed
}
