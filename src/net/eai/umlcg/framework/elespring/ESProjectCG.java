package net.eai.umlcg.framework.elespring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.eai.dev.ioUtil;
import net.eai.umlcg.basecg.ProjectCG;
import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.DEVProject;
import net.eai.umlmodel.Entity;

public class ESProjectCG implements ProjectCG{
	
	private DEVProject m_project;
	private String m_targetPath;
	private String m_templatePath;
	private String m_apiDoc = "";
	
	
	public ESProjectCG(String templatePath,String targetPath,DEVProject prj)
	{
		m_project = prj;
		m_targetPath = targetPath;
		m_templatePath = templatePath;
	}
	
	public void addApiDoc(String doc)
	{
		m_apiDoc += doc;
	}
	
	public void genCode()
	{
		String projectName = m_project.getName();
	}

	
	


	public DEVProject getProject() {
		return m_project;
	}


}
