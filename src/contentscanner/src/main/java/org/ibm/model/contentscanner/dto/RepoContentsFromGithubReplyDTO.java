package org.ibm.model.contentscanner.dto;

import java.util.List;

import org.ibm.model.contentscanner.model.ContentNode;

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
public class RepoContentsFromGithubReplyDTO {
	private Long id;
	private List<ContentNode> nodes;
}
