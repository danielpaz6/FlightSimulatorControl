package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

public class ServerHandler implements ClientHandler {

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient, HashMap<String, Double> serverData) throws Exception {
		// Read from the user
		BufferedReader userInput = new BufferedReader(
				new InputStreamReader(inFromClient));
		
		// Write to the user
		//PrintWriter userOutput = new PrintWriter(outToClient);
		
		
		//System.out.println("Sending data to the client....");
		//userOutput.println("set controls/flight/rudder 1");
		
		int i = 0;
		
		String line;
		while(!(line=userInput.readLine()).equals("end"))
		{
			System.out.println(line);
			
			i++;
			if(i > 20)
				break;
		}
	}


}
