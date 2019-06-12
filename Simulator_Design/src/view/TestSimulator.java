package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TestSimulator {
	public static void main(String[] args) {
		try {
			boolean stop = false;
			int port = 5400;
			
			ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(1000);
			
			System.out.println("Waiting for the Flight Simulator to be opened...");
			while (!stop) {
				try {
					Socket aClient=server.accept(); // blocking call
					
					System.out.println("Flight Simulator connected to our server!");
					
					try {
						BufferedReader userInput = new BufferedReader(
								new InputStreamReader(aClient.getInputStream()));
						
						String line;
						while((line=userInput.readLine()) != null)
						{
							System.out.println(line);
						}
						
						aClient.getInputStream().close();
						aClient.getOutputStream().close();
						aClient.close();
					} catch (IOException e) {/*...*/}
				} catch(SocketTimeoutException e) {
					System.out.println("No one connected to us!");
				}
			}
			
			server.close();
		} catch(Exception e) { e.printStackTrace(); }
	}
}
