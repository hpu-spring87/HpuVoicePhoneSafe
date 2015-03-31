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
			//			dpm.wipeData(0);//�ָ��ɳ�������
			//sdcard��ʽ������˵����ȫ����ɾ��
			//			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
		}else{
			openAdmin();
		}
	}
	
	public void wipeall(){
		ComponentName  who  = new ComponentName(this,SuperAdminReceiver.class);
		if(dPolicyManager.isAdminActive(who)){
			dPolicyManager.wipeData(0);//�ָ��ɳ�������
			//sdcard��ʽ������˵����ȫ����ɾ��
			//			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
		}else{
			openAdmin();
		}
	}
	
	private void openAdmin() {
	        //������ͼ������������豸����Ա
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			//�����
			ComponentName  mDeviceAdminSample  = new ComponentName(this,SuperAdminReceiver.class);
			//��Ȩ���Ǹ����
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
					mDeviceAdminSample);
			//��ӽ�˵
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					"���ҿ���һ�����������Խ����������ֹ͵��,�Լ���������Ĺ��ܣ���ֵ��ӵ�С�");
			startActivity(intent);
	}
	
	public void unInstall(View view){
		//1.����ʧȥ�����豸����Ա
		ComponentName  who  = new ComponentName(this,SuperAdminReceiver.class);
		dPolicyManager.removeActiveAdmin(who);
		//2.������ͨӦ��
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
