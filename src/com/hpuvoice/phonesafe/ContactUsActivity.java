package com.hpuvoice.phonesafe;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class ContactUsActivity extends Activity {

	WebView wv_contactus;
	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactus);
		
		wv_contactus = (WebView) findViewById(R.id.wv_contactus);
		
		wv_contactus.getSettings().setJavaScriptEnabled(true);
		
		wv_contactus.loadUrl("file:///android_asset/contact-us.html");
	}
	
	public void gohome(View view){
		Intent setIntent = new Intent(this,SetCenterActivity.class);
		startActivity(setIntent);
		finish();
	}
}
