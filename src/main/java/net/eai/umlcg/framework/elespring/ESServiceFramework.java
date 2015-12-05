package net.eai.umlcg.framework.elespring;

import java.util.ArrayList;
import java.util.List;

import net.eai.umlcg.basecg.EntityCG;
import net.eai.umlcg.basecg.FrameworkInterface;
import net.eai.umlcg.basecg.PackageCG;
import net.eai.umlcg.basecg.ProjectCG;
import net.eai.umlcg.basecg.UmlFactory;
import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.DEVProject;

public class ESServiceFramework  implements FrameworkInterface{

	private String m_templatePath;
	private String m_targetPath;
	
	public ESServiceFramework(String targetPath,String templatePath)
	{
		m_templatePath = templatePath;
		m_targetPath = targetPath;
	}

	@Override
	public boolean hasError() {
		// UML校验
		return false;
	}

	@Override
	public ProjectCG getProjectCG(DEVProject prj) {
		// project 全局生成器
		ESProjectCG pjCg = new ESProjectCG(m_templatePath,m_targetPath,prj);		
		return pjCg;
	}

	@Override
	public List<EntityCG> getEntityCGs(PackageCG packCG) {
		// 获取Entity生成器列表
		ArrayList<EntityCG> res = new ArrayList<EntityCG>();		
		res.add(new ESApiEntityCG((ESPackageCG) packCG,m_templatePath,m_targetPath));
		res.add(new ESSqlSchemaCG((ESPackageCG) packCG,m_templatePath,m_targetPath));
		res.add(new ESServiceCG((ESPackageCG) packCG,m_templatePath,m_targetPath));
		res.add(new ESApiDocCG((ESPackageCG) packCG,m_templatePath,m_targetPath));
		res.add(new ESUnitTestCG((ESPackageCG) packCG,m_templatePath,m_targetPath));
		
		
		return res;
	}

	@Override
	public PackageCG getPackageCG(ProjectCG projectCG,DEVPackage pack) {
		// 获取Packaae生成器		
		return new ESPackageCG((ESProjectCG) projectCG,pack,m_templatePath,m_targetPath);
		//return  null;
	}


	@Override
	public UmlFactory getEntityApiFactory() {		
		return null;
	}

	@Override
	public UmlFactory getEntityPageFactory() {
		return null;
	}

	public String getM_templatePath() {
		return m_templatePath;
	}

	public void setM_templatePath(String m_templatePath) {
		this.m_templatePath = m_templatePath;
	}


}
