package org.ibm.vcs_ui;

import org.ibm.rest.dto.GetUserDetailsDTO;
import org.ibm.rest.dto.RepositoryDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@SpringBootApplication
@RestController
@RequestMapping("/api/")
public class VcsUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VcsUiApplication.class, args);
	}

	@GetMapping("/addUser")
	@Operation(summary = "" + "Add a new user to the list of stored users. "
			+ "User IDs are arguments to most other methods from this controller. "
			+ "Other operations modify user resources, but this is the only function allowed to add a user."
			+ "Deleting a user will not always remove its resources."
			+ "No members of this class are modified by any operation usable from this controller.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the book", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDetailsDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "User already exists.", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad request.", content = @Content) })
	public RepositoryDTO addNewUser() {
		return new RepositoryDTO();
	}

	@Operation(summary = "" + "Initiates a download of the repository contents. The user is required as a parameter.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success in updating the user's contents.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "400", description = "Error accessing resources.", content = @Content),
			@ApiResponse(responseCode = "400", description = "Internal error when persisting data. Rollback occurred.", content = @Content) })
	@GetMapping("/scanRepoOfUser")
	public String scanRepoOfUser(String userName, String repoName) {
		return "";
	}

	@Operation(summary = "" + "Redownloads repository contents. The old data is replaced without warning. "
			+ "Upon failure everything is rolled back to a valid state.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success in updating the repository contents.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "400", description = "Error accessing resources.", content = @Content),
			@ApiResponse(responseCode = "400", description = "Internal error when persisting data. Rollback occurred.", content = @Content) })
	@GetMapping("/redownloadFileContents")
	public String redownloadFileContents() {
		return "";
	}

	@Operation(summary = "" + "Searches database using substring matching.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success in searching. A list of matched content sections will be returned.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "400", description = "Error accessing resources.", content = @Content), })
	@GetMapping("/searchAllByPattern")
	public String searchAllByPattern() {
		return "";
	}

	// end planned
	@GetMapping("/searchAllByFileLabel")
	public String searchAllByFileLabel() {
		return "";
	}

	@GetMapping("/searchAllByMethodLabel")
	public String searchAllByMethodLabel() {
		return "";
	}

	@GetMapping("/getFileWithname")
	public String getFileWithname() {
		return "";
	}

	@GetMapping("/setFileLabels")
	public String setFileLabels() {
		return "";
	}

	@GetMapping("/getFileLabels")
	public String getFileLabels() {
		return "";
	}

	@GetMapping("/getAllMethods")
	public String getAllMethods() {
		return "";
	}

	@PostMapping("/setMethodLabels/")
	@ResponseStatus(code = HttpStatus.OK)
	public String setMethodLabels() {
		return "";
	}
}
