package com.hpuvoice.phonesafe.adapter;

import com.hpuvoice.phonesafe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HeightToolsAdapterItem extends BaseAdapter {


	private Context context = null;
	private static String[] names = { "∫≈¬ÎπÈ Ùµÿ≤È—Ø"};
	private static int[] icons = { R.drawable.item_1};
	public HeightToolsAdapterItem(Context context){
		this.context = context;
	}
	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viHolder ;
		if(convertView != null){
			view = convertView;
			viHolder = (ViewHolder) view.getTag();
		}else{
			 view = View.inflate(context, R.layout.setting_center_item, null);
			 viHolder = new ViewHolder();
			 viHolder.tv_setcenter_item = (TextView) view.findViewById(R.id.tv_setcenter_item);
			 viHolder.iv_setcenter = (ImageView) view.findViewById(R.id.iv_setcenter);
			 view.setTag(viHolder);
		}
		viHolder.tv_setcenter_item.setText(names[position]);
		viHolder.iv_setcenter.setImageResource(icons[position]);
		return view;
	}
	
	static class ViewHolder{
		 TextView tv_setcenter_item;
		 ImageView iv_setcenter;
	}

}
