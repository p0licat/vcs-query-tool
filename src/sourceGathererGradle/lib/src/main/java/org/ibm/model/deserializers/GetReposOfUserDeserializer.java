package org.ibm.model.deserializers;

import java.io.IOException;

import org.ibm.model.dto.GetUserRepositoriesDTO;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class GetReposOfUserDeserializer extends StdDeserializer<GetUserRepositoriesDTO> {

	public GetReposOfUserDeserializer() {this(null);}
	
	protected GetReposOfUserDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public GetUserRepositoriesDTO deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JacksonException {
		JsonNode node = jp.getCodec().readTree(jp);
		String contentsUrl = node.get("contents_url").asText();
		String commitsUrl = node.get("commits_url").asText();
		String branchesUrl = node.get("branches_url").asText();
	
		String createdAt = node.get("created_at").asText();
		String updatedAt = node.get("updated_at").asText();
		String pushedAt = node.get("pushed_at").asText();
		
		return new GetUserRepositoriesDTO(contentsUrl, commitsUrl, branchesUrl, createdAt, updatedAt, pushedAt);
	}
	
}
