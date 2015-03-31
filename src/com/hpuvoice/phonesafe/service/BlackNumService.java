package com.hpuvoice.phonesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.hpuvoice.phonesafe.dao.BlackNumDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class BlackNumService extends Service {

	private class SMSReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objs) {
				SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
				String num = msg.getOriginatingAddress();
				num = num.replace("-", "").trim();
				String body = msg.getMessageBody();
				body = body.trim();
				boolean isNULL = blcakDao.isNull(num);
				if (!isNULL) {
					abortBroadcast();
				}
			}
		}

	}

	BroadcastReceiver receiver;
	BlackNumDao blcakDao;
	MyPhoneState myPhoneState;
	TelephonyManager telmanger;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// --------拦截短信---------
		receiver = new SMSReceiver();
		blcakDao = new BlackNumDao(getApplicationContext());
		IntentFilter filter = new IntentFilter();
		filter.setPriority(Integer.MAX_VALUE);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, filter);

		// ------拦截电话-----------
		telmanger = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		myPhoneState = new MyPhoneState();
		telmanger.listen(myPhoneState, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class MyPhoneState extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				boolean isNULL = blcakDao.isNull(incomingNumber);
				if (!isNULL) {
					stopcall();
					// ----删除通话记录----
					final Uri uri = Uri.parse("content://call_log/calls");
					final ContentResolver resolver = getContentResolver();
					resolver.registerContentObserver(uri, true,
							new ContentObserver(new Handler()) {

								@Override
								public void onChange(boolean selfChange) {
									super.onChange(selfChange);
									resolver.delete(uri, "number=?",
											new String[] { incomingNumber });
									resolver.unregisterContentObserver(this);
								}

							});
				}
				break;
			}
		}
	}

	public void stopcall() {
		try {
			Class<?> loadClass = BlackNumService.class.getClassLoader()
					.loadClass("android.os.ServiceManager");
			Method method = loadClass.getDeclaredMethod("getService",
					String.class);
			IBinder ibinder = (IBinder) method.invoke(null,
					Context.TELEPHONY_SERVICE);
			ITelephony asInterface = ITelephony.Stub.asInterface(ibinder);
			asInterface.endCall();
			// asInterface.cancelMissedCallsNotification();
			// asInterface.answerRingingCall();//需呀系统软件的权限
			// asInterface.call("1-555-521-5554");//有电话进入激活拨打其它电话必须跟endCall()结合使用

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		telmanger.listen(myPhoneState, PhoneStateListener.LISTEN_NONE);
		myPhoneState = null;

		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}

}
