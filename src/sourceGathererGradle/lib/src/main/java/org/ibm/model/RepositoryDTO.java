package org.ibm.model;

public class RepositoryDTO {
	
	@Override
	public String toString() {
		return "RepositoryDTO [name=" + name + ", nodeId=" + nodeId + ", id=" + id + ", description=" + description
				+ ", language=" + language + ", contentsUrl=" + contentsUrl + ", commitsUrl=" + commitsUrl
				+ ", branchesUrl=" + branchesUrl + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", pushedAt=" + pushedAt + "]";
	}

	String name;
	String nodeId;
	long id;
	
	String description;
	String language;
		
	
	String contentsUrl;
	String commitsUrl;
	String branchesUrl;
	
	//datetime
	String createdAt;
	String updatedAt;
	String pushedAt;

	public RepositoryDTO(String name, String nodeId, long id, String description, String language, String contentsUrl,
			String commitsUrl, String branchesUrl, String createdAt, String updatedAt, String pushedAt) {
		super();
		this.name = name;
		this.nodeId = nodeId;
		this.id = id;
		this.description = description;
		this.language = language;
		this.contentsUrl = contentsUrl;
		this.commitsUrl = commitsUrl;
		this.branchesUrl = branchesUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.pushedAt = pushedAt;
	}


}
