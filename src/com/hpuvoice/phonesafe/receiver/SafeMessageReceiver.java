package com.hpuvoice.phonesafe.receiver;

import com.hpuvoice.phonesafe.OpenSuperAdmin;
import com.hpuvoice.phonesafe.R;
import com.hpuvoice.phonesafe.service.GPSForService;


import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;


public class SafeMessageReceiver extends BroadcastReceiver{

	private SharedPreferences sp;
	private DevicePolicyManager manager;
	@Override
	public void onReceive(Context context, Intent intent) {
		manager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String safe_num = sp.getString("safe_num", "5556");
		for (Object object : objs) {
			SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
			String num = msg.getOriginatingAddress();
			num =  num.replace("-", "").trim();
			String body = msg.getMessageBody();
			body = body.trim();
			if(num.contains(safe_num)){
			switch (body) {
			case "#location#":
				Intent gpsserviceIntent = new Intent(context,GPSForService.class);
				context.startService(gpsserviceIntent);
				String lastlocation = sp.getString("lastlocation", "");
				if (TextUtils.isEmpty(lastlocation)) {
					SmsManager.getDefault().sendTextMessage(safe_num,
							null, "getting location !!!!!", null, null);
				} else {
					SmsManager.getDefault().sendTextMessage(safe_num,
							null, "localtion"+lastlocation, null, null);
				}
				abortBroadcast();
				break;
			case "#alarm#":
				AudioManager audioManager = (AudioManager) context
											.getSystemService(Context.AUDIO_SERVICE);
				int streamMaxVolume = audioManager
											.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取到指定声音类型的最大音量
				//设置声音的音量 参数1声音的类型
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				streamMaxVolume, 0);
				// 加载音乐资源
				MediaPlayer mediaPlayer =MediaPlayer.create(context, R.raw.alarm);
				//TODO   发布需要做的事情----------------------------------------
				//mediaPlayer.setLooping(true);
				mediaPlayer.setVolume(1.0f, 1.0f);
				mediaPlayer.start();
				abortBroadcast();
				break;
			case "#wipedata#":
				ComponentName whose = new ComponentName(context,"com.hpuvoice.phonesafe.receiver.SuperAdminReceiver");
				if (manager.isAdminActive(whose)) {
					manager.wipeData(0);//恢复成出厂设置
					//sdcard格式化或者说数据全部被删除
					//			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				}else{
					Intent intent2 =new Intent(context,OpenSuperAdmin.class);
					context.startActivity(intent2);
				}
				abortBroadcast();
				break;
			case "#lockscreen#":
					ComponentName who = new ComponentName(context,"com.hpuvoice.phonesafe.receiver.SuperAdminReceiver");
							if (manager.isAdminActive(who)) {
								manager.resetPassword(safe_num, 0);// 重新设置了密码,绑定的手机号
								manager.lockNow();// 锁屏
							}else{
								Intent intent2 =new Intent(context,OpenSuperAdmin.class);
								context.startActivity(intent2);
							}
				abortBroadcast();
				break;
			}
		}
	}
	}

}
