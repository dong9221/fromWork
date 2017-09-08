package cn.encdata.device.dao;

import java.util.List;

import cn.encdata.domain.Device;

public interface DeviceDao {
	List<Device> getAllDevice();
	void saveDeviceStatus(Device device);
}
