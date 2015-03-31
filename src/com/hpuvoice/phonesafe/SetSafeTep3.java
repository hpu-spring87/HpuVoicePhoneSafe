package com.hpuvoice.phonesafe;

import com.hpuvoice.phonesafe.engine.ReadContact;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.EditText;
import android.widget.Toast;

public class SetSafeTep3 extends Activity {

	private EditText et_safe_number;
	private SharedPreferences sp;
	private GestureDetector detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stubh
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe_3);
		detector=new GestureDetector(this, new MySimpleOnGestureListener());
		sp = getSharedPreferences("config", MODE_PRIVATE);
		et_safe_number = (EditText) findViewById(R.id.et_safe_number);
		String safenum = sp.getString("safe_num", "");
		et_safe_number.setText(safenum);
		
	}
	//==============获取联系人=======================================
	public void selectContact(View view){
		Intent intent = new Intent(this, ReadContact.class);
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;

		String number = data.getStringExtra("number").replace("-", "");
		et_safe_number.setText(number);
	}
	//==============获取联系人=======================================
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
		String safe_num = et_safe_number.getText().toString().trim();
		if(TextUtils.isEmpty(safe_num)){
			Toast.makeText(getApplicationContext(), "請綁定安全號碼，爲了手機安全找回.", Toast.LENGTH_SHORT).show();
			return;
		}
		Editor editor = sp.edit();
		editor.putString("safe_num",safe_num);
		editor.commit();
		Intent intent = new Intent(this,SetSafeTep4.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}
	
	public void pre(View view){
		prepage();
	}
	
	public void prepage(){
		Intent intent = new Intent(this,SetSafeTep2.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
}
