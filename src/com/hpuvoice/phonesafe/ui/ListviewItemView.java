package com.hpuvoice.phonesafe.ui;

import com.hpuvoice.phonesafe.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListviewItemView extends RelativeLayout{

	@ViewInject(R.id.tv_setcenter_item)
	private TextView tv_setcenter_item;
	@ViewInject(R.id.iv_setcenter)
	private ImageView iv_setcenter;
	public ListviewItemView(Context context) {
		super(context);
		ViewUtils.inject(this);
		initView(context);
	}
	
	
	
	public ListviewItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		ViewUtils.inject(this);
		initView(context);
	}
	public ListviewItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ViewUtils.inject(this);
		initView(context);
	}
	public void initView(Context context){
		View view = View.inflate(context, R.layout.setting_center_item, ListviewItemView.this);
		tv_setcenter_item  = (TextView) view.findViewById(R.id.tv_setcenter_item);
		iv_setcenter  = (ImageView) view.findViewById(R.id.iv_setcenter);
	}
	
	public void setText(String text){
		tv_setcenter_item.setText(text);
	}
	
	public void setIcons(int id){
		iv_setcenter.setImageResource(id);
	}
	

}
