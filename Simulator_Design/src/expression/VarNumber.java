package expression;

import java.util.HashMap;

import interpreter.Server;
import interpreter.VarTable;

public class VarNumber implements Expression
{
	Server server;
	String name;
	
	public VarNumber(String name, Server server) {
		this.server = server;
		this.name = name;
	}

	@Override
	public double calculate() {
		// No need to check if .get(name) exists, since we are checking it in the
		// Shunting-Yard class.
		
		VarTable t = server.getSymbolTbl().get(name);
		
		// if the type of the var is double:
		if(t.getType().equals("double"))
		{
			//return (double)server.getSymbolTbl().get(name).getValue();
			return (double)t.getValue();
		}
		else // Binded variable
		{
			//System.out.println("get " + (String)symbolTbl.get(name).getValue());
			
			return server.getServerData().get(t.getValue());
			
			//server.client.sendSimulatorOrder(var, value);
			//return 0;
			
		}
	}

}
