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
	 * ��ȡ��ǰ���з�������
	 * @param context
	 * @return
	 */
	public static int getRunningCount(Context context){
		ActivityManager amManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int size = amManager.getRunningAppProcesses().size();
		return size;
	}
	
	/**
	 * ��ȡ����RAM
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
	 * ��ȡ�ܵ�RAM
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
	 * ��ȡ�����ֻ��ܹ���ram  ���ݵͰ汾
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
