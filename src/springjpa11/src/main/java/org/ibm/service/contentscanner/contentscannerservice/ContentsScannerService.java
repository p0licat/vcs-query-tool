package org.ibm.service.contentscanner.contentscannerservice;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.ibm.config.servicemesh.ServiceMeshResourceManager;
import org.ibm.exceptions.ConfigurationProviderArgumentError;
import org.ibm.model.deserializers.contentservice.GetRepoContentsDeserializerFromGithubReply;
import org.ibm.model.deserializers.contentservice.model.ContentNode;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromEndpointResponseDTO;
import org.ibm.model.deserializers.contentservice.model.RepoContentsFromGithubReplyDTO;
import org.ibm.model.repohub.GitRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Service
public class ContentsScannerService {

	Logger logger = org.slf4j.LoggerFactory.getLogger(ContentsScannerService.class);
	@Autowired
	private ServiceMeshResourceManager meshResources;

	private ObjectMapper getMapperFor__getRepoContentsDeserializer() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RepoContentsFromGithubReplyDTO.class, new GetRepoContentsDeserializerFromGithubReply());
		mapper.registerModule(module);
		return mapper;
	}
	
	private HttpResponse<String> makeRequest(String url) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.GET().build();

		return httpClient.send(request, BodyHandlers.ofString());
	}
	
	public List<ContentNode> recursiveContentNodeScan(List<GitRepository> repos, String username) throws IOException, InterruptedException, ConfigurationProviderArgumentError {
		// String foundName = repos.get(0).getName(); // proxy variable guards against
				// JPA NotExists errors

				// possible errors for the above .get()
				// if it doesn't exist, ContentsScanningForInexistentRepoError
				int apiLimit = 75;
				Set<String> performedRequests = new HashSet<>(); // maybe this container should have versioned persistence or
																	// make this method responsible for completeness // for
																	// example creating a stack of get...ofDirectory and looping
																	// while there are changes to the stack. note that the loop
																	// should not mutate the stack, but return a new one
				Stack<String> queryQueue = new Stack<>();
				List<String> allFileUrls = new ArrayList<>();
				List<ContentNode> nodeList = new ArrayList<>();
				String[] regexMatch = repos.get(0).getContentsUrl().split("/\\{");
				queryQueue.add(regexMatch[0]);

				ObjectMapper mapper = this.getMapperFor__getRepoContentsDeserializer();
				while (!queryQueue.empty()) {
					if (performedRequests.size() > apiLimit) {
						break;
					}

					var e = queryQueue.pop();
					String result2 = this
							.makeRequest("http://" + this.meshResources.getResourceValue("networkAddr") + ":" + "8081" + "/getContentsOfRepoAtContentsUrlOfDirectory?username=" + username
									+ "&contentsUrl=" + e)
							.body();
					performedRequests.add(e);
					var currentRequestDeserialized2 = mapper.readValue(result2, RepoContentsFromEndpointResponseDTO.class);

					currentRequestDeserialized2.getNodes().forEach(r -> {
						if (r.getType().equals("file")) {
							// either use recursion or a queue of requests
							// must also create a new route in the contentscanner
							// /getContentsOfRepoAtContentsUrl?...

							// move business logic elsewhere...
							if (!performedRequests.contains(r.getContentsUrl())) {

								try {
									String result = this
											.makeRequest("http://"+ this.meshResources.getResourceValue("networkAddr") + ":" + "8081" + "/getContentsOfRepoAtContentsUrlOfFile?username="
													+ username + "&contentsUrl=" + r.getContentsUrl())
											.body();
									// result.persist()
									performedRequests.add(r.getContentsUrl());
									logger.info("Found a file: " + r.getContentsUrl());
									logger.info("Found a file: " + r.getDownloadsUrl());
									logger.info("Response for contentsRequest:" + result);
									// Future<String> future = Future.;
									if (!allFileUrls.contains(r.getDownloadsUrl())) {
										allFileUrls.add(r.getDownloadsUrl());
										nodeList.add(r);
									}
									// fileDownloadUrls.add( )
								} catch (IOException | InterruptedException e1) {
									logger.error(e1.toString());
								} catch (ConfigurationProviderArgumentError e1) {
									logger.error(e1.toString());
									throw new RuntimeException("Failure with configuration provider");
								}
							}

						} else {
							// directory request branch
							try {
								String result = this
										.makeRequest("http://" + this.meshResources.getResourceValue("networkAddr") + ":" + "8081" + "/getContentsOfRepoAtContentsUrlOfDirectory?username="
												+ username + "&contentsUrl=" + r.getContentsUrl())
										.body();
								performedRequests.add(r.getContentsUrl());
								var currentRequestDeserialized = mapper.readValue(result,
										RepoContentsFromEndpointResponseDTO.class);

								currentRequestDeserialized.getNodes().forEach(node -> {
									if (node.getType().compareTo("file") == 0) {
										logger.info("Sub-file of directory with name: " + r.getName() + " is: "
												+ node.getContentsUrl());
										if (!allFileUrls.contains(node.getDownloadsUrl())) {
											allFileUrls.add(node.getDownloadsUrl());
											nodeList.add(node);
										}
									} else if (node.getType().compareTo("dir") == 0) {
										if (!performedRequests.contains(node.getContentsUrl())) {
											queryQueue.add(node.getContentsUrl());
											logger.info("Directory: added to query queue the url: " + node.getContentsUrl());
											nodeList.add(node);
										}
									}
								});

							} catch (IOException | InterruptedException e1) {
								logger.error(e1.toString());
							} catch (ConfigurationProviderArgumentError e1) {
								logger.error(e1.toString());
								throw new RuntimeException("Failure with configuration provider");
							}
						}
					});
				}
				
				return nodeList;
	}

}
