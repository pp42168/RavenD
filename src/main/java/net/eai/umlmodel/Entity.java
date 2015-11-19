package net.eai.umlmodel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.eai.dev.ioUtil;
import net.eai.dev.entitycg.*;

import com.google.gson.Gson;

public class Entity {

	private List<EntityAttribute> attributes = new ArrayList<EntityAttribute>();
	private List<EntityOperation> operations = new ArrayList<EntityOperation>();
	private List<UMLDependency> ownedElements =  new ArrayList();
	private HashMap<String,Entity> depends = new HashMap<String,Entity>();
	private List<DEVPackage> dependPacks = new ArrayList();
	
	private HashMap<String,String> extraAttributes = new HashMap<String,String>();
	
	private DEVPackage devPackage;
	private String templatePath;
	private String persistenceTable;
	private int persistenceID;
	private String name;
	private String stereotype;
	private String documentation;
	private String _id;
	private StarUmlReference _parent;
	private String _type = "UMLClass";
	
	
	private HashMap<String,OperationParameter> tempLocalVariables = new HashMap<String,OperationParameter>();
	
	public Entity()
	{
		String str = this.toString();		
		_id = str.substring(str.indexOf("@"));
	}
	
	public Entity(DEVPackage pack)
	{
		String str = this.toString();		
		_id = str.substring(str.indexOf("@"));
		set_parent(new StarUmlReference(pack.get_id()));
		pack.getOwnedElements().add(this);
	}
	
	public EntityAttribute findAttributeByName(String name)
	{
		for(EntityAttribute val : attributes)
		{ 	
    		String attName = val.getName();
    		
    		if(attName.equals(name))
    			return val;
    		
		}
		
		return null;
	}
	

	public String getOwnerAtt(String owner)
	{
		for(EntityAttribute val : attributes)
		{ 	
    		String ownerVari = val.getName();
    		String ownerExp = val.getExtraAttribute("聚集类");
    		if(owner.equals(ownerExp))
    			return ownerVari;
		}
		
		return null;
	}

	public String getExtraAttribute(String key)
	{
		return extraAttributes.get(key);
	}
	
	public void setExtraAttribute(String key,String value)
	{
		extraAttributes.put(key, value);
	}
	
	public String genAttCodes(String template)
	{
		String res = "";
		for(EntityAttribute val : attributes)
		{ 	
    		String attName = val.getName();
    		String attType = val.getTypeStr();
    		
    		String line = template;
    		String upperAttName = attName.toUpperCase().substring(0, 1) 
    					+  attName.substring(1,attName.length());
    	

    		line = line.replace("@attName@", attName);
    		line = line.replace("@upperAttName@", upperAttName);
    		line = line.replace("@attType@", attType);
    		res += line;
		}
		
		return res;
	}

	public void collectExtraAttributes()
	{
		extraAttributes.clear();
		

		for(EntityAttribute val : attributes)
		{ 	
    		val.collectExtraAttributes();
		}
		
		for(EntityOperation val : operations)
		{ 	
    		val.collectExtraAttributes();
		}
		
		
		if(this.documentation == null)
			return ;
		
		
		String docs[] = this.documentation.split("\n");
		for(String line:docs)
		{
			if(line.contains("："))
			{
				String key = line.substring(0,line.indexOf("："));
				String value = line.substring(line.indexOf("：")+1);
				
				extraAttributes.put(key, value);
			}
		}
		
	}
	
	public void addDepend(String name,Entity entity)
	{
		depends.put(name,entity);
		UMLDependency umldepend = new UMLDependency();
		umldepend.set_parent(new StarUmlReference(this._id));
		umldepend.setSource(new StarUmlReference(this._id));
		umldepend.setTarget(new StarUmlReference(entity.get_id()));
		
		ownedElements.add(umldepend);
	}
	
	public String genUmlFragment(String path)
	{
		String code = "";
		
		
		
		return code;
	}
	
	public String getImports()
	{
		String imports = "";
		// imports from depends
		if(ownedElements != null)
		{
			for(UMLDependency depend:ownedElements)
			{
				if(depend.getTarget() == null)
					continue;
				String targetID = depend.getTarget().get$ref();
				Entity entity = (Entity) StarUmlObjectContainer.getObject(targetID) ;
				if(entity != null)
				{
					if(entity.getDevPackage() != (this.getDevPackage()))
					{
						imports += "import " + entity.getDevPackage().getPackageName() + "." + entity.getName() + ";\n";
					}
				}
			}
		}
		
		
		
		//extra imports from documention column
		if(this.documentation != null)
		{
			String lines[] = this.documentation.split("\n");
			for(String line:lines)
			{
				if(line.startsWith("import"))
				{
					imports += line + "\n";
				}
			}
		}
		
		return imports;
		
	}
	
	public String genCode(String path)
	{
		String attDefines = "";
		String attGetSet = "";
		String opDefines = "";
		String imports = "";
					
		
		imports = getImports();
		boolean hasID = false;
		String createEntityParas = ""; 
		// attributes defines & setters getters
		for(EntityAttribute val : attributes)
		{ 	
    		String attName = val.getName();
    		String attType = val.getTypeStr();
    		String visibility = "public";
    		
    		if(val.getMultiplicity() != null && !"".equals(val.getMultiplicity()))
    		{ 
    			attType = "List<" + attType + ">" ;
    		}

    		if(val.getVisibility() != null)
    			visibility = val.getVisibility();
    		
    		if("id".equals(attName))
    			hasID = true;
    		else{
    			//collect for creating function
    			if(!createEntityParas.equals(""))
    					createEntityParas += ",";
    			createEntityParas += attType + " " + attName;
    		}
    		
    		if("".equals(attType) || attType == null)
    			attType = "String";
    		
    		if(attType.startsWith("LinkedBlocking") || attType.startsWith("Concurrent"))
    		{
    			imports += "import java.util.concurrent.*;\r\n";
    			attDefines += "\tprivate " + attType + " " + attName + " = new " + attType + "();\r\n";
    		}
    		else
    			attDefines += "\tprivate " + attType + " " + attName + ";\r\n";
    		
    		//attributes getter and setter
    		if(visibility.equals("public")){
    			attGetSet += "\r\n\tpublic " + attType + " get" + attName.toUpperCase().substring(0, 1) + attName.substring(1,attName.length()) + "()"
        				+"\r\n\t{\r\n\t\treturn " + attName + ";\r\n\t}\r\n";
        		
        		attGetSet += "\r\n\tpublic void set" + attName.toUpperCase().substring(0, 1) + attName.substring(1,attName.length())
        				+ "(" + attType + " " + attName + ")"
        				+"\r\n\t{\r\n\t\tthis." + attName + " = " + attName + ";\r\n\t}\r\n";
    		}
		}
		
		if(hasID == false && "Entity".equals(this.stereotype))
		{
			attDefines +=  "\tprivate int id;\r\n";
			attGetSet += "\r\n\tpublic int getID()\r\n\t{\r\n\t\treturn id;\r\n\t}\r\n";    		
    		attGetSet += "\r\n\tpublic void setID(int id)\r\n\t{\r\n\t\tthis.id = id;\r\n\t}\r\n";
		}
		

		
		// operations
		for(EntityOperation val : operations)
		{ 	
    		String ret = "void";
    		List<OperationParameter> paras = val.getParameters();
    		String paraDef = "";
    		String paraVal = "";
    		String visibility = "public";
    		
    		HashMap<String,String> parasMap = new HashMap<String,String>();
    		
    		if(val.getVisibility() != null)
    			visibility = val.getVisibility();
    		
    		//normal operations
    		if(paras != null)
    		{
    			for(OperationParameter onePara : paras)
        		{
    				String typeName = (String) onePara.getType();    				
        			if("return".equals(onePara.getDirection()))
        				ret = typeName;
        			else{
        				if(!"".equals(paraDef))
        					paraDef += ",";
        				paraDef += typeName + " " + onePara.getName();        				

        				if(!"".equals(paraVal))
        					paraVal += ",";
        				paraVal += onePara.getName();
        			} 
        		}
    		}
    		
    		
    		if(val.getVisibility() != null)
    			visibility = val.getVisibility();
    		

    		// generator operation content according to known operation name
    		String persistentEntityOperationCode = genPersistentEntityOperations(val);
    		if(!persistentEntityOperationCode.equals(""))
    		{
    			opDefines += persistentEntityOperationCode;
    		}    		
    		else{
    			String operationContent = genOperationCode(val);


    			String docs = "";
    			if(val.getDocumentation() != null)
    			{
    				String lines[] = val.getDocumentation().split("\n");
    				for(String line:lines)
    				{
    					docs += "\t\t//" + line + "\r\n";
    				}			
    			}
    			
    			opDefines += "\r\n\t" + visibility + " " + ret + " " + val.getName() + "(" + paraDef + ")"
        				+"\r\n\t{\r\n" + docs +
        				"\r\n\t\t//business logic - " + val .getName()
        				+"\r\n\t\t//logic ends"  					
        				+ "\r\n\t\t" + operationContent 
        				+"\r\n\t\t//wrap up - "  + val.getName()  
        				+"\r\n\t\t//logic ends\r\n" 
                		+"\t}\r\n";
    		}
		}
		
		String code = "";

		if("Entity".equals(stereotype))
		{
			String createTableFunc = ioUtil.readFile(templatePath + "/entityDataAccess.java"); 
			createTableFunc = createTableFunc.replace("@entity@", name);		
			opDefines += createTableFunc;
			

			String content = attDefines + "\r\n" + attGetSet  + "\r\n" +  opDefines + "\r\n";
			code = ioUtil.readFile(templatePath + "/persistentEntity.java"); 
			code = code.replace("@imports@", imports);
			code = code.replace("@entity@", name);
			code = code.replace("@content@", content);
		
			
		}
		else
		{
			String content = attDefines + "\r\n" + attGetSet  + "\r\n" +  opDefines + "\r\n";
			code = ioUtil.readFile(templatePath + "/normalEntity.java"); 
			code = code.replace("@imports@", imports);
			code = code.replace("@entity@", name);
			code = code.replace("@content@", content);
		}
		
		
		return code;
	}
	
	public Entity getDepends(String name)
	{
		return this.depends.get(name);
	}
	
	
	

	@SuppressWarnings("rawtypes")
	private String genOperationCode(EntityOperation operation)
	{
		tempLocalVariables.clear();
		String code = "";
		
		
		if(operation.getOwnedElements() !=null)
		{
			for(UMLCollaboration col : operation.getOwnedElements())
			{ 	    			
				for(UMLInteraction interaction : col.getOwnedElements())
	    		{	
					for(UMLMessage msg : interaction.getMessages())
					{
						String assign = "";
						String source = "";
						String target = "";
						String call = "";
						
						

						//call function scope
						String ref = msg.getSource().get$ref();
						UMLParticipant part = (UMLParticipant) StarUmlObjectContainer.getObject(ref);
						source = part.getName();
						if(source.equals("this"))
						{
							ref = msg.getTarget().get$ref();
							UMLParticipant targetPart = (UMLParticipant) StarUmlObjectContainer.getObject(ref);
							target = targetPart.getName();
							
							if("static".equals(target) || "".equals(target))
							{
								String targetTypeRef = targetPart.getRepresent().get$ref();
								EntityAttribute represent = (EntityAttribute) StarUmlObjectContainer.getObject(targetTypeRef);
								target = getAttributeType(represent);				
							}	
							
							if("this".equals(target))
								target = "";
							else
								target += ".";
							
							
							//call
							EntityOperation operationToCall = null;
							if( msg.getSignature() != null)
							{
								ref = msg.getSignature().get$ref();
								operationToCall = (EntityOperation) StarUmlObjectContainer.getObject(ref);				
								call = operationToCall.getName();
							}
							else
								call = msg.getName();
							
							//call argument
							String argument = "";
							if(msg.getArguments() != null)
								argument = msg.getArguments();
							
							
							//assignment target
							
							assign = generateOperationAssignment(msg,operationToCall);
							if(assign.startsWith("$SET"))
							{
								code += "\r\n\t\t" + assign.substring(4) + target + call + "(" + genOperationArgument(argument,operationToCall) + "));\r\n";
							}
							else
							{
								//code += "\r\n\t\t" + assign.substring(4) + target + call + "(" + genOperationArgument(argument,operationToCall) + ");\r\n";
								
							}
							
						}
						
						
					}				
	    		}
			}
		
		}
		
		return code;
	}
	

	private String generateOperationAssignment(UMLMessage msg,EntityOperation operationToCall)
	{
		String assign = "";
		String target = msg.getAssignmentTarget();
		if(target != null)
			target = target.replace(" ", "");

		if(operationToCall == null)
			return assign;

		OperationParameter retPara = operationToCall.getReturn();
		
		if(target !=null && !"".equals(target))
		{
			if(tempLocalVariables.containsKey(target))
			{
				String variType = tempLocalVariables.get(target).getTypeStr();
				if(variType.equals(retPara.getTypeStr()))
				{
					assign =  target + " = " ;
				}
				else 
				{
					assign = "$SET" + target 
							+ ".set" 
						+ operationToCall.getName().substring(0,1).toUpperCase()
						+ operationToCall.getName().substring(1) + "(";
				}
			}
			else if(isAttributeExisted(target, null))
			{
				EntityAttribute att = getAttribute(target);
				String type = getAttributeType(att);
				if(type.equals(retPara.getTypeStr()))
				{
					assign =  target + " = " ;
				}
				else 
				{
					assign = "$SET" + target 
							+ ".set" 
						+ operationToCall.getName().substring(0,1).toUpperCase()
						+ operationToCall.getName().substring(1) + "(";
				}
			}
			else
			{
				assign =  target + " = " ;
					tempLocalVariables.put(msg.getAssignmentTarget(), retPara);	
			}
			
		}
		
		return assign;
	}
	
	private EntityAttribute findMember(String name)
	{
		for(EntityAttribute one: attributes)
		{
			if(one.getName().equals(name))
				return one;
		}
		
		return null;
	}
	
	private String genPersistentEntityOperations(EntityOperation op)
	{
		String opDefines = "";
		String paraDef = "";
		String paraVal = "";
		
		List<OperationParameter> paras =  op.getParameters();				
		List<OperationParameter> packParas = new ArrayList<OperationParameter>();
		
		//normal operations
		String condition = "\"where ";
		
		if(op.getName().equals("select"))
		{
			// case of this operation is "getInstances"
			
			if(paras != null)
			{
				for(OperationParameter onePara : paras){			
	    			if(!"return".equals(onePara.getDirection())){
	    				String typeName = onePara.getTypeStr();   
	    				if(typeName.equals("String") || typeName.equals("int") || typeName.equals("double"))
	    				{
	    					if(!condition.equals("\"where "))
	    						condition += " and ";
	    					if(!typeName.equals("String"))
	    						condition += onePara.getName() + " = \" + " + onePara.getName() + " + \"";
	    					else
	    						condition += onePara.getName() + " = '\" + " + onePara.getName() + " + \"'";
	    						
	        				if(!"".equals(paraDef))
	        					paraDef += ",";
	        				paraDef += typeName + " " + onePara.getName();        				

	        				if(!"".equals(paraVal))
	        					paraVal += ",";
	        				paraVal += onePara.getName();
	    				}
	    			}
	    		}
				
				condition += "\"";
			}
			
			
			String getInstancesFunc = ioUtil.readFile(templatePath + "/getInstances.java"); 
			getInstancesFunc = getInstancesFunc.replace("@parasDef@", paraDef);
			getInstancesFunc = getInstancesFunc.replace("@entity@", name);
			getInstancesFunc = getInstancesFunc.replace("@condition@", condition);
			opDefines += getInstancesFunc;
		}
		
		return opDefines;
	}
	
	public List<DEVPackage> getDependPacks()
	{
		return this.dependPacks;
	}
	
	private String genOperationArgument(String argument,EntityOperation operation)
	{
		String code = "";
		String arguments[] = argument.split(",");
		

		//case 1: same aguments nothing to do
		if(operation.getParameters() !=null)
		{
			for(OperationParameter para : operation.getParameters())
			{				
				if("return".equals(para.getDirection()))
					continue;
				
				boolean hasFound = false;
				for(int artI = 0;artI < arguments.length ; artI ++)
				{
					String paraDefType = para.getTypeStr();
					String paraArgName = arguments[artI];
					//first , find arg in the class' member list
					EntityAttribute argObject = findMember(paraArgName);
					if(argObject != null)
					{
						String argType = argObject.getTypeStr();
						if(argType.equals(paraDefType))
						{
							if(!"".equals(code))
								code += ",";
							code += paraArgName;
							hasFound = true;
						}
						else
						{
							Entity classEntity = (Entity) StarUmlObjectContainer.getObjectByName(argType);
							if(classEntity.isAttributeExisted(para.getName(), paraDefType))
							{
								if(!"".equals(code))
									code += ",";
								code += paraArgName 
										+ "." + genGetAttCode(para.getName());
								hasFound = true;
							}
							
						}
					}						
				}
				if(hasFound != true)
				{
					if(!"".equals(code))
						code += ",";
					code += "null";					
				}
				
			}
		}
		//case 2: met a type doesn't match 
		return code;
	}
	
	
	private String genGetAttCode(String attribute)
	{
		return "get" + attribute.substring(0,1).toUpperCase()
									+ attribute.substring(1) + "()";
	}
	
	private String genSetAttCode(String attribute,String setValue)
	{
		return "set" + attribute.substring(0,1).toUpperCase()
				+ attribute.substring(1) + "(" + setValue + ")";
	}
	
	public boolean isAttributeExisted(String name, String type)
	{
		for(EntityAttribute oneAtt:attributes)
		{
			if(type != null){
				if(oneAtt.getTypeStr().equals(type) && oneAtt.getName().equals(name))
				return true;
			}
			else
			{
				if(oneAtt.getName().equals(name))
				return true;
			}
		}
		return false;
	}
	
	private EntityAttribute getAttribute(String att)
	{
		for(EntityAttribute oneAtt:attributes)
		{
			if(oneAtt.getName().equals(att))
				return oneAtt;
		}
		return null;
	}
	
	
	private String getAttributeType(EntityAttribute att)
	{
		String attType = "String";   
		if(att.getType() != null && !"".equals(att.getType()))
		{
			String typeClass = att.getType().getClass().toString();
			if("class java.lang.String".equals(typeClass))
				attType = (String) att.getType();
			else{
				
				Map data = (Map)att.getType();
				String ref = (String) data.get("$ref");
				Entity referencedEntity = (Entity) StarUmlObjectContainer.getObject(ref);
				if(referencedEntity != null)
					attType = referencedEntity.getName();
			}
		}
		

		if("".equals(attType))
			attType = "String";
		
		return attType;
	}
	
	
	public void scanDepends()
	{
		if(this.getOwnedElements() != null)
		{
			for(UMLDependency depend:this.getOwnedElements())
			{
				if(depend.getTarget() == null)
					continue;
				String targetID = depend.getTarget().get$ref();
				Entity entity = (Entity) StarUmlObjectContainer.getObject(targetID) ;
				if(entity != null)
				{
					depends.put(entity.getName(), entity);
					if(!entity.getDevPackage().equals(this.getDevPackage()))
						dependPacks.add(entity.getDevPackage());
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void normalizeTypes()
	{
		for(EntityAttribute val : attributes)
		{ 	    		
    		String attType = getAttributeType(val);
    		val.setType(attType);
    		
    		String dictionary = val.getExtraAttribute("字典");
    		if(dictionary != null)
    			val.setType("MasterDictionary");
    		
    		if("Api".equals(stereotype))
    		{
    			if("response".equals(val.getName()))
    				val.setType(name + "Response");
    			else if("request".equals(val.getName()))
    				val.setType(name + "Request");
    		}
		}
		
		for(EntityOperation val : operations)
		{ 	
    		String ret = "void";
    		List<OperationParameter> paras = val.getParameters();  
    		
    		StarUmlObjectContainer.addObject(val.get_id(), val);
    		StarUmlObjectContainer.addObjectByName(val.getName(), val);
    		    		
    		if(paras != null)
    		{
    			for(OperationParameter onePara : paras)
    			{
    				Gson gson = new Gson();
    				String json = gson.toJson(onePara);
    				
    				String typeName = "String";
    				if(onePara.getType() != null)
					{
    					typeName = onePara.getType().getClass().toString();
    					if("class java.lang.String".equals(typeName))
        					typeName = (String) onePara.getType();
        				else{        				
        					Map data = (Map)onePara.getType();
        					String ref = (String) data.get("$ref");
        					Entity referencedEntity = (Entity) StarUmlObjectContainer.getObject(ref);
        					if(referencedEntity != null)
        						typeName = referencedEntity.getName();
        				}	
        	    		if("".equals(typeName))
        	    			typeName = "String";
					}
    				
    				

    	    		onePara.setType(typeName);
    			}
    		}
    		

    		if(val.getOwnedElements() != null){
    			for(UMLCollaboration col : val.getOwnedElements())
        		{ 	    	
        			if(col.getAttributes() != null){
            			for(EntityAttribute attribute : col.getAttributes())
                		{	
            				StarUmlObjectContainer.addObject(attribute.get_id(), attribute);
                		}   
        			}     			
        			
        			if(col.getOwnedElements() != null){
            			for(UMLInteraction interaction : col.getOwnedElements())
                		{	
            				if(interaction.getParticipants() != null)
            				{

                    			for(UMLParticipant participant : interaction.getParticipants())
                    			{
                    				StarUmlObjectContainer.addObject(participant.get_id(), participant); 
                    			}
            				}       				
                		}
        			}
        		}
    		}
		}
		
		
		
	}

	public EntityAttribute addAttribute(String name,String type)
	{
		EntityAttribute<String> att = new EntityAttribute<String>(this);
		att.setName(name);
		att.setType(type);
		attributes.add(att);
		
		return att;
	}
	
	
	
	public void addAttribute(EntityAttribute o)
	{
		attributes.add(o);
	}

	public void addOperation(EntityOperation o)
	{
		operations.add(o);
	}

	public String getPersistenceTable() {
		return persistenceTable;
	}

	public void setPersistenceTable(String persistenceTable) {
		this.persistenceTable = persistenceTable;
	}

	public int getPersistenceID() {
		return persistenceID;
	}

	public void setPersistenceID(int persistenceID) {
		this.persistenceID = persistenceID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}


	public String getTemplatePath() {
		return templatePath;
	}


	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}


	public List<EntityOperation> getOperations() {
		return operations;
	}


	public void setOperations(List<EntityOperation> operations) {
		this.operations = operations;
	}


	public String getDocumentation() {
		return documentation;
	}


	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}


	public List<UMLDependency> getOwnedElements() {
		return ownedElements;
	}


	public void setOwnedElements(List<UMLDependency> ownedElements) {
		this.ownedElements = ownedElements;
	}


	public DEVPackage getDevPackage() {
		return devPackage;
	}


	public void setDevPackage(DEVPackage devPackage) {
		this.devPackage = devPackage;
	}


	public List<EntityAttribute> getAttributes() {
		return attributes;
	}


	public void setAttributdes(List<EntityAttribute> attributes) {
		this.attributes = attributes;
	}


	public HashMap<String,Entity> getDepends() {
		return depends;
	}


	public void setDepends(HashMap<String,Entity> depends) {
		this.depends = depends;
	}


	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public String get_type() {
		return _type;
	}


	public void set_type(String _type) {
		this._type = _type;
	}


	public StarUmlReference get_parent() {
		return _parent;
	}


	public void set_parent(StarUmlReference _parent) {
		this._parent = _parent;
	}

	public HashMap<String,String> getExtraAttributes() {
		return extraAttributes;
	}

	public void setExtraAttributes(HashMap<String,String> extraAttributes) {
		this.extraAttributes = extraAttributes;
	}
	
}
