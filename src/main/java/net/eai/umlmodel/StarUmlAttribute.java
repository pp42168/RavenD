package net.eai.umlmodel;

public class StarUmlAttribute {
	private String name;
	private String _type = "UMLAttribute";
	private String _id ;
	private StarUmlReference _parent;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public StarUmlReference get_parent() {
		return _parent;
	}
	public void set_parent(StarUmlReference _parent) {
		this._parent = _parent;
	}
}
