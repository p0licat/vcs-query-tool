package org.ibm.model.dto;

public class GetUserRepositoriesDTO {

	String contentsUrl;
	String commitsUrl;
	String branchesUrl;
	
	//datetime
	String createdAt;
	String updatedAt;
	String pushedAt;
	
	public GetUserRepositoriesDTO(String contentsUrl, String commitsUrl, String branchesUrl, String createdAt,
			String updatedAt, String pushedAt) {
		super();
		this.contentsUrl = contentsUrl;
		this.commitsUrl = commitsUrl;
		this.branchesUrl = branchesUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.pushedAt = pushedAt;
	}
}
