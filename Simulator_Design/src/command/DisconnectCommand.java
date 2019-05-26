package command;

import java.util.LinkedList;

import interpreter.PeekableScanner;
import interpreter.Server;

public class DisconnectCommand extends CommonCommand {

	public DisconnectCommand(Server server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute() throws Exception {
		
		if(server.client == null) 
			throw new Exception("cannot disconnect from the client since he is already closed");
		
		server.client.sendSimulatorText("bye");
		
	/*	try {
		server.client.close();
		server.client = null;
		} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}*/
		return 0;
	}

	@Override
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
		doCommands.add(this);
		return true;
	}

}
