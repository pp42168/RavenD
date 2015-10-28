package net.eai.umlmodel;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class EntityAttribute<T>  {
	private int entityID;
	private String name;
	private String _id;
	private T type;
	private String visibility;
	private String multiplicity;
	private String documentation;
	private String stereotype;
	private String defaultValue;
	private String _type = "UMLAttribute";
	private StarUmlReference _parent;

	private HashMap<String,String> extraAttributes = new HashMap<String,String>();
	
	public EntityAttribute()
	{
		
	}
	
	public HashMap<String,String>  getExtraAttributes()
	{
		return extraAttributes;
	}
	

	public void setExtraAttribute(String key,String value)
	{
		extraAttributes.put(key, value);
	}
	
	
	public EntityAttribute(Entity entity)
	{
		String str = this.toString();		
		_id = str.substring(str.indexOf("@"));
		_parent = new StarUmlReference(entity.get_id());
	}
	
	public String getExtraAttribute(String key)
	{
		return extraAttributes.get(key);
	}
	
	public String addExtraAttribute(String key,String value)
	{
		documentation += "\n" + key + "：" + value;
		extraAttributes.put(key, value);
		return extraAttributes.get(key);
	}
	

	public void collectExtraAttributes()
	{
		extraAttributes.clear();
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
	
	
	
	public String getTypeStr()
	{
		String attType = "String";   
		if(type != null && !"".equals(type))
		{
			String typeClass = type.getClass().toString();
			if("class java.lang.String".equals(typeClass))
				attType = (String) type;
			else if ("class net.eai.umlmodel.Entity".equals(typeClass))
			{
				Entity data = (Entity)type;
				attType = data.getName();
			}
			else{
				
				LinkedHashMap data = (LinkedHashMap)type;
				String ref = (String) data.get("$ref");
				Entity referencedEntity = (Entity) StarUmlObjectContainer.getObject(ref);
				if(referencedEntity != null)
					attType = referencedEntity.getName();
			}
		}
		
		//if(multiplicity != null && !"".equals(multiplicity))
		//{
		//	attType = "List<" + attType + "> ";
		//}
		return attType;
	}
	
	
	public int getEntityID() {
		return entityID;
	}
	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public T getType() {
		return type;
	}
	public void setType(T type) {
		this.type = type;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}


	public String getMultiplicity() {
		return multiplicity;
	}


	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}


	public String getDocumentation() {
		return documentation;
	}


	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}


	public String getStereotype() {
		return stereotype;
	}


	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}


	public String getDefaultValue() {
		return defaultValue;
	}


	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public StarUmlReference get_parent() {
		return _parent;
	}

	public void set_parent(StarUmlReference _parent) {
		this._parent = _parent;
	}

	public String get_type() {
		return _type;
	}

	public void set_type(String _type) {
		this._type = _type;
	}
}
