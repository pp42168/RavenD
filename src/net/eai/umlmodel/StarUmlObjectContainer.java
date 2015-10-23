package net.eai.umlmodel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class StarUmlObjectContainer {

	static private LinkedHashMap objects = new LinkedHashMap();
	static private LinkedHashMap namespaces = new LinkedHashMap();
	static private ArrayList createdEntityPages = new ArrayList();
	 
	static public Object getObject(String key)
	 {
		 return objects.get(key);
	 }
	 	  
	 
		 	  

	static public void addPagePackage(String key,Object obj)
	 {
		createdEntityPages.add(obj);
	 }	
		
	static public ArrayList getPagePackageSet()
	 {
		return createdEntityPages;
	 }	
	
	static public void addObject(String key,Object obj)
	 {
		 objects.put(key, obj);
	 }	
	
	static public Object getObjectByName(String name)
	 {
		 return namespaces.get(name);
	 }
	 	  
	static public void addObjectByName(String name,Object obj)
	 {
		namespaces.put(name,obj);
	 }
	
}
