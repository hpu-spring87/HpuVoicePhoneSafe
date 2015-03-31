package com.hpuvoice.phonesafe;

import com.hpuvoice.phonesafe.ui.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SetSafeTep2 extends Activity {

	private SettingItemView siv_bind_sim;
	private SharedPreferences sp;
	private TelephonyManager tm;
	private GestureDetector detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe_2);
		detector=new GestureDetector(this, new MySimpleOnGestureListener());
		sp = getSharedPreferences("config", MODE_PRIVATE);
		siv_bind_sim = (SettingItemView) findViewById(R.id.siv_bind_sim);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String sim = sp.getString("sim", null);
		siv_bind_sim.setChecked(!TextUtils.isEmpty(sim));
		
		siv_bind_sim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if(siv_bind_sim.isChecked()){
					siv_bind_sim.setChecked(false);
					editor.putString("sim", null);
				}else{
					siv_bind_sim.setChecked(true);
					String sim = tm.getSimSerialNumber();
					editor.putString("sim", sim);
				}
				editor.commit();
			}
		});
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
		String sim = sp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			Toast.makeText(getApplicationContext(), "你還沒用綁定SIM卡哦！！", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(this,SetSafeTep3.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}
	public void pre(View view){
		prepage();
	}
	
	public void prepage(){
		Intent intent = new Intent(this,SetSafeTep1.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	
}
