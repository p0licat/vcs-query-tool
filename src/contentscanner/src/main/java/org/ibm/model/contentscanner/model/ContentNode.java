package org.ibm.model.contentscanner.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ContentNode {

	private int id;
	private String name;
	private String path;
	private String contentsUrl;
	private String downloadsUrl;
	// for fileSize, make a setter... persist
	private long size; // make this public to move to interface
	private String type;
}
