package com.hpuvoice.phonesafe;

import com.hpuvoice.phonesafe.receiver.SuperAdminReceiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class OpenSuperAdmin extends Activity {

	private DevicePolicyManager dPolicyManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dPolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		openAdmin();
		finish();
	}
	
	public void lockScreen() {
		ComponentName  who  = new ComponentName(this,SuperAdminReceiver.class);
		if(dPolicyManager.isAdminActive(who)){
			dPolicyManager.lockNow();
			dPolicyManager.resetPassword("fuckyou", 0);		
			//			dpm.wipeData(0);//恢复成出厂设置
			//sdcard格式化或者说数据全部被删除
			//			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
		}else{
			openAdmin();
		}
	}
	
	public void wipeall(){
		ComponentName  who  = new ComponentName(this,SuperAdminReceiver.class);
		if(dPolicyManager.isAdminActive(who)){
			dPolicyManager.wipeData(0);//恢复成出厂设置
			//sdcard格式化或者说数据全部被删除
			//			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
		}else{
			openAdmin();
		}
	}
	
	private void openAdmin() {
	        //定义意图，动作是添加设备管理员
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			//组件名
			ComponentName  mDeviceAdminSample  = new ComponentName(this,SuperAdminReceiver.class);
			//授权给那个组件
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
					mDeviceAdminSample);
			//添加解说
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					"打开我可以一键锁屏，可以禁用相机，防止偷拍,以及其他逆天的功能，你值得拥有。");
			startActivity(intent);
	}
	
	public void unInstall(View view){
		//1.让其失去超级设备管理员
		ComponentName  who  = new ComponentName(this,SuperAdminReceiver.class);
		dPolicyManager.removeActiveAdmin(who);
		//2.当成普通应用
		Intent intent = new Intent();
//		  <action android:name="android.intent.action.VIEW" />
//          <action android:name="android.intent.action.DELETE" />
//          <category android:name="android.intent.category.DEFAULT" />
//          <data android:scheme="package" />
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+getPackageName()));
		startActivity(intent);
		finish();
	}
}
