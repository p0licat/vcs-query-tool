package org.ibm.model.repohub;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	
	@ManyToOne
	private RepoHub masterRepoHub;
	
	@OneToOne(mappedBy="ownerRepo", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private RepoContents contentsNode;
	
	private String nodeId;
	@Column(unique=true)
	private long repoGitId;
	private String htmlUrl;
	private String description;
	private String url;
	private String contentsUrl;
}
