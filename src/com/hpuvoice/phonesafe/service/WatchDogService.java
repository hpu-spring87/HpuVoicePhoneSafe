package com.hpuvoice.phonesafe.service;

import java.util.List;

import com.hpuvoice.phonesafe.WatchDogActivity;
import com.hpuvoice.phonesafe.dao.WatchDogDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class WatchDogService extends Service {

	private List<String> queryAll;
	private WatchDogDao wdd;
	private String currentpkg;
	private CurrenNotLock currenNotLock;
	private ScreenOffReceiver offReceiver;
	private ScreenOnReceiver onReceiver;
	private boolean flag = true;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/***
	 * 接受临时数据广播
	 * 
	 * @author hpu-spring87
	 * 
	 */
	private class CurrenNotLock extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			currentpkg = intent.getStringExtra("currentpkg");

		}

	}

	/**
	 * 锁屏广播
	 * 
	 * @author hpu-spring87
	 * 
	 */
	private class ScreenOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			currentpkg = null;
			flag = false;
		}
	}

	private class ScreenOnReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			flag = true;
			runWatchDog();
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		wdd = new WatchDogDao(getApplicationContext());
		queryAll = wdd.queryAll();
		Uri uri = Uri.parse("content://comhpuvoice.phonesafe.db");
		getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {

			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				queryAll = wdd.queryAll();
			}
			
		});

		onReceiver = new ScreenOnReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(onReceiver, filter2);

		offReceiver = new ScreenOffReceiver();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(offReceiver, filter1);

		currenNotLock = new CurrenNotLock();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.hpuvoice.currentunlock");
		registerReceiver(currenNotLock, filter);

		runWatchDog();
	}

	private void runWatchDog() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				while (flag) {
					ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					for (RunningTaskInfo runningTaskInfo : runningTasks) {
						String packagename = runningTaskInfo.baseActivity
								.getPackageName();
						if (queryAll.contains(packagename)) {
							if (!packagename.equals(currentpkg)) {

								Intent intentwacth = new Intent(
										getApplicationContext(),
										WatchDogActivity.class);
								intentwacth
										.putExtra("packagename", packagename);
								intentwacth
										.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intentwacth);
							}
						}
					}

				}
			}
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
		if (currenNotLock != null) {
			unregisterReceiver(currenNotLock);
			currenNotLock = null;
		}
		if (onReceiver != null) {
			unregisterReceiver(onReceiver);
			onReceiver = null;
		}
		if (offReceiver != null) {
			unregisterReceiver(offReceiver);
			offReceiver = null;
		}
	}
}
