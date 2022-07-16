package org.ibm.model.repohub;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
	private int id;
	private String repoName;
	
	@OneToMany(mappedBy = "childOfRepo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Map<String, ContentFile> files;
	
	@OneToMany(mappedBy = "childOfRepo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Map<String, ContentDir> dirs;
}
