package org.ibm.springjpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
		GitRepository g = new GitRepository();
		g.setContentsNode(null);
		g.setContentsUrl("asdf");
		g.setDescription("asdf");
		g.setHtmlUrl("asdf");
		g.setMasterRepoHub(null);;
		g.setName("asdf");
		g.setNodeId("asdf");
		g.setUrl("asdf");
		em.persist(g);
		repository.save(g);
		em.flush();
	}
	
	@Test
	void testInMemoryService() {
		this.setUpOneRepo();
		List<ContentNode> nodesList = new ArrayList<>();
		String repoName = "asdf";
		
		ContentNode node;
		node = new ContentNode();
		node.setName("asdfd");
		node.setType("file");
		nodesList.add(node);
		
		service.persistContentNodes(nodesList, repoName, "username");
	}
	
}
