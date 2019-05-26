package interpreter;

public abstract class VarTable
{
	protected String type;
	
	public VarTable(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public abstract Object getValue(); 
	public abstract void setValue(Object o);
	
}
