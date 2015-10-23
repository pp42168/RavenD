package @orgPath@.@prj@.svr.web.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Named;

import me.ele.elog.Log;
import me.ele.elog.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("taskJob")
public class TaskJob {
	
	private final static Log LOGGER = LogFactory.getLog(TaskJob.class);
	
	/*@Autowired
	private WithDrawCashMapper withDrawCashMapper;
	
	@Autowired(required = false)
    @Named("httpRequestService")
    private IHttpRequest httpRequest;*/
	
//	@Scheduled(cron="0/10 * *  * * ? ")
	@Scheduled(cron = "0 0 2 * * ?")
	public void job(){
		

	}
	
	
}
