package com.hpuvoice.phonesafe.bean;

public class UpdataInfo {

	private String version;
	private String description;
	private String apkurl;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getApkurl() {
		return apkurl;
	}
	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}
	public UpdataInfo(String version, String description, String apkurl) {
		super();
		this.version = version;
		this.description = description;
		this.apkurl = apkurl;
	}
	public UpdataInfo() {
	}
	@Override
	public String toString() {
		return "UpdataInfo [version=" + version + ", description="
				+ description + ", apkurl=" + apkurl + "]";
	}
	
	
	
}
