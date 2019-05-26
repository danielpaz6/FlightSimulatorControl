package expression;

public abstract class BinaryExpression implements Expression {

	protected Expression left,right;
	public BinaryExpression(Expression left,Expression right) {
		this.left = left;
		this.right = right;
	}

}
