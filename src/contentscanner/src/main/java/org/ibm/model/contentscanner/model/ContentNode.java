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

	private String name;
	private String path;
	private String contentsUrl;
	private String downloadsUrl;
	// for fileSize, make a setter... persist
	private Long size; // make this public to move to interface
	private String type;
	private String shasum;
}
// actually a DTO, but hackishly shared by dto and model (for now)