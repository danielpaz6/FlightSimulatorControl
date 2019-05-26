package interpreter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import command.Command;
import expression.Expression;
import server.Client;
import server.ServerSide;
import server.SimulatorVariables;

public class Server {
	protected CommandFactory<Command> exp;
	protected ConcurrentHashMap<String, VarTable> symbolTbl;
	protected ConcurrentHashMap<String, Expression> cachedExp;
	protected ConcurrentHashMap<String, Double> serverData;
	public Client client;
	public ServerSide serverSide;
	private SimulatorVariables simVars;
	public volatile boolean stop;
	
	public Server(CommandFactory<Command> exp, ConcurrentHashMap<String, VarTable> symbolTbl,
			ConcurrentHashMap<String, Expression> cachedExp, SimulatorVariables simVars) {
		super();
		this.exp = exp;
		this.symbolTbl = symbolTbl;
		this.cachedExp = cachedExp;
		this.serverData = new ConcurrentHashMap<>();
		this.client = null;
		this.serverSide = null;
		this.simVars = simVars;
		stop = false;
	}
	
	public Server(CommandFactory<Command> exp, SimulatorVariables simVars) {
		super();
		this.exp = exp;
		this.symbolTbl = new ConcurrentHashMap<>();
		this.cachedExp = new ConcurrentHashMap<>();
		this.serverData = new ConcurrentHashMap<>();
		this.client = null;
		this.serverSide = null;
		this.simVars = simVars;
		stop = false;
		
		// Initializes the serverData HashMap that contains the flight's data.
		String[] tmpDataList = getSimVars().getVariables();
		for (String tmpData : tmpDataList) {
			getServerData().put(tmpData, 0.0);
		}
	}
	
	public CommandFactory<Command> getExp() {
		return exp;
	}
	public void setExp(CommandFactory<Command> exp) {
		this.exp = exp;
	}
	public ConcurrentHashMap<String, VarTable> getSymbolTbl() {
		return symbolTbl;
	}
	public ConcurrentHashMap<String, Expression> getCachedExp() {
		return cachedExp;
	}
	public void setCachedExp(ConcurrentHashMap<String, Expression> cachedExp) {
		this.cachedExp = cachedExp;
	}
	public ConcurrentHashMap<String, Double> getServerData() {
		return serverData;
	}
	public SimulatorVariables getSimVars() {
		return simVars;
	}
	public void setSimVars(SimulatorVariables simVars) {
		this.simVars = simVars;
	}
	
	
}
