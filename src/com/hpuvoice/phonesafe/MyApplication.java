package com.hpuvoice.phonesafe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.currentThread().setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
	}

	private class MyUncaughtExceptionHandler implements
			UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			PrintStream err;
			try {
				
				err = new PrintStream(new File(Environment.getExternalStorageDirectory(),"log.txt"));
				//err = new PrintStream(new File(getFilesDir() ,"log.txt"));
				//-----上面是逗号隔开，不是加号，不然肯定找不到路径。
				ex.printStackTrace(err);
				err.flush();
				err.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			android.os.Process.killProcess(android.os.Process.myPid());
		}

	}
}
