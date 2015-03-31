package com.hpuvoice.phonesafe.ui;

import com.hpuvoice.phonesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class SettingItemView extends RelativeLayout{

	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_title;
	
	private String desc_off;
	private String desc_on;
	
	public void initView(Context context){
		View.inflate(context, R.layout.setting_item_selfdefine, SettingItemView.this);
		cb_status = (CheckBox) this.findViewById(R.id.cb_status);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	
	public SettingItemView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs,defStyle);
		initView(context);
	}
	
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String tilte = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.hpuvoice.phonesafe", "title");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.hpuvoice.phonesafe", "desc_off");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.hpuvoice.phonesafe", "desc_on");
		tv_title.setText(tilte);
		setDesc(desc_off);//空间默认是关闭状态图
	}
	
	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}
	
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	
	public void setChecked(boolean isChecked){
		cb_status.setChecked(isChecked);
		if(isChecked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
	}
	
	public void setDesc(String desc){
		tv_desc.setText(desc);
	}

	
}
