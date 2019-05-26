package expression;

import java.util.ArrayList;

public abstract class MultiExpression implements Expression
{
	protected ArrayList<Expression> exps;

	public MultiExpression(ArrayList<Expression> exps) {
		this.exps = exps;
	}
}
