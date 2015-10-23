package net.eai.umlmodel;

import java.util.LinkedHashMap;
import java.util.List;

public class StarUmlOwnedElement {
	private String _type;
	private String _id;
	private LinkedHashMap<String,String> _parent;
	private String name;
	private List<StarUmlOwnedElement> ownedElements;
	private Object object;
	
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
	public LinkedHashMap<String,String> get_parent() {
		return _parent;
	}
	public void set_parent(LinkedHashMap<String,String> _parent) {
		this._parent = _parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<StarUmlOwnedElement> getOwnedElements() {
		return ownedElements;
	}
	public void setOwnedElements(List<StarUmlOwnedElement> ownedElements) {
		this.ownedElements = ownedElements;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
}
