package net.eai.umlmodel;

public class UMLParticipant {

	private String _id;
	private String _type;
	private String name;
	private StarUmlReference represent;
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public StarUmlReference getRepresent() {
		return represent;
	}
	public void setRepresent(StarUmlReference represent) {
		this.represent = represent;
	}
}
