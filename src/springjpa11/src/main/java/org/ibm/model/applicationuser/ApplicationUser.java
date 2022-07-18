package org.ibm.model.applicationuser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="application_user")
@Table(name="application_user")
public class ApplicationUser {
	@Id
	private int id;
	private String username;
	
	@Column(name="full_name")
	private String fullName;
	
	@Column(name="url")
	private String url;
	
	@Column(name="repos_url")
	private String reposUrl;
	
	@Column(name="followers_url")
	private String followers_url;
	
	@Column(name="node_id")
	private String nodeId;
	
	@Column(name="git_id")
	private String gitId;
}
