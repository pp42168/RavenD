package net.eai.talk.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import net.eai.talk.*;

public class ScriptRunner {

	private ScriptEngineManager manager = new ScriptEngineManager();
	
	protected ScriptEngine engine = manager.getEngineByName("nashorn");
	protected SimpleBindings binding = new SimpleBindings();
	protected LinkedList<ScriptRunner> blockedBy = new LinkedList<ScriptRunner>();
	protected String status = "loop";
	
	
	public String getStatus()
	{
		return status;
	}

	public String showTodo()
	{
		return "run script here";
	}
	

	public String run(String content)
	{

		System.out.print("running ScriptRunner....\r\n ");
		
		Object ret = null;
		
		try {
			ret = engine.eval(content, binding);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(ret != null && ret.getClass().getSimpleName().equals("String"))
			return (String) ret;
		else
			return "";
		
	}
	
	
	public void saveScript(String script, String name)
	{
		//engine.eval(reader, n)
	}
	
	
	public void registerUsage(Usage useage) 
	{
		
	}
	
	
	private String runScript(String script)
	{
		return "";
	}
	


	public LinkedList<ScriptRunner> getBlockedBy() {
		return blockedBy;
	}



	public void setBlockedBy(LinkedList<ScriptRunner> blockedBy) {
		this.blockedBy = blockedBy;
	}
	
	
}
