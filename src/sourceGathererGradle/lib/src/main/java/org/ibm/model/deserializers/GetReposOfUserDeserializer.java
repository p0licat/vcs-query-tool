package org.ibm.model.deserializers;

import java.io.IOException;

import org.ibm.model.dto.GetReposOfUserDTO;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

public class GetReposOfUserDeserializer extends StdDeserializer<GetReposOfUserDTO>{

	public GetReposOfUserDeserializer() {this(null);}
	
	protected GetReposOfUserDeserializer(Class<?> vc) {
		super(vc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GetReposOfUserDTO deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JacksonException {
		JsonNode node = jp.getCodec().readTree(jp);
		long id = (Integer) ((IntNode) node.get("id")).numberValue();
		
		
		return new GetReposOfUserDTO(id);
	}

}
