package com.hpuvoice.phonesafe.adapter;

import com.hpuvoice.phonesafe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class GridViewAdapter extends BaseAdapter {
	
private Context context = null;
private SharedPreferences sp = null;
private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
									"缓存清理", "高级工具", "设置中心" };
private static int[] icons = { R.drawable.item_1, R.drawable.item_2,
	R.drawable.item_3, R.drawable.item_4, R.drawable.item_5,
	R.drawable.item_6, R.drawable.item_7, R.drawable.item_8,
	R.drawable.item_9 };

public GridViewAdapter(Context context) {

	this.context = context;
	sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
}

public int getCount() {
	return names.length;
}

public Object getItem(int position) {
	return position;
}

public long getItemId(int position) {
	return position;
}

@SuppressLint("ViewHolder") 
public View getView(int position, View convertView, ViewGroup parent) {
	View view;
	ViewHolder viHolder ;
	if(convertView != null){
		view = convertView;
		viHolder = (ViewHolder) view.getTag();
	}else{
		  view = View.inflate(context, R.layout.main_item, null);
		  viHolder = new ViewHolder();
		  viHolder.iv_main_item = (ImageView) view.findViewById(R.id.iv_main_item);;
		  viHolder.tv_main_item = (TextView) view.findViewById(R.id.tv_main_item);
		  view.setTag(viHolder);
	}
	
	viHolder.iv_main_item.setImageResource(icons[position]);
	viHolder.tv_main_item.setText(names[position]);
	if(position==0){
		String name = sp.getString("lost_name", "");
		if(!"".equals(name)){
			viHolder.tv_main_item.setText(name);
		}
	}
	return view;
}
	static class ViewHolder{
		ImageView iv_main_item;
		TextView tv_main_item;
	}
}


