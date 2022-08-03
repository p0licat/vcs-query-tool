package org.ibm.rest.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetUserRepositoriesDTO {
	
	public ArrayList<RepositoryDTO> repositories;
	
}
