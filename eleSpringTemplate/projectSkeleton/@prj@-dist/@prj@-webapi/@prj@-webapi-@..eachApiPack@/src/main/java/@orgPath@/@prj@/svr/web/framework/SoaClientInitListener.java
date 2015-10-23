package @orgPath@.@prj@.svr.web.framework;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.util.ResourceUtils;

import me.ele.contract.client.ClientUtil;
import me.ele.etrack.Etrack;

/**
 * Application Lifecycle Listener implementation class SoaClientInitListener
 *
 */
//@WebListener
public class SoaClientInitListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public SoaClientInitListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			String path = ResourceUtils.getFile("classpath:Configure.json")
					.getAbsolutePath();
			System.out.println(path);
			ClientUtil.getContext().initClients(path);
		} catch (IOException ex) {
			//TODO handle exception
			ex.printStackTrace();
		}
		
		
	}

}
