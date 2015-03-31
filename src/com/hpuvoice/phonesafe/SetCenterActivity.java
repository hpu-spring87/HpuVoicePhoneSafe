package com.hpuvoice.phonesafe;


import com.hpuvoice.phonesafe.adapter.SetCenterAdapterItem;
import com.hpuvoice.phonesafe.service.WatchDogService;
import com.hpuvoice.phonesafe.ui.SettingItemView;
import com.hpuvoice.phonesafe.utils.ServiceUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SetCenterActivity extends Activity {

	private ListView lv_set_center;
	@ViewInject(R.id.cb_state_applock)
	private SettingItemView cb_state_applock;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setcenter);
		ViewUtils.inject(this);
		lv_set_center = (ListView) findViewById(R.id.lv_set_center);
		SetCenterAdapterItem scai = new SetCenterAdapterItem(getApplicationContext());
		lv_set_center.setAdapter(scai);
		lv_set_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Intent contactIntent = new Intent(SetCenterActivity.this,ContactUsActivity.class);
					startActivity(contactIntent);
					finish();
					break;
				case 1:
					Intent aboutIntent = new Intent(SetCenterActivity.this,AboutAppActivity.class);
					startActivity(aboutIntent);
					finish();
					break;

				default:
					break;
				}
			}
		});
	}
	
	/**
	 * µÇÂ½----
	 * @param view
	 */
		public void login(View view){
			Intent aboutIntent = new Intent(SetCenterActivity.this,LoginAppActivity.class);
			startActivity(aboutIntent);
			finish();
		}
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		isCheckAppLock();
	}
	/**
	 * ÊÇ·ñ¿ªÆô³ÌÐòËø
	 */
	public void isCheckAppLock() {
		if (ServiceUtils.isServiceRunning(this,
				"com.hpuvoice.phonesafe.service.WatchDogService")) {
			cb_state_applock.setChecked(true);
		} else {
			cb_state_applock.setChecked(false);
		}
		cb_state_applock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentblacknumber = new Intent(
						SetCenterActivity.this, WatchDogService.class);
				if (cb_state_applock.isChecked()) {
					stopService(intentblacknumber);
					cb_state_applock.setChecked(false);
				} else {
					startService(intentblacknumber);
					cb_state_applock.setChecked(true);
				}
			}
		});
	}
	public void gohome(View view){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}
}
