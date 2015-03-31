package com.hpuvoice.phonesafe.adapter;

import java.util.List;

import com.hpuvoice.phonesafe.R;
import com.hpuvoice.phonesafe.bean.BlackNumInfo;
import com.hpuvoice.phonesafe.dao.BlackNumDao;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BlackNumAdapterItem extends BaseAdapter {

	private Context context = null;
	private BlackNumDao bnd;
	private List<BlackNumInfo> blackNumInfos;
	ViewHolder viHolder ;
	public  int isDel = -1;
	public BlackNumAdapterItem(Context context,List<BlackNumInfo> blackNumInfos){
		this.context = context;
		bnd = new BlackNumDao(context);
		this.blackNumInfos = blackNumInfos;
	}
	
	public ViewHolder getViewHolder(){
		return viHolder;
		
	}
	@Override
	public int getCount() {
		return blackNumInfos.size();
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
		final int delpostion = position;
		final View view;
		
		if(convertView != null){
			view = convertView;
			viHolder = (ViewHolder) view.getTag();
		}else{
			 view = View.inflate(context, R.layout.show_blacknum_item, null);
			 viHolder = new ViewHolder();
			 viHolder.tv_num = (TextView) view.findViewById(R.id.tv_num);
			 viHolder.tv_title_id = (TextView) view.findViewById(R.id.tv_title_id);
			 viHolder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
			 view.setTag(viHolder);
		}
		
		
		viHolder.tv_num.setText(blackNumInfos.get(position).getNumber());
		int title_iditem = blackNumInfos.get(position).getTitle_id();
		switch (title_iditem) {
		case 1:
			viHolder.tv_title_id.setText("订餐电话");
			break;
		case 2:
			viHolder.tv_title_id.setText("公共服务");
			break;
		case 3:
			viHolder.tv_title_id.setText("运营商");
			break;
		case 4:
			viHolder.tv_title_id.setText("快递服务");
			break;
		case 5:
			viHolder.tv_title_id.setText("机票酒店");
			break;
		case 6:
			viHolder.tv_title_id.setText("银行证券");
			break;
		case 7:
			viHolder.tv_title_id.setText("保险服务");
			break;
		case 8:
			viHolder.tv_title_id.setText("品牌售后");
			break;
		}
		
		viHolder.iv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//-----弹出删除提示框----
				AlertDialog.Builder builder = new Builder(context);
				builder.setIcon(R.drawable.item_2);
				builder.setTitle("是否确定删除黑名？");
				builder.setMessage(blackNumInfos.get(delpostion).getNumber()+"即将删除，删除后滑动刷新界面...");
				builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						bnd.deleteBlaknum(blackNumInfos.get(delpostion).getNumber());
						blackNumInfos.remove(delpostion);
						isDel = 1;
					}
				});
				
				builder.setNegativeButton("取消", null);
				
				builder.show();
				
				
				
			}
		});
		
		
		return view;
	}
	
	 static class ViewHolder{
		 TextView tv_num;
		 TextView tv_title_id;
		 ImageView iv_delete;
		 
	 }
}
