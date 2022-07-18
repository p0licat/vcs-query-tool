package org.ibm.model.contents;

public interface Inode {
	public String getFileHash();
	public long getSize();
	public String getApiUrl();
	public String getType(); // refactor with enum
}
