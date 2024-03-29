package org.ibm.springjpa;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import jakarta.persistence.EntityManager;
import org.ibm.test.utility.StringGenerator;
import org.springframework.transaction.annotation.Transactional;

import org.ibm.jpaservice.contentsgatherer.ContentsGathererService;
import org.ibm.model.deserializers.contentservice.model.ContentNode;
import org.ibm.model.repohub.GitRepository;
import org.ibm.repository.GitRepoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@DataJpaTest
public class ContentsGathererServiceInMemoryTest {
	
	@Autowired
	ContentsGathererService service;

	@Autowired
	GitRepoRepository repository;
	
	@Autowired
	private EntityManager em;
	
	@Transactional
	private void setUpOneRepo() {
		final Function<Integer, String> f = (e) -> StringGenerator.generateRandomString(5);
		GitRepository g = new GitRepository();
		g.setContentsNode(null);
		g.setContentsUrl(f.apply(5));
		g.setContentsUrl(f.apply(5));
		g.setDescription(f.apply(5));
		g.setHtmlUrl(f.apply(5));
		g.setMasterRepoHub(null);
		g.setName(f.apply(5));
		g.setNodeId(f.apply(5));
		g.setUrl(f.apply(5));
		em.persist(g);
		repository.save(g);
		em.flush();
	}
	
	@Test
	void testInMemoryService() {
		final Function<Integer, String> f = (e) -> StringGenerator.generateRandomString(5);
		this.setUpOneRepo();
		List<ContentNode> nodesList = new ArrayList<>();
		String repoName = f.apply(5);
		
		ContentNode node;
		node = new ContentNode();
		node.setName(f.apply(5));
		node.setType(f.apply(5));
		nodesList.add(node);

		service.gatherFileContents(nodesList, repoName);
		service.persistContentNodes(nodesList, repoName, "username");
	}

	@Test
	void testInMemoryServiceSimple() {
		final Function<Integer, String> f = (e) -> StringGenerator.generateRandomString(5);
		this.setUpOneRepo();
		List<ContentNode> nodesList = new ArrayList<>();
		String repoName = f.apply(5);

		ContentNode node;
		node = new ContentNode();
		node.setName(f.apply(5));
		node.setType(f.apply(5));
		nodesList.add(node);

		service.persistContentNodes(nodesList, repoName, "username");
	}
}
