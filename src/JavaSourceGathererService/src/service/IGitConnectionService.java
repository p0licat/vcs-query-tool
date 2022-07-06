package service;

import java.lang.String;

public interface IGitConnectionService {

	public byte[] sendGETRequest(String URI);
	public byte[] sendPOSTRequest(String URI);
	
}
