package org.ibm.model.deserializers.contentservice;

import java.io.IOException;
import java.util.ArrayList;

import org.ibm.model.deserializers.contentservice.model.ContentNode;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromGithubReplyDTO;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class GetRepoContentsDeserializerFromGithubReply extends JsonDeserializer<RepoContentsFromGithubReplyDTO> {

	@Override
	public RepoContentsFromGithubReplyDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		
		JsonNode node = null;
		try { 
			node = jp.getCodec().readTree(jp);
		} catch (Exception e) {
			throw e;
		}
		
		ArrayList<ContentNode> result = new ArrayList<>();
		
		// 1 bug already caused by not refactoring this to an external dependency/module
		// 
		for (JsonNode child : node) {
			
			String name = child.get("name").asText();
			String path = child.get("path").asText();
			String shasum = child.get("sha").asText();
			Long size = child.get("size").longValue();
			String url = child.get("url").asText();
			String download_url = null;
			String type = child.get("type").asText();
			if (type.compareTo("file") == 0) {
				download_url = child.get("download_url").asText();
			}
			
			result.add(new ContentNode(name, path, url, download_url, size, type, shasum));
		}
		
		
		return new RepoContentsFromGithubReplyDTO(result); 
	}

}
