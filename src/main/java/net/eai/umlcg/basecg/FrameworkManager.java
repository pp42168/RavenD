package net.eai.umlcg.basecg;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.eai.dev.ioUtil;
import net.eai.dev.entitycg.PageEntity;
import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.DEVProject;
import net.eai.umlmodel.Entity;


public class FrameworkManager {

	private static LinkedHashMap<String,FrameworkInterface> allFrameworks 
				= new LinkedHashMap<String,FrameworkInterface>();

	public FrameworkInterface getFramework(String name)
	{
		return allFrameworks.get(name);
	}
	
	public void addFramework(String name,FrameworkInterface framework)
	{
		allFrameworks.put(name, framework);
	}
	
	public void genCode(String frameworkName,DEVProject project)
	{
		FrameworkInterface framework = getFramework(frameworkName);
		if(framework != null && !framework.hasError())
		{						
			// create Models
			UmlFactory pageFactoty = framework.getEntityApiFactory();
			UmlFactory apiFactory = framework.getEntityApiFactory();
			
			for(DEVPackage val:project.getPackages()){
				
			    Iterator<Entry<String, Entity>> entityIter = val.getEntities().entrySet().iterator();
		        while (entityIter.hasNext()) {
		            Entry<String, Entity> entry = entityIter.next();
		            Entity entityObj = entry.getValue();
		            
					if(pageFactoty != null)
						pageFactoty.createModels(project,val,entityObj);

					if(apiFactory != null)
						apiFactory.createModels(project,val,entityObj);	
		        } 
			}			
			
			// generate entity code			
			ProjectCG projectCG = framework.getProjectCG(project);			
			for(DEVPackage pac:project.getPackages()){

				PackageCG packageCG = framework.getPackageCG(projectCG,pac);
				if(packageCG == null)
					continue;
				
			    Iterator<Entry<String, Entity>> entityIter = pac.getEntities().entrySet().iterator();
		        while (entityIter.hasNext()) {
		            Entry<String, Entity> entry = entityIter.next();
		            Entity entityObj = entry.getValue();
		            
		            List<EntityCG> entityCGList = framework.getEntityCGs(packageCG);
					if(entityCGList != null)
					{
						for(EntityCG oneCG:entityCGList)
						{
							oneCG.genCode(entityObj);
						}
								
					}
		        } 
		        
		        packageCG.genCode();
		        
			}

			projectCG.genCode();
		}
		
	}
	
}
