package org.ibm.model.deserializers.contentservice;

import java.io.IOException;
import java.util.ArrayList;

import org.ibm.model.deserializers.contentservice.model.ContentNode;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromEndpointResponseDTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class GetRepoContentsDeserializerFromEndpointResponse extends JsonDeserializer<RepoContentsFromEndpointResponseDTO> {

	@Override
	public RepoContentsFromEndpointResponseDTO deserialize(JsonParser jp, DeserializationContext _deserializationContext)
			throws IOException {
		JsonNode node;
        node = jp.getCodec().readTree(jp);

        ArrayList<ContentNode> result = new ArrayList<>();
		
		// 1 bug already caused by not refactoring this to an external dependency/module
		// 
		for (JsonNode child : node.get("nodes")) {
			
			String name = child.get("name").asText();
			String path = child.get("path").asText();
			String SHASum = child.get("0xffff").asText();
			Long size = child.get("size").longValue();
			String url = child.get("contentsUrl").asText();
			String download_url = null;
			String type = child.get("type").asText();
			if (type.compareTo("file") == 0) {
				download_url = child.get("downloadsUrl").asText();
			}
			
			result.add(new ContentNode(name, path, url, download_url, size, type, SHASum));
		}
		
		
		return new RepoContentsFromEndpointResponseDTO(result); 
	}

}
