package net.eai.util;

public class NameConverter {
	
	public static String toUpperStartName(String ori)
	{
		return ori.toUpperCase().substring(0, 1) + ori.substring(1,ori.length()) ;
	}
	
	public static String toLowerStartName(String ori)
	{
		return ori.toLowerCase().substring(0, 1) + ori.substring(1,ori.length()) ;
	}
	
	
}
