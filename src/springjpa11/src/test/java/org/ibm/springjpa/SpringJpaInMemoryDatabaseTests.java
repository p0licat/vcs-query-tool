package org.ibm.springjpa;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.ibm.model.applicationuser.ApplicationUser;
import org.ibm.model.deserializers.GetReposOfUserDeserializerFromEndpointReply;
import org.ibm.model.repohub.GitRepository;
import org.ibm.repository.ApplicationUserRepository;
import org.ibm.repository.GitRepoRepository;
import org.ibm.rest.dto.GetUserRepositoriesDTO;
import org.ibm.rest.dto.RepositoryDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@DataJpaTest
public class SpringJpaInMemoryDatabaseTests {
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private GitRepoRepository gitRepoRepository;
	
	@Autowired 
	private ApplicationUserRepository userRepository;
	
	private HttpResponse<String> makeRequest(String url) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.GET().build();

		return httpClient.send(request, BodyHandlers.ofString());

	}
	
	private ObjectMapper getMapperFor__getReposOfUserDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(GetUserRepositoriesDTO.class, new GetReposOfUserDeserializerFromEndpointReply());
		mapper.registerModule(module);
		return mapper;
	}

	@Test
	@Transactional
	void testAddUserToRepository() {
		ApplicationUser user = new ApplicationUser();
		user.setNodeId("asdf");
		
		em.persist(user);
		userRepository.save(user);
		em.flush();
		Long count = userRepository.findAll().stream().filter(e -> e.getNodeId().compareTo("asdf")==0).count();
		Assertions.assertTrue(count > 0);
	}
	
	@Test
	@Transactional // testing a service would offload the Transactional flag to the actual service, keeping the tests pure.
	void testGetGitGathererServiceEndpoint_getRepositoriesAndPersist() {
		String url = "http://127.0.0.1:8080/scanReposOfUser?username=p0licat";
		try {
			HttpResponse<String> response = this.makeRequest(url);
			Assertions.assertTrue(response.statusCode() == 200);
			ObjectMapper mapper = this.getMapperFor__getReposOfUserDeserializer();
			GetUserRepositoriesDTO dto;
			try {
				dto = mapper.readValue(response.body(), GetUserRepositoriesDTO.class);
			} catch (NullPointerException e) {
				throw e; // should be custom exception from Deserializer.
				// otherwise refactor deserializers as a sort of external module
			}
			Assertions.assertTrue(dto.toString().length() > 0);
			
			for (RepositoryDTO i : dto.getRepositories()) {
				GitRepository repo = new GitRepository();
				repo.setId(i.getId().intValue());
				
				em.persist(repo);
				this.gitRepoRepository.save(repo); // need a DTO to Model converter
			}
		} catch (IOException e) {
			Assertions.fail();
		} catch (InterruptedException e) {
			Assertions.fail();
		}

	}
}
