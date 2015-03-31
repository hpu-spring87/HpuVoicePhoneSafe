package com.hpuvoice.phonesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {

	public static boolean  isServiceRunning(Context context,String serviceName){
		// 进程的管理者 
		ActivityManager  am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// 参数 最多返回多少个服务   RAM 断电丢失的内存  3G
		List<RunningServiceInfo> runningServices = am.getRunningServices(1000); 
		for(RunningServiceInfo info:runningServices){
			// 获取到了正在运行服务的全类名
			String className = info.service.getClassName();
			if(className.equals(serviceName)){
				return true;
			}
		}
		
		return false;
	}
}
