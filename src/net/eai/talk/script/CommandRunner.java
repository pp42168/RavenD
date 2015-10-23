package net.eai.talk.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptException;

import net.eai.talk.Tool;
import net.eai.talk.Usage;
import net.eai.talk.UsagePara;
import net.eai.talk.Word;

public class CommandRunner extends ScriptRunner{

	private LinkedHashMap<String,List<Word> > concepts = new LinkedHashMap<String,List<Word> >();
	private LinkedHashMap<ScriptRunner,ScriptRunner> blockedBy = new LinkedHashMap<ScriptRunner,ScriptRunner>();
	private LinkedList<ScriptRunner> runnerStack = new LinkedList<ScriptRunner>();
	private LinkedHashMap<String,ScriptRunner> paraSelectors = new LinkedHashMap<String,ScriptRunner>();

	
	
	@Override
	public String run(String content)
	{
		String response = "";

		System.out.print("running CommandRunner.... \r\n" );

		if(content.isEmpty())
			return "";		
	
		
		if(!runnerStack.isEmpty())
			return processStackRunner(content);		
		

		String commands[] = content.split(" ");
		String headWord = commands[0];
		
		List<Word> means = concepts.get(headWord);
		if(means != null && means.size() == 1)
		{
			Word w = means.get(0);
			
			if(w.type().equals("tool"))		
				response = processToolCommand(means);
			
			else if(w.type().equals("usage"))
			{
				Usage u = (Usage) w;
				response = processUsageCommand(u,content);
			}
			
			else if(w.type().equals("Parameter"))
			{
				UsagePara p = (UsagePara) w;
				response = processParaCommand(p,commands);
			}
		}
		else
		{
			return showCommands();
		}

		return response;

	}
	
	
	

	public void registerTool(Tool tool)
	{
		setConcept(tool.getToolName(),tool);
		for(Usage usage: tool.getUsages())
		{
			setConcept(usage.name(),usage);
			Object o = tool.getClassObject();
			binding.put(tool.getToolName(), o);
			
			List<UsagePara> paras = usage.getNessaseryParas();
			
			for(UsagePara para:paras)
			{
				setConcept(para.name(),para);
			}
		}
	}
	
	private String processUsageCommand(Usage u,String content)
	{
		String response = "";
		UsageRunner ur = new UsageRunner(engine, binding, u);
		
		response = ur.run(content);
		if(!ur.getStatus().equals("finished")){
			runnerStack.add(ur);
			runnerStack.addAll(ur.getBlockedBy());					
		}
		
		return response;
	}
	
	
	private String processToolCommand(List<Word> means)
	{
		String response = "";

		Word w = means.get(0);
		Tool tool = (Tool) w;
		List<Usage> useges = tool.getUsages();
		response = "Tool " + tool + " usage: ";
		for(Usage use:useges){
			response += "\r\n" + use.getMethod().getName();
		}		
		
		return response;
	}


	
	private String processParaCommand(UsagePara p,String[] commands)
	{
		String response = "";

		Usage usage = p.getUsage();
		
		if(commands.length == 1)
		{
			response =  p.name() + "of usage " + usage.name() + " : " + p.getValue().name();
		}
		else if(commands.length == 2)
		{	
			Word value = new Word(commands[1]);
			p.setValue(value);
			usage.setPara(p.name(), value);
			response =  p.name() + "of usage : " + usage.name() + " is now " + p.getValue().name();
		}
		
		return response;
	}

	
	private String processStackRunner(String content)
	{
		String response  = "";
		ScriptRunner runner = runnerStack.getLast();
		runner.run(content);
		if(runner.getStatus().equals("finished"))
		{
			runnerStack.removeLast();
			while(!runnerStack.isEmpty())
			{
				runner = runnerStack.getLast();
				response = runner.run("");
				if(runner.getStatus().equals("finished"))
				{
					runnerStack.removeLast();
					return response;
				}
				else
				{

					runnerStack.addAll(runner.getBlockedBy());					
					
					return response;
				}
			}				
		}	
		return response;
	}

	private void setConcept(String name,Word word)
	{
		if(!concepts.containsKey(name))
		{
			List<Word> contextValue = new ArrayList<Word> ();
			contextValue.add(word);
			concepts.put(name, contextValue);
		}
		else
		{
			List<Word> contextValue = concepts.get(name);
			contextValue.add(word);
		}
	}


	private String showCommands()
	{
		String show = "";
		Iterator<Entry<String,List<Word> > > paraIter = concepts.entrySet().iterator();
		while (paraIter.hasNext()) {
			Entry<String,List<Word> > entry =  paraIter.next();      

			String name = entry.getKey();
			List<Word> words = entry.getValue();
			for(Word oneWord:words)
			{
				if(!oneWord.type().equals("Parameter"))
					show += "\r\n" + oneWord.type() + " : " + name;
			}
		}
		return show;
	}

}
