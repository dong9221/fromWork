package cn.encdata.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import cn.encdata.device.dao.DeviceDao;
import cn.encdata.device.dao.DeviceDaoImpl;
import cn.encdata.domain.Device;
import cn.encdata.jobs.countData.CountData;
import cn.encdata.jobs.pingIP.PingIP;

@Controller
@PropertySources({
	@PropertySource("classpath:pingIp.properties"),
	@PropertySource("classpath:countData.properties")
})

public class JobsHandler {
	private static final Log log = LogFactory.getLog(JobsHandler.class);
	
	@Value("${job.ping.thread.number}")
	private int threadNum; 
	
	@Autowired
	PingIP pip;
	
	@Autowired
	DeviceDao deviceDao;
	
	@Autowired
	private CountData countData;
	
	
	@Scheduled(cron="${job.countData.everyday.cron}")
	public void runcountData(){
		log.info("准备执行countData..." +deviceDao);
		countData.countData();
	}
	
	//@Scheduled(cron="0 31 14 * * ?")
	@Scheduled(cron="${job.ping.everyday.cron}")
	public void runPingIp(){
		
		List<String> ipList = new ArrayList<String>();
		List<Device> deviceList = deviceDao.getAllDevice();
		for(Device d:deviceList){
			if(d.getDeviceIp()!=null &&!"".equals(d.getDeviceIp())){
				ipList.add(d.getDeviceIp());
			}
		}
		pip.initData(ipList);
		log.info("准备执行ping..." +deviceDao);
		final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		pip.setCountDownLatch(countDownLatch);
		for(int i = 0;i<threadNum;i++){
			Thread t = new Thread(pip);
			t.start();
			
		}
		
		try {
			countDownLatch.await();
			for(Device device:deviceList){
				device.setDeviceReachable(PingIP.resVal.get(device.getDeviceIp()));
				deviceDao.saveDeviceStatus(device);
			}
			log.info("-----------end-----------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
