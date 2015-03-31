package com.hpuvoice.phonesafe;


import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AntiVirusActivity extends Activity{

	TextView tv_kill_tips;
	PackageManager pm;
	LinearLayout ll_show_kill;
	Button bt_kill_all;
	ProgressBar pb_show_kill;
	ImageView iv_cache;
	ImageView iv_cache_bak;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antivirus);
		//------------------------初始化数据----------------------
		
		pm = getPackageManager();
		tv_kill_tips = (TextView) findViewById(R.id.tv_kill_tips);
		bt_kill_all = (Button) findViewById(R.id.bt_kill_all);
		ll_show_kill = (LinearLayout) findViewById(R.id.ll_show_kill);
		pb_show_kill = (ProgressBar) findViewById(R.id.pb_show_kill);
		iv_cache = (ImageView) findViewById(R.id.iv_cache);
		iv_cache_bak = (ImageView) findViewById(R.id.iv_cache_bak);

		//------------------------初始化数据----------------------

		// -----------------首页旋转动画-------------------------
		RotateAnimation animation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(2000);
		animation.setRepeatCount(Animation.INFINITE);
		iv_cache.setAnimation(animation);
		
		RotateAnimation animationbak = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationbak.setDuration(300);
		animationbak.setRepeatCount(Animation.INFINITE);
		iv_cache_bak.setAnimation(animationbak);
		// -----------------首页旋转动画-------------------------
		//-----------------------接受意图数据---------------------
		Intent getIntent = getIntent();
		String is_kill_simple = getIntent.getStringExtra("kill_simple");
		if(is_kill_simple != null){
			showKill();
			bt_kill_all.setText("正在卖力查杀");
		}
		//-----------------------接受意图数据---------------------
		
	}
	
	

	/**
	 * handle处理结果---
	 */
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.toString().contains("正在扫描")){
				TextView tv_item = new TextView(AntiVirusActivity.this);
				tv_item.setTextColor(Color.GREEN);
				tv_item.setTextSize(13f);
				tv_item.setText(msg.obj.toString());
				ll_show_kill.addView(tv_item, 0);
			}
			
			if(msg.toString().contains("扫描完毕")){
				pb_show_kill.setVisibility(View.INVISIBLE);
				tv_kill_tips.setText(msg.obj.toString());
				bt_kill_all.setText("深度查杀");
			}
			
		};
	};
	
	public void kill_all(View view){
		showKill();
		bt_kill_all.setText("正在卖力查杀");
	}
	/**
	 * 病毒查杀---
	 */
	private void showKill() {
		
		new Thread() {

			@Override
			public void run() {
				super.run();
				List<PackageInfo> infos = getPackageManager()
						.getInstalledPackages(
								PackageManager.GET_UNINSTALLED_PACKAGES
										| PackageManager.GET_SIGNATURES);
				int virustotal = 0;
				int count = 1;
				pb_show_kill.setMax(infos.size());
				for (PackageInfo info : infos) {
					try {
						sleep(300);
						Message msg = Message.obtain();

						String packname = info.packageName;
						ApplicationInfo appinfo = pm
								.getPackageInfo(packname, 0).applicationInfo;
						String appname = appinfo.loadLabel(pm).toString();
						msg.obj = "正在扫描:" + appname;
						handler.sendMessage(msg);
						// 获得签名
						//Signature[] signs = info.signatures;
						//String str = signs[0].toCharsString();
						//String md5 = MD5Encoder.disgest(str);
						pb_show_kill.setProgress(count);
						count++;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				Message msg = Message.obtain();
				msg.what = 88;
				msg.obj = "扫描完毕 ,共发现" + virustotal + "个病毒";
				handler.sendMessage(msg);
				//iv_cache.clearAnimation();
				//iv_cache_bak.clearAnimation();
			}

		}.start();
	}

	 	/**
		 * 返回主界面
		 * 
		 * @param view
		 */
		public void gohome(View view) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
}
