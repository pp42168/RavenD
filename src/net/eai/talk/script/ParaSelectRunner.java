package net.eai.talk.script;

import java.util.ArrayList;
import java.util.List;

import net.eai.talk.Usage;
import net.eai.talk.Word;

public class ParaSelectRunner extends ScriptRunner {

	private List<String> selection = new ArrayList<String>();
	private int selectedIndex = -1;
	private String mode = "guess";
	private Usage usage;
	private Word paraName;
	
	ParaSelectRunner(Usage u, Word p, String m)
	{
		mode = m;
		usage = u;
		paraName = p;
	}
	
	public void addSelection(String s)
	{
		selection.add(s);
	}
	
	public String showTodo()
	{
		int index = 0;
		String res = "";
		for(String s:selection)
		{
			res += "\r\n[" + index + "]" + s ;
			if(index == 0)
			{
				res += "[Enter]";
			}
		}		
		return res;
	}
	
	@Override
	public String run(String content)
	{

		System.out.print("running ParaSelectRunner....\r\n " );
		
		String res = showTodo();
		int sel = Integer.getInteger(content, -1);
		if("".equals(content))
			sel = 0;			

		selectedIndex = sel;
		if(sel == -1 && "guess".equals(mode)){
			selection.add(content);
			selectedIndex = selection.size();
			usage.setPara(paraName.name(), new Word(content));
			status = "finished";			
		}		
		else if(sel != -1){
			status = "finished";			
		}
		
		return res;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
}
