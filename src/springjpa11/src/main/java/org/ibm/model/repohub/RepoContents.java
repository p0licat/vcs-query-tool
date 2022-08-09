package org.ibm.model.repohub;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.ibm.model.contents.ContentDir;
import org.ibm.model.contents.ContentFile;

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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String repoName;
	
	@ManyToOne
	private GitRepository ownerRepo;
	
	@OneToMany(mappedBy = "childOfRepo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	//@JoinColumn(name="id")
	private List<ContentFile> files;
	
	@OneToMany(mappedBy = "childOfRepo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	//@JoinColumn(name="id")
	private List<ContentDir> dirs;
}
