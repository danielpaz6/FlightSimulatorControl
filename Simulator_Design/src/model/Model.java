package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import algorithms.BestFirstSearch;
import algorithms.MatrixProblem;
import algorithms.Position;
import algorithms.State;
import interpreter.Interpreter;
import interpreter.Server;
import server.Client;
import server_side.FileCacheManager;
import server_side.MySerialServer;
import server_side.SearchableClientHandler;
import server_side.SearcherSolver;

public class Model extends Observable implements SimModel {
	Server server; // Simulator Server
	MySerialServer mapServer; // Map Problem Solver Server
	Thread checkConnection;
	Interpreter interpreter;
	
	//Map ip and port details.
	String mapServerIp;
	int mapServerPort;
	
	// Default Map Solver IP
	int mapSolverIP = 6420;
	
	String mapPathSol;
	double simPlaneX, simPlaneY;
	
	
	// Thread pool for map solutions
	ExecutorService mapExecutor = Executors.newFixedThreadPool(2);
	
	
	public Model(Server server) {
		this.server = server;
		interpreter = null;
		
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
						server.client.theServer.getInputStream().read();
						
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
	public void connectToMapServer(String ip, double port, String mapCor, int planeX, int planeY, int destX,
	int destY) {
		mapServerIp = ip;
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
			} catch (Exception e) {
				mapServer = null;
				setChanged();
				notifyObservers("connectToMapServer_failed");
			}
			
		}).start();
		

		setChanged();
		notifyObservers("connectToMapServer_success");
		
		
		// Connecting as client to the Map Server Solver 
		calculateMap(mapCor, planeX, planeY, destX, destY);
		
	}
	
	@Override
	public void runScript(String text) {
		interpreter = new Interpreter(server);
		interpreter.start(text);
	}

	public void calculateMap(String mapCor, int planeX, int planeY, int destX, int destY) {
		
		Socket s=null;
		PrintWriter out=null;
		BufferedReader in=null;
		try {
			s = new Socket(mapServerIp, mapServerPort);
			
			// If we passed this line, it means we logged in to Map Server.
			setChanged();
			notifyObservers("connectToServer_success");
			
			
			s.setSoTimeout(3000);
			out=new PrintWriter(s.getOutputStream());
			in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			//System.out.println(mapCor);
			//System.out.println("-------------");
			String[] rows = mapCor.split("\n");

			for(int i = 2; i < rows.length; i++) {
				//System.out.println(rows[i].trim());
				out.println(rows[i].trim());
				
				out.flush();
			}
			out.println("end");
			out.flush();
			
			out.println(planeX + "," + planeY);
			out.flush();
			out.println(destX + "," + destY);
			out.flush();
			
			mapPathSol = in.readLine();
			//System.out.println("sol: " + mapPathSol);
			setChanged();
			notifyObservers("done map calculate");
	
			out.close();
			in.close();
			s.close();
			
		} catch (IOException e) {
			System.out.println("Could not connect to the map server!");
			mapServer = null;
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
