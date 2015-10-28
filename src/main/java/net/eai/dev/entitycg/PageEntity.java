package net.eai.dev.entitycg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.eai.dev.*;
import net.eai.umlmodel.Entity;
import net.eai.umlmodel.EntityAttribute;
import net.eai.umlmodel.EntityOperation;
import net.eai.umlmodel.OperationParameter;
import net.eai.umlmodel.StarUmlObjectContainer;
import net.eai.umlmodel.UMLDependency;
import net.eai.util.NameConverter;

public class PageEntity {


	private String templatePath;
	private Entity m_entity;
	private List<UMLDependency> ownedElements;
	private String documentation;
	private String devPackage;
	
	private String jsOperations = "";

	public PageEntity (Entity entity)
	{
		templatePath = entity.getTemplatePath();
		m_entity = entity;
		setDocumentation(entity.getDocumentation());
		setOwnedElements(entity.getOwnedElements());
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
	
	private String applyAttributes(String code)
	{
		Iterator<Entry<String, String>> entityIter = m_entity.getExtraAttributes().entrySet().iterator();
		while (entityIter.hasNext()) {
			Entry<String, String> entry =  entityIter.next();      
			
    		String key = entry.getKey();
    		String val = entry.getValue();
    		code = code.replace("@key@", val);    		
		}		
		return code;
	}
	
	private String applyEleExtraTags(EntityAttribute att, String code)
	{
		String extraTags = att.getExtraAttribute("extratag");
		if(extraTags == null)
		{
			extraTags = "";
		}
		
		code = code.replace("@extratag@", extraTags);
		return code;
	}
	
	private String importedEntityApis(LinkedHashMap<String,Entity> dependEntities,ArrayList<Entity> importedEntityes)
	{
		String code = "";


		String renderChooseFuncCode = "";
		Iterator<Entry<String, Entity>> renderChooseIter = dependEntities.entrySet().iterator();
		while (renderChooseIter.hasNext()) {
			Entry<String, Entity> entry =  renderChooseIter.next();   
			Entity val = entry.getValue();
			
			if(importedEntityes.contains(val))
				continue;
			
			importedEntityes.add(val);
			if("Entity".equals(val.getStereotype()))
				code += "\r\n\t<script src=\"" + val.getName() + "Api.js\"></script>";
			
			
			LinkedHashMap<String,Entity> subDepends =  getDepends(val);
			if(subDepends != null )
				code += importedEntityApis(subDepends,importedEntityes);
		}
		return code;
	}
	
	public String genCode(String parentID)
	{
		return  genCode(parentID,this);
		
	}
	
	
	public String genCode(String parentID,PageEntity page)
	{
		String htmlTemplateName = m_entity.getStereotype().substring(4);
		if(htmlTemplateName.equals(""))
			htmlTemplateName = "hplusEmptyPage";
			
		//  depends
		LinkedHashMap<String,Entity> dependEntities = getDepends(m_entity);
		
		
		String code = "";
		if(m_entity.getStereotype().startsWith("Html"))
			code = ioUtil.readFile(templatePath + "/htmlPages/" + htmlTemplateName + ".html");
		else 
			code = ioUtil.readFile(templatePath + "/htmlElements/" + htmlTemplateName + ".html");

		
		String headerCode = ioUtil.readFile(templatePath + "/htmlElements/header.html");
		ArrayList<Entity> importedEntityes = new ArrayList<Entity> ();
		code = code.replace("@EntityAjaxImports@", importedEntityApis(dependEntities,importedEntityes));
		code = code.replace("@eleID@", parentID);
		code = code.replace("@Header@", headerCode);
		
		HashMap<String,String> extraAttributes = m_entity.getExtraAttributes();
		Iterator<Entry<String, String>> extraAttributesIter = extraAttributes.entrySet().iterator();
		while (extraAttributesIter.hasNext()) {
			Entry<String, String> entry =  extraAttributesIter.next();   
			String key = entry.getKey();
			String val = entry.getValue();		
			
			if(key.startsWith("@"))
				code = code.replace(key, val);
		}
		
		
		
		String content = "";
		List<EntityAttribute> atts = m_entity.getAttributes();
		boolean hasRow = false;
		for(EntityAttribute oneAtt:atts)
		{
			String type = oneAtt.getTypeStr();
			String attName = oneAtt.getName();
			if(type.equals("String"))
			{
				// 变量赋值，以及一些固定的排版符号
				if(oneAtt.getName().startsWith("row")){
					if(hasRow)
						content += "\r\n</div>\r\n";
					content += "\r\n<div class=\"" + oneAtt.getName() + "\">";
					hasRow = true;
				}
				else if(oneAtt.getName().equals("rowend")){
					content += "\r\n</div>\r\n";
					hasRow = false;
				}
				else if(oneAtt.getDefaultValue() != null){
					code = code.replace("@" + attName + "@", oneAtt.getDefaultValue());
				}
			}
			else if(dependEntities.containsKey(type))
			{
				// 依赖的object
				PageEntity pageEle = new PageEntity(dependEntities.get(type));
				pageEle.setTemplatePath(this.templatePath);
				String eleCode = pageEle.genCode(parentID + "-" + oneAtt.getName(),page);
				
				eleCode = eleCode.replace("@name@", oneAtt.getName());
				eleCode = this.applyAttributes(eleCode);
				if(hasRow){
					String col = "";
					if(oneAtt.getMultiplicity() == null)
						col = "12";
					else
						col = oneAtt.getMultiplicity();		
					/*
					if(code.contains("@" + oneAtt.getName() + "@"))
					{
						code = code.replace("@" + oneAtt.getName() + "@", 
								"\r\n<div class=\"col-md-" + col + "\">\r\n" + eleCode + "</div>"
									);	
					}
					else*/
						content += "\r\n<div class=\"col-md-" + col + "\">\r\n" + eleCode + "</div>";
				}
				else{
					/*if(code.contains("@" + oneAtt.getName() + "@"))
					{
						code = code.replace("@" + oneAtt.getName() + "@", eleCode);
					}
					else*/
						content +=  eleCode;
				}
			}
			else{
				// 默认的entity 模板
				String eleCode = ioUtil.readFile(templatePath + "/htmlElements/" + type + ".html");
				eleCode = eleCode.replace("@eleID@", parentID + "-" + oneAtt.getName());
				eleCode = this.applyAttributes(eleCode);
				eleCode = eleCode.replace("@name@", oneAtt.getName());
				

				HashMap<String,String> attExtraAttributes = oneAtt.getExtraAttributes();
				Iterator<Entry<String, String>> attExtraAttributesIter = attExtraAttributes.entrySet().iterator();
				while (attExtraAttributesIter.hasNext()) {
					Entry<String, String> entry =  attExtraAttributesIter.next();   
					String key = entry.getKey();
					String val = entry.getValue();		
					
					if(key.startsWith("@"))
						eleCode = eleCode.replace(key, val);
				}
				
				
				
			//	if(code.contains("@" + oneAtt.getName() + "@"))
			//		code = code.replace("@" + oneAtt.getName() + "@", eleCode);
				//else
			//	{

					if(hasRow){
						String col = "";
						if(oneAtt.getMultiplicity() == null)
							col = "12";
						else
							col = oneAtt.getMultiplicity();		
						
						content += "\r\n<div class=\"col-" + col + "\">\r\n" + eleCode + "</div>";
					}
					else
						content +=  eleCode;
			//	}
				
			}
			
			code = applyEleExtraTags(oneAtt,code);
			
		}
		
		if(hasRow == true)
			content += "\r\n</div>\r\n";

		code = code.replace("@content@", content);
		
		String operations = genOperations(parentID,dependEntities,page);
		
		page.setJsOperations(page.getJsOperations() + operations);

		if(m_entity.getStereotype().startsWith("Html"))
			code = code.replace("@jsOperations@", jsOperations);
		
		
		return code;
	}
	

	private String initN2NJs(EntityOperation oneOp,String parentID,
			LinkedHashMap<String,Entity> dependEntities)
	{
		List<OperationParameter> parameters = oneOp.getParameters();
		
		//first para is the Entity name

		String ownerEntityStr = parameters.get(0).getName();
		String itemStr = parameters.get(1).getName();
		String candinateStr = parameters.get(2).getName();

		Entity itemEntity = findEntity(itemStr);
		Entity candinateEntity = findEntity(candinateStr);
		
		String loacation = "table";
		String pageSize = "20";
		String trigger = "init";
		
		
		
		
			
		//get all the attributes
		boolean hasDoc = false;
		String tableHead = "";
		String tableData = "";
		
		for(EntityAttribute val : candinateEntity.getAttributes())
		{ 	
			String attName = val.getName();
			String attType = val.getTypeStr();
			
    		if(!"n/a".equals(val.getExtraAttribute("显示名")))
    		{    

    			String showName = attName;
    			if(val.getExtraAttribute("显示名") != null)
    				showName = val.getExtraAttribute("显示名");
    			
    			hasDoc = true;
    			tableHead += "<th>" + showName + "</th>";

    			Entity typeEntity = candinateEntity.getDepends(attType);
    			if(typeEntity == null)
    				tableData += "\t\t\t\ttableData += \"<td>\" + responseData[one]." + val.getName() +" + \"</td>\";\r\n";
    			else
    				tableData += "\t\t\t\ttableData += \"<td>\" + responseData[one]." + val.getName() +"Name + \"</td>\";\r\n";
    		}			
		}
		

		String renderJs = ioUtil.readFile(templatePath + "/jsTemplates/N2NEdit.js");

		renderJs = renderJs.replace("@CandinateColumns@", tableData);	
		String selectedTableData = "\t\t\t\ttableData += \"<td>\" + responseData[one]." 
									+ NameConverter.toLowerStartName(ownerEntityStr) +"Name + \"</td>\";\r\n";		
		renderJs = renderJs.replace("@SelectedColumns@", selectedTableData);
		
		renderJs = renderJs.replace("@Location@",parentID + "-" + loacation);
		renderJs = renderJs.replace("@CandinateThead@",tableHead);		
		renderJs = renderJs.replace("@pageSize@", pageSize);	
			
		
		return renderJs;
	}
		
	private String genOwnerRenderCode(String owner,Entity targetEntity)
	{
		String code = "";

	//	String template =  "data += \"<label class=\\\"col-sm-6\\\"> \" + data.data.xxx + " </label>"";
		Entity ownerEntity = targetEntity.getDepends(owner);
		if(ownerEntity != null)
		{
			for(EntityAttribute val : ownerEntity.getAttributes())
			{ 	
				String attName = val.getName();
				String attType = val.getTypeStr();
				
				if(attName.contains("."))
					continue;
				
				if(val.getExtraAttribute("显示名") != null)
	    		{    
	    			String showName = attName;
	    			if(val.getExtraAttribute("显示名") != null)
	    				showName = val.getExtraAttribute("显示名");
	    			
	    			
	    			if(ownerEntity.getDepends(attType) != null)
		    			code += "\r\n\t\t\tdivdata += \"<label class=\\\"col-sm-6\\\"> " + showName + ": \" + data.data."
		    	    			+ attName + "Name+ \"</label>\"";
	    			else
	    				code += "\r\n\t\t\tdivdata += \"<label class=\\\"col-sm-6\\\"> " + showName + ": \" + data.data."
	    			+ attName + "+ \"</label>\"";
	    		}
			}
		}
		
		return code;
	}
	

	
	
	private String initEntityList(EntityOperation oneOp,String parentID,
			LinkedHashMap<String,Entity> dependEntities)
	{
		List<OperationParameter> parameters = oneOp.getParameters();
		
		//first para is the Entity name
		
		String EntityName = parameters.get(0).getName();
		Entity theEntity = findEntity(EntityName);

		String owner = "";
		String ownerVari = "";

		if(parameters.size() > 1)
			owner = parameters.get(1).getName();
		if(parameters.size() > 2)
			ownerVari = parameters.get(2).getName();
		
		
		
			
		//get all the attributes
		boolean hasDoc = false;
		String tableHead = "";
		String operations = "";
		String tableData = "";
		
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
    			
    			
    			// rendering condition choose	
    			if(val.getExtraAttribute("搜索") != null)
    			{
    				if(dictionary == null && typeEntity !=null)
    					renderChoose += "\r\n\trenderChoose" + attType + "(\"page-searchCard\",\"全部\");";
    				else if(dictionary != null)
    					renderChoose += "\r\n\trenderDicChoose(\"page-searchCard-" + attName + "\",\"" + dictionary + "\",\"全部\");";
    			}

    			
            	
    		}			
		}
		
		/*
		//render choose
		String renderChooseFuncCode = "";
		Iterator<Entry<String, String>> renderChooseIter = renderChooseFuncs.entrySet().iterator();
		while (renderChooseIter.hasNext()) {
			Entry<String, String> entry =  renderChooseIter.next();   
			String key = entry.getKey();
			String val = entry.getValue();				
			
			renderChoose += "\r\n\trenderChoose" + key + "(\"page-searchCard\",\"全部\");";
			
		}
		
		//render dictionary
		Iterator<Entry<String, String>> renderDicIter = renderDictionaries.entrySet().iterator();
		while (renderDicIter.hasNext()) {
			Entry<String, String> entry =  renderDicIter.next();   
			String key = entry.getKey();
			String val = entry.getValue();	
			
			renderChoose += "\r\n\trenderDicChoose(\"" + val + "\",\"" + key + "\",\"全部\");";
		}
		*/

		String entityShowName = EntityName;
		if(theEntity.getExtraAttribute("显示名") != null)
			entityShowName = theEntity.getExtraAttribute("显示名");
	
		String renderJs ;
		
		String onSearchFunc = "";
		if(onsearch != "")
		{
			onSearchFunc = ioUtil.readFile(templatePath + "/jsTemplates/onSearch.js");
			onSearchFunc = onSearchFunc.replace("@condition@", onsearch);
			onSearchFunc = onSearchFunc.replace("@Entity@", EntityName);			
			
		}
		
		if(owner.equals(""))
		{
			renderJs = ioUtil.readFile(templatePath + "/jsTemplates/initEntityList.js");
			renderJs = renderJs.replace("@Entity@", EntityName);	
			renderJs = renderJs.replace("@renderChoose@", renderChoose);
		}
		else
		{

			renderJs = ioUtil.readFile(templatePath + "/jsTemplates/initOwnedEntityList.js");
			renderJs = renderJs.replace("@Entity@", EntityName);
			String ownercode = genOwnerRenderCode(owner,theEntity);
			renderJs = renderJs.replace("@renderOwnerInfo@", ownercode);
			renderJs = renderJs.replace("@ownerEntity@", owner);
			renderJs = renderJs.replace("@ownerVari@", ownerVari);
			renderJs = renderJs.replace("@renderChoose@", renderChoose);
			
		}

		
			
		
		return renderJs;
	}
		
	private String genOperations(String parentID,LinkedHashMap<String,Entity> dependEntities,PageEntity page)
	{
		String code = "";	
		
		List<EntityOperation> ops = m_entity.getOperations();
		for(EntityOperation oneOp:ops)
		{

			//获取注释
			String commentCode = "";
			if(oneOp.getDocumentation() != null)
			{
				String lines[] = oneOp.getDocumentation().split("\n");
				for(String line:lines)
				{
					commentCode += "\t\t\t//" + line + "\n";
				}			
			}

			// 各种 特殊 operations 的生成
			if(oneOp.getName().equals("initEntityList"))
			{
				code += initEntityList(oneOp,parentID,dependEntities);
			}
			else if(oneOp.getName().equals("initEntityForm"))
			{
			}
			else if(oneOp.getName().equals("initN2NJs"))
			{
				initN2NJs(oneOp,parentID,dependEntities);
			}
		}
		
		
		
		return code;
		 
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
	
	
	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public List<UMLDependency> getOwnedElements() {
		return ownedElements;
	}

	public void setOwnedElements(List<UMLDependency> ownedElements) {
		this.ownedElements = ownedElements;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public String getDevPackage() {
		return devPackage;
	}

	public void setDevPackage(String devPackage) {
		this.devPackage = devPackage;
	}

	public String getJsOperations() {
		return jsOperations;
	}

	public void setJsOperations(String jsOperations) {
		this.jsOperations = jsOperations;
	}

}
