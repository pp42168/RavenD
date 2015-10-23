package net.eai.talk;

public abstract class Command extends Word{

	public abstract void execute();

	public String type()
	{
		return "command";
	}
}
