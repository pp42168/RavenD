package net.eai.umlmodel;

import com.google.gson.Gson;

import net.eai.dev.ioUtil;
import net.eai.umlcg.decorator.ServiceDecorator;

import java.util.ArrayList;
import java.util.List;

public class DEVProject {

	private ArrayList<DEVPackage> packages  = new ArrayList<DEVPackage>();
	private ArrayList<DEVPackage> webApis  = new ArrayList<DEVPackage>();
	private String projectPath;
	
	private String _type;
	private String _id;
	private String name;
	private List<DEVPackage> ownedElements;
	
	
	public String getProjectPath() {
		return projectPath;
	}
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	

	@SuppressWarnings("rawtypes")
	public static DEVProject importFromJson(String jsonfile) {

		String json = ioUtil.readFile(jsonfile); 
		Gson gson = new Gson();
		DEVProject devProject= gson.fromJson(json, DEVProject.class);	
		ModelCollector collector = new ModelCollector(devProject);
		if(devProject != null && devProject.getOwnedElements() != null)
		{
			collector.collectPacks();
			
			for(DEVPackage devPackage:devProject.getPackages())
			{
				devPackage.normalizeTypes();
				ServiceDecorator dec = new ServiceDecorator(devPackage);
				dec.addContract();
				devProject.addWebApi(dec.genApiPack());
			}

			for(DEVPackage devPackage:devProject.getApiPacks())
			{
				devPackage.normalizeTypes();
			}
			
			
			
		}
		
		return devProject;
		 
	}
	

	public void addWebApi(DEVPackage pack)
	{
		webApis.add(pack);
	}
	
	
	public void addModel(DEVPackage pack)
	{
		packages.add(pack);
	}
	
	public List<DEVPackage> getPackages()
	{
		return packages;
	}

	public List<DEVPackage> getApiPacks()
	{
		return webApis;
	}
	
	
	
/*
	public void createPrototype()
	{
		for(DEVPackage val:packages){

		
		if(val.getDbConfigXml() != null)
			dbXml += val.getDbConfigXml();
		
			
		}
	}*/
	
	
	
	
	@SuppressWarnings("unchecked")
	public void exportDotNetCode()
	{

		String dbXml = "";
		String strutsCode = "";
		String createTables = "";

		String sqlMap = "";

		String csprojectContent = "";

		for(DEVPackage val:packages){

    		String[] s = val.exportDotNetCode(projectPath);
			sqlMap+=s[0];
			csprojectContent+=s[1];

    		if(val.getDbConfigXml() != null)
    			dbXml += val.getDbConfigXml();

    		createTables += val.getCreateTables();

		}
		String templatePath = "codeTemplate";
		String path = projectPath + "/" + getName() + "/";
	//	PersistEntityDotNet.genCommon(path, templatePath, sqlMap);
	//	PersistEntityDotNet.genSlnProject(path, templatePath,csprojectContent);

		
	}
	
	
	public String get_type() {
		return _type;
	}
	public void set_type(String _type) {
		this._type = _type;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DEVPackage > getOwnedElements() {
		return ownedElements;
	}
	public void setOwnedElements(List<DEVPackage > ownedElements) {
		this.ownedElements = ownedElements;
	}
}
