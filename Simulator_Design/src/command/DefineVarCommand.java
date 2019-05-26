package command;

import java.util.LinkedList;
import java.util.Scanner;


import interpreter.PeekableScanner;
import interpreter.Server;
import interpreter.VarBind;
import interpreter.VarDouble;


public class DefineVarCommand extends CommonCommand {

	public DefineVarCommand(Server server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute() {
		// no need  to execute nothing, the AssignmentCommand does this work.
		return 0;
	}

	@Override
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
		String par1,par3,par4, varValue;
		Scanner textBeforeEquals;
		
		par1 = text.next().trim();
		//par2 = text.peek().trim();
		//par3 = text.next().trim();
		
		if(!par1.matches("^[a-zA-Z0-9_]+$") || par1.matches("^[0-9].*"))
		{
			throw new Exception("Variable must start with a letter and can contain only : a-zA-Z0-9 and _");	
		}
		if(server.getSymbolTbl().containsKey(par1))
			throw new Exception("The variable " + par1 +" is already exists. ");
		
		if(text.peek().trim().charAt(0) != '='){
			
			server.getSymbolTbl().put(par1, new VarDouble(0.0));
			return true;
		}
		
		text.next().trim(); // equals to '='
		par3 = text.next().trim(); //holds bind or an expression.
		// The DefineVarCommand only puts the variable in the HashMap, and the 
		// AssigmentCommand will update the var's value.
		
		if(par3.equals("bind")) {
			par4 = text.next().trim();
			varValue = par4.replaceAll("\"", "");
			//server.getSymbolTbl().put(par1, new VarBind(null));
			server.getExp().getNewCommand("bind",server,par1,varValue).test(text, doCommands);
		}
		else {
				server.getSymbolTbl().put(par1, new VarDouble(0.0));
				varValue = par3;
				server.getExp().getNewCommand("=",server,par1,varValue).test(text, doCommands);
			}
		

		
			
	/*	if(par3.matches("^bind \"[^\"]*\"$"))
			server.getSymbolTbl().put(par1, new VarBind(null));
		else 
			server.getSymbolTbl().put(par1, new VarDouble(0.0));
		//The command will also updates the symbolTbl*/
		
		return true;
	}

}
