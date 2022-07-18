package org.ibm.model.contents;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ibm.model.repohub.RepoContents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="content_directory")
@Table(name="content_directory")
public class ContentDir implements Inode {

	@Id
	private int id;
	private String fileName;
	private String shaSum;
	private String contentsUrl;
	// for fileSize, make a setter... persist
	private long fileSize; // make this public to move to interface
	
	@ManyToOne
	private RepoContents childOfRepo;
	
	@Override
	public String getFileHash() {
		return this.shaSum;
	}

	@Override
	public long getSize() {
		return -1;
	}

	@Override
	public String getApiUrl() {
		return this.contentsUrl;
	}

	@Override
	public String getType() {
		return "directory";
	}

}
