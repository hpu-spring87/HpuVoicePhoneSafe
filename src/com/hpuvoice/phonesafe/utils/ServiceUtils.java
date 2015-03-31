package com.hpuvoice.phonesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {

	public static boolean  isServiceRunning(Context context,String serviceName){
		// ���̵Ĺ����� 
		ActivityManager  am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// ���� ��෵�ض��ٸ�����   RAM �ϵ綪ʧ���ڴ�  3G
		List<RunningServiceInfo> runningServices = am.getRunningServices(1000); 
		for(RunningServiceInfo info:runningServices){
			// ��ȡ�����������з����ȫ����
			String className = info.service.getClassName();
			if(className.equals(serviceName)){
				return true;
			}
		}
		
		return false;
	}
}
