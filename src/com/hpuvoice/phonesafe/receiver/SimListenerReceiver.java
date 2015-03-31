package com.hpuvoice.phonesafe.receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;


public class SimListenerReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		if(sp.getBoolean("protectting", false)){
			String saveSim = sp.getString("sim", "");
			tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String phonenum = tm.getLine1Number();
			String curSim = tm.getSimSerialNumber();
			if(saveSim.equals(curSim)){
				//----监听sim卡正常
			}else{
				Toast.makeText(context, "sim card 已经变更.....", Toast.LENGTH_SHORT).show();
				System.out.println("sim card 已经变更.....");
				SmsManager.getDefault().sendTextMessage(sp.getString("safe_num", ""), null, "sim changge from phone...."+phonenum, null, null);
			}
		}
	}

}
