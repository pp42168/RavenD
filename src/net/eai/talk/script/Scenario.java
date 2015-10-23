package net.eai.talk.script;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import net.eai.talk.Word;
import net.eai.talk.exception.UsageException;

import java.lang.reflect.Method;

public class Scenario { 

	private Scenario parent;	
	private Date createTime;
	private String contet;
	private List<Word> means;
	private String type;
	
	private ScriptRunner currentRunner = new ScriptRunner();

	
	
	public String acceptContent(String content)
	{
		return getCurrentRunner().run(content);
	}
	
	//root entry
	public Scenario()
	{
		createTime = new Date();		
	}
	
	//sub entry
	public Scenario(Scenario p)
	{
		createTime = new Date();
		parent = p;
	}
	

	public List<Word> getMeans()
	{
		return means;
	}
	
	
	public void addMeans(List<Word> meanlist)
	{
		means.addAll(meanlist);
	}

	public String getContet() {
		return contet;
	}

	public void setContet(String contet) {
		this.contet = contet;
	}

	public ScriptRunner getCurrentRunner() {
		return currentRunner;
	}

	public void setCurrentRunner(ScriptRunner currentRunner) {
		this.currentRunner = currentRunner;
	}


}
