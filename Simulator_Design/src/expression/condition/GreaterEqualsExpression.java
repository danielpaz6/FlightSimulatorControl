package expression.condition;

import expression.BinaryExpression;
import expression.Expression;

public class GreaterEqualsExpression extends BinaryExpression {

	public GreaterEqualsExpression(Expression left, Expression right) {
		super(left, right);
	}

	@Override
	public double calculate() {
		return left.calculate() >= right.calculate() ? 1 : 0;
	}

}
