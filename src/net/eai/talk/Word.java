package net.eai.talk;

public class Word {
	
	private String wName;
	private String wType;
	
	
	
	public Word()
	{
		
	}
	
	public Word(String n)
	{
		wName = n;
		wType = "word";
	}
	
	public String name()
	{
		if(wName == null)
			return "word";
		else 
			return wName;
	}
	
	public void setName(String name)
	{
		wName = name;
	}
	
	public void setType(String type)
	{
		wType = type;
	}

	public String type()
	{
		if(wType == null)
			return "word";
		else 
			return wType;
	}

}
