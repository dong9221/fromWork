package cn.dong.device.dao;

import java.util.List;

import cn.dong.domain.Device;

public interface DeviceDao {
	List<Device> getAllDevice();
	void saveDeviceStatus(Device device);
}
