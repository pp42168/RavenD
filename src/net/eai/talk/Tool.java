package net.eai.talk;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;  

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Tool extends Word {

	private Object classObject;
	private String toolName;
	
	public Tool(Object obj)
	{
		classObject = obj;
		toolName = obj.getClass().getSimpleName();
	}
	
	public Object getClassObject()
	{
		return classObject;
	}
	public List<Usage> getUsages()
	{
		ArrayList<Usage> useages = new ArrayList<Usage>();
		Class<?> toolClass = classObject.getClass();
		Method[] ms = toolClass.getDeclaredMethods();
		for(Method m:ms)
		{
			int modifier = m.getModifiers();
			if(Modifier.isPublic(modifier))
			{
				Usage usage = new Usage(this,m);
				useages.add(usage);
			}
		
		}
		return useages;
	}
	
	
	@Override
	public String name() {
		return toolName;
	}


	public String getToolName() {
		return toolName;
	}


	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	
	
}
