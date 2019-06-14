package model;

import java.util.List;

import algorithms.Position;
import algorithms.State;
import interpreter.Server;

public interface SimModel {
	// Manual Control Methods
	public void setAileron(double val);
	public void setElevator(double val);
	public void setThrottle(double val);
	public void setRudder(double val);
	
	public double getAileron();
	public double getelevator();
	public double getThrottle();
	public double getRudder();
	public double getPlaneX();
	public double getPlaneY();
	// General Methods
	public Server getServer(); // includes Server, Client details
	public void connectToServer(String ip, double port); // Connecting to the Simulator as a client
	
	// Map Methods
	public void connectToMapServer(String ip, double port, double[][] coordinates, int planeX, int planeY, int destX, int destY); // Getting solution from Server
	public void calculateMap(double[][] coordinates, int planeX, int planeY, int destX, int destY);
	public String getPath();	
	// Run a script Methods
	public void runScript(String text);
}
