package org.ibm.model.repohub;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.ibm.model.applicationuser.ApplicationUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="repo_hub")
@Table(name="repo_hub")
public class RepoHub {
	@Id
	private int id;
	private String url;
	
	@OneToOne
	private ApplicationUser hubOwner;
	
	@OneToMany
	private Set<GitRepository> repositories;
}
