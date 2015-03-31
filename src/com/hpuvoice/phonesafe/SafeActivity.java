package com.hpuvoice.phonesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SafeActivity extends Activity {
	
	private TextView tv_safe_number;
	private ImageView iv_protectting_status;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
		setContentView(R.layout.activity_safe);
		tv_safe_number = (TextView) findViewById(R.id.tv_safe_number);
		iv_protectting_status = (ImageView) findViewById(R.id.iv_protectting_status);
		
		String safe_num = sp.getString("safe_num", null);
		tv_safe_number.setText(safe_num);
		
		boolean isprotected = sp.getBoolean("protecting", true);
		if(isprotected){
			iv_protectting_status.setImageResource(R.drawable.show_cat_1);
		}else{
			iv_protectting_status.setImageResource(R.drawable.show_cat_2);
		}
		}else{
			Intent intent = new Intent(this,SetSafeTep1.class);
			startActivity(intent);
			finish();
		}
		
	}
	
	public void reEnterSetting(View view){
		Intent intent = new Intent(this,SetSafeTep1.class);
		startActivity(intent);
		finish();
	}
	public void showhelp(View view){
		Builder builder = new Builder(this);
		builder.setIcon(R.drawable.helpicon);
		builder.setTitle("-ʹ��С��ʾ-");
		builder.setMessage("    �ֻ������غ����Ұ�ȫ����һ��һ������С�ĵ����ã���Ȼ��ȥ��Ҳ�ˮ��" +
				"�м��м�...");
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void gohome(View view){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}
}
