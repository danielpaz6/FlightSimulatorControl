package command;


import java.util.LinkedList;
import interpreter.PeekableScanner;
import interpreter.Server;
import interpreter.VarBind;
import interpreter.VarDouble;

public class AssignmentCommand extends CommonCommand{
	
	private String varName;
	private String varValue;
	
																						
	public AssignmentCommand(Server server,String varName, String varValue) {
		super(server);
		this.varName = varName.trim();
		this.varValue = varValue.trim();
	}

	@Override
	public int execute() throws Exception{
		
			/*
			 * if the var type is bind, and in the right side we have double,
			 * it means that we already allocated the var and now we are in 
			 * assignment situation, and in that case we will call the flight simulator
			 * and sets its data accordingly. */
			
			if(server.getSymbolTbl().get(varName).getType().equals("double")){
				
					server.getSymbolTbl().put(varName, new VarDouble(server.getCachedExp().get(varValue).calculate()));
					
				}
			// the type of the var is bind, and in that case we'll update the server's data
			else {
				String varSimulator = (String)server.getSymbolTbl().get(varName).getValue();
				double varAmount = server.getCachedExp().get(varValue).calculate();
				server.client.sendSimulatorOrder(varSimulator, varAmount); // for example: set /controls/flight/aileron 1
				
				//server.getServerData().put(varSimulator, varAmount);
			}
			
		return 0;
	}

	@Override
	public boolean test(PeekableScanner text,LinkedList<Command> doCommands) throws Exception {
		// Checks that the VariableName is okay
		if(!server.getSymbolTbl().containsKey(varName))
			throw new Exception("Variable " + varName + " is not exists");
		
		// Checks the varValue
		
		if(varValue.contains("\"")) {
	
			/*	if(server.getSymbolTbl().get(varName) != null && server.getSymbolTbl().get(varName).getType().equals("double")) {
				throw new Exception("var " + varName + "is double, and can't be transfered to bind");
			}*/
			server.getSymbolTbl().put(varName, new VarBind(null));
			doCommands.add(server.getExp().getNewCommand("bind", server, varName, varValue));
			
		}
		else if(varValue.equals("bind"))
		{
			// we do this just in case the var's type wasn't initialize.
			server.getSymbolTbl().put(varName, new VarBind(null));
			
			varValue = text.next().trim().replaceAll("\"", "");
			doCommands.add(server.getExp().getNewCommand("bind", server, varName, varValue));
		}
		else if(varValue.matches("^[a-zA-Z0-9\\s-+*/()]+$"))
		{
			// we do this just in case the var's type wasn't initialize.
			server.getSymbolTbl().put(varName, new VarDouble(0.0));
			//server.getSymbolTbl().put(varName, new VarDouble(0.0));
			if(varValue.equals("return")) 
				throw new Exception("return word cannot be used as variable name.");
			
			server.getCachedExp().put(varValue, calcExpression(varValue));
			doCommands.add(server.getExp().getNewCommand("=", server, varName, varValue));
		}
		else
			throw new Exception("Syntax error at: " + varName + " = " + varValue);
		
		
		return true;
	}

}
