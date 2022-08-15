package org.ibm.rest.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
public class RequestUserRepositoriesDTO extends BaseDTO {
	
	public ArrayList<RepositoryDTO> repositories;
}
