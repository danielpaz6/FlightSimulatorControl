package interpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import command.AssignmentCommand;
import command.Command;
import command.IfCommand;
import expression.Expression;

public class CheckParser
{
	Server server;
	public CheckParser(Server server) {
		//super(exp, symbolTbl, cachedExp);
		this.server = server;
	}

	public LinkedList<Expression> execute(PeekableScanner text, LinkedList<Command> doCommands) throws Exception
	{
		Command cmd;
		
		while(text.hasNext())
		{
			String currentCmd = text.next().trim();
			
			if(currentCmd.isEmpty())
				continue;
			
			// if we see "}", it means we need to exit the current Parser so the outerParser will continue;
			// ( this is a recursive method )
			if(currentCmd.trim().equals("}"))
				break;
			
			/*if(!currentCmd.trim().equals("if"))
				cmd = server.getExp().getNewCommand(currentCmd);
			else
				cmd = new IfCommand(server);*/
			
			if(text.peek().trim().charAt(0) != '=')
			{
				try {
					cmd = server.getExp().getNewCommand(currentCmd, server);
				} catch(Exception e) { // probably Null Exception:
					throw new Exception("Cannot find the command: " + currentCmd);
				}
				
				cmd.test(text, doCommands);
			}
			else
			{
				text.next(); // skipping the "=" character
				String cmd3 = text.next().trim();
				
				if(cmd3.equals("bind"))
				{
					cmd3 = text.next().trim(); // skipping the bind and moving to the value
					server.getExp().getNewCommand("bind", server, currentCmd, cmd3).test(text, doCommands);
				}
				else
					server.getExp().getNewCommand("=", server, currentCmd, cmd3).test(text, doCommands);
				
			}
			
		}
		
		return null;
	}
}
