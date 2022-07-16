package org.ibm.rest.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RepositoryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	String nodeId;
	String name;
	String description;
	String language;
	// urls
	String contentsUrl;
	String commitsUrl;
	String branchesUrl;
	// datetime
	String createdAt;
	String updatedAt;
	String pushedAt;

}
