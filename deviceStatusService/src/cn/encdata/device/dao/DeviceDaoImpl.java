package cn.encdata.device.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.encdata.domain.Device;

@Repository("deviceDao")
public class DeviceDaoImpl implements DeviceDao{
	private static Log log = LogFactory.getLog(DeviceDaoImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Device> getAllDevice() {
		List<Device> deviceList = new ArrayList<Device>();
		String sql = "select * from d_device_c t";
		log.info("getAllDevice sql:"+sql);
		List<Map<String, Object>> list   = jdbcTemplate.queryForList(sql);
		for (Map m:list){
			Device device = new Device();
			device.setDeviceId((String)m.get("SBBH"));
			device.setDeviceIp((String)m.get("IPDZ"));
			device.setDeviceReachable(false);
			deviceList.add(device);
		}
		log.info("getAllDevice result:"+deviceList);
		return deviceList;
	}
	@Override
	public void saveDeviceStatus(Device device) {
		if(!device.isDeviceReachable()){
			String sql = "insert into tt_device_fault(xjbh,jcsj,jcjg) values(?,sysdate,'01')";
			log.info("saveDeviceStatus sql:"+sql+"param:"+device.getDeviceId());
			jdbcTemplate.update(sql,new String[]{device.getDeviceId()});
		}
		
	}

}
