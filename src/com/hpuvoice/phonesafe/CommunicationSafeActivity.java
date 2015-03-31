package com.hpuvoice.phonesafe;

import java.util.ArrayList;
import java.util.List;

import com.hpuvoice.phonesafe.adapter.BlackNumAdapterItem;
import com.hpuvoice.phonesafe.bean.BlackNumInfo;
import com.hpuvoice.phonesafe.bean.MsgInfo;
import com.hpuvoice.phonesafe.dao.BlackNumDao;
import com.hpuvoice.phonesafe.engine.BackUpMsgService;
import com.hpuvoice.phonesafe.engine.BackUpMsgService.BackUpProcess;
import com.hpuvoice.phonesafe.service.BlackNumService;
import com.hpuvoice.phonesafe.ui.SettingItemView;
import com.hpuvoice.phonesafe.utils.ServiceUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CommunicationSafeActivity extends Activity {

	@ViewInject(R.id.lv_show_blacknum)
	private ListView lv_show_blacknum;
	@ViewInject(R.id.cb_calling_blacknum)
	private SettingItemView cb_calling_blacknum;
	@ViewInject(R.id.pb_lv_num)
	private ProgressBar pb_lv_num;
	private BlackNumDao blacknumdao;
	List<BlackNumInfo> blackNumInfos;
	public BlackNumAdapterItem numitem;
	public static final int unit = 10;
	public int startindex = 0;

	private ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communicationsafe);
		ViewUtils.inject(this);

		pDialog = new ProgressDialog(this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pb_lv_num.setVisibility(View.INVISIBLE);
		// ---------------
		testdao1();
		blacknumdao = new BlackNumDao(getApplicationContext());
		blackNumInfos = blacknumdao.queryPart(unit, startindex);
		numitem = new BlackNumAdapterItem(
				CommunicationSafeActivity.this, blackNumInfos);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		isCheckBlackNum();
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(CommunicationSafeActivity.this, "������ɣ���", Toast.LENGTH_SHORT).show();
		};
	};
	
	/**
	 * ���ݶ���---
	 * @param view
	 */
	public void backupMsg(View view){
		
		pDialog.show();
		new Thread(){
			public void run() {
				BackUpMsgService.backUpMsg(CommunicationSafeActivity.this,new BackUpProcess() {
					
					@Override
					public void BackUpNow(int nowProcess) {
						pDialog.setProgress(nowProcess);
					}
					
					@Override
					public void BackUpMax(int max) {
						pDialog.setMax(max);
					}
				});
				handler.sendEmptyMessage(0);
				pDialog.dismiss();
				
			};
		}.start();
	}
	
	/**
	 * �������ݶ���--
	 * @param view
	 */
	public void showBackupMsg(View view){
		
		List<MsgInfo> parseBackUpMsg = BackUpMsgService.parseBackUpMsg(this);
		for (MsgInfo msgInfo : parseBackUpMsg) {
			System.out.println("-------------������ʼ--------------");
			System.out.println(msgInfo.toString());
			System.out.println("-------------��������--------------");
		}
	}

	/**
	 * �Ƿ�ѡ��������������
	 */
	public void isCheckBlackNum() {
		if (ServiceUtils.isServiceRunning(this,
				"com.hpuvoice.phonesafe.service.BlackNumService")) {
			cb_calling_blacknum.setChecked(true);
		} else {
			cb_calling_blacknum.setChecked(false);
		}
		cb_calling_blacknum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentblacknumber = new Intent(
						CommunicationSafeActivity.this, BlackNumService.class);
				if (cb_calling_blacknum.isChecked()) {
					stopService(intentblacknumber);
					cb_calling_blacknum.setChecked(false);
				} else {
					startService(intentblacknumber);
					cb_calling_blacknum.setChecked(true);
				}
			}
		});
	}

	// ---------------------------------------------------------------------
	public void testdao1() {
		for (int i = 0; i < 10; i++) {

			BlackNumInfo blackNumInfo = new BlackNumInfo("china", "10086" + i,
					3);
			BlackNumDao blackNumDao = new BlackNumDao(getApplicationContext());
			blackNumDao.addBlackNum(blackNumInfo);
		}
	}

	/**
	 * ��ʾ����������
	 * 
	 * @param view
	 */
	public void showblacknum(View view) {

		/**
		 * �Զ����첽���
		 */
		/*
		 * new AsyncTaskUtils() {
		 * 
		 * @Override public void preTask() {
		 * pb_lv_num.setVisibility(View.VISIBLE); }
		 * 
		 * @Override public void doInTask() { blackNumInfos =
		 * blacknumdao.queryAll(); }
		 * 
		 * @Override public void postTask() {
		 * pb_lv_num.setVisibility(View.INVISIBLE); BlackNumAdapterItem numitem
		 * = new BlackNumAdapterItem(
		 * CommunicationSafeActivity.this,blackNumInfos);
		 * lv_show_blacknum.setAdapter(numitem); } }.execute();
		 */
		
		

		fillListView();
		lv_show_blacknum.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int lastposition = lv_show_blacknum
							.getLastVisiblePosition();
					if (lastposition == blackNumInfos.size() - 1) {
						startindex += 10;
						fillListView();
					}
					
					if(numitem.isDel == 1){
						numitem.notifyDataSetChanged();
					}
					
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					if(numitem.isDel == 1){
						numitem.notifyDataSetChanged();
					}
					break;
				case OnScrollListener.SCROLL_STATE_FLING:

					break;

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
	}

	/***
	 * -------���listView ����
	 */
	public void fillListView() {
		/**
		 * �ٷ��첽���
		 * 
		 * @param --�������---����---��������----
		 */
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				/**
				 * ��ѯȫ�������ݽ϶࣬���÷�����ѯ
				 */
				if (blackNumInfos == null) {
					blackNumInfos = blacknumdao.queryPart(unit, startindex);
				} else {
					List<BlackNumInfo> newinfos = new ArrayList<BlackNumInfo>();
					newinfos = blacknumdao.queryPart(unit, startindex);
					blackNumInfos.addAll(newinfos);
				}
				/**
				 * ��ѯȫ�������ݽ��٣�����ȫ����ѯ
				 */
				// blackNumInfos = blacknumdao.queryAll();
				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pb_lv_num.setVisibility(View.VISIBLE);
			}

			@Override
			protected void onPostExecute(Void result) {
				pb_lv_num.setVisibility(View.INVISIBLE);
				// -------�����������������Զ�ˢ�£�����������ת������----------
					lv_show_blacknum.setAdapter(numitem);
					numitem.notifyDataSetChanged();
				super.onPostExecute(result);
			}
		}.execute();
	}

	/**
	 * ���Ӻ�����
	 * 
	 * @param view
	 */
	public void addblacknum(View view) {
		AlertDialog.Builder builder = new Builder(CommunicationSafeActivity.this);
		View addview = View.inflate(CommunicationSafeActivity.this, R.layout.show_add_blacknum, null);
		builder.setView(addview);
		final EditText et_blacknum = (EditText) addview.findViewById(R.id.et_blacknum);
		final EditText et_blackname = (EditText) addview.findViewById(R.id.et_blackname);
		final RadioGroup rg_title_id_1 = (RadioGroup) addview.findViewById(R.id.rg_title_id_1);
		final RadioGroup rg_title_id_2 = (RadioGroup) addview.findViewById(R.id.rg_title_id_2);
		final RadioGroup rg_title_id_3 = (RadioGroup) addview.findViewById(R.id.rg_title_id_3);
		final RadioGroup rg_title_id_4 = (RadioGroup) addview.findViewById(R.id.rg_title_id_4);

		
		builder.setPositiveButton("ȷ��", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String addblcaknum = et_blacknum.getText().toString().trim();
				String addname = et_blackname.getText().toString().trim();
				int title_id = 2;
				if(!TextUtils.isEmpty(addblcaknum) && !TextUtils.isEmpty(addname)){
					int checkid1 = rg_title_id_1.getCheckedRadioButtonId();
					switch (checkid1) {
					case R.id.rb_title_1:
						//----------No.1--------------
						title_id = 1;
						break;
					case R.id.rb_title_2:
						//----------No.2--------------
						title_id = 2;
						break;
					}
					int checkid2 = rg_title_id_2.getCheckedRadioButtonId();
					switch (checkid2) {
					case R.id.rb_title_3:
						//----------No.1--------------
						title_id = 3;
						break;
					case R.id.rb_title_4:
						//----------No.2--------------
						title_id = 4;
						break;
					}
					int checkid3 = rg_title_id_3.getCheckedRadioButtonId();
					switch (checkid3) {
					case R.id.rb_title_5:
						//----------No.1--------------
						title_id = 5;
						break;
					case R.id.rb_title_6:
						//----------No.2--------------
						title_id = 6;
						break;
					}
					int checkid4 = rg_title_id_4.getCheckedRadioButtonId();
					switch (checkid4) {
					case R.id.rb_title_7:
						//----------No.1--------------
						title_id = 7;
						break;
					case R.id.rb_title_8:
						//----------No.2--------------
						title_id = 8;
						break;
					}
					BlackNumInfo addNumInfo = new BlackNumInfo(addname, addblcaknum, title_id);
					if(addNumInfo != null){
						blackNumInfos.add(0,addNumInfo);
						numitem.notifyDataSetChanged();
						blacknumdao.addBlackNum(addNumInfo);
					}
				}else{
					Toast.makeText(getApplicationContext(), "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		builder.setNegativeButton("ȡ��", null);
		
		builder.show();
	}

	/**
	 * ����������
	 * 
	 * @param view
	 */
	public void gohome(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
