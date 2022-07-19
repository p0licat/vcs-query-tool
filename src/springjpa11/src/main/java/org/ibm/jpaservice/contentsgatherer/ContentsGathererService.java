package org.ibm.jpaservice.contentsgatherer;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.ibm.model.contents.ContentDir;
import org.ibm.model.contents.ContentFile;
import org.ibm.model.deserializers.contentservice.model.ContentNode;
import org.ibm.model.repohub.GitRepository;
import org.ibm.model.repohub.RepoContents;
import org.ibm.repository.GitRepoRepository;
import org.ibm.repository.RepoContentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ContentsGathererService {

	@Autowired
	private GitRepoRepository repoRepository; // commented untill check for repoName existence is determined necessary

	@Autowired
	private RepoContentsRepository repoContentsRepository;
	
	@Autowired
	private EntityManager em;

	@Transactional
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
				em.flush();
				
				if (repoContents == null) {
					repoContents = new RepoContents();
					repoContents.setDirs(null);
					repoContents.setFiles(null);
					GitRepository foundOwnerRepo = this.repoRepository.findAll().stream().filter(ef -> ef.getName().compareTo(repoName) == 0).findFirst().orElse(null);
					repoContents.setOwnerRepo(foundOwnerRepo);
					repoContents.setRepoName(repoName);
					
					em.persist(repoContents);
					this.repoContentsRepository.save(repoContents);
					dir.setChildOfRepo(repoContents);
				} else {
					dir.setChildOfRepo(repoContents); // should rename to childOfRepoConents
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
				em.flush();
				
				if (repoContents == null) {
					repoContents = new RepoContents();
					repoContents.setDirs(null);
					repoContents.setFiles(null);
					GitRepository foundOwnerRepo = this.repoRepository.findAll().stream().filter(ef -> ef.getName().compareTo(repoName) == 0).findFirst().orElse(null);
					repoContents.setOwnerRepo(foundOwnerRepo);
					repoContents.setRepoName(repoName);
					
					em.persist(repoContents);
					this.repoContentsRepository.save(repoContents);
					file.setChildOfRepo(repoContents);
				} else {
					file.setChildOfRepo(repoContents);
				}
				
			}
			// finally
			
			
		});
	}

}
