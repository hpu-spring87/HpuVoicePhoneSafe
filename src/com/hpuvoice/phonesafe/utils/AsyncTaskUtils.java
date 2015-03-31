package com.hpuvoice.phonesafe.utils;

import android.os.Handler;

public abstract class AsyncTaskUtils {

	public abstract void preTask();
	public abstract void doInTask();
	public abstract void postTask();
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			postTask();
		};
	};
	
	public void execute(){
		preTask();
		
		new Thread(){
			public void run() {
				doInTask();
				handler.sendEmptyMessage(0);
			};
		}.start();
	}
}
