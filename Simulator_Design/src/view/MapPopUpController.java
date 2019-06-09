package view;

import java.util.Observable;
import java.util.Observer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import viewmodel.ViewModel;

public class MapPopUpController implements View, Observer {
	
	ViewModel viewModel;
	
	@FXML
	private TextField text_ip, text_port;
	
	@FXML
	private Label warning_connectToServer;
	
	@Override
	public void setViewModel(ViewModel viewModel) {
		this.viewModel = viewModel;
		
		viewModel.text_ip.bind(this.text_ip.textProperty());
		viewModel.text_port.bind(this.text_port.textProperty());
		
		//warning_connectToServer.wrapTextProperty().bind(viewModel.connect_result);
		warning_connectToServer.visibleProperty().bind(viewModel.connect_result);
	}
	
	public void connectToMapSolver() {
		viewModel.connectToMapSolver();
	}

	@Override
	public void update(Observable o, Object arg)
	{
		if(o == viewModel) {
			if(arg.equals("done_closePopUpMap")) {
			    Stage stage = (Stage) text_ip.getScene().getWindow();
			    stage.close();
			}
		}		
	}
}
