package com.hpuvoice.phonesafe;

import com.hpuvoice.phonesafe.utils.MD5Encoder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WatchDogActivity extends Activity {

	@ViewInject(R.id.tv_lock_name)
	TextView tv_lock_name;
	@ViewInject(R.id.iv_lock_icon)
	ImageView iv_lock_icon;
	@ViewInject(R.id.et_unlock_pwd)
	EditText et_unlock_pwd;
	String pkgString;
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watchdog);
		ViewUtils.inject(this);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		Intent intent = getIntent();
		 pkgString = intent.getStringExtra("packagename");
		
		if(pkgString != null){
			PackageManager pm = getPackageManager();
			try {
				PackageInfo info = pm.getPackageInfo(pkgString, 0);
				CharSequence loadLabel = info.applicationInfo.loadLabel(pm).toString();
				Drawable drawable = info.applicationInfo.loadIcon(pm);
				
				tv_lock_name.setText(loadLabel);
				iv_lock_icon.setImageDrawable(drawable);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 解锁--
	 */
	public void unlock(View view){
		String putpwd = et_unlock_pwd.getText().toString().trim();
		String setpwd = sp.getString("password", "");
		if(!TextUtils.isEmpty(setpwd)){
			if(MD5Encoder.disgest(putpwd).equals(setpwd)){
				finish();
				Intent unlockintent = new Intent("com.hpuvoice.currentunlock");
				unlockintent.putExtra("currentpkg", pkgString);
				sendBroadcast(unlockintent);
			}else{
				Toast.makeText(getApplicationContext(), "密码输入不正确！！！密码为你设置手机防盗密码.", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(getApplicationContext(), "你还没有设置密码哦..", Toast.LENGTH_SHORT).show();
			finish();
			Intent unlockintent = new Intent("com.hpuvoice.currentunlock");
			unlockintent.putExtra("currentpkg", pkgString);
			sendBroadcast(unlockintent);
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
	@Override
	public void onBackPressed() {
		goHomeActivity();
	}
	
	public void gohome(View view){
		goHomeActivity();
	}
	public void goHomeActivity(){
		Intent intent=new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		startActivity(intent);
		finish();
	}

}
