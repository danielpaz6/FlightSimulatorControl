package command;

import java.util.HashMap;

import expression.Expression;
import expression.ShuntingYard;
import interpreter.Interpreter;
import interpreter.Server;
import interpreter.VarTable;

public abstract class CommonCommand implements Command {

//	protected HashMap<String, VarTable> symbolTbl;
	protected Server server;
	
	
	
	public CommonCommand(Server server) {
		this.server = server;
	}



	public Expression calcExpression(String exp) throws Exception
	{
		ShuntingYard calcExp = new ShuntingYard();
		Expression solvedExp = calcExp.calc(exp,server);
		
		// At this point, it means caclExp.cacl didn't throw Exception
		// Therefore, we can safely put the solution in a Cached HashMap
		// So we could use it later to the Parser Part.
		
		//cachedExp.put(par,exp);
		
		return solvedExp;
	}
	

}
