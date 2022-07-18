package org.ibm.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RepositoryDTO implements Serializable {

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
