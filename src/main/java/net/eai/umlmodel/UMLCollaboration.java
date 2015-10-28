package net.eai.umlmodel;

import java.util.List;

public class UMLCollaboration {
	private List<UMLInteraction> ownedElements;
	private List<EntityAttribute> attributes;
	
	
	

	public List<UMLInteraction> getOwnedElements() {
		return ownedElements;
	}

	public void setOwnedElements(List<UMLInteraction> ownedElements) {
		this.ownedElements = ownedElements;
	}

	public List<EntityAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<EntityAttribute> attributes) {
		this.attributes = attributes;
	}


}
