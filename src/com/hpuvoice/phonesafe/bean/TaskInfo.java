package com.hpuvoice.phonesafe.bean;

import android.graphics.drawable.Drawable;

public class TaskInfo {

	private Drawable icon;
	private String appname;
	private String packagename;
	private String version;
	private long memory;
	private boolean isSystemApp;
	private boolean isChenked;
	
	

	public boolean isChenked() {
		return isChenked;
	}

	public void setChenked(boolean isChenked) {
		this.isChenked = isChenked;
	}

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	public TaskInfo(Drawable icon, String appname, String packagename,
			String version, long memory, boolean isSystemApp,boolean isChenked) {
		super();
		this.icon = icon;
		this.appname = appname;
		this.packagename = packagename;
		this.version = version;
		this.memory = memory;
		this.isSystemApp = isSystemApp;
		this.isChenked = isChenked;
	}

	public TaskInfo() {
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public boolean isSystemApp() {
		return isSystemApp;
	}

	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((packagename == null) ? 0 : packagename.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskInfo other = (TaskInfo) obj;
		if (packagename == null) {
			if (other.packagename != null)
				return false;
		} else if (!packagename.equals(other.packagename))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AppInfo [icon-->" + icon + ", appname-->" + appname
				+ ", packagename-->" + packagename + ", isSystemApp-->"
				+ isSystemApp + "version->" + version + "--" + memory + "]";
	}

}
