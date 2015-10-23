package net.eai.talk;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.eai.dev.ToolMeta;
import net.eai.talk.exception.UsageException;

public class Usage extends Word  {

	private String methodName;
	private Tool tool;
	private List<UsagePara> nessaseryParas = new ArrayList<UsagePara>();
	private LinkedHashMap<String,Word> parasToUse = new LinkedHashMap<String,Word>();
	private Method method;
	
	
	public void setPara(String paraName,Word paraValue)
	{
		parasToUse.put(paraName, paraValue);
	}
	
	
	public Usage(Tool owner,Method m)
	{
		tool = owner;
		method = m;
		methodName = m.getName();

		ToolMeta anno = m.getAnnotation(ToolMeta.class);

		
		int paraInd = 0;
		for(Parameter p : m.getParameters())
		{
			UsagePara w = new UsagePara();
			w.setUsage(this);
			w.setType(p.getClass().getSimpleName());
			if(p.isNamePresent())
			{
				w.setName(p.getName());
			}
			else if(anno != null)
			{
				w.setName(anno.para()[paraInd]);
			}	
			nessaseryParas.add(w);
			paraInd ++;
		}
		
		
		
	}
	
	public String type()
	{
		return "usage";
	}
	
	public List<UsagePara> getNessaseryParas()
	{
		return nessaseryParas;
	}
	
	
	public LinkedHashMap<String,Word> getExistParas()
	{
		return parasToUse;
	}
	
	public LinkedHashMap<Word,Word> analyzePara(String content)
	{
		LinkedHashMap<Word,Word> para = new LinkedHashMap<Word,Word>();
		
		
		return para;
	}
	
	public Tool getTool()
	{
		return tool;
	}
	
	public String getUseCommand(List<Word> paras) throws UsageException
	{
		String command = "";
		UsageException ex = new UsageException();
		if(paras != null)
		{
			for(int i=0;i<paras.size();i++)
			{
				if(i <= nessaseryParas.size())
				{
					 Word para = paras.get(i);
					 UsagePara nWord = nessaseryParas.get(i);
					 
					 if(para.type().equals(nWord.name()))
						 parasToUse.put(nWord.name(), para);
				}
			}
		}
		
		
		
		ArrayList<Word> missingParas = new ArrayList<Word>();
		for(Word word:nessaseryParas)
		{
			if(!parasToUse.containsKey(word)){
				missingParas.add(word);
			}				
		}
		
		if(!missingParas.isEmpty())
		{
			ex.setMissingParas(missingParas);
			throw ex;
		}
		
		
		return command;
	}
	

	public void setName(String name)
	{
		 methodName = name;
	}
	
	public String name()
	{
		return methodName;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}
