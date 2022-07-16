package org.ibm.model.repohub;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.ibm.model.applicationuser.ApplicationUser;
import org.ibm.model.contents.Inode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="repo_contents")
@Table(name="repo_contents")
public class RepoContents {
	@Id
	private int id;
	
	@OneToMany
	private Map<String, Inode> inodes;
}
