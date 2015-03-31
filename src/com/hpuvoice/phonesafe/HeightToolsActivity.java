package com.hpuvoice.phonesafe;

import com.hpuvoice.phonesafe.adapter.HeightToolsAdapterItem;
import com.hpuvoice.phonesafe.dao.QueryAddressDao;
import com.hpuvoice.phonesafe.service.CallingShowAddressService;
import com.hpuvoice.phonesafe.ui.ListviewItemView;
import com.hpuvoice.phonesafe.ui.SettingItemView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HeightToolsActivity extends Activity {

	@ViewInject(R.id.lv_height_tools)
	private ListView lv_height_tools;
	@ViewInject(R.id.et_num)
	private EditText et_num;
	@ViewInject(R.id.cb_calling_isshowaddress)
	private SettingItemView cb_calling_isshowaddress;
	@ViewInject(R.id.liv_showstyle)
	private ListviewItemView liv_showstyle;
	@ViewInject(R.id.liv_show_location)
	private ListviewItemView liv_show_location;
	private SharedPreferences sp;
	final String[] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heighttools);
		ViewUtils.inject(this);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean isopencallshow = sp.getBoolean("isopencallshow", false);
		cb_calling_isshowaddress.setChecked(isopencallshow);
		
		int styleid = sp.getInt("styleId", 0);
		String liv_textString = "切换归属地显示风格"+"-->"+items[styleid];
	    liv_showstyle.setText(liv_textString);
		liv_showstyle.setIcons(R.drawable.item_1);
		
		liv_show_location.setText("自定义归属地显示位置");
		liv_showstyle.setIcons(R.drawable.item_2);
		
		HeightToolsAdapterItem hItem = new HeightToolsAdapterItem(getApplicationContext());
		cb_calling_isshowaddress.setDesc("选择来电显示归属地");
		boolean isshowcalling = sp.getBoolean("isshowcalling", false);
		cb_calling_isshowaddress.setChecked(isshowcalling);
		if(isshowcalling){
			Intent intentcalling = new Intent(getApplicationContext(),CallingShowAddressService.class);
			startService(intentcalling);
		}
		
		callingshowaddress();
		lv_height_tools.setAdapter(hItem);
		lv_height_tools.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					//--------------------------号码归属地显示------------------------------
					addressShow();
					//--------------------------号码归属地显示------------------------------
					break;
				default:
					break;
				}
			}
		});
	}
	/**
	 * -----------开启来电显示-----------------------------------------------------
	 */
	public void callingshowaddress(){
		cb_calling_isshowaddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				Intent intentcalling = new Intent(getApplicationContext(),CallingShowAddressService.class);
				if(cb_calling_isshowaddress.isChecked()){
					stopService(intentcalling);
					cb_calling_isshowaddress.setChecked(false);
					editor.putBoolean("isshowcalling", false);
				}else{
					startService(intentcalling);
					cb_calling_isshowaddress.setChecked(true);
					editor.putBoolean("isshowcalling", true);
					editor.commit();
				}
			}
		});
	}
	
	/**
	 * 显示号码归属地
	 * @param view
	 */
	public void addressShow(){
		final QueryAddressDao qad = new QueryAddressDao(getApplicationContext());
	    AlertDialog.Builder builder = new AlertDialog.Builder(HeightToolsActivity.this);
		View view = View.inflate(getApplicationContext(), R.layout.phonenum_address_show, null);
	    TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
		builder.setView(view);
		String phonenum = et_num.getText().toString().trim();
		String loca = qad.queryAddress(phonenum);
		if(! TextUtils.isEmpty(loca)){
			tv_address.setText(loca);
		}
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
			builder.show();
	}
	
	/**
	 * ----来电归属风格选择
	 * @param view
	 */
	
	public void showstyle(View view){
		
		AlertDialog.Builder builder = new Builder(HeightToolsActivity.this);
		builder.setSingleChoiceItems(items, 0,new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String liv_textString = "切换归属地显示风格"+"-->"+items[which];
			    liv_showstyle.setText(liv_textString);
				Editor editor = sp.edit();
				editor.putInt("styleId", which);
				editor.commit();
			}
		});
		builder.setTitle("归属地显示风格");
		builder.setIcon(R.drawable.item_2);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	public void showlocation(View view){
		Intent intentlocationIntent =  new Intent(this,DragToastActivity.class);
		startActivity(intentlocationIntent);
	}
	
	public void gohome(View view){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}
}
