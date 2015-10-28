package net.eai.talk.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import net.eai.dev.UmlException;
import net.eai.talk.Usage;
import net.eai.talk.UsagePara;
import net.eai.talk.Word;

public class UsageRunner extends ScriptRunner {

	private Usage usage;
	private List<String> paraNeeded = new ArrayList<String> ();
	
	
	UsageRunner(ScriptEngine e, SimpleBindings b, Usage u)
	{		
		engine = e;
		binding = b;
		usage = u;
	}
	
	void setUsage(Word para,Word value)
	{
		usage.setPara(para.name(), value);
	}

	public String run(String content)
	{

		System.out.print("running UsageRunner....\r\n ");
		
		String currentParas = "";
		String response = "";

		blockedBy.clear();
		Map<String,Word> currentPara = usage.getExistParas();
		paraNeeded.clear();
		for(UsagePara para : usage.getNessaseryParas())
		{
			if(currentPara.containsKey(para.name())){
				
				if(!"".equals(currentParas))
					currentParas += ",";
				Word paraValue = currentPara.get(para.name());
				if(paraValue.type().equals("word"))
					currentParas += "\"" + paraValue.name() + "\"";
			}
			else
			{
				paraNeeded.add(para.name());
				response = "please specify parameter : " + para.name() ;
				String key = usage.name() + ":" + para.name();
				ParaSelectRunner selector = new ParaSelectRunner(usage,para,"guess");
				blockedBy.addFirst(selector);
				break;
			}
		}

		if(blockedBy.isEmpty()){
			try {
				String script = usage.getTool().name() + "." + usage.name() + "(" + currentParas + ")";
				System.out.print("\r\nrunning " + script + "\r\n");
				Object ret = engine.eval(script, binding);
				if(ret != null && ret.getClass().getSimpleName().equals("String"))
					response = (String) ret;
			} catch (Exception e) {
				if(e.getCause().getClass().getName().equals(UmlException.class.getName()))
				{
					UmlException umlEx = (UmlException) e.getCause();
					response = umlEx.getErrorMessage();
				}
				else
					e.printStackTrace();
			}
			
			status = "finished";
		}

		
		return response;
	}


	
}
