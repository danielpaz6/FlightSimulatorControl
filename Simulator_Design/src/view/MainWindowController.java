package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;

import javafx.application.HostServices;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import viewmodel.ViewModel;

public class MainWindowController implements Initializable, View, Observer {
	
	ViewModel viewModel;
	private HostServices hostServices;
	
	// The Script editor
	
	// + ( More ) Buttons
	@FXML
	private JFXButton btn_add, btn_back, btn_status, btn_script, btn_manual;
	
	// Navigator Buttons
	@FXML
	private JFXButton btn_dashboard, btn_projects, btn_map;
	
	@FXML
	private JFXSlider rudderSlider, throttleSlider;
	
	// Pages
	@FXML
	private AnchorPane add_pane, options_pane, dashboard_pane, projects_pane, script_pane, manual_pane,map_pane;
	
	@FXML
	private AnchorPane connectSim_pane,connectSim_pane2;
	
	// Joystick components
	@FXML
	private AnchorPane joystickAnchor;
	
	@FXML
	private Circle outerjoystick, btn_joystick;
	
	// Run a script page
	
	@FXML
	private JFXTextArea scriptTextArea;
	
	// Map
	@FXML
	private MapDisplayer mapDisplayer;
	
	@FXML
	private Joystick joystick;
	
	// Etc
	@FXML
	private Label todaysDate;
	
	public DoubleProperty aileron, elevator;
	public StringProperty getTextFromFile;
	
	// Map Data Members
	
	@FXML
	JFXButton btn_connectSim211;
	
	private double maxMapPlane;
	IntegerProperty planeCordX, planeCordY;
	IntegerProperty destCordX, destCordY;
	
	DoubleProperty simPlaneX, simPlaneY;
	
	public StringProperty mapPathSol;
	
	// Project data members
	
	@FXML
	ListProject projectList;
	
	// Status data members
	
	@FXML
	Label server_online, server_offline, client_online, client_offline, map_online, map_offline;
	
	
	double pressX, pressY;
	// File
	
	StringProperty scriptFileName;
	
	public MainWindowController() {
		aileron = new SimpleDoubleProperty();
		elevator = new SimpleDoubleProperty();
		getTextFromFile = new SimpleStringProperty();
		planeCordX = new SimpleIntegerProperty();
		planeCordY = new SimpleIntegerProperty();
		destCordX = new SimpleIntegerProperty();
		destCordY = new SimpleIntegerProperty();
		mapPathSol = new SimpleStringProperty();
		scriptFileName = new SimpleStringProperty();
		simPlaneX = new SimpleDoubleProperty();
		simPlaneY = new SimpleDoubleProperty();
	}
	
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices ;
    }
    
    public HostServices getHostServices() {
        return hostServices;
    }
	
	@Override
	public void setViewModel(ViewModel viewModel) {
		this.viewModel = viewModel;
		
		// Only after having a View Model, we can bind variables to the View-Model's variables.
		viewModel.aileron.bind(this.aileron); // using this. to emphasis that when the view changes, the view model will also change.
		viewModel.elevator.bind(this.elevator);
		
		viewModel.throttle.bind(throttleSlider.valueProperty());
		viewModel.rudder.bind(rudderSlider.valueProperty());
		
		viewModel.scriptText.bind(scriptTextArea.textProperty());
		
		viewModel.mapCoordinateString.bind(getTextFromFile);
		
		viewModel.planeCordX.bind(this.planeCordX);
		viewModel.planeCordY.bind(this.planeCordY);
		viewModel.destCordX.bind(this.destCordX);
		viewModel.destCordY.bind(this.destCordY);
		
		this.mapPathSol.bind(viewModel.mapPathSol);
		this.simPlaneX.bind(viewModel.simPlaneX);
		this.simPlaneY.bind(viewModel.simPlaneY);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.scriptFileName.bind(projectList.scriptFileName);
		
		projectList.setXMLDirectory("./resources/projects.xml");
		projectList.drawProjects();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		todaysDate.setText("Last time online: " + dateFormat.format(date));
		
		// Joy-stick setup
		joystick.setOnMouseDragged((e) -> {
			joystick.moveJoystick(e.getSceneX(), e.getSceneY());
			aileron.set(joystick.aileronVal);
			elevator.set(joystick.elevatorVal);
			
			viewModel.updateAileronAndElevator();
		});
		
		joystick.setOnMousePressed((e) -> {
			joystick.moveJoystick(e.getSceneX(), e.getSceneY());
			aileron.set(joystick.aileronVal);
			elevator.set(joystick.elevatorVal);
			
			viewModel.updateAileronAndElevator();
		});
		
		joystick.setOnMouseReleased((e) -> {
			joystick.resetJoystick();
			aileron.set(joystick.aileronVal);
			elevator.set(joystick.elevatorVal);
			
			viewModel.updateAileronAndElevator();
		});
	}
	
	public void handleButtonAction(ActionEvent event)
	{
		if(event.getSource() == btn_add)
		{
			add_pane.setVisible(true);
			add_pane.toFront();
		}
		else if(event.getSource() == btn_back)
		{
			add_pane.setVisible(false);
		}
		else if(event.getSource() == btn_status)
		{
			if(options_pane.isVisible())
				options_pane.setVisible(false);
			else
				options_pane.setVisible(true);
		}
		else if(event.getSource() == btn_dashboard) {
			dashboard_pane.toFront();
		}
		else if(event.getSource() == btn_projects) {
			projects_pane.toFront();
		}
		else if(event.getSource() == btn_script) {
			script_pane.toFront();
		}
		else if(event.getSource() == btn_manual) {
			manual_pane.toFront();
		}
		else if(event.getSource() == btn_map) {
			map_pane.toFront();
		}
	}
	
	public void openGitHub() {
		getHostServices().showDocument("https://github.com/danielpaz6/");
		getHostServices().showDocument("https://github.com/OmerNahum/");
	}
	
	public void learnMore() {
		getHostServices().showDocument("https://github.com/danielpaz6/FlightSimulatorControl");
	}
	
	public void handleMouseEvent(MouseEvent event) {
		if(event.getSource() == btn_status) {
			options_pane.setVisible(false);
		}
	}
	
	public void openFile() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose a flight simulator script");
		fc.setInitialDirectory(new File("./"));
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fc.getExtensionFilters().add(extFilter);
		
		/*System.out.println("your text is: ");
		//System.out.println(scriptContent.getText());
		if(scriptContent == null) {
			System.out.println("NULL!!!");
		}*/
		//System.out.println(scripttext.getAccessibleText());
		//System.out.println(scripttext.accessibleTextProperty().toString());
		
		File chosen = fc.showOpenDialog(null);
		if(chosen != null) {
			try {
				Scanner s = new Scanner(chosen);
				String getTextFromFile2 = s.useDelimiter("\\A").next();
				scriptTextArea.setText(getTextFromFile2);
				
				//scripttext.getAccessibleText();
				//System.out.println(getTextFromFile);
				//System.out.println(scriptContent.getText());
				//scripttext.setAccessibleText(getTextFromFile.toString());
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadMap() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose a CSV map file.");
		fc.setInitialDirectory(new File("./"));
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fc.getExtensionFilters().add(extFilter);

		File chosen = fc.showOpenDialog(null);
		if(chosen != null) {
			try {
				Scanner s = new Scanner(chosen);
				getTextFromFile.set(s.useDelimiter("\\A").next().trim());
				String[] rows = getTextFromFile.get().split("\n");
				
				String[] xy = rows[0].split(",");
				
				
				int corX = Integer.parseInt(xy[0].replace("\"", "").trim());
				int corY = Integer.parseInt(xy[1].replace("\"", "").trim());
				
				double distance = Double.parseDouble(rows[1].replace("\"", "").trim());
				
				int numOfRows = rows.length - 2;
				int numOfColums = (rows[2].split(",")).length;
				
				double[][] coords = new double[numOfRows][numOfColums];
				double max = 0;
				
				for(int i = 2; i < numOfRows + 2; i++) {
					String[] colums = rows[i].split(",");
					for(int j = 0; j < numOfColums; j++) {
						coords[i - 2][j] = Double.parseDouble(colums[j]);
						
						if(coords[i - 2][j] > max)
							max = coords[i - 2][j];
					}
				}
				
				mapDisplayer.isMapLoaded = true;
				maxMapPlane = max;
				planeCordX.set(corX);
				planeCordY.set(corY);
				
				mapDisplayer.setMapData(coords, max, corX, corY, distance);
				mapDisplayer.redraw(max);
				mapDisplayer.movePlane(corX, corY);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void putMarkOnMap(MouseEvent e) {
		
		double posX = e.getX();
		double posY = e.getY();
		
		mapDisplayer.redraw(maxMapPlane);
		mapDisplayer.movePlaneByPosition(mapDisplayer.planeX, mapDisplayer.planeY);
		mapDisplayer.markDestByMouse(posX, posY);
		
		// Data binding the MarkOn sign to the View Model.
		destCordX.set(mapDisplayer.destY);
		destCordY.set(mapDisplayer.destX);
		
		// Once picked new destination, we'll call re-calculate the path to the dest.
		viewModel.calculateMap(mapDisplayer.coordinates);
	}
	
	public void openConnectPopUp() throws IOException {
		//FXMLLoader fxl = new FXMLLoader();
		
		FXMLLoader fxl = new FXMLLoader(getClass().getResource("ConnectPopUpWindow.fxml"));
		AnchorPane root = (AnchorPane)fxl.load();
		
		Scene scene = new Scene(root,250,260);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.show();
		
		ConnectPopUpController mwc= fxl.getController();
		mwc.setViewModel(viewModel);
		viewModel.addObserver(mwc);
	}
	
	public void openConnectMapPopUp() throws IOException {
		//FXMLLoader fxl = new FXMLLoader();
		
		FXMLLoader fxl = new FXMLLoader(getClass().getResource("MapPopUpWindow.fxml"));
		AnchorPane root = (AnchorPane)fxl.load();
		
		Scene scene = new Scene(root,250,260);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.show();
		
		MapPopUpController mwc= fxl.getController();
		mwc.setViewModel(viewModel);
		viewModel.addObserver(mwc);
	}
	
	public void throttleDrag() {
		viewModel.updateThrottle();
	}
	
	public void rudderDrag() {
		viewModel.updateRudder();
	}
	
	public void loadFromFile() {
		viewModel.loadFromFile();
	}
	
	public void projectClicked() {
		if(scriptFileName.get() != null && scriptFileName.get().contains(".txt"))
		{
			try {
				Scanner scanner = new Scanner(new File(scriptFileName.get()));
				String text = scanner.useDelimiter("\\A").next();
				scanner.close();
				scriptTextArea.setText(text);
				script_pane.toFront();
				
				projectList.scriptFileName.set("");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == viewModel)
		{ 
			if(arg.equals("done_closePopUp")) {
				client_online.setVisible(true);
				client_offline.setVisible(false);
				server_online.setVisible(true);
				server_offline.setVisible(false);
				
				connectSim_pane.setVisible(false);
				connectSim_pane2.setVisible(false);
				
				//Change the plane on the mapDisplayer.
				if(mapDisplayer.isMapLoaded == true)
				{
					//mapDisplayer.setPlaneOnMap(simPlaneX.get(), simPlaneY.get());
					mapDisplayer.updateIsPlaneMoved(simPlaneX.get(), simPlaneY.get());
					
					planeCordX.set(mapDisplayer.planeY);
					planeCordY.set(mapDisplayer.planeX);
									
					// Once plane moved, we'll call re-calculate the path to the dest.
					if(mapDisplayer.isPlaneMoved == true)
					{
						viewModel.calculateMap(mapDisplayer.coordinates);
						mapDisplayer.isPlaneMoved = false;
					}
				}
			}
			else if(arg.equals("setVisibleTrue_to_ConnectAnchorPane")) {
				connectSim_pane.setVisible(true);
				connectSim_pane2.setVisible(true);
			}
			else if(arg.equals("done map calculate")) {				
				//System.out.println("done map calculate msg:");
				String tmpPath = mapPathSol.get();
				//System.out.println(tmpPath);
				
				if(mapDisplayer.isMapLoaded == true)
					mapDisplayer.setPlaneOnMap(simPlaneX.get(), simPlaneY.get());
				
				//System.out.println("about to print the setPath():");
				mapDisplayer.setPath(tmpPath);
				mapDisplayer.drawPath(tmpPath);
			}
			else if(arg.equals("done_closePopUpMap")) {
				map_online.setVisible(true);
				map_offline.setVisible(false);
				btn_connectSim211.setVisible(false);
			}
			else if(arg.equals("doneMap_first_init")) {
				viewModel.calculateMap(mapDisplayer.coordinates);
			}
		}
		
	}

}
