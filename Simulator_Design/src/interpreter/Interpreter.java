package interpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import command.Command;
import expression.Expression;

public class Interpreter
{
	/*protected HashMap<String, Command> exp;
	protected HashMap<String, VarTable> symbolTbl;
	protected HashMap<String, Expression> cachedExp;
	*/
	
	protected Server server;
	Thread interpreterThread;

	public Interpreter(Server server) {
		super();
		this.server = server;
	}
	
	public int compile(String text)
	{
		int output = 0;
		LinkedList<Command> doCommands = new LinkedList<>();
		
		// ------ STEP 1: Lexer
		Lexer lexer = new Lexer(server);
		PeekableScanner sc = lexer.execute(text);
		
		// ------ STEP 2: Parser
		// A. CheckParser
		CheckParser checkParser = new CheckParser(server);
		LinkedList<Expression> e;
		try {
			e = checkParser.execute(sc, doCommands);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// B. Parser
		
		for (Command command : doCommands)
		{
			if(server.stop == true)
			{
				System.out.println("STOPPED THE INTERPRETER!");
				break;
			}
			
			try {
				command.execute();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		// Closing the server
		
		if(server.serverSide != null)
			server.serverSide.stop();
		
		// Closing the Client if the script didn't close it
		
		/*
		if(server.client != null)
		{
			try {
				server.client.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		*/
		
		/*
		if(e != null)
		{
			System.out.println("e != null syso: " + e);
			return;
		}
		*/
		
		return server.getSymbolTbl().containsKey("return") ? (int)(double)server.getSymbolTbl().get("return").getValue() : 0; 
		
		//return output;
	}
	
	public void start(String text) {
		interpreterThread = new Thread(() -> {
			this.compile(text);
		});
		
		interpreterThread.start();
	}
	
	public void stop() {
		server.stop = true;
	}
}
