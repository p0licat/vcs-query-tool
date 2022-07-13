package org.ibm.model.dto;

import java.util.ArrayList;

import org.ibm.model.RepositoryDTO;

public class GetUserRepositoriesDTO {

	public GetUserRepositoriesDTO(ArrayList<RepositoryDTO> repositories) {
		super();
		this.repositories = (ArrayList<RepositoryDTO>) repositories.clone();
	}

	ArrayList<RepositoryDTO> repositories;
	
	@Override
	public String toString() {
		String result = "{";
		for (RepositoryDTO d : this.repositories) {
			result += d.toString();
			result += '\n';
		}
		result += "}";
		return result;
	}
}
