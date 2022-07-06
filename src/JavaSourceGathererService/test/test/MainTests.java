package test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;

public class MainTests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainTests t = new MainTests();
		t.TestHttpRequest();
	}
	
	public void TestHttpRequest() {
		String url = "http://127.0.0.1:3000";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(java.net.URI.create(url + "/" + "index.html"))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		HttpClient client = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.followRedirects(Redirect.NORMAL)
				.build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			
			System.out.println("Response code: " + response.statusCode());
			System.out.println(response.body());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
