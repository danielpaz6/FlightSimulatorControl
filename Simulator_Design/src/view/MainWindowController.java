package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import viewmodel.ViewModel;

public class MainWindowController implements Initializable, View, Observer {
	
	ViewModel viewModel;
	
	// The Script editor
	@FXML
	private VirtualizedScrollPane scripttext;
	@FXML
	private CodeArea scriptContent;
	
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
	private AnchorPane add_pane, options_pane, dashboard_pane, projects_pane, script_pane, manual_pane;
	
	@FXML
	private AnchorPane connectSim_pane;
	
	// Joystick components
	@FXML
	private AnchorPane joystickAnchor;
	
	@FXML
	private Circle outerjoystick, btn_joystick;
	
	// Run a script page
	
	@FXML
	private JFXTextArea scriptTextArea;
	
	// Data Members
	
	private double radius = 0;
	private double centerX = 0; // mouse location
	private double centerY = 0;
	
	private double initializedCenterX = 0; // real location
	private double initializedCenterY = 0;
	
	public DoubleProperty aileron, elevator;
	
	
	public MainWindowController() {
		aileron = new SimpleDoubleProperty();
		elevator = new SimpleDoubleProperty();
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
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializedCenterX = btn_joystick.getLayoutX();
		initializedCenterY = btn_joystick.getLayoutY();
		
		
		System.out.println(initializedCenterX + "," + initializedCenterY);
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
	}
	
	public void handleMouseEvent(MouseEvent event) {
		if(event.getSource() == btn_status) {
			options_pane.setVisible(false);
		}
	}
	
	private double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
	}
	
	public void dragable(MouseEvent event) {
		if(radius == 0) {
			radius = outerjoystick.getRadius();
			centerX = (btn_joystick.localToScene(btn_joystick.getBoundsInLocal()).getMinX() + btn_joystick.localToScene(btn_joystick.getBoundsInLocal()).getMaxX())/2;
			centerY = (btn_joystick.localToScene(btn_joystick.getBoundsInLocal()).getMinY() + btn_joystick.localToScene(btn_joystick.getBoundsInLocal()).getMaxY())/2;
						
			/*System.out.println("-----------------------------------------");
			System.out.println("Center: (X,Y) = " + centerX + "," + centerY);
			System.out.println("Layout: (X, Y) = " + initializedCenterX + "," + initializedCenterY);
			System.out.println("-----------------------------------------");*/
		}
		
		double x1 = event.getSceneX();
		double y1 = event.getSceneY();
		double x2, y2;
		
		double distance = dist(event.getSceneX(), event.getSceneY(), centerX, centerY);
		if(distance <= radius) {
			btn_joystick.setLayoutX(initializedCenterX + x1 - centerX);
			btn_joystick.setLayoutY(initializedCenterY + y1 - centerY);
			
			x2 = x1;
			y2 = y1;
		}
		else
		{
			if(x1 > centerX) {
				double alfa = Math.atan((y1-centerY)/(x1-centerX));
				double w = radius * Math.cos(alfa);
				double z = radius * Math.sin(alfa);
				
				x2 = centerX + w;
				y2 = centerY + z;
				
				
				btn_joystick.setLayoutX(initializedCenterX + x2 - centerX);
				btn_joystick.setLayoutY(initializedCenterY + y2 - centerY);
			}
			else
			{
				double alfa = Math.atan((centerY - y1) / (centerX - x1));
				double w = radius * Math.cos(alfa);
				double z = radius * Math.sin(alfa);
				
				x2 = centerX - w;
				y2 = centerY - z;
			}
			
			btn_joystick.setLayoutX(initializedCenterX + x2 - centerX);
			btn_joystick.setLayoutY(initializedCenterY + y2 - centerY);
		}
		
		// Setting the Aileron & Elevator values
		aileron.set((x2 - centerX) / radius);
		elevator.set((centerY - y2) / radius);
		
		viewModel.updateAileronAndElevator();
		//System.out.println("(airleron, elevator) = ("+aileron.get()+","+elevator.get()+")");		
	}
	
	public void dragable_exit() {
		btn_joystick.setLayoutX(initializedCenterX);
		btn_joystick.setLayoutY(initializedCenterY);
		
		aileron.set(0);
		elevator.set(0);
		viewModel.updateAileronAndElevator();
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
				String getTextFromFile = s.useDelimiter("\\A").next();
				scriptTextArea.setText(getTextFromFile);
				
				//scripttext.getAccessibleText();
				//System.out.println(getTextFromFile);
				//System.out.println(scriptContent.getText());
				//scripttext.setAccessibleText(getTextFromFile.toString());
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
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
	
	public void throttleDrag() {
		viewModel.updateThrottle();
	}
	
	public void rudderDrag() {
		viewModel.updateRudder();
	}
	
	public void loadFromFile() {
		viewModel.loadFromFile();
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == viewModel)
		{
			if(arg.equals("done_closePopUp"))
				connectSim_pane.setVisible(false);
			else if(arg.equals("setVisibleTrue_to_ConnectAnchorPane"))
				connectSim_pane.setVisible(true);
		}
		
	}

}
