package org.ibm.jpaservice.contentsgatherer;

import java.util.List;

import javax.persistence.EntityManager;

import org.ibm.model.contents.ContentDir;
import org.ibm.model.contents.ContentFile;
import org.ibm.model.deserializers.contentservice.model.ContentNode;
import org.ibm.model.repohub.RepoContents;
import org.ibm.repository.RepoContentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan(basePackages = "org.ibm.*")
@EntityScan("org.ibm.*")
@Component
public class ContentsGathererService {

//	@Autowired
//	private GitRepoRepository repoRepository; // commented untill check for repoName existence is determined necessary

	@Autowired
	private RepoContentsRepository repoContentsRepository;
	
	@Autowired
	private EntityManager em;

	public void persistContentNodes(List<ContentNode> nodes, String repoName) {
		nodes.forEach(e -> {
			if (e.getType().compareTo("dir") == 0) {
				RepoContents repoContents;
				var foundRepoContents = repoContentsRepository.findAll().stream()
						.filter(rc -> rc.getRepoName().compareTo(repoName) == 0).findFirst().orElse(null);
				repoContents = foundRepoContents;
				
				ContentDir dir = new ContentDir();
				dir.setFileName(e.getName());
				dir.setContentsUrl(e.getContentsUrl());
				dir.setFileSize(0);
				dir.setShaSum(e.getShasum());
				
				em.persist(dir);
				
				if (repoContents == null) {
					em.persist(repoContents);
					this.repoContentsRepository.save(repoContents);
					dir.setChildOfRepo(repoContents);
				} else {
					dir.setChildOfRepo(repoContents);
				}
				
				// persistence achieved from ManyToOne direction
			} else if (e.getType().compareTo("file") == 0) {
				RepoContents repoContents;
				var foundRepoContents = repoContentsRepository.findAll().stream()
						.filter(rc -> rc.getRepoName().compareTo(repoName) == 0).findFirst().orElse(null);
				repoContents = foundRepoContents;
				
				ContentFile file = new ContentFile();
				file.setContents(""); // use getDownloadsUrl
				file.setDownloadUrl(e.getDownloadsUrl());
				file.setFileName(e.getName());
				file.setFileSize(e.getSize());
				file.setShaSum(e.getShasum());
				
				em.persist(file);
				
				if (repoContents == null) {
					em.persist(repoContents);
					this.repoContentsRepository.save(repoContents);
					file.setChildOfRepo(repoContents);
				} else {
					file.setChildOfRepo(repoContents);
				}
				
			}
		});
	}

}
