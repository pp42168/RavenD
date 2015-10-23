package @orgPath@.@prj@.svr.web.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class BaseValidator implements Validator {
	private final static Logger logger = LoggerFactory.getLogger(BaseValidator.class);


	@Override
	public final void validate(Object target, Errors errors) {
		try {
			doValidate(target, errors);
		} catch (Throwable e) {
			logger.error("validation.general.error ");
			errors.reject("validation.general.error", "表单数据校验发生错误");
		}
	}

	protected abstract void doValidate(Object target, Errors errors);

}
