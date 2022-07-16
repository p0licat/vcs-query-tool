package org.ibm.model.deserializers;

import java.io.IOException;

import org.ibm.rest.dto.GetUserDetailsDTO;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class GetDetailsOfUserDeserializer extends StdDeserializer<GetUserDetailsDTO> {

	public GetDetailsOfUserDeserializer() {this(null);}
	
	protected GetDetailsOfUserDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public GetUserDetailsDTO deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JacksonException {
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		// sometimes the endpoint wraps Object into list
		// like [{Object}] instead of "{Object}"
		if (node.getNodeType() == JsonNodeType.ARRAY) {
			node = node.get(0); 
		}
		
		
		long id = (Integer) ((IntNode) node.get("id")).numberValue();
		String reposUrl = node.get("repos_url").asText();
		
		String subscriptionsUrl = node.get("subscriptions_url").asText();
		String nodeId = node.get("node_id").asText();
		String userLogin = node.get("login").asText();
		
		return new GetUserDetailsDTO(userLogin, id, nodeId, subscriptionsUrl, reposUrl);
	}

}
