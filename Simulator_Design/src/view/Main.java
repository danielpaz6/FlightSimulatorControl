package view;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Model;
import model.SimModel;
import server.SimVarsByXML;
import viewmodel.ViewModel;

import command.AssignmentCommand;
import command.BindCommand;
import command.Command;
import command.ConnectCommand;
import command.DefineVarCommand;
import command.DisconnectCommand;
import command.IfCommand;
import command.LoopCommand;
import command.OpenServerCommand;
import command.PrintCommand;
import command.ReturnCommand;
import command.SleepCommand;
import interpreter.CommandFactory;
import interpreter.Server;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
	        
	        //FXMLLoader fxl = new FXMLLoader();
	        
			FXMLLoader fxl = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
			AnchorPane root = (AnchorPane)fxl.load();
			
			
			//System.out.println("test");
			
			Scene scene = new Scene(root,700,400);
			
			// Sets the icon of the application
			Image applicationIcon = new Image(getClass().getResourceAsStream("../airplane.png"));
			primaryStage.getIcons().add(applicationIcon);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Flight Simulator Control");
			primaryStage.setMinWidth(700);
			primaryStage.setMinHeight(450);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// Initializes the Server Variable that includes the Command List and Simulator variables. 
			CommandFactory<Command> exp = new CommandFactory<>();
			/*Server server = new Server(exp, () -> {
				return new String[] {"simX", "simY", "simZ"};
			});*/
			
			Server server = new Server(exp, new SimVarsByXML("generic_small.xml"));
			
			exp.insertCommand("openDataServer", OpenServerCommand.class);
			exp.insertCommand("connect", ConnectCommand.class);
			exp.insertCommand("var", DefineVarCommand.class);
			exp.insertCommand("if", IfCommand.class);
			exp.insertCommand("while", LoopCommand.class);
			exp.insertCommand("sleep", SleepCommand.class);
			exp.insertCommand("print", PrintCommand.class);
			exp.insertCommand("=", AssignmentCommand.class);
			exp.insertCommand("return", ReturnCommand.class);
			exp.insertCommand("disconnect", DisconnectCommand.class);
			exp.insertCommand("bind", BindCommand.class);
			
			Model m = new Model(server);
			
			ViewModel vm = new ViewModel(m); // View Model
			m.addObserver(vm);
			
			MainWindowController mwc= fxl.getController();
			mwc.setViewModel(vm);
			mwc.setHostServices(getHostServices());
			vm.addObserver(mwc);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		launch(args);
		System.exit(0);
	}

}
