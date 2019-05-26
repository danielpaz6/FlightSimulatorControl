package expression;

public class Number implements Expression
{
	private double value;
	
	public Number(double value) {
		this.value = value;
	}

	@Override
	public double calculate() {
		return value;
	}
}
