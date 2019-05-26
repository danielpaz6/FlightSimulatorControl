package interpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import command.Command;
import expression.Expression;

public interface ICheckParser {
	public LinkedList<Expression> execute(PeekableScanner text, HashMap<String, Command> exp, HashMap<String, Expression> cachedExp) throws Exception;
}
