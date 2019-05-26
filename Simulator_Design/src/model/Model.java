package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

import algorithms.BestFirstSearch;
import algorithms.MatrixProblem;
import algorithms.Position;
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
	
	public void connectToMapServer(String ip, double port, String mapText) {
		mapServer = new MySerialServer((int)port); 
		
		try {
			
			// Starting the Server
			SearchableClientHandler<String, Position> ch = new SearchableClientHandler<>(
					new SearcherSolver<MatrixProblem, String, Position>(new BestFirstSearch<Position>()),
					new FileCacheManager<MatrixProblem, String>("./maze.xml")
			);
			
			mapServer.start(ch, "end"); // running the server
			
			// Starting the Client
			Socket s=null;
			PrintWriter out=null;
			BufferedReader in=null;
			s = new Socket(ip, (int)port);
			s.setSoTimeout(3000);
			out=new PrintWriter(s.getOutputStream());
			in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			
			
			//out.print(matrix[i][j]+",");
			
			setChanged();
			notifyObservers("connectToMapServer_success");
		} catch (Exception e) {
			
			server.client = null;
			setChanged();
			notifyObservers("connectToMapServer_failed");
		}
	}
	
	@Override
	public void runScript(String text) {
		interpreter = new Interpreter(server);
		interpreter.start(text);
	}

}
