package com.hpuvoice.phonesafe;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
public class CacheClearActivity extends FragmentActivity {

	private SdcardClearFragment sFragment;
	private PhoneClearFragment pFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cacheclear);

		sFragment = new SdcardClearFragment();
		pFragment = new PhoneClearFragment();

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		beginTransaction.add(R.id.rl_container, pFragment);
		beginTransaction.commit();
 
	}

	/**
	 * 缓存清理-1----
	 * 
	 * @param view
	 */
	public void clearApp(View view) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		beginTransaction.replace(R.id.rl_container, pFragment);
		beginTransaction.commit();
	}

	/**
	 * 缓存清理-3----
	 * 
	 * @param view
	 */
	public void clearSD(View view) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		beginTransaction.addToBackStack(null);
		beginTransaction.replace(R.id.rl_container, sFragment);
		beginTransaction.commit();
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
