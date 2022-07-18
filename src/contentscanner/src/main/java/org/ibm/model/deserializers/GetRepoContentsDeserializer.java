package org.ibm.model.deserializers;

import java.io.IOException;

import org.ibm.model.contentscanner.dto.RepoContentsDTO;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class GetRepoContentsDeserializer extends JsonDeserializer<RepoContentsDTO> {

	@Override
	public RepoContentsDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		return null;
	}

}
