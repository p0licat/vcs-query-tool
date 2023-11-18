package org.ibm.model.contents;

public interface Inode {
	String getFileHash();
	long getSize();
	String getApiUrl();
	String getType(); // refactor with enum
}
