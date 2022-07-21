package org.ibm.model.deserializers.contentservice.model;

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
public class RepoFileFromGitHubReplyDTO {
	private String name;
	private String path;
	private String sha;
	private Long size;
	private String url;
	private String download_url;
	private String type;
	private String encoding;
	private String content;
}
