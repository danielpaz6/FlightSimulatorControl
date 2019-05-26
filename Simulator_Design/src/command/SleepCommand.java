package command;

import java.util.LinkedList;

import expression.ShuntingYard;
import interpreter.PeekableScanner;
import interpreter.Server;

public class SleepCommand extends CommonCommand {
	String timePar;
	
	public SleepCommand(Server server) {
		super(server);
	}

	@Override
	public int execute() throws Exception {
		// No need to catch Sleep Exception.
		try {
			Thread.sleep((long)(server.getCachedExp().get(timePar).calculate()));
		}
		catch(Exception e) {}
		return 0;
	}

	@Override
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
		String par1 = text.next();
		ShuntingYard sy = new ShuntingYard();
		server.getCachedExp().put(par1, sy.calc(par1, server));
		this.timePar = par1;
		
		doCommands.add(this);
		
		return false;
	}

}
