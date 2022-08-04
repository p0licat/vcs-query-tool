package org.ibm.springjpa;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.ibm.model.applicationuser.ApplicationUser;
import org.ibm.model.deserializers.GetReposOfUserDeserializerFromEndpointReply;
import org.ibm.model.repohub.GitRepository;
import org.ibm.model.repohub.RepoHub;
import org.ibm.repository.ApplicationUserRepository;
import org.ibm.repository.GitRepoRepository;
import org.ibm.repository.RepoHubRepository;
import org.ibm.rest.dto.GetUserRepositoriesDTO;
import org.ibm.rest.dto.RepositoryDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SpringjpaApplicationTests {

	@Autowired
	private GitRepoRepository gitRepoRepository;
	
	@Autowired
	private EntityManager em;
	
	@Autowired 
	private ApplicationUserRepository userRepository;
	
	@Autowired
	private RepoHubRepository hubRepository;
	
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
	@Rollback(false)
	void testContextAndCreate() {
		
	}
	
	@Test
	@Transactional
	@Rollback(false)
	void testAddUserToRepositoryThenHubAndRepos() {
		String url = "http://127.0.0.1:8080/scanReposOfUserOffline?username=p0licat";
		
		ApplicationUser user = new ApplicationUser();
		user.setNodeId("asdf");
		user.setUsername("p0licat");
		user.setGitId("12345");
		user.setReposUrl(url); // not the actual URL
		// bad test, could be replaced by mocked GitHub endpoint or actual request
		// should always rollback... unless H2 db
		em.persist(user);
		userRepository.save(user);
		
		Long count = userRepository.findAll().stream().filter(e -> e.getNodeId().compareTo("asdf")==0).count();
		Assertions.assertTrue(count > 0);
		
		RepoHub newRepoHub = new RepoHub();
		em.persist(newRepoHub);
		newRepoHub.setHubOwner(user);
		newRepoHub = hubRepository.save(newRepoHub);
		
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

			Set<RepositoryDTO> newSet = Set.copyOf(dto.getRepositories());
			Set<GitRepository> reposSet = new HashSet<>();
			for (RepositoryDTO r : newSet) {
				GitRepository g = new GitRepository();
				g.setContentsNode(null);
				g.setContentsUrl(r.getContentsUrl());
				g.setDescription(r.getDescription());
				g.setHtmlUrl(r.getContentsUrl()); // ??
				g.setMasterRepoHub(newRepoHub); // using OneToMany removes the need for ManyToOne ?
				g.setName(r.getName());
				g.setNodeId(r.getNodeId());
				g.setRepoGitId(r.getId());
				
				em.persist(g);
				reposSet.add(this.gitRepoRepository.save(g));
			}

		} catch (IOException e) {
			Assertions.fail();
		} catch (InterruptedException e) {
			Assertions.fail();
		}
	}
	
	@Test
	@Rollback(false)
	@Transactional
	void testGetGitGathererServiceEndpoint_getRepositoriesAndPersist() {
		String url = "http://127.0.0.1:8080/scanReposOfUserOffline?username=p0licat";
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
				repo.setRepoGitId(i.getId().intValue());
				
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
