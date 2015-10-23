package @orgPath@.@prj@.svr.web.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

public class ValidatedContextLoaderListener extends
		org.springframework.web.context.ContextLoaderListener {
	final static Logger logger = LoggerFactory
			.getLogger(ValidatedContextLoaderListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		try {
			SpringContextValidator
					.detectNullFields(getCurrentWebApplicationContext());
		} catch (Throwable e) {
			logger.error("检查WebApplicationContext出错", e);
		}

	}

}
