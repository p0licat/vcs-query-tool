package org.ibm.service.persistence.contentsfilesservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.ibm.model.contents.ContentFile;
import org.ibm.repository.RepoContentsRepository;
import org.ibm.service.requests.contentsrequesterservice.ContentsRequesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentsFilesService {
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
				em.persist(f);
				f.setContents(this.contentsRequesterService.requestContentsOfDownloadUrl(f.getDownloadUrl()));
			}
		}
		
		return false;
	}
	
	
}
