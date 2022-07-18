package org.ibm.model.deserializers;

import java.io.IOException;

import org.ibm.model.contentscanner.dto.RepoContentsFromGithubReplyDTO;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class GetRepoContentsDeserializerFromGithubReply extends JsonDeserializer<RepoContentsFromGithubReplyDTO> {

	@Override
	public RepoContentsFromGithubReplyDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		return null;
	}

}
