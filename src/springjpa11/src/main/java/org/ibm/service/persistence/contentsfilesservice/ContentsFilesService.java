package org.ibm.service.persistence.contentsfilesservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;

import org.ibm.model.contents.ContentFile;
import org.ibm.model.repohub.RepoContents;
import org.ibm.repository.RepoContentsRepository;
import org.ibm.service.requests.contentsrequesterservice.ContentsRequesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContentsFilesService {
	
	Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private EntityManager em;

	@Autowired
	private RepoContentsRepository contentsRepo;

	@Autowired
	private ContentsRequesterService contentsRequesterService;

	public List<ContentFile> getAllFiles() {
		List<ContentFile> allFiles = new ArrayList<>();
		var allContents = this.contentsRepo.findAll();
		for (var f : allContents) {
			var ffiles = f.getFiles();
			for (var e : ffiles) {
				allFiles.add(e);
			}
		}

		return allFiles;
	}

	@Transactional
	public boolean gatherAllContents(List<ContentFile> allFiles) throws IOException, InterruptedException {
		for (ContentFile f : allFiles) {
			if (f.getContents().compareTo("") == 0) {
				try {
					em.persist(f); // apparently does not persist by itself
					if (!f.getFileName().contains(".java") && !f.getFileName().contains(".py")) {
						continue;
					}
					logger.info("FILE: " + f.getFileName());
					f.setContents(this.contentsRequesterService.requestContentsOfDownloadUrl(f.getDownloadUrl()));
					em.flush();
				} catch (Exception e) {
					continue;
				}
			}
		}

		return true;
	}

	public List<ContentFile> findAllContainingSubstring(String search) {
		List<RepoContents> allContentsRepo = this.contentsRepo.findAll();
		var allFiles = new ArrayList<ContentFile>();
		for (var i : allContentsRepo) {
			var localAllFiles = i.getFiles().stream().filter(e -> e.getContents().contains(search))
					.collect(Collectors.toList());
			allFiles.addAll(localAllFiles);
		}
		return allFiles;
	}

}
