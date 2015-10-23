package net.eai.umlcg.basecg;

import java.io.File;

import net.eai.dev.ToolMeta;
import net.eai.dev.UmlException;
import net.eai.umlcg.framework.elespring.ESServiceFramework;
import net.eai.umlmodel.DEVProject;

public class UmlCG {

	private DEVProject project = null;
	private ESServiceFramework es = null;
	private CodeSkeletonBuilder skeleton = null;
	private String templatePath = null;
	private String orgPath = "me.ele";


	public UmlCG()
	{
		templatePath = "eleSpringTemplate";
		es = new ESServiceFramework("",templatePath);	
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
		if (!file.exists()) {
			throw new UmlException("template error","template path " + templatePath + " doesn't exist");			
		}

		project.setProjectPath(target);

		skeleton = new CodeSkeletonBuilder(
				target,
				templatePath + "/projectSkeleton",
				project
				);

		skeleton.setFramework(es);		
		skeleton.setM_orgPath(orgPath);
		skeleton.genCodeSkeleton();		

		return "done";
	}

}
