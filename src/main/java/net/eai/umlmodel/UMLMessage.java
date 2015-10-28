package net.eai.umlmodel;

public class UMLMessage {
	
	private String name;
	private String _id;
	private StarUmlReference source;
	private StarUmlReference target;
	private StarUmlReference signature;
	private String visibility;
	private String messageSort;
	private String arguments;
	private String assignmentTarget;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public StarUmlReference getSignature() {
		return signature;
	}

	public void setSignature(StarUmlReference signature) {
		this.signature = signature;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getMessageSort() {
		return messageSort;
	}

	public void setMessageSort(String messageSort) {
		this.messageSort = messageSort;
	}

	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public String getAssignmentTarget() {
		return assignmentTarget;
	}

	public void setAssignmentTarget(String assignmentTarget) {
		this.assignmentTarget = assignmentTarget;
	}
}
