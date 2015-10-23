package @orgPath@.@prj@.@pack@;

import me.ele.contract.iface.IServiceInitializer;
import @orgPath@.@prj@.@pack@.mapper.*;
import @orgPath@.@prj@.@pack@.model.*;
import @orgPath@.@prj@.@pack@.service.*;
import @orgPath@.@prj@.@pack@.service.impl.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class @^prj@ServiceInitializer implements IServiceInitializer {
	private static ApplicationContext ac;

	@Override
	public Object getImpl(Class<?> iface) {
		if (iface == @^pack@Service.class) {
			return ac.getBean(@^pack@ServiceImpl.class);
		} else {
			return null;
		}
	}

	@Override
	public void init() {
		// new ClassPathXmlApplicationContext("application-context.xml");
		ac = new FileSystemXmlApplicationContext("conf/application-context.xml");
	}
}
