package expression;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import interpreter.Server;
import interpreter.VarTable;

public class ShuntingYard
{
	public Expression calc(String exp, Server server) throws Exception
	{	
		// The beginning of the algorithm
		ArrayDeque<String> queue = new ArrayDeque<>();
		Stack<String> stack = new Stack<>();
		Stack<Expression> stackExp = new Stack<>();
		
		/*
		 * The algorithm doesn't know to handle expression such as "-x+3" or "-5 + 2",
		 * hence, we'll add a neutral zero before so we'll get: "0-x+3" or "0-5+2" and that kind of expression the algorithm solves.
		 */
		if(exp.startsWith("-"))
			exp = "0" + exp;
		
		String[] tokens = exp.split("(?<=[-+*/()])|(?=[-+*/()])");
		String lastToken = "";
		
		for(String token : tokens)
		{
			token = token.trim();
			if(token.equals("") || token == null)
				continue;
			
			if(isDouble(token))
			{
				queue.add(token);
				lastToken = token;
				continue;
			}
			
			// Checks if the token is variable
			if(token.matches("^[a-zA-Z0-9_]+$"))
			{
				if(server.getSymbolTbl().containsKey(token))
					queue.add(token);
				else
					throw new Exception("Cannot find the variable: " + token);		
			}
			
			switch(token)
			{
				case "/":
				case "*":
				case "(":
					stack.push(token);
					break;
				
				case "+":
				case "-":
					// Checking if the "-" is a negative sign of a number,
					// or whether it's a Minus Binary Operator.
					if(token.equals("-") && lastToken.matches("^[[\\/\\*\\+\\-\\(]]$"))
					{
						//System.out.println("--->" + lastToken);
						stack.push("~");
						break;
					}
					
					while (!stack.empty() && (!stack.peek().equals("("))){
						queue.add(stack.pop());
					}
					
					stack.push(token);
					break;
				
				case ")":
					while (!stack.peek().equals("(")){
						queue.add(stack.pop());
					}
					stack.pop();
					break;
			}
			
			lastToken = token;
		}
		
		while(!stack.isEmpty()) {
			queue.add(stack.pop());
		}
		
		//queue.forEach(System.out::println);
		
		for(String str : queue)
		{
			if(isDouble(str))
				stackExp.push(new Number(Double.parseDouble(str)));
			else if(str.matches("^[a-zA-Z0-9_]+$"))
				stackExp.push(new VarNumber(str, server));
			else
			{
				if(stackExp.isEmpty() == true)
					throw new Exception("Invalid Expression: " + exp);
				
				Expression right = stackExp.pop();
				Expression left = null;
				
				// Since ~ ( Negative Sign ) is unary expression, no need to open two Expression, but only one.
				if(str.charAt(0) != '~')
				{
					if(stackExp.isEmpty() == true)
						throw new Exception("Invalid Expression: " + exp);
					
					left = stackExp.pop();
				}
				
				switch(str)
				{
				    case "/":
				    	stackExp.push(new Div(left, right));
				    	break;
				    case "*":
				    	stackExp.push(new Mul(left, right));
				    	break;
				    case "+":
				    	stackExp.push(new Plus(left, right));
				    	break;
				    case "-":
				    	stackExp.push(new Minus(left, right));
				    	break;
				    case "~":
				    	stackExp.push(new Mul(new Number(-1), right)); // unary operator
				    	break;
				}
			}
		}
		
		//return Math.floor((stackExp.pop().calculate() * 1000)) /1000;
		return stackExp.pop();
	}
	
	private boolean isDouble(String val) {
		try {
			Double.parseDouble(val);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
