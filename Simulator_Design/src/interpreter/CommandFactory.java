package interpreter;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory<Command> {
	private interface Creator<Command> {
		public Command create(Object ... args);
	}
	
	Map<String, Creator<Command>> map;
	
	public CommandFactory() {
		this.map = new HashMap<>();
	}
	
	public void insertCommand(String key, Class<? extends Command> c) {
		map.put(key, new Creator<Command>() {
			@Override
			public Command create(Object ... args) {
				try {
					Class<?>[] classes = new Class[args.length];
					for (int i = 0; i < args.length; i++)
					{
						if(args[i].getClass().isPrimitive())
						{
							classes[i] = args[i].getClass();
						}
						else
						{
							try {
								classes[i] = (Class<?>)Class.forName(args[i].getClass().getName()).getField("TYPE").get(null);
							} catch(Exception e) {
								classes[i] = args[i].getClass();
							}
						}
					}
					
					return c.getConstructor(classes).newInstance(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return null;
			}
		});
	}
	
	public Command getNewCommand(String key, Object ... args) {
		return map.get(key).create(args);
	}
}
