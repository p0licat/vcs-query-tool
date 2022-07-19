package org.ibm.rest.dto.endpointresponse;

import java.util.List;
import java.util.Set;

import org.ibm.model.deserializers.contentservice.model.ContentNode;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PopulateUserRepositoriesEndpointResponseDTO {
	List<ContentNode> nodeList;
	Set<String> performedRequests;
}
