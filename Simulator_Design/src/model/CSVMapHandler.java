package model;

import java.io.InputStream;
import java.io.OutputStream;

import algorithms.MatrixProblem;
import server_side.ClientHandler;

public class CSVMapHandler<Solution, StateType> implements ClientHandler<MatrixProblem, Solution> {

	@Override
	public void handleClient(InputStream in, OutputStream out, String exitStr) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
