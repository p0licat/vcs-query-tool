package org.ibm.model.serializers.reposerializer;

import org.ibm.model.repohub.GitRepository;
import org.ibm.rest.dto.RepositoryDTO;

/*
 * General purpose serializer.
 * Handles conversion between model objects and rest endpoint DTOs.
 * To be used by RestControllers for DTO conversion. (does not support deserialization)
 */
public class RepoSerializer {
	public static RepositoryDTO fromGitRepository(GitRepository g) {
		RepositoryDTO d = new RepositoryDTO();
		d.setBranchesUrl("");
		d.setCommitsUrl("");
		d.setContentsUrl(g.getContentsUrl());
		d.setCreatedAt("");
		d.setDescription(g.getDescription());
		//d.setLanguage(g.getContentsNode().getLanguage());
		d.setName(g.getName());
		d.setNodeId(g.getNodeId());
		return d;
	}
}
