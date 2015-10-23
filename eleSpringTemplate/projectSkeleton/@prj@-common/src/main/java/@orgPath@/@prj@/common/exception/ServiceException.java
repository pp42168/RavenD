package @orgPath@.@prj@.common.exception;

public class ServiceException extends me.ele.contract.exception.ServiceException {
	private static final long serialVersionUID = 1L;
	protected String code;
	protected String errorMessage;

	public ServiceException(String code, String errorMessage) {
		super(code, errorMessage);
        setErrorMessage(errorMessage);
        setCode(code);
    }
	
	public ServiceException(IExceptionCode code) {
		super(ExceptionHelper.getMessage(code));
		this.code = ExceptionHelper.getCode(code);
		this.errorMessage = this.getMessage();
	}

	public ServiceException(IExceptionCode code, String message, Throwable cause) {
		super(message, cause);
		this.code = ExceptionHelper.getCode(code);
		this.errorMessage = ExceptionHelper.getMessage(code);
	}

	public ServiceException(IExceptionCode code, String message) {
		super(message);
		this.code = ExceptionHelper.getCode(code);
		this.errorMessage = ExceptionHelper.getMessage(code);
	}

	public ServiceException(IExceptionCode code, Throwable cause) {
		super(ExceptionHelper.getMessage(code), cause);
		this.code = ExceptionHelper.getCode(code);
		this.errorMessage = this.getMessage();
	}

	public ServiceException(String msg) {
		super(msg);
	}
	
	
	 public void setCode(String code) {
	        this.code = code;
	    }

	    public String getCode() {
	        return code;
	    }

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
