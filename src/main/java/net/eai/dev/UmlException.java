package net.eai.dev;

import java.util.LinkedHashMap;

public class UmlException extends Exception {
	
	private static final long serialVersionUID = 1L;

	protected String errorCode;
	protected String errorMessage;
	
	private LinkedHashMap<String,String> paraErrors = new LinkedHashMap<String,String>();

	
	
	public UmlException(String para,String em)
	{
		errorCode = "paraError";
		errorMessage = em;
		paraErrors.put(para, em);
	}
	
	
	public void addParaError(String para,String error)
	{
		paraErrors.put(para, error);
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public LinkedHashMap<String,String> getParaErrors() {
		return paraErrors;
	}


}
