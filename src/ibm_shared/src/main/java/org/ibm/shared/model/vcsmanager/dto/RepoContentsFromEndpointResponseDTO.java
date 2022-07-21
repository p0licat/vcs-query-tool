package org.ibm.shared.model.vcsmanager.dto;

import java.util.List;

import org.ibm.shared.model.vcsmanager.model.ContentNode;

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
public class RepoContentsFromEndpointResponseDTO {
	private List<ContentNode> nodes;
}
