package org.ibm.model.dto;

import java.util.ArrayList;

import org.ibm.model.RepositoryDTO;

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
