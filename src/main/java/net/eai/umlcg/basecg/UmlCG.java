package net.eai.umlcg.basecg;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

import net.eai.dev.JarUtil;
import net.eai.dev.ToolMeta;
import net.eai.dev.UmlException;
import net.eai.dev.ioUtil;
import net.eai.umlcg.framework.elespring.ESServiceFramework;
import net.eai.umlcg.scanner.EntityScanner;
import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.DEVProject;
import net.eai.umlmodel.Entity;

public class UmlCG {

	private DEVProject project = null;
	private ESServiceFramework es = null;
	private CodeSkeletonBuilder skeleton = null;
	private String templatePath = null;
	private String orgPath = "me.ele";

	

	public UmlCG()
	{
		templatePath = ":jar";//"eleSpringTemplate";
		//templatePath = "eleSpringTemplate";
		es = new ESServiceFramework("",templatePath);	
	}


	//@ToolMeta(para = {"uml","target"})
	public String importTable(String uml,String target) 
	{

		EntityScanner scanner = new EntityScanner();
		DEVPackage p = scanner.scanDb();

		Gson gson = new Gson();
		return gson.toJson(p);
	//	return "done";
		
	}
	
	//@ToolMeta(para = {"uml","target"})
	public String importDbTables(String packName) 
	{
		EntityScanner scanner = new EntityScanner();
		DEVPackage p = scanner.scanDb();
		p.setName(packName);

		Gson gson = new Gson();
		return gson.toJson(p);
		
	}
		
		
	@ToolMeta(para = {"uml","target"})
	public String gen(String uml,String target) throws UmlException
	{
		

		project = DEVProject.importFromJson(uml);

		if(project == null){
			throw new UmlException("uml"," can't find uml file");
		}

		File file = new File(templatePath);
		//判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists() && !":jar".equals(templatePath)) {
			throw new UmlException("template error","template path " + templatePath + " doesn't exist");			
		}

		project.setProjectPath(target);

		try {
			boolean isJar = false;
			String tempp = templatePath;
			if(":jar".equals(tempp))
			{
				JarUtil j = new JarUtil();
				j.copyFromJarPath("/eleSpringTemplate", "eleSpringTemplate");
				tempp = "eleSpringTemplate";
				es.setM_templatePath(tempp);
				isJar = true;
			}
			
			
			skeleton = new CodeSkeletonBuilder(
					target,
					tempp + "/projectSkeleton",
					project
					);
			
			skeleton.setFramework(es);		
			skeleton.setM_orgPath(orgPath);
			skeleton.genCodeSkeleton();		
			

			if(isJar)
				ioUtil.deleteFile("eleSpringTemplate");

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "done";
	}


	public String getTemplatePath() {
		return templatePath;
	}


	public void setTemplatePath(String templaftePath) {
		this.templatePath = templaftePath;
		es.setM_templatePath(templaftePath);
	}

}
