package com.hpuvoice.phonesafe.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class PhoneStorageUtils {

	/**
	 * 获取到可用的sd卡大小 
	 * @return
	 */
	public static long getSDAvailable(){
	    File path = Environment.getExternalStorageDirectory(); 
        StatFs stat = new StatFs(path.getPath());
        @SuppressWarnings("deprecation")
		long blockSize = stat.getBlockSize();   
        //long totalBlocks = stat.getBlockCount(); --------->>不过时的方法。
        @SuppressWarnings("deprecation")
		long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
	}
	/**
	 * 获取到手机内存的可用空间 
	 * @return
	 */
	public static long getRomAvailable(){
	    File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());
        @SuppressWarnings("deprecation")
		long blockSize = stat.getBlockSize();   
        //long totalBlocks = stat.getBlockCount(); --------->>不过时的方法。
        @SuppressWarnings("deprecation")
		long availableBlocks = stat.getAvailableBlocks(); 
        return availableBlocks * blockSize;
	}
}
