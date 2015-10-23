package @orgPath@.@prj@.svr.web.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import me.ele.elog.Log;
import me.ele.elog.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CheckChargeTimer")
public class CheckChargeTimer {

	private final static Log LOGGER = LogFactory.getLog(CheckChargeTimer.class);
	private ScheduledExecutorService myScheduler = Executors.newScheduledThreadPool(5);
	
	/*@Autowired(required = true)
	@Named("paymentService")
	private PaymentService paymentService;*/

	public void execute() {
		Runnable task = new Runnable() {
			public void run() {
				try {
				
					//String str = paymentService.test();
					
					//System.out.println(str);
				} catch (Exception e) {
					LOGGER.error(e);
				}
			}
		};
		if (myScheduler.isShutdown()) {
			myScheduler = Executors.newScheduledThreadPool(5);
			myScheduler.scheduleWithFixedDelay(task, 1, 60, TimeUnit.SECONDS);
		} else {
			myScheduler.scheduleWithFixedDelay(task, 1, 60, TimeUnit.SECONDS); // 延迟1秒，每隔60秒一次
		}
	}
	

	// 停止任务，不再提交新任务，已提交任务会继续执行以致完成
	public void stop() {
		myScheduler.shutdown();
	}

}
