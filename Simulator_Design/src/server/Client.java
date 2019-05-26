package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import interpreter.Server;

public class Client
{
	public Socket theServer;
	BufferedReader userInput;
	BufferedReader serverInput;
	PrintWriter outToServer;
	PrintWriter outToScreen;	
	
	public void sendSimulatorOrder(String var, double value) {
		//System.out.println("Order: " + "set " + var + " " + value);
		outToServer.println("set " + var + " " + value);
		outToServer.flush();
		
		//System.out.println("Finished to Order after a flush");
	}
	
	public void sendSimulatorText(String text) {
		outToServer.println(text);
		outToServer.flush();
	}
	
	private void readInputsAndSend(BufferedReader in, PrintWriter out,String exitStr)
	{
		try {
			String line;
			while(!(line=in.readLine()).equals(exitStr)){
				out.println(line);
				out.flush();
			}
		}
		catch (IOException e) { e.printStackTrace();}
	}

	public void start(String ip, int port) throws Exception
	{
		theServer = new Socket(ip, port);
		System.out.println("Client: We are now connected to the Flight Simulator!");
		
		userInput = new BufferedReader(new InputStreamReader(System.in));
		serverInput = new BufferedReader(new InputStreamReader(theServer.getInputStream()));
		outToServer = new PrintWriter(theServer.getOutputStream());
		outToScreen = new PrintWriter(System.out);

	}
	
	public void close() throws Exception {
		userInput.close();
		serverInput.close();
		outToServer.close();
		outToScreen.close();
		theServer.close();	
	}

}
