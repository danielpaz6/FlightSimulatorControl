package command;

import expression.Expression;

public class CommandExpression implements Expression{

	CommonCommand c;
	
	
	
	public CommandExpression(CommonCommand c) {
		super();
		this.c = c;
	}


	@Override
	public double calculate() {
		try {
			c.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
