package com.hpuvoice.phonesafe.adapter;

import java.util.List;

import com.hpuvoice.phonesafe.R;
import com.hpuvoice.phonesafe.bean.MainItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MainAdapterItem extends BaseAdapter {
    private List<MainItem> listItem = null;
	private Context context = null;

	public MainAdapterItem(List<MainItem> listItem, Context context) {
		
		this.listItem = listItem;
		this.context = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listItem.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItem.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ViewHolder") public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viHolder ;
		if(convertView != null){
			view = convertView;
			viHolder = (ViewHolder) view.getTag();
		}else{
			view = View.inflate(context, R.layout.main_below_item, null);
			 viHolder = new ViewHolder();
			 viHolder.iv_main_below = (ImageView) view.findViewById(R.id.iv_main_below);
			 viHolder.tv_main_below = (TextView) view.findViewById(R.id.tv_main_below);
			 viHolder.tv_main_below2 = (TextView) view.findViewById(R.id.tv_main_below2);
			 view.setTag(viHolder);
		}
		

		viHolder.tv_main_below.setText(listItem.get(position).getItem().toString());
		viHolder.tv_main_below2.setText(listItem.get(position).getItem2().toString());
		viHolder.iv_main_below.setImageDrawable(listItem.get(position).getIcon());
		return view;
	}
	
	static class ViewHolder{
	     ImageView iv_main_below ;
		 TextView tv_main_below ;
	     TextView tv_main_below2 ;
	}

}
