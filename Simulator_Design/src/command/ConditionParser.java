package command;

import java.util.LinkedList;
import java.util.List;

import expression.Expression;
import expression.ShuntingYard;
import expression.condition.EqualsExpression;
import expression.condition.GreaterEqualsExpression;
import expression.condition.GreaterExpression;
import expression.condition.LessEqualsExpression;
import expression.condition.LessExpression;
import expression.condition.NotEqualsExpression;
import interpreter.Server;

public abstract class ConditionParser extends CommonCommand {
	protected LinkedList<Command> cmds;
	protected Expression condition;
	
	public ConditionParser(Server server) {
		super(server);
		cmds = new LinkedList<>();
	}
	
	/*public ConditionParser(List<Command> cmds, String condition) {
		this.cmds = cmds;
		this.condition = condition;
	}*/
	
	public boolean isValidCondition(String condition) throws Exception {
		int count = 0;
		
		if(condition.matches(".*<[^=].*"))
			count++;
		
		if(condition.contains("<="))
			count++;
		
		if(condition.matches(".*>[^=].*"))
			count++;
		
		if(condition.contains(">="))
			count++;
		
		if(condition.contains("=="))
			count++;

		if(condition.contains("!="))
			count++;
		
		if(count != 1)
			throw new Exception("Your if/while must contain only 1 condition");
		
		return true;
	}
	
	public String replaceConditions(String text) {
		text = text.replaceAll("\\+is\\+", "==");
		text = text.replaceAll("\\+not\\+", "!=");
		text = text.replaceAll("\\+lessEqual\\+", "<=");
		text = text.replaceAll("\\+bigEqual\\+", ">=");
		text = text.replaceAll("\\+big\\+", "<");
		text = text.replaceAll("\\+small\\+", ">");
		
		return text;
	}
	
	public void updateCondition(String par1) throws Exception {
		String[] exps;
		ShuntingYard sy = new ShuntingYard();
		
		if(par1.contains("==")) {
			exps = par1.split("==");
			this.condition = 
					new EqualsExpression(
							sy.calc(exps[0], server),
							sy.calc(exps[1], server)
			);
		}
		else if(par1.matches(".*<[^=].*")) {
			exps = par1.split("<");
			this.condition =
					new LessExpression(
							sy.calc(exps[0], server),
							sy.calc(exps[1], server)
			);
		}
		else if(par1.contains("<=")) {
			exps = par1.split("<=");
			this.condition =
					new LessEqualsExpression(
							sy.calc(exps[0], server),
							sy.calc(exps[1], server)
			);
		}
		else if(par1.matches(".*>[^=].*")) {
			exps = par1.split(">");
			this.condition =
					new GreaterExpression(
							sy.calc(exps[0], server),
							sy.calc(exps[1], server)
			);
		}
		else if(par1.contains(">=")) {
			exps = par1.split(">=");
			this.condition = 
					new GreaterEqualsExpression(
							sy.calc(exps[0], server),
							sy.calc(exps[1], server)
			);
		}
		else if(par1.contains("!=")) {
			exps = par1.split("!=");
			this.condition = 
					new NotEqualsExpression(
							sy.calc(exps[0], server),
							sy.calc(exps[1], server)
			);
		}
	}
	
	public boolean isTrue()
	{
		
		return true; // TODO
	}
}
