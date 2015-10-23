package @orgPath@.@prj@.svr.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import @orgPath@.@prj@.svr.web.utils.CheckChargeTimer;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class StartCheckChargeListener implements ServletContextListener {

	// @Autowired
	// @Named("CheckChargeTimer")
	// CheckChargeTimer checkChargeTimer;

	public StartCheckChargeListener() {
		super();
	}

	public void contextDestroyed(ServletContextEvent arg0) {

		
	}

	public void contextInitialized(ServletContextEvent e) {
		System.out.println("-------------StartAutoStopListener.init-------------");
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(e.getServletContext());
		CheckChargeTimer checkChargeTimer = (CheckChargeTimer) ac.getBean("CheckChargeTimer");
		
		checkChargeTimer.execute();
		
	}
}
