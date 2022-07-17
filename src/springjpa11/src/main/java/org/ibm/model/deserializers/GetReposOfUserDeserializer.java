package org.ibm.model.deserializers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ibm.rest.dto.GetUserRepositoriesDTO;
import org.ibm.rest.dto.RepositoryDTO;

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

	@SuppressWarnings("deprecation")
	@Override
	public GetUserRepositoriesDTO deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JacksonException {
		
		JsonNode node = null;
		try { 
			node = jp.getCodec().readTree(jp);
		} catch (Exception e) {
			throw e;
		}

		ArrayList<RepositoryDTO> result = new ArrayList<RepositoryDTO>();

		// should have a custom exception here
		// otherwise NPE
		for (JsonNode child : node.get("repositories")) {
			
			Map<String, Object> reflectiveMap = new HashMap<String, Object>();
			
			for (Field f : RepositoryDTO.class.getDeclaredFields()) {
				if (f.getName().contains("UID")) {
					continue;
				}
				Object newObject = null;
				if (f.getType().equals(Long.class)) {
					newObject = new Integer(1).longValue();
				}
				if (f.getType().equals(String.class)) {
					newObject = new String("");
				}
				if (newObject == null) {
					throw new IOException("Type not present.");
				}
				reflectiveMap.put(f.getName(), newObject);
			}
			
			for (Field f : RepositoryDTO.class.getDeclaredFields()) {
				if (f.getName().contains("UID")) {
					continue;
				}
				if (f.getType().equals(Long.class)) {
					 reflectiveMap.put(f.getName(), child.get(f.getName()).longValue());
				} else if (f.getType().equals(String.class)) {
					reflectiveMap.put(f.getName(), child.get(f.getName()).asText());
				}
			}
			
			result.add(new RepositoryDTO(
					(Long)reflectiveMap.get("id"), 
					(String)reflectiveMap.get("nodeId"), 
					(String)reflectiveMap.get("name"), 
					(String)reflectiveMap.get("description"), 
					(String)reflectiveMap.get("language"), 
					(String)reflectiveMap.get("contentsUrl"), 
					(String)reflectiveMap.get("commitsUrl"), 
					(String)reflectiveMap.get("branchesUrl"), 
					(String)reflectiveMap.get("createdAt"), 
					(String)reflectiveMap.get("updatedAt"), 
					(String)reflectiveMap.get("pushedAt")));
		}
		
		return new GetUserRepositoriesDTO(result); 
	}
	
}
