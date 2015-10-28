package net.eai.talk;

import java.util.LinkedList;
import java.util.List;

public class Speaker {
	
	private Memory memory = new Memory();
	private LinkedList<String> toSay = new LinkedList<String>();
	private LinkedList<String> said = new LinkedList<String>();
	
	void willSay(String content)
	{
		toSay.addLast(content);
	}
	
	String say()
	{
		if(toSay.isEmpty())
			return "";
		
		String saying = toSay.getFirst();
		toSay.removeFirst();
		said.addLast(saying);
		return saying;
	}
	
}
