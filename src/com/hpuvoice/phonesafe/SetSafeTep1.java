package com.hpuvoice.phonesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class SetSafeTep1 extends Activity {

	private GestureDetector detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe_1);
		detector=new GestureDetector(this, new MySimpleOnGestureListener());
	}
	
	
	public void next(View view){
		nextpage();
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
				return true;
			}
			return true;
		}
		
	}
	public void nextpage(){
		Intent intent = new Intent(this,SetSafeTep2.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	//--------------定义手势监听类-----------------------------------------------
}
