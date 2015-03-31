package com.hpuvoice.phonesafe.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.hpuvoice.phonesafe.R;
import com.hpuvoice.phonesafe.receiver.ScreenOffReceiver;
import com.hpuvoice.phonesafe.receiver.WidgetsReceiver;
import com.hpuvoice.phonesafe.utils.TaskUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class AppWidgetsService extends Service {

	Timer timer;
	private CleanProcess cleanProcess;
	private ScreenOffReceiver screenoff;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	/**
	 * ----自定义广播---
	 */
	private class CleanProcess extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
			for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
				if(runningAppProcessInfo.processName.equals(getPackageName())){
					continue;
				}
				am.killBackgroundProcesses(runningAppProcessInfo.processName);
			}
		}
		
	}
	

	@Override
	public void onCreate() {
		super.onCreate();
		screenoff = new ScreenOffReceiver();
		IntentFilter screenfilter = new IntentFilter();
		screenfilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenoff, screenfilter);
		
		
		cleanProcess = new CleanProcess();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.hpuvoice.phonewidgets");
		registerReceiver(cleanProcess, filter );
		final AppWidgetManager awm = AppWidgetManager.getInstance(this);
		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				ComponentName provider = new ComponentName(
						AppWidgetsService.this, WidgetsReceiver.class);
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.app_home_widget);
				views.setTextViewText(R.id.tv_home_running, "Running Apps:"
						+ TaskUtils.getRunningCount(getApplicationContext()));
				views.setTextViewText(
						R.id.tv_home_memory,
						"Avaliable RAM::"
								+ Formatter.formatFileSize(
										getApplicationContext(),
										TaskUtils
												.avaliableRAM(getApplicationContext())));
				Intent intentupdate = new Intent("com.hpuvoice.phonewidgets");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intentupdate , PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_home_clear, pendingIntent );
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 1000, 2000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if(cleanProcess != null){
			unregisterReceiver(cleanProcess);
			cleanProcess = null;
		}
		if(screenoff != null){
			unregisterReceiver(screenoff);
			screenoff = null;
		}
	}

}
