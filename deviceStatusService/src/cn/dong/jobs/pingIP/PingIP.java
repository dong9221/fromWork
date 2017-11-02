package cn.dong.jobs.pingIP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.dong.domain.Device;
@Service
public class PingIP implements Runnable{
	private static final Log log = LogFactory.getLog(PingIP.class);
	private Queue<String> ipQueue = new LinkedList<String>();
//	private List<Device> resList = new ArrayList<>();
	public static Map<String,Boolean> resVal = new ConcurrentHashMap<String,Boolean>();
	public CountDownLatch countDownLatch;
	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

	@Value("${job.ping.timeout}")
	private int timeout;
	private boolean runnable = false;
	public PingIP() {
		super();
	}	
	
	
	public void initData(List<String> deviceList){
		
		for(int i = 0;i<deviceList.size();i++){
			
			ipQueue.offer(deviceList.get(i).trim());
		}
		if(ipQueue.size()>0){
			log.debug("size:"+ipQueue.size());
			runnable =true;
		}
	}

	@Override
	public void run() {
		try{
			while(runnable){
				String ipAddr ="";
				synchronized (ipQueue) {
					if(ipQueue.size()>0){
						ipAddr = ipQueue.poll();
					}else{
						runnable = false;
						break;
					}
					
				}
				boolean pingRes =false;
				try {
					InetAddress addr = InetAddress.getByName(ipAddr);
					try {
						pingRes = addr.isReachable(timeout);
						log.info(ipAddr+":"+pingRes+":"+timeout);
					} catch (IOException e) {
						e.printStackTrace();
						pingRes = false;
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
					pingRes = false;
				}
				resVal.put(ipAddr, pingRes);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(countDownLatch!=null){
				countDownLatch.countDown();
			}
			log.info("----over-----");
		}
		
		
	}
}
