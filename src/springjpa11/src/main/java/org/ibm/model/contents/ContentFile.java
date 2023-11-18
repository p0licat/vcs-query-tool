package org.ibm.model.contents;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.ibm.model.repohub.RepoContents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="contents_file")
@Table(name="contents_file")
public class ContentFile implements Inode {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String fileName;
	private String shaSum;
	private String downloadUrl;
	private Long fileSize;
	
	@Lob
	private String contents;
	
	@ManyToOne
	private RepoContents childOfRepo;
	
	@Override
	public String getFileHash() {
		return this.shaSum;
	}

	@Override
	public long getSize() {
		return this.fileSize;
	}

	@Override
	public String getApiUrl() {
		return this.downloadUrl;
	}

	@Override
	public String getType() {
		return "file";
	}
	
}
