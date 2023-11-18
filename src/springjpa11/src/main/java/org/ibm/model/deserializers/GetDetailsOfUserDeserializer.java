package org.ibm.model.deserializers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.ibm.rest.dto.GetUserDetailsDTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;


public class GetDetailsOfUserDeserializer extends StdDeserializer<GetUserDetailsDTO> {

	public GetDetailsOfUserDeserializer() {
		this(null);
	}

	protected GetDetailsOfUserDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public GetUserDetailsDTO deserialize(JsonParser jp, DeserializationContext _deserializationContext)
			throws IOException {

		JsonNode node = jp.getCodec().readTree(jp);

		// sometimes the endpoint wraps Object into list
		// like [{Object}] instead of "{Object}"
		if (node.getNodeType() == JsonNodeType.ARRAY) {
			node = node.get(0);
		}

		Map<String, Object> reflectiveMap = new HashMap<>();

		for (Field f : GetUserDetailsDTO.class.getDeclaredFields()) {
			if (f.getName().contains("UID")) {
				continue;
			}
			Object newObject = null;
			if (f.getType().equals(Long.class) || f.getType().equals(long.class)) {
				newObject = Integer.valueOf(1).longValue();
			}
			if (f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
				newObject = Integer.valueOf(1).longValue();
			}
			if (f.getType().equals(String.class)) {
				newObject = "";
			}
			if (newObject == null) {
				throw new IOException("Type not present.");
			}
			reflectiveMap.put(f.getName(), newObject);
		}

		for (Field f : GetUserDetailsDTO.class.getDeclaredFields()) {
			if (f.getName().contains("UID")) {
				continue;
			}
			if (f.getType().equals(Long.class)) {
				reflectiveMap.put(f.getName(), node.get(f.getName()).longValue());
			} else if (f.getType().equals(String.class)) {
				reflectiveMap.put(f.getName(), node.get(f.getName()).asText());
			}
		}

		return new GetUserDetailsDTO((String) reflectiveMap.get("userLogin"), (Long) reflectiveMap.get("id"),
				(String) reflectiveMap.get("nodeId"), (String) reflectiveMap.get("subscriptionsUrl"),
				(String) reflectiveMap.get("reposUrl"), (String) reflectiveMap.get("fullName"));
	}

}
