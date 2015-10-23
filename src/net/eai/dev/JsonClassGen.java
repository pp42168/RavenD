package net.eai.dev;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.eai.dev.entitycg.*;
import net.eai.umlmodel.*;
import net.eai.util.HttpGetter;

import com.google.gson.Gson;


@SuppressWarnings("rawtypes")
public class JsonClassGen {
	
	public JsonClassGen(String packageName,String templatePath,String apiClassName,String targetPath)
	{
		this.packageName = packageName;
		this.templatePath = templatePath;
		this.apiClassName = apiClassName;
		this.targetPath = targetPath;
	}
	
	private String packageName;
	private String templatePath;
	private String apiClassName;
	private String targetPath;
	
	private LinkedHashMap<String,Entity> knownClasses = new LinkedHashMap<String,Entity>();
	private LinkedHashMap<String,String> controlCommands = new LinkedHashMap<String,String>();
	private LinkedHashMap<String,String> apiFunctions = new LinkedHashMap<String,String>();
	
	public void addApi(String apiName,String url)
	{
		apiFunctions.put(apiName, url);
	}
	
	public void addControl(String target ,String command)
	{
		controlCommands.put(target, command);
	}
	
	
	public void genCode()
	{		
		String code = ioUtil.readFile(templatePath + "/ApiProxy.java"); 
		String packagePath = packageName.replace(".", "/");
		 
		String funcCode = "";
		Iterator<Entry<String, String>> iter = apiFunctions.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry =  iter.next();		
			String key = entry.getKey();
			String url = entry.getValue();
			String tempfile = key + ".js";
			String str = ioUtil.readFile(tempfile);
			
			
			Gson gson = new Gson();
		//	GetCategoryResponse response = gson.fromJson(str, GetCategoryResponse.class);
			/*if(str.isEmpty()){
				str = HttpGetter.httpGet(url, "GET", ""); 
				ioUtil.writeFile(tempfile ,str);
			}*/
			@SuppressWarnings("unused")
			String responseEntityName = genClassCode(key,str);
			//funcCode += genCodeByUrl(entry.getKey(),url,responseEntityName);
		}
		code = code.replace("@package@", packageName);
		code = code.replace("@entity@", apiClassName);
		code = code.replace("@targetFunctionCodes@", funcCode);
		
		ioUtil.writeFile(targetPath + "/" + packagePath + "/" + apiClassName + ".java",code);
	}
	
	private String genCodeByUrl(String funName,String url,String responseEntityName)
	{
		String paraStr = url.substring(url.indexOf("?") + 1);		
		String baseUrl = url.substring(0,url.indexOf("?"));
		String[] paraList = paraStr.split("&");

		String code = ioUtil.readFile(templatePath + "/ApiProxyFunc.java"); 
		
		String opParaCode = "";
		String ApiUrlParaAssignment = "";
		for(String onePara:paraList)
		{
			String[] assignment = onePara.split("=");
			if(!opParaCode.isEmpty())
				opParaCode += ",";
			
			if(!ApiUrlParaAssignment.isEmpty())
				ApiUrlParaAssignment += " + \"&";
			else
				ApiUrlParaAssignment += "\"";
				
			
			ApiUrlParaAssignment += assignment[0] + "=\" + " + assignment[0];
			opParaCode += "String " + assignment[0];
		}
		
		code = code.replace("@ApiUrl@", baseUrl);
		code = code.replace("@ApiUrlParaAssignment@", ApiUrlParaAssignment);
		code = code.replace("@ApiFuncName@", funName);
		code = code.replace("@ApiParas@", opParaCode);
		code = code.replace("@ApiResponse@", responseEntityName);
		return code;
	}
	
	public String genClassCode(String funcName,String jsonStr)
	{
		String contractPack = packageName + ".apiContract";
		String path = targetPath;
		Gson gson = new Gson();
		Object result = gson.fromJson(jsonStr, Object.class);
		String responseEntityName = funcName.toUpperCase().substring(0, 1) + funcName.substring(1,funcName.length())  + "Response";
		
		String packagePath = contractPack.replace(".", "/");
		LinkedHashMap data = (LinkedHashMap)result;
		
		Entity entity = getEntityDef(responseEntityName,data);		
		knownClasses.put(responseEntityName, entity);
		
		
		
		
		//生成契约Entity
		String ibatisMapperCode = "";		
		boolean hasEntity = false;
		Iterator<Entry<String, Entity>> entityIter = knownClasses.entrySet().iterator();
		while (entityIter.hasNext()) {
			Entry<String, Entity> entry =  entityIter.next();
		
    		Entity val = entry.getValue();
    		String code = val.genCode(path);

    		if("Entity".equals(val.getStereotype())){
    		}
    		code = "package " + contractPack + ";\r\n\r\n" + code;
    		ioUtil.writeFile(path + "/" + packagePath + "/" + val.getName() + ".java",code);
		}
		/*
		//如果有必要的话，生成DAO
		if(hasEntity == true)
		{
			String mapperFile = ioUtil.readFile(templatePath + "/DataMapper.java"); 
			mapperFile = mapperFile.replace("@content@", ibatisMapperCode);
			mapperFile = mapperFile.replace("@packageName@", contractPack);
			
			String mapperXmlFile = ioUtil.readFile(templatePath + "/DataMapper.xml"); 
			mapperXmlFile = mapperXmlFile.replace("@packageName@", contractPack);
			ioUtil.writeFile(path + "/" + packagePath + "/DataMapper.xml",mapperXmlFile);
			ioUtil.writeFile(path + "/" + packagePath + "/DataMapper.java",mapperFile);
		}
		*/
		return responseEntityName;
		
	}

	@SuppressWarnings("unchecked")
	private Entity getEntityDef(String name,LinkedHashMap data)
	{
		Entity entity = new Entity();
		
		//control command
		String command = controlCommands.get(name);
		if(command != null)
		{
			if(command.equals("setPersist"))
				entity.setStereotype("Entity");
		}
		
		
		entity.setName(name);
		entity.setTemplatePath(templatePath);
		Iterator<Entry<String, Object>> entityIter = data.entrySet().iterator();
		while (entityIter.hasNext()) {
			Entry<String, Object> entry =  entityIter.next();
		
    		String key = entry.getKey();
    		Object val = entry.getValue();

    		EntityAttribute att = getAttDef(key,val);
    		
    		att.setName(key.substring(0,1).toLowerCase()
    				+ key.substring(1));
    		entity.addAttribute(att);    
		}
		
		return entity;
	}
	
	
	private EntityAttribute getAttDef(String name,Object val)
	{
		String valType = val.getClass().toString();
		valType = valType.substring(valType.lastIndexOf(".")+1);
		
		name = name.substring(0,1).toUpperCase()
				+ name.substring(1);
		
		if(valType.equals("LinkedHashMap"))
		{
			LinkedHashMap attData = (LinkedHashMap) val;
			
			Entity newClass = getEntityDef(name,attData);	

			Entity existedClass = knownClasses.get(name);
			
			if(existedClass != null)
				newClass = existedClass;
			else
				knownClasses.put(name, newClass);
			
			EntityAttribute<Entity> att = new EntityAttribute<Entity>();
			att.setType(newClass);    
			return att;
		}
		else if(valType.equals("ArrayList"))
		{
			ArrayList listData = (ArrayList) val;
    		EntityAttribute<String> att = new EntityAttribute<String>();
    		String listedType = "";
			if(!listData.isEmpty())
			{
				if(name.endsWith("s"))
					name = name.substring(0,name.length() - 1);
				
				EntityAttribute listedAtt = getAttDef(name,listData.get(0));   	
				att.setType("List<" + listedAtt.getTypeStr() + ">");	
			}
			return att;
		}
		else{
			if(valType.equals("Double")){
				if(name.endsWith("_id") || name.endsWith("ID") )
					valType = "int";
				else if(name.endsWith("_type") || name.endsWith("Type"))
					valType = "int";
				else if(name.endsWith("_code") || name.endsWith("Code") || name.equals("code"))
					valType = "int";
					
			}
    		EntityAttribute<String> att = new EntityAttribute<String>();
    		att.setType(valType);
			return att;
		}
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getApiClassName() {
		return apiClassName;
	}

	public void setApiClassName(String apiClassName) {
		this.apiClassName = apiClassName;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	
}
