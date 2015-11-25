package net.eai.umlcg.basecg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.eai.dev.ioUtil;
import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.DEVProject;
import net.eai.umlmodel.Entity;
import net.eai.umlmodel.EntityAttribute;
import net.eai.umlmodel.EntityOperation;

public class CodeSkeletonBuilder {

	private DEVProject m_project;
	private String m_targetPath;
	private String m_templatePath;
	private FrameworkInterface framework = null;
	private LinkedHashMap<String,String> touchList = new LinkedHashMap<String,String>();
	private List<String> filters = new ArrayList<String>();
	private String m_orgPath = "me.ele";
	
	
	public CodeSkeletonBuilder(String targetPath,String templatePath,DEVProject prj)
	{
		m_project = prj;
		m_targetPath = targetPath;
		m_templatePath = templatePath;
	}
	
	
	//////////  generate project skeleton //////////////

	public void addNoTouch(String path)
	{
			touchList.put(path, "notouch");
	}
	
	public void addFilter(String path)
	{
		filters.add(path);
	}

	public void addDoTouch(String path)
	{
		touchList.put(path, "dotouch");
	}
	
	
	public void genCodeSkeleton()
	{
		copyPathWithTemplate(m_templatePath,m_targetPath,
				null,null);
	}
	
	private boolean pathFiltered(String path)
	{
		for(String oneFilter:filters)
		{
			if(path.contains(oneFilter))
				return true;
		}
		return false;
	}

	private void copyPathWithTemplate(String templatePath,String targetPath,
			DEVPackage pack,Entity entity)
	{
		File root = new File(templatePath);  
	    File[] files = root.listFiles();
	    if(files != null)
	    {
	    	 for(File file:files){   
	    		 //process with directories
	 	    	if(file.isDirectory()){
	 	    		if(pathFiltered(targetPath))
	 	    		{
	 	    			System.out.print("\nignored file " + targetPath);
	 	    		}
	 	    		else
	 	    		{
		 	    		processDirectories(file,pack,entity,templatePath,targetPath);
	 	    		}
	 	    	}
	 	    	// process with files
	 	    	else{	
	 	    		
	 	    		if(!file.getName().contains(".DS_Store"))
	 	    			processFile(file,pack,entity,targetPath);
	 	    	}
	 	    }
	    }
	}
	
	private void processEachEntity(File file,DEVPackage pack,Entity entity,
			String tempPath,String thisPath,String targetPath,String type)
	{
		if(ioUtil.containF(tempPath, "each" + type + "@"))
		{
			for(Object oneObject:pack.getEntities().values())
		    {
				Entity oneEntity = (Entity) oneObject;
				if(!type.equals(oneEntity.getStereotype()))
					continue;
				
				tempPath = ioUtil.replaceF(tempPath, "each" + type, oneEntity.getName());
	 	   		copyPathWithTemplate(file.getPath(),targetPath  + thisPath,
	 	    				pack,oneEntity);
		    }
		}
		else
 			copyPathWithTemplate(file.getPath(),targetPath  + thisPath,
 					pack,entity);
		
	}
	
	@SuppressWarnings("rawtypes") 	
	private void processDirectories(File file,DEVPackage pack,Entity entity,String templatePath,String targetPath)
	{
		
 		
 			
		int parentPathLength = templatePath.length();
 		String tempPath = file.getPath().substring(parentPathLength);
 		
 			if(tempPath.startsWith("@CGele"))
 				return;

 			tempPath = ioUtil.replaceF(tempPath, "prj", m_project.getName());
 			String orgDirPath = m_orgPath.replace(".", "/");
	    	if(tempPath.contains("@orgPath@"))
	    		tempPath = tempPath.replace("@orgPath@", orgDirPath);
	    	if(pack != null)
	    		tempPath = ioUtil.replaceF(tempPath, "pack",  pack.getName()) ;	    		
	    	//	tempPath = tempPath.replace("@pack@", pack.getName());
	    	
	    	if(ioUtil.containF(tempPath,"eachPack"))
	    	{
	    		for(DEVPackage onePack:m_project.getPackages())
	    		{
	    			String thisPath = ioUtil.replaceF(tempPath, "eachPack", onePack.getName());
    				processEachEntity(file,onePack,entity,tempPath,thisPath,targetPath,"Entity");
    				processEachEntity(file,onePack,entity,tempPath,thisPath,targetPath,"Service");
	    		}
	    	}
	    	else if(ioUtil.containF(tempPath, "eachApiPack"))
	    	{
	    		for(DEVPackage onePack:m_project.getApiPacks())
	    		{
	    			
	    			String thisPath = ioUtil.replaceF(tempPath, "eachApiPack", onePack.getName());
    				processEachEntity(file,onePack,entity,tempPath,thisPath,targetPath,"Api");
	    		}
	    	}
	    	else
	    	{
	    		copyPathWithTemplate(file.getPath(),targetPath  + tempPath,
	    				pack,entity);
	    	}
 		
	}
	
	private boolean processEachFile(String eachType, File file,String targetFileName,DEVPackage pack,String targetPath)
	{
			
		if(ioUtil.containF(targetFileName, "each" + eachType))
		{
			for(Object oneObject:pack.getEntities().values())
	 	    {
					Entity oneEntity = (Entity) oneObject;				
					if(eachType.equals(oneEntity.getStereotype()))
					{
						String fileName = ioUtil.replaceF(targetFileName, "each" + eachType,  oneEntity.getName());
		    			String fileData = genFileDataWithTemplate(file,pack,oneEntity);
		 	    		ioUtil.writeFile(targetPath + "/" + fileName,fileData);
					}
	 	    }
		}
		else
			return false;
		
		return true;
	}
	
	private void processFile(File file,DEVPackage pack,Entity entity,String targetPath)
	{
		String targetFileName = file.getName();

		targetFileName = ioUtil.replaceF(targetFileName, "prj", m_project.getName());
	    	if(pack != null)
	    		targetFileName = ioUtil.replaceF(targetFileName,"pack", pack.getName());
	    	
 	 	if(ioUtil.containF(targetFileName, "eachPack"))
 		{
 			for(DEVPackage onePack:m_project.getPackages())
	    		{
 					String thisPackFileName = ioUtil.replaceF(targetFileName,"eachPack", onePack.getName());

	 				boolean processed = false;

	 	 			processed = processed || processEachFile("Service", file, targetFileName, pack, targetPath);
	 	 			processed = processed || processEachFile("Entity", file, targetFileName, pack, targetPath);
	 	 			processed = processed || processEachFile("Contract", file, targetFileName, pack, targetPath);
	 	 			
 					if(!processed)
 					{
 						String fileData = genFileDataWithTemplate(file,onePack,entity);
	    				if(!"".equals(fileData))
	    					ioUtil.writeFile(targetPath + "/" + thisPackFileName,fileData);
 					}
	    		}
 		}
 		else if(ioUtil.containF(targetFileName, "eachApiPack"))
 		{
 			for(DEVPackage onePack:m_project.getPackages())
	    		{ 				
	 				String thisPackFileName = ioUtil.replaceF(targetFileName,"eachApiPack", onePack.getName());
	 				if(!processEachFile("Api", file, thisPackFileName, onePack, targetPath))
 					{
 						String fileData = genFileDataWithTemplate(file,onePack,entity);
	    				if(!"".equals(fileData))
	    					ioUtil.writeFile(targetPath + "/" + thisPackFileName,fileData);
 					}
	    		}
 		}
 		else 
 		{
 			boolean processed = false;
 			processed = processEachFile("Entity", file, targetFileName, pack, targetPath);
 			processed = processed || processEachFile("Api", file, targetFileName, pack, targetPath);
 			processed = processed || processEachFile("Service", file, targetFileName, pack, targetPath);
 			processed = processed || processEachFile("Contract", file, targetFileName, pack, targetPath);
 			processed = processed || processEachFile("Exception", file, targetFileName, pack, targetPath);
 			
 			if(!processed){

 	 			String fileData = genFileDataWithTemplate(file,pack,entity);
 	 	    	ioUtil.writeFile(targetPath + "/" + targetFileName,fileData);
 			}
 		} 		
	}
	
	private String getCGLines(String line,DEVPackage pack,Entity entity)
	{
		String prefix = line.substring(0,line.indexOf("#CG"));
		line = line.substring(line.indexOf("#CG"));
		
		String commands[] = line.split(" ");
	    ProjectCG pcg = framework.getProjectCG(m_project);
	    String CGType = commands[1];
		if(line.startsWith("#CG PackCG "))
		{
			PackageCG packCG = framework.getPackageCG(pcg, pack);
			return packCG.genCode();
			
		}
		else if(line.startsWith("#CG EntityCG "))
		{
			String entityCGType = commands[2];
			
			
			PackageCG packCG = framework.getPackageCG(pcg, pack);
			List<EntityCG> entityCGs = framework.getEntityCGs(packCG);
			for(EntityCG cg:entityCGs)
			{
				if(entityCGType.equals(cg.getType()))
				{
					if(entity == null)
					{
						String code = "";
						for(Object oneObject:pack.getEntities().values())
			 	    	{
	    					Entity oneEntity = (Entity) oneObject;
	    					String oneCode = cg.genCode(oneEntity);
	    					
	    					code += oneCode.replace("\n", "\n" + prefix);
			 	    	}
						return code;
					}
					else
						return cg.genCode(entity).replace("\n", "\n" + prefix);
				}
			}
			
		}
		
		return line;
	}
	
	
	@SuppressWarnings("rawtypes")
	private String genFileDataWithTemplate(File file,DEVPackage pack,Entity entity)
	{		
	    String content = "";

		if(file.exists())
		{
			try {
				String line;
			    BufferedReader reader = new BufferedReader(new FileReader(file));  
				line = reader.readLine();
				
				
				
				while(line !=null){  
					
					if(line !=null && line.contains("@<@")){
						while(line != null)
						{
							String newLine = reader.readLine();
							
							line += "\n" + newLine;
							//System.out.print(line);
							if(newLine == null || newLine.contains("@>@"))
							{	
								line = line.replace("@>@", "");
								line = line.replace("@<@", "");		
								break;
							}						
						}
					}
					

					if(framework != null && line.contains("#CG "))
					{
						content += getCGLines(line,pack,entity);
					}
					else
					{
						line = line.replace("@orgPath@", m_orgPath);
						line = ioUtil.replaceF(line, "prj", m_project.getName());
						if(pack != null)
							line = ioUtil.replaceF(line, "pack", pack.getName());
						if(entity != null)
							line = ioUtil.replaceF(line, "entity", entity.getName());
						
						if(pack != null)
						{
							if(ioUtil.containF(line, "eachDependPack"))
							{
								List<DEVPackage> packs = (List<DEVPackage>) pack.getDependPacks();
								for(DEVPackage dependPack:packs)
								{
									String oneLine =  ioUtil.replaceF(line, "eachDependPack", dependPack.getName());
									content += oneLine + "\n";								
								}
							}
							else if(entity != null)
							{
								if(ioUtil.containF(line, "eachDependEntity"))
								{
									for(Entity dependEntity:entity.getDepends().values())
									{
										String oneLine =  ioUtil.replaceF(line, "eachDependEntity", dependEntity.getName());
										content += oneLine + "\n";								
									}
								}
								else if(ioUtil.containF(line, "eachOp"))
								{
									for(EntityOperation op:entity.getOperations())
									{
										String oneLine =  ioUtil.replaceF(line, "eachOp", op.getName());
										content += oneLine + "\n";								
									}
								}
								else if(ioUtil.containF(line, "eachAtt"))
								{
									for(EntityAttribute att:entity.getAttributes())
									{
										String oneLine =  ioUtil.replaceF(line, "eachAtt", att.getName());
										oneLine =  ioUtil.replaceF(oneLine, "attType", att.getTypeStr());
										oneLine =  ioUtil.replaceF(oneLine, "eachAtt.document", att.getDocumentation());
										oneLine =  ioUtil.replaceF(oneLine, "eachAtt.default", att.getDefaultValue());
										content += oneLine + "\n";								
									}
								}
								else
									content += genEachEntity(line,pack) + "\n"; 
							}
							else if(ioUtil.containF(line, "eachService"))
							{
								for(Object o:pack.getEntities().values())
								{
									Entity e = (Entity) o;
									if("Service".equals(e.getStereotype()))
									{
										String oneLine =  ioUtil.replaceF(line, "eachService", e.getName());
										content += oneLine + "\n";	
									}																
								}
							}
							else
								content += genEachEntity(line,pack) + "\n"; 
							
						}						
						else if(ioUtil.containF(line, "eachPack")){
							for(DEVPackage onePack:m_project.getPackages())
				 	    	{
								String oneLine =  ioUtil.replaceF(line,"eachPack", onePack.getName());
								content += genEachEntity(oneLine,onePack) + "\n";
				 	    	}
						}
						else if(ioUtil.containF(line, "eachApiPack"))
				 		{
				 			for(DEVPackage onePack:m_project.getApiPacks())
				 			{
				 				String oneLine =  ioUtil.replaceF(line,"eachApiPack", onePack.getName());
				 				content += oneLine + "\n";
				 			}
				 		}
						else{
							content += line + "\n"; 
						}
					}
					
						
					line = reader.readLine();
			    }  
			    reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return content;
	}

	
	private String genEachEntity(String line,DEVPackage pack)
	{
		String content = "";
		
		
		if(ioUtil.containF(line, "eachEntity")){
			
			for(Object oneObject:pack.getEntities().values())
	    	{
				Entity oneEntity = (Entity) oneObject;
	 			if(!"Entity".equals(oneEntity.getStereotype()))
	 					continue;
	 			String oneEntityLine =  ioUtil.replaceF(line,"eachEntity", oneEntity.getName());
 				content += oneEntityLine + "\n";
	    	}
			return content;
		}
		else if(ioUtil.containF(line, "eachContract"))
		{
			for(Object oneObject:pack.getEntities().values())
	    	{
				Entity oneEntity = (Entity) oneObject;
	 			if(!"Contract".equals(oneEntity.getStereotype()))
	 					continue;
	 			String oneEntityLine =  ioUtil.replaceF(line,"eachContract", oneEntity.getName());
 				
				content += oneEntityLine + "\n";
	    	}
			return content;
		}
		return line;
	}

	public FrameworkInterface getFramework() {
		return framework;
	}


	public void setFramework(FrameworkInterface framework) {
		this.framework = framework;
	}


	public String getM_orgPath() {
		return m_orgPath;
	}


	public void setM_orgPath(String m_orgPath) {
		this.m_orgPath = m_orgPath;
	}
	
}
