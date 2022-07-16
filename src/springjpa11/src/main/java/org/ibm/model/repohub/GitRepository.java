package org.ibm.model.repohub;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="git_repository")
@Table(name="git_repository")
public class GitRepository {
	@Id
	private int id;
	private String name;
	
	@ManyToOne
	private RepoHub masterRepoHub;
	
	@OneToOne(mappedBy="ownerRepo", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private RepoContents contentsNode;
	
	private String nodeId;
	private long repoGitId;
	private String htmlUrl;
	private String description;
	private String url;
	private String contentsUrl;
}
