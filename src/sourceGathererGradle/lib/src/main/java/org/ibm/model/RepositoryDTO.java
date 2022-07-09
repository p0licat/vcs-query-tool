package org.ibm.model;

public class RepositoryDTO {
	
	String contentsUrl;
	String commitsUrl;
	String branchesUrl;
	
	//datetime
	String createdAt;
	String updatedAt;
	String pushedAt;
	
	public RepositoryDTO(String contentsUrl, String commitsUrl, String branchesUrl, String createdAt, String updatedAt,
			String pushedAt) {
		super();
		this.contentsUrl = contentsUrl;
		this.commitsUrl = commitsUrl;
		this.branchesUrl = branchesUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.pushedAt = pushedAt;
	}

	@Override
	public String toString() {
		return "RepositoryDTO [contentsUrl=" + contentsUrl + ", commitsUrl=" + commitsUrl + ", branchesUrl="
				+ branchesUrl + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", pushedAt=" + pushedAt
				+ "]";
	}
	
	
}
