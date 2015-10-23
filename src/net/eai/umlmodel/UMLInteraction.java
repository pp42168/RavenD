package net.eai.umlmodel;

import java.util.List;

public class UMLInteraction {

	private String isReentrant;
	private List<UMLMessage> messages;
	private List<UMLParticipant> participants;
	private String _id;

	public List<UMLParticipant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<UMLParticipant> participants) {
		this.participants = participants;
	}
	
	
	public String getIsReentrant() {
		return isReentrant;
	}

	public void setIsReentrant(String isReentrant) {
		this.isReentrant = isReentrant;
	}

	public List<UMLMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<UMLMessage> messages) {
		this.messages = messages;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
}
