package @orgPath@.@prj@.common.exception;

public class SystemException extends me.ele.contract.exception.SystemException {
	private static final long serialVersionUID = 1L;
	protected String code;
	protected String errorMessage;

	public SystemException(String code, String errorMessage) {
		super(code, errorMessage);
        setErrorMessage(errorMessage);
        setCode(code);
    }

	public SystemException(IExceptionCode code) {
		super(ExceptionHelper.getMessage(code));
		this.code = ExceptionHelper.getCode(code);
		this.errorMessage = this.getMessage();
	}

	public SystemException(IExceptionCode code, String message, Throwable cause) {
		super(message, cause);
		this.code = ExceptionHelper.getCode(code);
		this.errorMessage = ExceptionHelper.getMessage(code);
	}

	public SystemException(IExceptionCode code, String message) {
		super(message);
		this.code = ExceptionHelper.getCode(code);
		this.errorMessage = ExceptionHelper.getMessage(code);
	}

	public SystemException(IExceptionCode code, Throwable cause) {
		super(ExceptionHelper.getMessage(code), cause);
		this.code = ExceptionHelper.getCode(code);
		this.errorMessage = this.getMessage();
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String errorCode) {
		this.code = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
