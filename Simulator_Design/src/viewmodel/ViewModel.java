package viewmodel;

import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.SimModel;

public class ViewModel extends Observable implements Observer {
	SimModel model;
	
	// Connect Pop-up Window & Map Pop-up Window
	public StringProperty text_ip, text_port;
	public BooleanProperty connect_result, connectMap_result;
	
	// Manual variables
	public DoubleProperty aileron, elevator, throttle, rudder;
	
	// Run a script variables
	public StringProperty scriptText;
	
	// Map String
	public StringProperty mapCoordinateString;
	
	public IntegerProperty planeCordX, planeCordY;
	public IntegerProperty destCordX, destCordY;
	public DoubleProperty simPlaneX,simPlaneY;
	public StringProperty mapPathSol; 
	
	
	
	public ViewModel(SimModel model) {
		this.model = model;
		text_ip = new SimpleStringProperty();
		text_port = new SimpleStringProperty();
		connect_result = new SimpleBooleanProperty();
		
		aileron = new SimpleDoubleProperty();
		elevator = new SimpleDoubleProperty();
		throttle = new SimpleDoubleProperty();
		rudder = new SimpleDoubleProperty();
		
		scriptText = new SimpleStringProperty();
		
		mapCoordinateString = new SimpleStringProperty();
		
		planeCordX = new SimpleIntegerProperty();
		planeCordY = new SimpleIntegerProperty();
		destCordX = new SimpleIntegerProperty();
		destCordY = new SimpleIntegerProperty();
		simPlaneX = new SimpleDoubleProperty();
		simPlaneY = new SimpleDoubleProperty();
		mapPathSol = new SimpleStringProperty();
	}
	
	public void connectToSim()
	{
		try {
			model.connectToServer(text_ip.get(), Double.parseDouble(text_port.get()));
		}
		// Probably because text_port.get() = null and can't be parsed to Double
		catch(Exception e) {
			connect_result.set(true);
		}
	}
	
	public void connectToMapSolver() {
		try {
			model.connectToMapServer(text_ip.get(), Double.parseDouble(text_port.get()), this.mapCoordinateString.get(),this.planeCordX.get(),this.planeCordY.get(),this.destCordX.get(),this.destCordY.get());
		}
		// Probably because text_port.get() = null and can't be parsed to Double
		catch(Exception e) {
			connect_result.set(true);
		}
	}
	
	public void updateAileronAndElevator() {
		model.setAileron(aileron.get());
		model.setElevator(elevator.get());
	}
	
	public void updateThrottle() {
		model.setThrottle(throttle.get());
	}
	
	public void updateRudder() {
		model.setRudder(rudder.get());
	}
	
	public void loadFromFile() {
		model.runScript(scriptText.get());
	}
	
	public void calculateMap() {
		model.calculateMap(this.mapCoordinateString.get(),this.planeCordX.get(),this.planeCordY.get(),this.destCordX.get(),this.destCordY.get());
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o == model) {
			if(arg.equals("connectToServer_success")) {
				simPlaneX.set(model.getPlaneX());
				simPlaneY.set(model.getPlaneY());
				
				connect_result.set(false); // change the visible to False
				setChanged();
				notifyObservers("done_closePopUp");
			}
			else if(arg.equals("connectToMapServer_success")) {
				connect_result.set(false); // change the visible to False
				setChanged();
				notifyObservers("done_closePopUpMap"); // closing the map pop up
			}
			else if(arg.equals("connectToServer_failed")) {
				// will pop up message: Sorry! couldn't connect to the Simulator
				connect_result.set(true); // change the visible to True
			}
			else if(arg.equals("connectToMapServer_failed")) {
				connect_result.set(true); // change the visible to True
			}
			else if(arg.equals("Disconnected_from_client")) {
				// it means the model noticed that the user closed the Flight Simulator or it was already closed
				// in that case, we'll notify the View that he needs setVisible the "you must connect to simulator" AnchorPane.
				setChanged();
				notifyObservers("setVisibleTrue_to_ConnectAnchorPane");
			}
			else if(arg.equals("done map calculate")) {
				mapPathSol.set(model.getPath());
				setChanged();
				notifyObservers("done map calculate");
			}
		}
	}
}
