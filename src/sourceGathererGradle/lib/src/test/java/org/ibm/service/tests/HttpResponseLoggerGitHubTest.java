package org.ibm.service.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ibm.model.dto.GetUserDetailsDTO;
import org.ibm.service.GitHubConnectionService;


public class HttpResponseLoggerGitHubTest {

	public static void main(String[] args) {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com/users");
		GetUserDetailsDTO response = service.getUserDetails("p0licat").orElse(null);
		System.out.println(response);
		File f = new File("request_out.txt");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream s = new FileOutputStream(f);
			s.write(response.toString().getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
