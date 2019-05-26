package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public interface ClientHandler {
	void handleClient(InputStream inFromClient, OutputStream outToClient, HashMap<String, Double> serverData) throws Exception;
}
