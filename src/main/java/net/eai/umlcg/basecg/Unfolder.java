package net.eai.umlcg.basecg;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.DEVProject;

public class Unfolder {

	private DEVProject m_project;
	private String m_targetPath;
	private String m_templatePath;
	private FrameworkInterface framework = null;
	private LinkedHashMap<String,String> touchList = new LinkedHashMap<String,String>();
	private List<String> filters = new ArrayList<String>();
	private String m_orgPath = "me.ele";
	
		
	public Unfolder(File tempFile,DEVProject prj,DEVPackage pack)
	{
//		m_project = prj;
//		m_targetPath = targetPath;
//		m_templatePath = templatePath;
	}
	
}
