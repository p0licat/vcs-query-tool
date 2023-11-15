package org.ibm.model.repohub;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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
	
	@OneToMany(mappedBy = "masterRepoHub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<GitRepository> repositories;
}
