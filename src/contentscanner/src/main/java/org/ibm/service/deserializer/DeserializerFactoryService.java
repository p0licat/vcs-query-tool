package org.ibm.service.deserializer;

import org.ibm.model.deserializers.GetRepoContentsDeserializerFromGithubReply;
import org.ibm.model.deserializers.GetRepoContentsFilePathDeserializerFromGitHubReply;
import org.ibm.shared.model.vcsmanager.dto.RepoContentsFromGithubReplyDTO;
import org.ibm.shared.model.vcsmanager.dto.RepoFileFromGitHubReplyDTO;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Service
public class DeserializerFactoryService {
	public ObjectMapper getMapperFor__getRepoContentsDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RepoContentsFromGithubReplyDTO.class, new GetRepoContentsDeserializerFromGithubReply());
		mapper.registerModule(module);
		return mapper;
	}

	public ObjectMapper getMapperFor__getRepoContentsDeserializerSingleFile() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RepoFileFromGitHubReplyDTO.class,
				new GetRepoContentsFilePathDeserializerFromGitHubReply());
		mapper.registerModule(module);
		return mapper;
	}
}
