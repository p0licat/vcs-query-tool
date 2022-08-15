package org.ibm.service.serialization;

import java.util.HashMap;
import java.util.Map;

import org.ibm.model.deserializers.GetDetailsOfUserDeserializer;
import org.ibm.model.deserializers.ScanReposOfUserDeserializerFromEndpointReply;
import org.ibm.rest.dto.GetUserDetailsDTO;
import org.ibm.rest.dto.RequestUserRepositoriesDTO;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Service
public class MapperProvider {

	private Map<String, ObjectMapper> mapperRegistry;

	public MapperProvider() {
		this.mapperRegistry = new HashMap<>();
		mapperRegistry.put("GetUserDetailsDTO", getMapperFor__getUserDetailsDeserializer());
		mapperRegistry.put("RequestUserRepositoriesDTO", getMapperFor__scanReposOfUserDeserializer());
	}

	public ObjectMapper getMapper(String className) throws IndexOutOfBoundsException {
		return this.mapperRegistry.get(className);
	}

	private ObjectMapper getMapperFor__getUserDetailsDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserDetailsDTO.class, new GetDetailsOfUserDeserializer());
		mapper.registerModule(module);
		return mapper;
	}

	private ObjectMapper getMapperFor__scanReposOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RequestUserRepositoriesDTO.class, new ScanReposOfUserDeserializerFromEndpointReply());
		mapper.registerModule(module);
		return mapper;
	}
}
