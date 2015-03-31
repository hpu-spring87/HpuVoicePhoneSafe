package com.hpuvoice.phonesafe.service;

import com.hpuvoice.phonesafe.dao.QueryAddressDao;
import com.hpuvoice.phonesafe.ui.SelfDefineToastView;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallingShowAddressService extends Service {

	private TelephonyManager tm;
	private QueryAddressDao qAddressDao;
    private SelfDefineToastView selftoast;
    CallingListener listener;
	private OutCallReceiver outCallReceiver;
	@SuppressWarnings("unused")
	private SharedPreferences sp;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new CallingListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		qAddressDao = new QueryAddressDao(getApplicationContext());
		
		selftoast = new SelfDefineToastView(getApplicationContext());
		
		outCallReceiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(outCallReceiver, filter );
	}
	
	private class CallingListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			 
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
					selftoast.hidencallingtoast();
				//-----------------------------------
				//hideToast();
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				String text = qAddressDao.queryAddress(incomingNumber);
				if(text!=null){
					selftoast.callingToast(text);
				}else{
					selftoast.callingToast("no such num");
				}
				//-----------------------------------
				//showMyToast(text);
				break;
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		
		if(outCallReceiver != null ){
		unregisterReceiver(outCallReceiver);
		outCallReceiver = null;
		}
	}
	
	
	//-------------定义广播接受，监听外拨电话---------------------------------
	private class OutCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String outcallnum = getResultData();
			String text = qAddressDao.queryAddress(outcallnum);
			selftoast.callingToast(text);
			//showMyToast(text);
		}
		
	}
	//-------------定义广播接受，监听外拨电话---------------------------------
}
