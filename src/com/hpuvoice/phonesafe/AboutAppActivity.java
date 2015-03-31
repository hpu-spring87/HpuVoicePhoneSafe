package com.hpuvoice.phonesafe;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class AboutAppActivity extends Activity {

	WebView wv_aboutapp;
	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutapp);
		
		wv_aboutapp = (WebView) findViewById(R.id.wv_aboutapp);
		
		wv_aboutapp.getSettings().setJavaScriptEnabled(true);
		
		wv_aboutapp.loadUrl("file:///android_asset/about.html");
	}
	
	public void gohome(View view){
		Intent setIntent = new Intent(this,SetCenterActivity.class);
		startActivity(setIntent);
		finish();
	}
}
