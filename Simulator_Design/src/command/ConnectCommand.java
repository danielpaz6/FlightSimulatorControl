package command;


import java.util.LinkedList;
import java.util.Scanner;

import interpreter.PeekableScanner;
import interpreter.Server;
import server.Client;


public class ConnectCommand extends CommonCommand {

	String ip;
	String port;
	public ConnectCommand(Server server) {
		super(server);
		
	}

	@Override
	public int execute() throws Exception{
		if(server.client != null){
			throw new Exception("Connect command was already called");
		}
		server.client = new Client();
		
		server.client.start(ip,(int)server.getCachedExp().get(port).calculate());
		
		return 0;
	}

	@Override
	public boolean test(PeekableScanner text,LinkedList<Command> doCommands) throws Exception {
		String par1 = text.next().trim();
		String par2 = text.next().trim();
		
		String[] ipParts = par1.split("\\.");
		
		if(ipParts.length != 4)
			throw new Exception("Invalid IP: " + par1);
		for (String oct : ipParts) {
			
			if(!oct.matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b")) {
				throw new Exception("Invalid IP: " +par1); 
			}
		}
		server.getCachedExp().put(par2,calcExpression(par2));
		this.ip = par1;
		this.port = par2;
		
		doCommands.add(this);
		
		
		return true;
	}

}
