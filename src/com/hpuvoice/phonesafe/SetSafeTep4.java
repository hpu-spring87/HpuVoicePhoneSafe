package com.hpuvoice.phonesafe;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SetSafeTep4 extends Activity {

	private CheckBox cb_protecting;
	private SharedPreferences sp;
	private GestureDetector detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe_4);
		detector=new GestureDetector(this, new MySimpleOnGestureListener());
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_protecting = (CheckBox) findViewById(R.id.cb_protecting);
		boolean protectting = sp.getBoolean("protectting", true);
		if(protectting){
			cb_protecting.setText("当前状态：手机防盗保护已经开启");
			openSuperAdmin();
		}else{
			cb_protecting.setText("当前状态：手机防盗保护已经关闭");
		}
		cb_protecting.setChecked(protectting);
		
		cb_protecting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor editor = sp.edit();
				editor.putBoolean("protectting", isChecked);
				editor.commit();
				if(isChecked){
					cb_protecting.setText("当前状态：手机防盗保护已经开启");
					openSuperAdmin();
				}else{
					cb_protecting.setText("当前状态：手机防盗保护已经关闭");
				}
			}
		});
	}
	
	private void openSuperAdmin() {
		Intent intent =new Intent(SetSafeTep4.this,OpenSuperAdmin.class);
		startActivity(intent);
	}
	//--------------定义手势监听类-----------------------------------------------
		private class MySimpleOnGestureListener extends SimpleOnGestureListener{
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				float startX=e1.getRawX();
				float endX=e2.getRawX();
				float startY=e1.getRawY();
				float endY=e2.getRawY();
				if(Math.abs(startY-endY)>50){
					return true;
				}
				if((startX-endX)>100){
					nextpage();
				}else if((endX-startX)>100){
					prepage();
				}
				return true;
			}
			
		}
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			detector.onTouchEvent(event);
			return super.onTouchEvent(event);
		}
		//--------------定义手势监听类-----------------------------------------------
	
	public void next(View view){
		nextpage();
	}
	
	public void nextpage(){
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		Intent intent = new Intent(this,SafeActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}
	
	public void pre(View view){
		prepage();
	}
	public void prepage(){
		Intent intent = new Intent(this,SetSafeTep3.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	
}
