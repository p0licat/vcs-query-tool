package org.ibm.model.deserializers.contentservice;

import java.io.IOException;

import org.ibm.model.deserializers.contentservice.model.RepoFileFromGitHubReplyDTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class GetRepoContentsFilePathDeserializerFromGitHubReply extends JsonDeserializer<RepoFileFromGitHubReplyDTO> {

	@Override
	public RepoFileFromGitHubReplyDTO deserialize(JsonParser jp, DeserializationContext _deserializationContext)
			throws IOException {
		JsonNode node;
        node = jp.getCodec().readTree(jp);

        String name = node.get("name").asText();
		String path = node.get("path").asText();
		String sha = node.get("sha").asText();
		Long size = node.get("size").longValue();
		String url = node.get("url").asText();
		String download_url;
		String type = node.get("type").asText();
		String encoding = node.get("encoding").asText();
		String content = node.get("encoding").asText();
		if (type.compareTo("file") == 0) {
			download_url = node.get("download_url").asText();
		} else {
			throw new IOException("Fatal error.");
		}
		
		return new RepoFileFromGitHubReplyDTO(name, path, sha, size, url, download_url, type, encoding, content);
	}
}
