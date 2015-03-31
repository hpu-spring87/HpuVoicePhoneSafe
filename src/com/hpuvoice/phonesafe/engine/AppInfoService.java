package com.hpuvoice.phonesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.hpuvoice.phonesafe.bean.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppInfoService {

	@SuppressWarnings("unused")
	private Context context;
	private PackageManager pManager;

	public AppInfoService(Context context) {
		this.context = context;
		pManager = context.getPackageManager();
	}
	
	public List<AppInfo> getAllApps(){
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		List<PackageInfo> packinfos = pManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo packageInfo : packinfos) {
			//packageInfo.sourceDir = "apkpath";  //"apkpath" is your apk's absolute path
			//packageInfo.publicSourceDir = "apkpath";
			//packageInfo.applicationInfo.sourceDir = mSavePath+"/"+APKNAME;//your apk absolute 
			//packageInfo.applicationInfo.publicSourceDir = mSavePath+"/"+APKNAME;//your apk absolute;
			String version = packageInfo.versionName;
			String packname = packageInfo.packageName;
			ApplicationInfo info = packageInfo.applicationInfo;
			Drawable icon = info.loadIcon(pManager);
			String name = info.loadLabel(pManager).toString();
			boolean isSystemApp = filterApp(info);
			AppInfo appInfo = new AppInfo(icon, name, packname, version, isSystemApp);
			appInfos.add(appInfo);
		}
		return appInfos;
		
	}
	
	/**
	 * 判断是否为第三方应用-true代表是系统应用
	 * @param info
	 * @return
	 */
	public boolean filterApp(ApplicationInfo info){
		if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
			return true;
		}else{
			return false;
		}
	}
}
