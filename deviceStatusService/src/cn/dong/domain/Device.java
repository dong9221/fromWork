package cn.dong.domain;

public class Device {
	private String deviceId;
	private String deviceName;
	private String deviceIp;
	private boolean deviceReachable;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	public boolean isDeviceReachable() {
		return deviceReachable;
	}
	public void setDeviceReachable(boolean deviceReachable) {
		this.deviceReachable = deviceReachable;
	}
	
}
