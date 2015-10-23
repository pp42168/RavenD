package net.eai.dev.entitycg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.eai.dev.ioUtil;
import net.eai.umlmodel.Entity;
import net.eai.umlmodel.EntityAttribute;
import net.eai.umlmodel.EntityOperation;
import net.eai.umlmodel.OperationParameter;
import net.eai.umlmodel.StarUmlObjectContainer;
import net.eai.umlmodel.UMLDependency;

public class AjaxFactory {

	private String templatePath;
	private Entity m_entity;
	private HashMap<String,Entity> depends = new HashMap<String,Entity>();
	private List<UMLDependency> ownedElements;
	private String documentation;
	private String devPackage;
	
	
	public AjaxFactory(Entity apiEntity)
	{
		m_entity= apiEntity;
		devPackage = apiEntity.getDevPackage().getPackageName();
		templatePath = m_entity.getTemplatePath();
	}
	
	

	public void genApiAjaxs(String path)
	{
		
		
		List<EntityOperation> ops = m_entity.getOperations();
		
		for(EntityOperation oneOp:ops)
		{
			if(oneOp.getName().startsWith("manage"))
			{
				String entityName = oneOp.getName().replace("manage", "");
				Entity entityObj = (Entity) StarUmlObjectContainer.getObjectByName(entityName) ;
				if(entityObj != null)
				{					

					String code = ioUtil.readFile(templatePath + "/jsTemplates/entityAjax.js");

					code = initEntityForm(entityObj,code);
					
					code = genEntityListAjax(entityObj,code);
					

					String packPath = devPackage.substring(devPackage.lastIndexOf(".") +1);
					ioUtil.writeFileWithExistCode(path + "/../WebContent/" + packPath + "/" + entityName + "Api.js",code);
					
				}				
			}
			else
			{
				
			}
		}
		
	}
	
	
	private String genEntityListAjax(Entity theEntity,String templateCode)
	{
		
		
		//first para is the Entity name
		
		String EntityName = theEntity.getName();
		String pageSize = "20";
		
		String code = templateCode;
		
			
		//get all the attributes
		boolean hasDoc = false;
		String tableHead = "";
		String operations = "";
		String tableData = "";
		
		String owner = "";
		String ownerVari = "";

		LinkedHashMap<String,String> renderChooseFuncs = new LinkedHashMap<String,String> ();
		LinkedHashMap<String,String> renderDictionaries = new LinkedHashMap<String,String> ();
		String renderChoose = "";
		String onsearch = "";
		
		for(EntityAttribute val : theEntity.getAttributes())
		{ 	
			String attName = val.getName();
			String attType = val.getTypeStr();
			
			
			if(attName.contains("."))
				continue;

			String dictionary = val.getExtraAttribute("字典");	
			
			
					
    		if(val.getExtraAttribute("显示名") != null/*!"n/a".equals(val.getExtraAttribute("显示名"))*/)
    		{    

    			//table data response rendering
    			String showName = attName;
    			if(val.getExtraAttribute("显示名") != null)
    				showName = val.getExtraAttribute("显示名");
    			

    			if(val.getExtraAttribute("列表显示") != null && val.getExtraAttribute("列表显示").equals("否"))
    				continue;
    			
    			hasDoc = true;
    			tableHead += "<th>" + showName + "</th>";

    			Entity typeEntity = theEntity.getDepends(attType);
    			if(typeEntity == null){
    				if(attType.equals("Date"))
    					tableData += "\t\t\t\ttableData += \"<td>\" + responseData[one]." + val.getName() +".substr(0,10) + \"</td>\";\r\n";
    				else
    					tableData += "\t\t\t\ttableData += \"<td>\" + responseData[one]." + val.getName() +" + \"</td>\";\r\n";
    			}
    			else
    				tableData += "\t\t\t\ttableData += \"<td>\" + responseData[one]." + val.getName() +"Name + \"</td>\";\r\n";
    			
    		}		
    		
    		

			Entity typeEntity = theEntity.getDepends(attType);
			String searchLabel = val.getExtraAttribute("搜索");
			
			if(searchLabel != null && typeEntity != null){
				String rederKey;
    			LinkedHashMap<String,String> renderMap;    			
    			if(dictionary == null){
    				rederKey = typeEntity.getName();
    				renderMap = renderChooseFuncs;
    			}
    			else{
    				rederKey = dictionary;
    				renderMap = renderDictionaries;
    			}
    			
    			if(!renderMap.containsKey(rederKey))
    				renderMap.put(rederKey, "page-searchCard-" + attName);
    			else{
    				String value = renderMap.get(rederKey);
    				value += " " + "page-searchCard-" + attName;
    				renderMap.put(rederKey,value);
    			}        			
    			onsearch += "\r\n\tcondition += \"&" + attName + "=\" + encodeURIComponent($(\"#page-searchCard-" + attName + "\").val());";
    		}
			else if(searchLabel != null)
			{
				onsearch += "\r\n\tcondition += \"&" + attName + "=\" + encodeURIComponent($(\"#page-searchCard-" + attName + "\").val());";
			}
			
		}
		
		
		// operations of the entities
		for(EntityOperation val : theEntity.getOperations())
		{ 
			String showName = val.getName();
			if(val.getExtraAttribute("显示名") != null/*!"n/a".equals(val.getExtraAttribute("显示名"))*/)
    		{    
    			showName = val.getExtraAttribute("显示名");
    			
    			if(val.getName().startsWith("listOwned"))
    			{	
    				String entityName = val.getName().substring("listOwned".length());
    				operations += " | ";
    				operations += "<a href='" + EntityName + "Owned" + entityName + "Page.html?" + EntityName + "ID=\" + responseData[one].id + \"'> " + showName + "</a>" ;
    			}
    			else
    			{
    				operations += " | ";	
        			operations += "<a onclick=\'" + val.getName() + "(responseData[one].id)' href='javascript:void(0);'> " + showName + "</a>" ;
        		}    			
    		}
		}
		
		
		//render choose
		String renderChooseFuncCode = "";
		Iterator<Entry<String, String>> renderChooseIter = renderChooseFuncs.entrySet().iterator();
		while (renderChooseIter.hasNext()) {
			Entry<String, String> entry =  renderChooseIter.next();   
			String key = entry.getKey();
			String val = entry.getValue();
			
			String[] locations = val.split(" ");
			
			String rederCode = "";
			for(String oneLoc:locations)
			{
				rederCode += "\r\n\t\t\t$(\"#\" + location + \"" + oneLoc + "\").children().remove();";
				rederCode += "\r\n\t\t\tchooseData = \"<option value=''>全部</option>\" + chooseData;";	
				rederCode += "\r\n\t\t\t$(\"#\" + location + \"" + oneLoc + "\").append(chooseData);";			
				rederCode += "\r\n\t\t\tchooseSelect(\"#" + oneLoc + "\",$(\"#" + oneLoc + "-ID\").val());";
				rederCode += "\r\n\t\t\t$(\"#\" + location + \"" + oneLoc + "\").chosen(); ";
			}
			
			String oneRederChooseFunc = ioUtil.readFile(templatePath + "/jsTemplates/renderChoose.js");
			oneRederChooseFunc = oneRederChooseFunc.replace("@Entity@", key);		
			oneRederChooseFunc = oneRederChooseFunc.replace("@rederCode@", rederCode);			
			
			renderChoose += "\r\n\trenderChoose" + key + "();";
			
			renderChooseFuncCode += oneRederChooseFunc;
		}
		
		//render dictionary
		Iterator<Entry<String, String>> renderDicIter = renderDictionaries.entrySet().iterator();
		while (renderDicIter.hasNext()) {
			Entry<String, String> entry =  renderDicIter.next();   
			String key = entry.getKey();
			String val = entry.getValue();	
			
			renderChoose += "\r\n\trenderDicChoose(\"" + val + "\",\"" + key + "\",\"全部\");";
		}
		

		String entityShowName = EntityName;
		if(theEntity.getExtraAttribute("显示名") != null)
			entityShowName = theEntity.getExtraAttribute("显示名");
	
		String renderJs = "";
		
		String onSearchFunc = "";
		if(onsearch != "")
		{
			onSearchFunc = ioUtil.readFile(templatePath + "/jsTemplates/onSearch.js");
			onSearchFunc = onSearchFunc.replace("@condition@", onsearch);
			onSearchFunc = onSearchFunc.replace("@Entity@", EntityName);			
			
		}
		
		renderJs = code;// ioUtil.readFile(templatePath + "/jsTemplates/initEntityList.js");
		renderJs = renderJs.replace("@Entity@", EntityName);			
		renderJs = renderJs.replace("@onSearch@",  onSearchFunc);			
		renderJs = renderJs.replace("@EntityName@", entityShowName);
		//renderJs = renderJs.replace("@Location@",parentID + "-" + loacation);
		renderJs = renderJs.replace("@thead@",tableHead);		
		renderJs = renderJs.replace("@Columns@", tableData);	
		renderJs = renderJs.replace("@pageSize@", pageSize);	
		renderJs = renderJs.replace("@operation@", operations);	
	//	renderJs = renderJs.replace("@renderChooseFuncs@", renderChooseFuncCode);
		renderJs = renderJs.replace("@renderChoose@", renderChoose);
	
		code = renderJs;
			
		return code;
	}
	
	
	
	


	private LinkedHashMap<String,Entity> getDepends(Entity theEntity)
	{
		LinkedHashMap<String,Entity> dependEntities = new LinkedHashMap<String,Entity>() ;
		if(theEntity.getOwnedElements() != null)
		{
			for(UMLDependency depend:theEntity.getOwnedElements())
			{
				String targetID = depend.getTarget().get$ref();
				Entity entity = (Entity) StarUmlObjectContainer.getObject(targetID) ;
				if(entity != null)
				{
					dependEntities.put(entity.getName(), entity);
				}
			}
		}

		HashMap<String,Entity> depends = m_entity.getDepends();

		Iterator<Entry<String, Entity>> entityIter = depends.entrySet().iterator();
		while (entityIter.hasNext()) {
			Entry<String, Entity> entry =  entityIter.next();      
			
    		String key = entry.getKey();
    		Entity val = entry.getValue();
    		dependEntities.put(key, val);
    		
		}
		
		return dependEntities;
	}
	

	private Entity findEntity(String targetEntityName)
	{
		LinkedHashMap<String,Entity> dependEntities = getDepends(m_entity);
		Iterator<Entry<String, Entity>> ApiIter = dependEntities.entrySet().iterator();
		while (ApiIter.hasNext()) {
			Entry<String, Entity> entry =  ApiIter.next();      
			
    		String apiName = entry.getKey();
    		Entity apiObject = entry.getValue();


    		if(apiName.equals(targetEntityName))
    			return apiObject;
    		/*
    		if("Api".equals(apiObject.getStereotype()))
    		{

    			LinkedHashMap<String,Entity> apisDepends = getDepends(apiObject);
    			Iterator<Entry<String, Entity>> entityIter = apisDepends.entrySet().iterator();
    			while (entityIter.hasNext()) {
    				Entry<String, Entity> entityEntry =  entityIter.next();      
    				
    	    		String entityName = entityEntry.getKey();
    	    		Entity entityObject = entityEntry.getValue();
    	    		
    	    		if(entityName.equals(targetEntityName))
    	    			return entityObject;
    			}
    		}*/
		}
		
		return null;
		
	}
	


	private void genOnChooseFun(String chooseEntity,EntityAttribute val,LinkedHashMap<String,String> choosens)
	{
		String attName = val.getName();
		
		String theExtendedAtt = attName.substring(attName.indexOf("."),attName.length());
		
		String complexName = attName.replace(".","-");
		
		if(!choosens.containsKey(attName + ":" + chooseEntity))
		{
			choosens.put(attName + ":" + chooseEntity, "");
		}
		
		String oneCode = choosens.get(attName + ":" + chooseEntity);		
		oneCode += "\r\n\t\t\t$(\"#\" + location + \"" + complexName + "\").val(data.data" + theExtendedAtt + "Name);";
		
		choosens.put(attName + ":" + chooseEntity, oneCode);
		
	}
	


	public String initEntityForm(Entity theEntity,String code)
	{
		String rules = "";
		String messages = "";		
		
		
		//first para is the Entity name
		
		//get all the attributes
		boolean hasDoc = false;
		String DivSetting = "";
		String renderChoose = "";
		String chooseNameSetting = "";
		LinkedHashMap<String,String> onChooseFuns = new LinkedHashMap<String,String> ();

		LinkedHashMap<String,String> renderChooseFuncs = new LinkedHashMap<String,String> ();
		LinkedHashMap<String,String> renderDictionaries = new LinkedHashMap<String,String> ();
		
		String owner = null;
		String ownerVari = null;
		for(EntityAttribute val : theEntity.getAttributes())
		{ 	
			String attName = val.getName();
			String attType = val.getTypeStr();
			
			if(val.getExtraAttribute("聚集类") != null)
			{
				owner = val.getExtraAttribute("聚集类");
				ownerVari = attName;
			}
			
			if(val.getExtraAttribute("显示名") != null/*!"n/a".equals(val.getExtraAttribute("显示名"))*/)
    		{   
				String showName = attName;
				
					
				if(val.getExtraAttribute("显示名") != null)
				{
					showName = val.getExtraAttribute("显示名");
				}
    			hasDoc = true;
    			Entity typeEntity = theEntity.getDepends(attType);
    			
    			String dictionary = val.getExtraAttribute("字典");
    			
    			if(typeEntity == null && dictionary == null){
    				if(!attName.contains(".")){
    					if(attType.equals("Date"))
    						DivSetting += "\r\n\t\t\t$(\"#\" + location + \"-" + attName + "\").val(data.data." + attName + ".substr(0,10));";
    					else
            				DivSetting += "\r\n\t\t\t$(\"#\" + location + \"-" + attName + "\").val(data.data." + attName + ");";
    						
    				}
        			if("id".equals(attName.toLowerCase()))
        				DivSetting += "\r\n\t\t\t$(\"#\" + location + \"-" + attName + "\").attr(\"disabled\",\"disabled\");";
    			}
    			else{
        			DivSetting += "\r\n\t\t\t$(\"#\" + location + \"-" + attName + "\").val(data.data." + attName + ");";
        			DivSetting += "\r\n\t\t\t$(\"#\" + location + \"-" + attName + "-name\").val(data.data." + attName + "Name);";
        			
        			
        			if(val.getMultiplicity() != null && !val.getMultiplicity().equals(""))
        			{
                		DivSetting += "\r\n\t\t\t$(\"#\" + location + \"-" + attName + "-ID\").val(data.data." + attName + ");";
            			DivSetting += "\r\n\t\t\tchooseSelect(\"#\" + location + \"-" + attName + "\",data.data." + attName + ");";        				
        			}
        			else
        			{
                		DivSetting += "\r\n\t\t\t$(\"#\" + location + \"-" + attName + "-ID\").val(data.data." + attName + "ID);";
            			DivSetting += "\r\n\t\t\tchooseSelect(\"#\" + location + \"-" + attName + "\",data.data." + attName + "ID);";    
        				
        			}
        			
        			String rederKey;
        			LinkedHashMap<String,String> renderMap;
        			
        			if(dictionary == null){
        				rederKey = typeEntity.getName();
        				renderMap = renderChooseFuncs;
        				DivSetting += "\r\n\t\t\trenderChoose" + attType + "(location);";
            			
        				
        			}
        			else{
        				rederKey = dictionary;
        				renderMap = renderDictionaries;
        				DivSetting += "\r\n\t\t\trenderDicChoose(location + \"-" + attName + "\",\"" + dictionary + "\");";
            			
        			}
        			
        			if(!renderMap.containsKey(rederKey))
        			{            			
        				renderMap.put(rederKey,attName);
        			}
        			else
        			{
        				String value = renderMap.get(rederKey);
        				value += " " + attName;
        				renderMap.put(rederKey,value);
        			}

        			chooseNameSetting += "\r\n\t\t\t$(\"#\" + location + \"-" + attName + "-ID\").val($(\"#\" + location + \"-" + attName + "\").val());";
        			chooseNameSetting += "\r\n\t\t\t$(\"#\" + location + \"-" + attName + "-name\").val($(\"#\" + location + \"-" + attName + "\").find(\"option:selected\").text());";
                	
        			
    			}
    			
    			if(attName.contains("."))
				{
					String theChoosen = attName.substring(0,attName.indexOf("."));
					EntityAttribute theAtt = theEntity.findAttributeByName(theChoosen);					
					genOnChooseFun(theAtt.getTypeStr(),val,onChooseFuns);
					continue;
				}
    			
    			if(!rules.isEmpty()){
    				rules += ",";
    				messages += ",";
    			}
				
				rules += "\r\n\t\t\t" + attName + ": \"required\"";				
    			messages += "\r\n\t\t\t" + attName + ": \"请输入" + showName + "\"";
    		}
		}
		


		String renderJs = code;
		/*
		if(owner != null){
			renderJs = ioUtil.readFile(templatePath + "/jsTemplates/initOwnedEntityForm.js");
			renderJs = renderJs.replace("@ownerEntity@", owner);
			renderJs = renderJs.replace("@ownerVari@", ownerVari);
		}
		else
			renderJs = ioUtil.readFile(templatePath + "/jsTemplates/initEntityForm.js");*/
			
		
		


		//render choose
		String renderChooseFuncCode = "";
		Iterator<Entry<String, String>> renderChooseIter = renderChooseFuncs.entrySet().iterator();
		while (renderChooseIter.hasNext()) {
			Entry<String, String> entry =  renderChooseIter.next();   
			String key = entry.getKey();
			String val = entry.getValue();
			
			String[] locations = val.split(" ");
			
			String rederCode = "";
			for(String oneLoc:locations)
			{
				rederCode += "\r\n\t\t\t\t$(\"#\" + location + \"-" + oneLoc + "\").children().remove();";
				rederCode += "\r\n\t\t\t\t$(\"#\" + location + \"-" + oneLoc + "\").append(chooseData);";
				rederCode += "\r\n\t\t\t\tchooseSelect(\"#\" + location + \"-" + oneLoc + "\",$(\"#\" + location + \"-" + oneLoc + "-ID\").val());";	
				   			
    			
				rederCode += "\r\n\t\t\t\t$(\"#\" + location + \"-" + oneLoc + "\").chosen(); ";
			}
			
			String oneRederChooseFunc = ioUtil.readFile(templatePath + "/jsTemplates/renderChoose.js");
			oneRederChooseFunc = oneRederChooseFunc.replace("@Entity@", key);		
			oneRederChooseFunc = oneRederChooseFunc.replace("@rederCode@", rederCode);			
			
			renderChoose += "\r\n\trenderChoose" + key + "();";
			
			renderChooseFuncCode += oneRederChooseFunc;
		}
		
		//render dictionary
		Iterator<Entry<String, String>> renderDicIter = renderDictionaries.entrySet().iterator();
		while (renderDicIter.hasNext()) {
			Entry<String, String> entry =  renderDicIter.next();   
			String key = entry.getKey();
			String val = entry.getValue();	
			
			renderChoose += "\r\n\trenderDicChoose(\"" + val + "\",\"" + key + "\");";
		}
		
		
		
		
		
		renderJs = renderJs.replace("@renderChooseFuncs@", renderChooseFuncCode);

		renderJs = renderJs.replace("@Entity@", theEntity.getName());
		renderJs = renderJs.replace("@DivSetting@",DivSetting);	
		renderJs = renderJs.replace("@rules@", rules);
		renderJs = renderJs.replace("@chooseNameSetting@", chooseNameSetting);
		renderJs = renderJs.replace("@messages@", messages);
		

		
		Iterator<Entry<String, String>> onChooseIter = onChooseFuns.entrySet().iterator();
		while (onChooseIter.hasNext()) {
			Entry<String, String> entry =  onChooseIter.next();   
			String choose = entry.getKey();
			String rederCode = entry.getValue();

			String theChoosen = choose.substring(0,choose.indexOf("."));
			String theChooseEntity = choose.substring(choose.indexOf(":") + 1 ,choose.length());
			
			

			String onChooseJs = ioUtil.readFile(templatePath + "/jsTemplates/onChooseEntity.js");
			onChooseJs = onChooseJs.replace("@Choose@", theChoosen);
			onChooseJs = onChooseJs.replace("@Entity@", theChooseEntity);
			onChooseJs = onChooseJs.replace("@DivSetting@", rederCode);
			
			renderJs += onChooseJs;
			
			renderChoose += "\r\n\t\t\t$(\"#page-" + theChoosen + "\").attr(\"onchange\",\"onChoose" + theChoosen + "(this.value)\");";
		}
		

		renderJs = renderJs.replace("@renderChoose@", renderChoose);
			
		return renderJs;
	}
	
	
	
	private LinkedHashMap<String,String> getEleAtts(Entity ele)
	{
		LinkedHashMap<String,String> atts = new LinkedHashMap<String,String>();
		
		for(EntityAttribute val : ele.getAttributes())
		{ 
			atts.put(val.getName(), val.getDefaultValue());
		}
		
		return atts;
	}
	
	
} 
