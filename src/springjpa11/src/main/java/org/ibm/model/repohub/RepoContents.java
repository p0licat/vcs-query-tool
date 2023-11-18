package org.ibm.model.repohub;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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
