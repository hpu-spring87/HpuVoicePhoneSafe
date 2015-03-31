package com.hpuvoice.phonesafe.utils;

import java.io.BufferedReader;
import java.io.FileReader;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public class TaskUtils {

	/**
	 * 获取当前运行服务数量
	 * @param context
	 * @return
	 */
	public static int getRunningCount(Context context){
		ActivityManager amManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int size = amManager.getRunningAppProcesses().size();
		return size;
	}
	
	/**
	 * 获取可用RAM
	 * @param context
	 * @return
	 */
	public static long avaliableRAM(Context context){
		ActivityManager amManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mInfo = new MemoryInfo();
		amManager.getMemoryInfo(mInfo);
		return mInfo.availMem;
	}
	/**
	 * 获取总得RAM
	 * @param context
	 * @return
	 */
	public static long totalRAM(Context context){
		ActivityManager amManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mInfo = new MemoryInfo();
		amManager.getMemoryInfo(mInfo);
		return mInfo.totalMem;
	}
	
	/**
	 * 获取到了手机总共的ram  兼容低版本
	 * @param context
	 * @return
	 */
	public static long  getTotalRam(){
		try {
			FileReader reader=new FileReader("/proc/meminfo");
			@SuppressWarnings("resource")
			BufferedReader br=new BufferedReader(reader);
			String readLine = br.readLine();
			StringBuilder sb=new StringBuilder();
			char[] charArray = readLine.toCharArray();
			for(char c:charArray){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
			String string = sb.toString();
			long parseLong = Long.parseLong(string);
			return parseLong*1024;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
