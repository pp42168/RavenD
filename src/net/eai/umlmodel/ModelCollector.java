package net.eai.umlmodel;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

public class ModelCollector {

	private DEVProject devProject;
	
	
	public ModelCollector(DEVProject prj)
	{
		devProject = prj;
		
	}
	
	
	private boolean hasEntity(DEVPackage pack,String type)
	{
		List ownedElements = (List) pack.getOwnedElements();
		for(int i = 0;i<ownedElements.size();i++)
		{
			Object obj = ownedElements.get(i);
			Entity entity = null;
			if(obj.getClass().toString().contains("LinkedHashMap"))			
				entity = getEntity((LinkedHashMap)ownedElements.get(i));			
			else if(obj.getClass().toString().contains("DEVPackage"))
				entity = (Entity)ownedElements.get(i);	
			
						
			if(entity != null){
				if(type.equals(entity.getStereotype()))
					return true;
			}
		}
		
		return false;
	}
	
	private Entity getEntity(LinkedHashMap data)
	{
		Gson gson = new Gson();
		Entity entity = null;
	    String type = (String) data.get("_type");
	    if (type.equals("UMLClass")) {
	         String eleJson = gson.toJson(data);
	         entity = gson.fromJson(eleJson, Entity.class);
	    }
		return entity;
	}
	

	private DEVPackage getPackage(LinkedHashMap data)
	{
		
		Gson gson = new Gson();
		DEVPackage pack = null;
	    String type = (String) data.get("_type");
	    if (type.equals("UMLPackage")) {
	         String eleJson = gson.toJson(data);
	         pack = gson.fromJson(eleJson, DEVPackage.class);
	    }
		return pack;
	}
	
	
	private void findModels(DEVPackage parentPack,String mustHasType)
	{

		List ownedElements = (List) parentPack.getOwnedElements();
		
		
		for(int i = 0;i<ownedElements.size();i++)
		{
			Object obj = ownedElements.get(i);
			DEVPackage devPackage = null;
			String type = obj.getClass().toString();
			if(obj.getClass().toString().contains("LinkedHashMap"))			
				 devPackage = getPackage((LinkedHashMap)ownedElements.get(i));			
			else if(obj.getClass().toString().contains("DEVPackage"))
				 devPackage = (DEVPackage)ownedElements.get(i);	
			
			if(devPackage != null)
			{
				if(hasEntity(devPackage,mustHasType))
				{
					devPackage.setPackageName(devProject.getName() + "." + devPackage.getName());
					devPackage.setProjectName(devProject.getName());
					DEVPackage.initDevPackage(devPackage);
					if(mustHasType.equals("Service"))
						devProject.addModel(devPackage);
					else if(mustHasType.equals("Api"))
						devProject.addWebApi(devPackage);
				}
				
				findModels(devPackage,mustHasType);
			}
		}
		
	}
	

	public void  collectPacks()
	{
		
		List<DEVPackage> ownedElements = (List<DEVPackage>) devProject.getOwnedElements();
		for(int i = 0;i<ownedElements.size();i++)
		{
			DEVPackage devPackage = ownedElements.get(i);
			
			if("Models".equals(devPackage.getName()) || "Models".equals(devPackage.getStereotype()))
			{
				findModels(devPackage,"Service");
			}
			
			if("WebApis".equals(devPackage.getName()) || "WebApis".equals(devPackage.getStereotype()))
			{
				findModels(devPackage,"Api");
			}
		}
		
	
	}

}
