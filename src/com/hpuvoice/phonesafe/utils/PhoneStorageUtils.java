package com.hpuvoice.phonesafe.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class PhoneStorageUtils {

	/**
	 * ��ȡ�����õ�sd����С 
	 * @return
	 */
	public static long getSDAvailable(){
	    File path = Environment.getExternalStorageDirectory(); 
        StatFs stat = new StatFs(path.getPath());
        @SuppressWarnings("deprecation")
		long blockSize = stat.getBlockSize();   
        //long totalBlocks = stat.getBlockCount(); --------->>����ʱ�ķ�����
        @SuppressWarnings("deprecation")
		long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
	}
	/**
	 * ��ȡ���ֻ��ڴ�Ŀ��ÿռ� 
	 * @return
	 */
	public static long getRomAvailable(){
	    File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());
        @SuppressWarnings("deprecation")
		long blockSize = stat.getBlockSize();   
        //long totalBlocks = stat.getBlockCount(); --------->>����ʱ�ķ�����
        @SuppressWarnings("deprecation")
		long availableBlocks = stat.getAvailableBlocks(); 
        return availableBlocks * blockSize;
	}
}
