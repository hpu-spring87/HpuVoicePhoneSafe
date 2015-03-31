package com.hpuvoice.phonesafe.engine;

import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class TrafficCountService {

	private Context context;
	@SuppressWarnings("unused")
	private PackageManager pManager;

	public TrafficCountService(Context context) {
		this.context = context;
		pManager = context.getPackageManager();
	}
	
	public void getAllApps() throws NameNotFoundException{
		
		//------先获取应用信息，再获取包信息，再获取权限，一步步遍历，如果直接获取包信息，会报错----
        StringBuffer appNameAndPermissions=new StringBuffer();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : packages) {
       // Log.d("test", "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName);
        try {
        PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
                      appNameAndPermissions.append(packageInfo.packageName+"*:\n");
         //Get Permissions
        String[] requestedPermissions = packageInfo.requestedPermissions;
         if(requestedPermissions != null) {
         for (int i = 0; i < requestedPermissions.length; i++) {
        // Log.d("test", requestedPermissions[i]);
         System.out.println("--------------------------------");
         System.out.println(requestedPermissions[i]);
         System.out.println("--------------------------------");
        appNameAndPermissions.append(requestedPermissions[i]+"\n");
        }
         appNameAndPermissions.append("\n");
        }
        } catch (NameNotFoundException e) {
         e.printStackTrace();
        }}
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
