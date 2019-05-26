package command;

import java.util.LinkedList;
import java.util.Scanner;

import interpreter.CheckParser;
import interpreter.PeekableScanner;
import interpreter.Server;

public class LoopCommand extends ConditionParser {

	public LoopCommand(Server server) {
		super(server);
	
	}

	@Override
	public int execute() throws Exception{
		
		while(condition.calculate() == 1){
			for (Command cmd : cmds) {
				
				if(server.stop == true) // if the interpreter called stop()
					break;
				
				cmd.execute();
			}
		}
		
		return 0;
	}

	@Override
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
		String par1 = text.next();
		par1 = replaceConditions(par1);
		
		isValidCondition(par1);// throws exception if it's invalid.

		updateCondition(par1);
		
		String par2 = text.next().trim();
		if(!par2.equals("{"))
			throw new Exception("if/while syntax error.");
		
		CheckParser cp = new CheckParser(server);
		cp.execute(text,cmds);
		
		doCommands.add(this);
		
		
		return true;
	}

}
