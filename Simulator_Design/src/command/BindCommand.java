package command;

import java.util.LinkedList;

import interpreter.PeekableScanner;
import interpreter.Server;
import interpreter.VarBind;

public class BindCommand extends CommonCommand {
	String varName;
	String varValue;
	
	public BindCommand(Server server,String varName, String varValue) {
		super(server);
		this.varName = varName;
		this.varValue = varValue;
	}

	@Override
	public int execute() throws Exception {
		
		// we do this just in case the var's type wasn't initialize.
		server.getSymbolTbl().put(varName, new VarBind(varValue));
		return 0;
	}

	@Override
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
	
		// we do this just in case the var's type wasn't initialize.
		server.getSymbolTbl().put(varName, new VarBind(null));
		
		doCommands.add(this);
		
		return true;
	}

}
