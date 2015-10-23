package @orgPath@.@prj@.common.zmqrpc;

import @orgPath@.@prj@.common.exception.ExceptionHelper;

public class ZmqRpcException extends Exception{

	private static final long serialVersionUID = 1L;

	protected String errorCode;

	protected String errorMessage;

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


}
