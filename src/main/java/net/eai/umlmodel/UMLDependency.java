package net.eai.umlmodel;

public class UMLDependency {
	private String _id;
	private String _type = "UMLDependency";
	private StarUmlReference _parent;
	private StarUmlReference source;
	private StarUmlReference target;
	
	UMLDependency()
	{
		String str = this.toString();		
		_id = str.substring(str.indexOf("@"));
	}
	
	public StarUmlReference get_parent() {
		return _parent;
	}
	public void set_parent(StarUmlReference _parent) {
		this._parent = _parent;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public StarUmlReference getSource() {
		return source;
	}
	public void setSource(StarUmlReference source) {
		this.source = source;
	}
	public StarUmlReference getTarget() {
		return target;
	}
	public void setTarget(StarUmlReference target) {
		this.target = target;
	}
	public String get_type() {
		return _type;
	}
	public void set_type(String _type) {
		this._type = _type;
	}
}
