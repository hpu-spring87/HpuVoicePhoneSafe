package com.hpuvoice.phonesafe.ui;

import com.hpuvoice.phonesafe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class SelfDefineToastView extends View {

   
	private  Context context;
	private  WindowManager wmManager;
	private  View view;
	private  SharedPreferences sp;
	WindowManager.LayoutParams params;
	public SelfDefineToastView(Context context) {
		super(context);
		this.context = context;
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	@SuppressWarnings("deprecation")
	public  void callingToast(String address){
	    wmManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    view = View.inflate(context, R.layout.toast_selfdefine, null);
	   
	    params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	    params.format = PixelFormat.TRANSLUCENT;
	    //params.type = WindowManager.LayoutParams.TYPE_TOAST;----Toast不可触摸
	    params.type=WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
	    params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
	            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	           //| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
	    params.gravity=Gravity.LEFT|Gravity.TOP;
	    //params.x = sp.getInt("left", 100);
	    //params.y = sp.getInt("top", 100);
	    params.x = 100;
	    params.y = 300;
	    if(params.x < 0){
			params.x = 0;
		}
		
		if(params.y < 0){
			params.y = 0;
		}
		
		if(params.x > wmManager.getDefaultDisplay().getWidth()-view.getWidth()){
			params.x= wmManager.getDefaultDisplay().getWidth()-view.getWidth();
		}
		
		if(params.y > wmManager.getDefaultDisplay().getHeight()-view.getHeight()){
			params.y= wmManager.getDefaultDisplay().getHeight()-view.getHeight();
		}
		wmManager.addView(view, params );
		 touch();
		
		int[] bgcolor = new int[] { 
				R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		TextView iv_toast_show = (TextView) view.findViewById(R.id.tv_toast_show);
		int styleid = sp.getInt("styleId", 0);
		
		iv_toast_show.setTextColor(Color.WHITE);
		iv_toast_show.setText(address);
		iv_toast_show.setBackgroundResource(bgcolor[styleid]);
	}
	
	public void touch() {
		view.setOnTouchListener(new OnTouchListener() {
			
			private int startX;
			private int startY;

			@SuppressWarnings("deprecation")
			@SuppressLint("ClickableViewAccessibility") @Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) { 
				case MotionEvent.ACTION_DOWN: 
					System.out.println("按下");
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE: 
					System.out.println("移动");
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dX = newX - startX;
					int dY = newY - startY;
					WindowManager.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
					layoutParams.x+=dX;
					layoutParams.y+=dY;
					if(layoutParams.x < 0){
						layoutParams.x = 0;
					}
					if(layoutParams.x >wmManager.getDefaultDisplay().getWidth() ){
						layoutParams.x  = wmManager.getDefaultDisplay().getWidth() ;
					}
					if(layoutParams.y < 0){
						layoutParams.y = 0;
					}
					if(layoutParams.y > wmManager.getDefaultDisplay().getHeight()){
						layoutParams.y = wmManager.getDefaultDisplay().getHeight();
					}
					wmManager.updateViewLayout(view, layoutParams);
					startX = newX;
					startY = newY;

					break;
				case MotionEvent.ACTION_UP:// 抬起
					System.out.println("抬起");
					WindowManager.LayoutParams params =  (LayoutParams) view.getLayoutParams();
					int left=params.x;
					int top=params.y;
					Editor edit = sp.edit();
					edit.putInt("left", left);
					edit.putInt("top", top);
					edit.commit();

				}
				// True if the listener has consumed the event, false otherwise.
				// true 事件执行完了 false 没有执行完
				return true;
			}

		});
	}
	
	public  void hidencallingtoast(){
		if(wmManager!=null && view != null){
			wmManager.removeView(view);
		}
	}
	
}
