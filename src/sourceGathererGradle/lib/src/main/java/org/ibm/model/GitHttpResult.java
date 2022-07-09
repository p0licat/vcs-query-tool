package org.ibm.model;



import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class GitHttpResult {
	public Map<String, String> resultSet = null;
	
	public GitHttpResult(BufferedReader resultBuffer) {
		this.resultSet = new HashMap<String, String>();
		
		String line = null;
		while (true) {
			try {
				line = resultBuffer.readLine();
			} catch (IOException e) {
				
			}
			if (line == null) {
				break;
			}
			
			String[] values = line.split(":");
			this.resultSet.put(values[0], values[1]);
		}
	}
}
