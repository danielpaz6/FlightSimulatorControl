package interpreter;

public class Lexer
{
	Server server;
	public Lexer(Server server) {
		this.server = server;
	}

	public PeekableScanner execute(String text)
	{	
		text = text.replaceAll("==", "+is+");
		text = text.replaceAll("!=", "+not+");
		text = text.replaceAll("<=", "+lessEqual+");
		text = text.replaceAll(">=", "+bigEqual+");
		text = text.replaceAll("<[^=]", "+big+");
		text = text.replaceAll(">[^=]", "+small+");
		
		PeekableScanner lexer = new PeekableScanner(text);
		//lexer.useDelimiter("((?<=[a-zA-Z0-9={}])\\s(?=[a-zA-Z0-9={}(]))|[\\n\\r]+");
		//lexer.useDelimiter("((?<=[a-zA-Z0-9={}])\\s(?=[a-zA-Z0-9={}(]))|[\\n\\r]+|((?<=[{}])|(?=[{}]))");
		//lexer.useDelimiter("((?<=[a-zA-Z0-9={}])\\s(?=[a-zA-Z0-9={}(]))|[\\n\\r]+|((?<=[{}])|(?=[{}]))|((?<=[=])|(?=[=]))");
		lexer.useDelimiter("((?<=[a-zA-Z0-9={}\"])\\s+(?=[a-zA-Z0-9={}\"(])|[\\n\\r]+|(?<=[{}])|(?=[{}])|(?<=[=])|(?=[=]))(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		
		/*
		 * Should be changed to:
		 * String[] s3 = text.split("((?<=[a-zA-Z0-9={}\"])\\s+(?=[a-zA-Z0-9={}\"(])|[\\n\\r]+|(?<=[{}])|(?=[{}])|(?<=[=])|(?=[=]))(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		 */
		
		
		return lexer;
	}
	
}
