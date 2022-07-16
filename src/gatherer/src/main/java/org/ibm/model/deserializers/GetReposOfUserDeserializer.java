package org.ibm.model.deserializers;

import java.io.IOException;
import java.util.ArrayList;

import org.ibm.model.RepositoryDTO;
import org.ibm.model.dto.GetUserRepositoriesDTO;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class GetReposOfUserDeserializer extends StdDeserializer<GetUserRepositoriesDTO> {

	public GetReposOfUserDeserializer() {this(null);}
	
	protected GetReposOfUserDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public GetUserRepositoriesDTO deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JacksonException {
		
		JsonNode node = null;
		try { 
			node = jp.getCodec().readTree(jp);
		} catch (Exception e) {
			throw e;
		}
		
		ArrayList<RepositoryDTO> result = new ArrayList<>();
		
		for (JsonNode child : node) {
			
			String name = child.get("name").asText();
			Long id = (Long) child.get("id").longValue();
			String nodeId = child.get("node_id").asText();
			String description = child.get("description").asText();
			String language = child.get("language").asText();
			
			String contentsUrl = child.get("contents_url").asText();
			String commitsUrl = child.get("commits_url").asText();
			String branchesUrl = child.get("branches_url").asText();
			
			String createdAt = child.get("created_at").asText();
			String updatedAt = child.get("updated_at").asText();
			String pushedAt = child.get("pushed_at").asText();
			
			result.add(new RepositoryDTO(id, nodeId, name, description, language, contentsUrl, commitsUrl, branchesUrl, createdAt, updatedAt, pushedAt));
		}
		
		
		return new GetUserRepositoriesDTO(result); 
	}
	
}
