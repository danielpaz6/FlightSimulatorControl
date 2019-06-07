package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import interpreter.Server;

public class ServerSide {
	private int port;
	//private ExecutorService es;
	private volatile boolean stop;
	public Server serv;
	private String[] tmpDataList;
	public int routines;
	
	public ServerSide(int port, ClientHandler ch, Server server, int routines) {
		this.port = port;
		stop = false;
		this.serv = server;
		this.routines = routines; // how many times to update the Simulator variables per second.
		
		//es = Executors.newCachedThreadPool();
		
		// Initializes the serverData HashMap that contains the flight's data.
		tmpDataList = server.getSimVars().getVariables();
		for (String tmpData : tmpDataList) {
			serv.getServerData().put(tmpData, 0.0);
		}
	}
	
	private void runServer() throws Exception {
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(1000);
		
		System.out.println("Waiting for the Flight Simulator to be opened...");
		while (!stop) {
			//System.out.println("Waiting for a client at port : " + port);
			try {
				Socket aClient=server.accept(); // blocking call
				
				// Release the main thread, so it could run again.
				synchronized (this) {
					//(serv.lock).notifyAll();
					notifyAll();
				}
				
				System.out.println("Server: We are connected to the Flight Simulator!...");
				
				try {
					BufferedReader userInput = new BufferedReader(
							new InputStreamReader(aClient.getInputStream()));
					
					String line;
					while((line=userInput.readLine()) != null)
					{
						//System.out.println(line);
						
						String[] flightData = line.split(",");
						
						// Updates the HashMap serverData
						/*
						 * Since we don't have ConcurrentLinkedHashMap, we implemented one,
						 * We created 2 Arrays and 1 ConcurrentHashMap, the role of the 2 Arrays
						 * is to keep the order of the HashMap, because otherwise we couldn't know the location of 
						 * each data that we put in the HashMap.
						 */
						for(int i = 0; i < tmpDataList.length; i++)
						{
							serv.getServerData().put(tmpDataList[i], Double.parseDouble(flightData[i]));
						}
						
						// Updates the Simulator symbol table routines times in a second.
						Thread.sleep(1000 / routines);
					}
					
					aClient.getInputStream().close();
					aClient.getOutputStream().close();
					aClient.close();
				} catch (IOException e) {/*...*/}
			} catch(SocketTimeoutException e) {/*...*/}
		}
		
		server.close();
		//es.shutdown();
	}
	
	public void start()
	{	
		new Thread(() -> {
			try {
				runServer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		
		// stops the main thread from running until we are connected to the flight simulator.
		//System.out.println("We are in blocking call!");
		
		synchronized(this) {
			try {
				//System.out.println("Waiting...");
				wait();
				//(serv.lock).wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("We released from the blocking call!");
	}
	
	public void stop(){
		stop = true;
		//es.shutdown();
	}
}
