package org.ibm.service.serialization;

import java.lang.reflect.InvocationTargetException;

import org.ibm.exceptions.serializable.CustomMultiSerializationServiceError;
import org.ibm.rest.dto.BaseDTO;
import org.ibm.rest.dto.GetUserDetailsDTO;
import org.ibm.rest.dto.RequestUserRepositoriesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@ComponentScan(basePackages = { "org.ibm.service.serialization" })
public class DTOSerializationService {

	@Autowired
	private MapperProvider mapperProvider;
	
	public BaseDTO deserializeClass(String jsonResponse, String objectName) throws CustomMultiSerializationServiceError {
		for (var i : this.getClass().getMethods()) {
			if (i.getReturnType().getName().compareTo(objectName) == 0) {
				try {
					
					return (BaseDTO) i.invoke(this, jsonResponse);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		throw new CustomMultiSerializationServiceError(
				"Error when looking for DTO type or within deserialization method" + RequestUserRepositoriesDTO.class.getName());
	}

	public RequestUserRepositoriesDTO deserializeClass1(String jsonResponse) throws CustomMultiSerializationServiceError {
		ObjectMapper mapper = this.mapperProvider.getMapper(RequestUserRepositoriesDTO.class.getSimpleName());
		try {
			return mapper.readValue(jsonResponse, RequestUserRepositoriesDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		throw new CustomMultiSerializationServiceError(
				"Error when deserializing " + RequestUserRepositoriesDTO.class.getName());
	}
	
	public GetUserDetailsDTO deserializeClass2(String jsonResponse) throws CustomMultiSerializationServiceError {
		ObjectMapper mapper = this.mapperProvider.getMapper(GetUserDetailsDTO.class.getSimpleName());
		try {
			return mapper.readValue(jsonResponse, GetUserDetailsDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		throw new CustomMultiSerializationServiceError(
				"Error when deserializing " + GetUserDetailsDTO.class.getName());
	}

}
