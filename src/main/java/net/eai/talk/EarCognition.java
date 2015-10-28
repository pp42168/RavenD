package net.eai.talk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.eai.talk.exception.UsageException;
import net.eai.talk.script.Scenario;

public class EarCognition {

	private Memory memory;
	private Scenario currentOn = null;
	private Speaker speaker = new Speaker();

	EarCognition(Memory mem)
	{
		memory = mem;
	}

	public void beBlown(Scenario scenWind)
	{
		touch(scenWind);
	}
	
	
	//////// private methods //////////

	private void focus(Scenario scene)
	{
		memory.saveScene(scene);
	}

	private void touch(Scenario scene)
	{		
		miss(scene);
		feel(scene);	
		think(scene);
	}


	private void feel(Scenario scene)
	{
		
	}

	private void miss(Scenario scene)
	{
		List<Word> concepts = null;
		if(memory.getConcepts().containsKey(scene.getContet()))
			concepts = memory.getConcepts().get(scene.getContet());
		
		if(concepts != null)
		{
			scene.addMeans(concepts);
		}
	}

	private void think(Scenario scene)
	{
		try {
			List<Word> means = scene.getMeans();
			if(means.size() == 1)
			{
				Word w = means.get(0);
				if(w.type().equals("tool"))				
				{	
					Tool tool = (Tool) w;
					List<Usage> useges = tool.getUsages();
					String toolUsages = "Tool " + tool + " usage: ";
					for(Usage use:useges){
						toolUsages += "\r\n" + use.getMethod().getName();
					}
					speaker.willSay(toolUsages);
					
				}
				else if(w.type().equals("usage"))
				{
					Usage u = (Usage) w;
					String toolUsages = w.name() + "";
					
					Iterator<Entry<String,Word>> paraIter = u.getExistParas().entrySet().iterator();
					while (paraIter.hasNext()) {
						Entry<String, Word> entry =  paraIter.next();      
						
						String key = entry.getKey();
						Word val = entry.getValue();
						toolUsages += "\r\n" + key + ":" + val;
					}	

					speaker.willSay(toolUsages);
					
					
				}

			}
			else
			{
				List<Word> methods  = new ArrayList<Word>();
				List<Word> tools  = new ArrayList<Word>();

				speaker.willSay("multiple meanings unspported.");
				/*for(Word w:words)
				{
					if(w.type().equals("tool"))				
						tools.add(w);		
					else if(w.type().equals("methods"))
						methods.add(w);
				}*/
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
