package net.eai.umlmodel;

import java.util.LinkedHashMap;
import java.util.Map;

public class OperationParameter<T> {
	private String name;
	private T type;
	private String direction;
	private String stereotype;
	private String multiplicity;
	private String _type = "UMLParameter";
	private String _id ;
	private StarUmlReference _parent;
	

	OperationParameter()
	{
		
	}
	
	public OperationParameter(Entity entity)
	{
		String str = this.toString();		
		_id = str.substring(str.indexOf("@"));
		_parent = new StarUmlReference(entity.get_id());
	}

	public String getTypeStr()
	{
		String attType = "String";   
		if(type != null && !"".equals(type))
		{
			String typeClass = type.getClass().toString();
			if("class java.lang.String".equals(typeClass))
				attType = (String) type;
			else{
				
				Map data = (Map)type;
				String ref = (String) data.get("$ref");
				Entity referencedEntity = (Entity) StarUmlObjectContainer.getObject(ref);
				if(referencedEntity != null)
					attType = referencedEntity.getName();
			}
		}

		if(multiplicity != null && !"".equals(multiplicity))
		{
			attType = "List<" + attType + "> ";
		}
		return attType;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public T getType() {
		return type;
	}
	public void setType(T type) {
		this.type = type;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}

	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}
}
