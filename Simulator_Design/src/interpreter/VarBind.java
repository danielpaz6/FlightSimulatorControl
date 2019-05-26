package interpreter;

public class VarBind extends VarTable
{
	private String value;
	
	public VarBind(String value) {
		super("bind");
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		this.value = (String)o;
	}
}