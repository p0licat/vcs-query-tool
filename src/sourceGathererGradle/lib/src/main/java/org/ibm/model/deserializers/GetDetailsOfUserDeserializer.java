package org.ibm.model.deserializers;

import java.io.IOException;

import org.ibm.model.dto.GetUserDetailsDTO;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

@SuppressWarnings("serial")
public class GetDetailsOfUserDeserializer extends StdDeserializer<GetUserDetailsDTO> {

	public GetDetailsOfUserDeserializer() {this(null);}
	
	protected GetDetailsOfUserDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public GetUserDetailsDTO deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JacksonException {
		JsonNode node = jp.getCodec().readTree(jp);
		long id = (Integer) ((IntNode) node.get("id")).numberValue();
		String reposUrl = node.get("repos_url").asText();
		
		return new GetUserDetailsDTO(id, reposUrl);
	}

}
