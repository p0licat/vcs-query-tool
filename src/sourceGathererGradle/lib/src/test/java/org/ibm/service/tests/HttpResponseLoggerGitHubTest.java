package org.ibm.service.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ibm.model.dto.GetUserDetailsDTO;
import org.ibm.model.dto.GetUserRepositoriesDTO;
import org.ibm.service.GitHubConnectionService;


public class HttpResponseLoggerGitHubTest {
	
	private void testOne() {
		{
			GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
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
				s.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private void testTwo() {
		GitHubConnectionService service = new GitHubConnectionService("https://api.github.com");
		GetUserRepositoriesDTO response = service.getRepositoriesOfUser("p0licat").orElse(null);
		System.out.println(response);
		File f = new File("request_out_repos.txt");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream s = new FileOutputStream(f);
			s.write(response.toString().getBytes());
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		HttpResponseLoggerGitHubTest mainObj = new HttpResponseLoggerGitHubTest();
		mainObj.testTwo();
	}
	
	

}
