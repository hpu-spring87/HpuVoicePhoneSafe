package com.hpuvoice.phonesafe;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class LoginAppActivity extends Activity {

	WebView wv_loginapp;
	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginapp);
		
		wv_loginapp = (WebView) findViewById(R.id.wv_loginapp);
		
		wv_loginapp.getSettings().setJavaScriptEnabled(true);
		
		wv_loginapp.loadUrl("file:///android_asset/login.html");
	}
	
	public void gohome(View view){
		Intent setIntent = new Intent(this,SetCenterActivity.class);
		startActivity(setIntent);
		finish();
	}
}
