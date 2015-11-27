package net.eai.talk;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import net.eai.talk.script.Scenario;

public class Memory {

	private LinkedHashMap<String,List<Word> > concepts;
	private LinkedList<Scenario> senarios = new LinkedList<Scenario>();
	private LinkedHashMap<String,Object> status;
	
	Memory()
	{
		concepts = new LinkedHashMap<String,List<Word> >();
		senarios = new LinkedList<Scenario>();
	}
	
	public void saveScene(Scenario scnene)
	{
		senarios.add(scnene);
	}
	
	public void setConcept(String name,Word word)
	{
		if(!concepts.containsKey(name))
		{
			List<Word> contextValue = new ArrayList<Word> ();
			contextValue.add(word);
		}
		else
		{
			List<Word> contextValue = concepts.get(name);
			contextValue.add(word);
		}
	}
	

	public void knowingTool(Tool tool)
	{		
		setConcept(tool.name(),tool);
		for(Usage usage: tool.getUsages())
		{
			setConcept(usage.name(),usage);
		}
	}
	
	public LinkedHashMap<String,List<Word> > getConcepts() {
		return concepts;
	}
	public void setConcepts(LinkedHashMap<String,List<Word> > concepts) {
		this.concepts = concepts;
	}
	public List<Scenario> getSenarios() {
		return senarios;
	}
	public void setSenarios(LinkedList<Scenario> senarios) {
		this.senarios = senarios;
	}

	public LinkedHashMap<String,Object> getStatus() {
		return status;
	}

	public void setStatus(LinkedHashMap<String,Object> status) {
		this.status = status;
	}
	
	
}
