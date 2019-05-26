package command;

import java.util.LinkedList;

import interpreter.PeekableScanner;
import interpreter.Server;
import interpreter.VarDouble;

public class ReturnCommand extends CommonCommand{

	String exp;
	public ReturnCommand(Server server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute() throws Exception {
		Thread.sleep(100);
		server.getSymbolTbl().put("return", new VarDouble(server.getCachedExp().get(exp).calculate()));
		return 0;
	}

	@Override
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
		String par1 = text.next().trim();
		server.getCachedExp().put(par1, calcExpression(par1));
		
		this.exp = par1;
		doCommands.add(this);
		return true;
	}

}
