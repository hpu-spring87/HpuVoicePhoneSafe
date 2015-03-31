package com.hpuvoice.phonesafe;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility") public class DragToastActivity extends Activity {

	@ViewInject(R.id.rl_location)
	private LinearLayout rl_location;
	@ViewInject(R.id.tv_bottom)
	private TextView tv_bottom;
	@ViewInject(R.id.tv_top)
	private TextView tv_top;
	private SharedPreferences sp;
	private int screenWidth;
	private int screenheight;
	@SuppressLint("ClickableViewAccessibility") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		setContentView(R.layout.activity_dragtoast);
		ViewUtils.inject(this);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenheight = dm.heightPixels;
		int left = sp.getInt("left", 0);
		int top = sp.getInt("top", 0);
		
		RelativeLayout.LayoutParams layoutParams =  (LayoutParams) rl_location.getLayoutParams();
		layoutParams.leftMargin = left;
		layoutParams.topMargin = top;
		rl_location.setLayoutParams(layoutParams);
		
		touchListener();
		doubleClick();
	}
	
	long[] mHits = new long[2];
	private void doubleClick() {
		rl_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1); 
				mHits[mHits.length - 1] = SystemClock.uptimeMillis(); 
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) { 
					int left=(screenWidth-rl_location.getWidth())/2;
					int top=(screenheight-rl_location.getHeight())/2;
					
					rl_location.layout(left, top, left+rl_location.getWidth(), top+rl_location.getHeight());
				}
			}
		});
	}

	private void touchListener() {
		rl_location.setOnTouchListener(new OnTouchListener() {
			private int startX;
			private int startY;
			private int newX;
			@SuppressLint("ClickableViewAccessibility") private int newY;
			@SuppressLint("CommitPrefEdits") @Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					 startX = (int) event.getRawX();
					 startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					 newX = (int) event.getRawX();
					 newY = (int) event.getRawY();
					
					int dX = newX-startX;
					int dY = newY-startY;
					int l = rl_location.getLeft()+dX;
					int t = rl_location.getTop()+dY;
					int r = l+rl_location.getWidth();
					int b = t+rl_location.getHeight();
					rl_location.layout(l, t, r, b);
					if (l < 0 || t < 0 || r > screenWidth
							|| b > screenheight - 30) {
						break;
					}
					if (t > screenheight / 2) {
						// 上面的可见
						tv_top.setVisibility(View.VISIBLE);
						// 下面不可见
						tv_bottom.setVisibility(View.INVISIBLE);
					} else {
						// 上面的可见
						tv_bottom.setVisibility(View.VISIBLE);
						// 下面不可见
						tv_top.setVisibility(View.INVISIBLE);
					}
					rl_location.layout(l, t, r, b);// 重新分配控件的位置
					// 步骤5
					startX = newX;
					startY = newY;
					break;
				case MotionEvent.ACTION_UP:
					int left =  rl_location.getLeft();
					int top =  rl_location.getTop();
					Editor editor = sp.edit();
					editor.putInt("left", left);
					editor.putInt("top", top);
					editor.commit();
					break;
				default:
					break;
				}
				return false;
			}
		});
	}
}
