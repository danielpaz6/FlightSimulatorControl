package interpreter;

public class VarDouble extends VarTable
{
	private double value;
	
	public VarDouble(double value) {
		super("double");
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public void setValue(Object o) {
		this.value = (double)value;
		
	}
	
}
