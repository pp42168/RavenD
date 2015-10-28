package net.eai.umlcg.framework.elespring;

import java.util.Iterator;
import java.util.Map.Entry;

import net.eai.dev.ioUtil;
import net.eai.dev.entitycg.PageEntity;
import net.eai.umlcg.basecg.PackageCG;
import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.Entity;

public class ESPackageCG implements PackageCG {
	
	private ESProjectCG m_projectCG;
	private String m_templatePath;
	private String m_targetPath;
	
	private String ibatisMapper = "";
	
	DEVPackage m_package;
	
	DEVPackage getPackage()
	{
		return m_package;
	}
	
	ESProjectCG getProjectCG()
	{
		return m_projectCG;
	}
	
	public void setTargetPath(String targetPath)
	{
		m_targetPath = targetPath;
	}
	
	
	public ESPackageCG(ESProjectCG cg,DEVPackage pack,String templatePath,String targetPath)
	{
		m_projectCG = cg;
		m_templatePath = templatePath;
		m_targetPath = targetPath;
		m_package = pack;
		
	}
	
	public void addIbatisMapper(String code)
	{
		ibatisMapper += code;
	}
	
	public String genCode()
	{
		  String prjName = m_projectCG.getProject().getName();
			String packageName =  "me.ele." + prjName + "." + m_package.getName();
	        String packagePath = packageName;
	        packagePath = packagePath.replace(".", "/");
	        
	        
	        String mapperFile = ioUtil.readFile(m_templatePath + "/DataMapper.java");


	        String packPath = prjName + "-impl/" + prjName + "-" +  m_package.getName() 
	        		+ "-impl/src/main/java/" + packagePath + "/impl/mapper";

	        if (!ibatisMapper.isEmpty()) {
	            mapperFile = mapperFile.replace("@content@", ibatisMapper);
	            mapperFile = mapperFile.replace("@packageName@", packageName);

	            String mapperXmlFile = ioUtil.readFile(m_templatePath + "/DataMapper.xml");
	            mapperXmlFile = mapperXmlFile.replace("@packageName@", packageName);
	            ioUtil.writeFile(m_targetPath + "/" + packPath  + "/" + m_package.getName() + "XMapper.xml", mapperXmlFile);
	            ioUtil.writeFile(m_targetPath + "/" + packPath  + "/" + m_package.getName() + "XMapper.java", mapperFile);
	            
	         //   m_projectCG.addDaoConf("\t\t<mapper resource=\"" + packagePath + "/DataMapper.xml\" />\r\n");
	        }  
	        
	        return "";
	}
	
	
}
