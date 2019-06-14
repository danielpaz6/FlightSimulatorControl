package command;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import expression.Expression;
import interpreter.PeekableScanner;
import interpreter.Server;

public interface Command {

	public int execute() throws Exception;
	public boolean test(PeekableScanner text,LinkedList<Command>doCommands) throws Exception;
}
