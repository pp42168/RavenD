package net.eai.umlmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("rawtypes")
public class EntityOperation {
	
	private String name;
	private List<OperationParameter> parameters;
	private String _id;
	private String visibility;
	private String documentation;
	private List<UMLCollaboration> ownedElements;
	private String _type = "UMLOperation";
	private StarUmlReference _parent;
	
	private HashMap<String,String> extraAttributes = new HashMap<String,String>();
	
	public EntityOperation()
	{
		
	}
	

	public HashMap<String,String>  getExtraAttributes()
	{
		return extraAttributes;
	}
	
	public EntityOperation(Entity entity)
	{
		parameters = new ArrayList();
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
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OperationParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<OperationParameter> parameters) {
		this.parameters = parameters;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public List<UMLCollaboration> getOwnedElements() {
		return ownedElements;
	}

	public void setOwnedElements(List<UMLCollaboration> ownedElements) {
		this.ownedElements = ownedElements;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	
	public OperationParameter getReturn ()
	{
		if(parameters == null)
			return null;
		
		for(OperationParameter para : parameters)
		{
			if("return".equals(para.getDirection()))
				return para;
		}
		
		return null;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
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
}
