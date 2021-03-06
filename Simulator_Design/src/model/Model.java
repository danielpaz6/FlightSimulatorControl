package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import interpreter.Interpreter;
import interpreter.Server;
import server.Client;

public class Model extends Observable implements SimModel {
	Server server; // Simulator Server
	
	// Map Data members
	//MySerialServer mapServer; // Map Problem Solver Server
	Socket clientMap;
	PrintWriter out;
	BufferedReader in;
	
	Thread checkConnection;
	Interpreter interpreter;
	
	//Map ip and port details.
	String mapServerIp;
	int mapServerPort;
	
	String mapPathSol;
	double simPlaneX, simPlaneY;
	
	
	public Model(Server server) {
		this.server = server;
		interpreter = null;
		clientMap = null;
		out = null;
		in = null;
		
		/*
		 * Every 5 seconds, the model will check if we are still connected to the Flight Simulator.
		 * And if we are connected to the Flight Simulator, we'll setVisible false to the "you must connect to simulator" AnchorPane in the Manual Page.
		 */
		checkConnection = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {}
				
				if(server.client != null)
				{
					try {
						//System.out.println("1");
						server.client.theServer.getInputStream().available();
						//System.out.println("2");
						
						// If you are here it means you are connected to the Flight Simulator
						simPlaneX = server.getServerData().get("/sim/current-view/viewer-x-m");
						simPlaneY = server.getServerData().get("/sim/current-view/viewer-y-m");
						
						
						setChanged();
						notifyObservers("connectToServer_success");
					} catch (IOException e) {
						// You are not connected to the Flight Simulator
						server.client = null;
						if(interpreter != null) {
							interpreter.stop();
						}
						
						setChanged();
						notifyObservers("Disconnected_from_client");
					}
					
				}
			}
		});
		checkConnection.start();
	}
	
	private void stopInterpreter() {
		if(interpreter != null) {
			interpreter.stop();
			interpreter = null;
		}
	}
	
	@Override
	public void setAileron(double val) {
		// Make sure we are connected to the client
		if(server.client != null) {
			stopInterpreter();
			server.client.sendSimulatorOrder("/controls/flight/aileron", val);
		}
	}

	@Override
	public void setElevator(double val) {
		// Make sure we are connected to the client
		if(server.client != null) {
			stopInterpreter();
			server.client.sendSimulatorOrder("/controls/flight/elevator", val);
		}
	}

	@Override
	public void setThrottle(double val) {
		// Make sure we are connected to the client
		if(server.client != null) {
			stopInterpreter();
			server.client.sendSimulatorOrder("/controls/engines/current-engine/throttle", val);
		}
	}

	@Override
	public void setRudder(double val) {
		// Make sure we are connected to the client
		if(server.client != null) {
			stopInterpreter();
			server.client.sendSimulatorOrder("/controls/flight/rudder", val);
		}
	}

	@Override
	public double getAileron() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getelevator() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getThrottle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRudder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Server getServer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isMapServerAlive() {
		return clientMap != null ? true : false;
	}

	@Override
	public void connectToServer(String ip, double port) {
		server.client = new Client();
		
		try {
			server.client.start(ip, (int)port);
			
			setChanged();
			notifyObservers("connectToServer_success");
		} catch (Exception e) {
			
			server.client = null;
			setChanged();
			notifyObservers("connectToServer_failed");
		}
		
	}
	
	@Override
	public void connectToMapServer(String ip, double port, double[][] coordinates, int planeX, int planeY, int destX,
	int destY) {
		/*mapServerIp = ip;
		mapServerPort = (int)port;
		
		mapServer = new MySerialServer(mapSolverIP); 
		
		// Starting the Map Solver Server in new thread
		new Thread(()-> {
			try {
				SearchableClientHandler<String, Position> ch;
				ch = new SearchableClientHandler<>(
						new SearcherSolver<MatrixProblem, String, Position>(new BestFirstSearch<Position>()),
						new FileCacheManager<MatrixProblem, String>("./maze.xml")
				);
				
				mapServer.start(ch, "end"); // running the server
				System.out.println("Connected to the Map Server!");
			} catch (Exception e) {
				System.out.println("Failed to login to the Map Server!");
				mapServer = null;
				setChanged();
				notifyObservers("connectToMapServer_failed");
			}
			
		}).start();
		

		setChanged();
		notifyObservers("connectToMapServer_success");
		*/
		
		// Connecting as client to the Map Server Solver and return cheapest path
		//calculateMap(coordinates, planeX, planeY, destX, destY);
		
		//setChanged();
		//notifyObservers("doneMap_first_init");
		
		/*try {
			
			clientMap = new Socket(mapServerIp, mapServerPort);
			clientMap.close();
		} catch(Exception e) {
			e.printStackTrace();
			clientMap = null;
			setChanged();
			notifyObservers("connectToMapServer_failed");
			return;
		}*/
		
		mapServerIp = ip;
		mapServerPort = (int)port;
		
		setChanged();
		notifyObservers("connectToMapServer_success");
		
		setChanged();
		notifyObservers("doneMap_first_init");
	}
	
	@Override
	public void runScript(String text) {
		interpreter = new Interpreter(server);
		interpreter.start(text);
	}

	public void calculateMap(double[][] coordinates, int planeX, int planeY, int destX, int destY) {
		//System.out.println("Enter calculateMap():");
		try {
			//if(clientMap == null) { 
			clientMap = new Socket(mapServerIp, mapServerPort);
			
			System.out.println("We connected successfully to the Map!");
			System.out.println(coordinates.length);
			// If we passed this line, it means we logged in to Map Server.
			setChanged();
			notifyObservers("connectToServer_success");
			//System.out.println("after notify");
			
			clientMap.setSoTimeout(3000);				
			out=new PrintWriter(clientMap.getOutputStream());
			in=new BufferedReader(new InputStreamReader(clientMap.getInputStream()));
			//}
			
			String result;
			for(int i = 0; i < coordinates.length; i++) {
				result = Arrays.stream(coordinates[i])
				        .mapToObj(x -> String.valueOf((int)x))
				        .collect(Collectors.joining(",")).trim();
				
				if(result.isEmpty() || result == null)
					continue;
				
				//System.out.println(result);
				out.println(result);
				out.flush();
			}
			
			//System.out.println("end");
			out.println("end");
			out.flush();
			
			//System.out.println("Plane COORDS: " + planeX + "," + planeY);
			//System.out.println("DEST COORDS: " + destX + "," + destY);
			
			out.println(planeX + "," + planeY);
			out.flush();
			out.println(destX + "," + destY);
			out.flush();
			
			
			//System.out.println("we are about to in.readline()");
			mapPathSol = in.readLine();
			
			//System.out.println("We done map calculate!");
			setChanged();
			notifyObservers("done map calculate");
			
	
			out.close();
			in.close();
			clientMap.close();
			
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Could not connect to the map server!");
			clientMap = null;
			setChanged();
			notifyObservers("connectToMapServer_failed");
		}
		
	}

	@Override
	public String getPath() {
		return this.mapPathSol;
	}

	@Override
	public double getPlaneX() {
		return this.simPlaneX;
	}

	@Override
	public double getPlaneY() {
		return this.simPlaneY;
	}
}
