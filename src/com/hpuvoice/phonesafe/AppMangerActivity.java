package com.hpuvoice.phonesafe;

import java.util.ArrayList;
import java.util.List;

import com.hpuvoice.phonesafe.bean.AppInfo;
import com.hpuvoice.phonesafe.dao.WatchDogDao;
import com.hpuvoice.phonesafe.engine.AppInfoService;
import com.hpuvoice.phonesafe.utils.DensityUtil;
import com.hpuvoice.phonesafe.utils.PhoneStorageUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppMangerActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.lv_show_apps)
	private ListView lv_show_apps;
	@ViewInject(R.id.tv_app_count)
	private TextView tv_app_count;
	@ViewInject(R.id.tv_phone_rom)
	private TextView tv_phone_rom;
	@ViewInject(R.id.tv_phone_sd)
	private TextView tv_phone_sd;
	@ViewInject(R.id.ib_islock_left)
	private ImageView ib_islock_left;
	@ViewInject(R.id.ib_islock_right)
	private ImageView ib_islock_right;
	@ViewInject(R.id.lv_show_lockapps)
	private ListView lv_show_lockapps;
	List<AppInfo> appInfos;
	List<AppInfo> lockappInfos = new ArrayList<AppInfo>();
	List<AppInfo> otherappInfos = new ArrayList<AppInfo>();
	List<AppInfo> sysappInfos = new ArrayList<AppInfo>();
	PopupWindow popupWindow;
	AppInfo infoitem;
	AppAdapter adapter;
	LockAppAdapter lockadapter;
	private WatchDogDao wdd;
	private WatchDogDao lockwdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanger);
		ViewUtils.inject(this);
		filldata();
		filllockdata();

		wdd = new WatchDogDao(getApplicationContext());
		lockwdd = new WatchDogDao(getApplicationContext());

		long sdAvailable = PhoneStorageUtils.getSDAvailable();
		String sd = Formatter.formatFileSize(this, sdAvailable);
		tv_phone_sd.setText("Sd可用*/" + sd);

		long romAvailable = PhoneStorageUtils.getRomAvailable();
		String rom = Formatter.formatFileSize(this, romAvailable);
		tv_phone_rom.setText("手机内存*/" + rom);

		lv_show_apps.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return true;
				}
				if (position == otherappInfos.size() + 1) {
					return true;
				}
				AppInfo infoitemlongclick;
				if (position <= otherappInfos.size()) {
					infoitemlongclick = otherappInfos.get(position - 1);

				} else {
					infoitemlongclick = sysappInfos.get(position - 2
							- otherappInfos.size());
				}
				ViewHolder vhHolder = (ViewHolder) view.getTag();

				if (infoitemlongclick.getPackagename().equals(getPackageName())) {
					return true;
				}

				if (wdd.isLock(infoitemlongclick.getPackagename())) {
					wdd.deleteLockApp(infoitemlongclick.getPackagename());
					vhHolder.iv_watch_state
							.setImageResource(R.drawable.ic_launcher);
				} else {
					wdd.addLockApp(infoitemlongclick.getPackagename());
					vhHolder.iv_watch_state.setImageResource(R.drawable.locked);
				}
				return true;
			}
		});

		// -------------添加点击监听=================
		lv_show_apps.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return;
				}
				if (position == otherappInfos.size() + 1) {
					return;
				}
				AppInfo infoitemclick;
				if (position <= otherappInfos.size()) {
					infoitemclick = otherappInfos.get(position - 1);

				} else {
					infoitemclick = sysappInfos.get(position - 2
							- otherappInfos.size());
				}
				// ---------popuWindows----自定义---
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int x = location[0];
				int y = location[1];
				View popuView = View.inflate(getApplicationContext(),
						R.layout.show_app_popuwindow, null);

				TextView tv_app_uninstall = (TextView) popuView
						.findViewById(R.id.tv_app_uninstall);
				TextView tv_app_share = (TextView) popuView
						.findViewById(R.id.tv_app_share);
				TextView tv_app_start = (TextView) popuView
						.findViewById(R.id.tv_app_start);
				TextView tv_app_detail = (TextView) popuView
						.findViewById(R.id.tv_app_detail);
				tv_app_uninstall.setOnClickListener(AppMangerActivity.this);
				tv_app_share.setOnClickListener(AppMangerActivity.this);
				tv_app_start.setOnClickListener(AppMangerActivity.this);
				tv_app_detail.setOnClickListener(AppMangerActivity.this);
				popupWindow = new PopupWindow(popuView, -2, -2);
				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,
						x + DensityUtil.dp2px(getApplicationContext(), 30), y);

				ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				sa.setDuration(300);
				popuView.setAnimation(sa);
			}
		});

		// -------------添加滚动监听=================
		lv_show_apps.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (otherappInfos != null && sysappInfos != null) {
					if (firstVisibleItem >= otherappInfos.size() + 1) {
						tv_app_count.setText("系统软件----(" + sysappInfos.size()
								+ ")个");
					} else {
						tv_app_count.setText("第三方软件----("
								+ otherappInfos.size() + ")个");
					}
				}
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;
				}
			}
		});
	}

	private void filllockdata() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				AppInfoService appInfoService = new AppInfoService(
						getApplicationContext());
				appInfos = appInfoService.getAllApps();
				for (AppInfo iteminfo : appInfos) {
					lockwdd = new WatchDogDao(getApplicationContext());
					if (lockwdd.isLock(iteminfo.getPackagename())) {
						lockappInfos.add(infoitem);
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);

				if (lockadapter == null) {
					lockadapter = new LockAppAdapter();
					lv_show_lockapps.setAdapter(lockadapter);
				} else {
					lockadapter.notifyDataSetChanged();
				}

			}
		}.execute();
	}

	/***
	 * -----lock填充数据----
	 */

	private void filldata() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				AppInfoService appInfoService = new AppInfoService(
						getApplicationContext());
				appInfos = appInfoService.getAllApps();
				for (AppInfo iteminfo : appInfos) {
					if (iteminfo.isSystemApp()) {
						sysappInfos.add(iteminfo);
					} else {
						otherappInfos.add(iteminfo);
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (adapter == null) {
					adapter = new AppAdapter();
					lv_show_apps.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}

			}
		}.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	/**
	 * ---------定义poupuwindow内部控件监听事件--------
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_app_uninstall:
			if (infoitem.isSystemApp()) {
				Toast.makeText(getApplicationContext(),
						"system app not uninstall", Toast.LENGTH_SHORT).show();
			} else {
				if (infoitem.getPackagename().equals(getPackageName())) {
					return;
				}
				uninstallApp();
			}
			break;
		case R.id.tv_app_share:
			shareApp();
			break;
		case R.id.tv_app_start:
			startApp();
			break;
		case R.id.tv_app_detail:
			detailApp();
			break;

		default:
			break;
		}
	}

	private void detailApp() {
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:" + infoitem.getPackagename()));
		startActivity(intent);
	}

	private void startApp() {
		PackageManager manager = getPackageManager();
		Intent launchIntentForPackage = manager
				.getLaunchIntentForPackage(infoitem.getPackagename());
		if (launchIntentForPackage != null) {
			startActivity(launchIntentForPackage);
		} else {
			Toast.makeText(getApplicationContext(), "系统关键应用,无法打开",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void shareApp() {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "推送软件来啦");
		startActivity(intent);
	}

	private void uninstallApp() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + infoitem.getPackagename()));
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		filldata();
	}

	private class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return appInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {
				TextView showmode1 = new TextView(AppMangerActivity.this);
				showmode1.setText("第三方软件----(" + otherappInfos.size() + ")个");
				return showmode1;
			}
			if (position == otherappInfos.size() + 1) {
				TextView showmode2 = new TextView(AppMangerActivity.this);
				showmode2.setTextSize(17);
				showmode2.setTextColor(Color.WHITE);
				showmode2.setBackgroundColor(Color.GRAY);
				showmode2.setText("系统软件----(" + sysappInfos.size() + ")个");
				return showmode2;
			}
			View appView;
			ViewHolder viewHolder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				appView = convertView;
				viewHolder = (ViewHolder) appView.getTag();
			} else {
				viewHolder = new ViewHolder();
				appView = View.inflate(AppMangerActivity.this,
						R.layout.show_app_item, null);
				viewHolder.iv_app_icon = (ImageView) appView
						.findViewById(R.id.iv_app_icon);
				viewHolder.tv_app_name = (TextView) appView
						.findViewById(R.id.tv_app_name);
				viewHolder.tv_app_position = (TextView) appView
						.findViewById(R.id.tv_app_position);
				viewHolder.tv_app_version = (TextView) appView
						.findViewById(R.id.tv_app_version);
				viewHolder.iv_watch_state = (ImageView) appView
						.findViewById(R.id.iv_watch_state);
				appView.setTag(viewHolder);
			}

			if (position <= otherappInfos.size()) {
				infoitem = otherappInfos.get(position - 1);

			} else {
				infoitem = sysappInfos.get(position - 2 - otherappInfos.size());
			}
			viewHolder.iv_app_icon.setImageDrawable(infoitem.getIcon());
			viewHolder.tv_app_name.setText(infoitem.getAppname());
			if (infoitem.isSystemApp()) {
				viewHolder.tv_app_position.setText("系统软件");
			} else {
				viewHolder.tv_app_position.setText("第三方软件");
			}
			viewHolder.tv_app_version.setText(infoitem.getVersion());

			if (wdd.isLock(infoitem.getPackagename())) {

				viewHolder.iv_watch_state.setImageResource(R.drawable.locked);
			} else {
				viewHolder.iv_watch_state
						.setImageResource(R.drawable.ic_launcher);
			}
			return appView;
		}

	}

	private class LockAppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lockappInfos.size()+1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			System.out.println("lock adapter in-------------->>>");
			if (position == 0) {
				TextView showmode1 = new TextView(AppMangerActivity.this);
				showmode1.setText("加锁软件:" + lockappInfos.size() + " 个--");
				return showmode1;
			}
			AppInfo lockinfoitem = lockappInfos.get(position-1);
			View lockappView;
			ViewHolder viewHolder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				lockappView = convertView;
				viewHolder = (ViewHolder) lockappView.getTag();
			} else {
				viewHolder = new ViewHolder();
				lockappView = View.inflate(AppMangerActivity.this,
						R.layout.show_app_item, null);
				viewHolder.iv_app_icon = (ImageView) lockappView
						.findViewById(R.id.iv_app_icon);
				viewHolder.tv_app_name = (TextView) lockappView
						.findViewById(R.id.tv_app_name);
				viewHolder.tv_app_position = (TextView) lockappView
						.findViewById(R.id.tv_app_position);
				viewHolder.tv_app_version = (TextView) lockappView
						.findViewById(R.id.tv_app_version);
				viewHolder.iv_watch_state = (ImageView) lockappView
						.findViewById(R.id.iv_watch_state);
				lockappView.setTag(viewHolder);
			}

			viewHolder.iv_app_icon.setImageDrawable(lockinfoitem.getIcon());
			viewHolder.tv_app_name.setText(lockinfoitem.getAppname());
			if (lockinfoitem.isSystemApp()) {
				viewHolder.tv_app_position.setText("系统软件");
			} else {
				viewHolder.tv_app_position.setText("第三方软件");
			}
			viewHolder.tv_app_version.setText(lockinfoitem.getVersion());
			viewHolder.iv_watch_state.setImageResource(R.drawable.locked);
			return lockappView;
		}

	}

	static class ViewHolder {
		ImageView iv_app_icon;
		TextView tv_app_name;
		TextView tv_app_position;
		TextView tv_app_version;
		ImageView iv_watch_state;
	}

	/**
	 * ------定义切换事件------
	 */
	public void showall(View view) {

		ib_islock_left.setImageResource(R.drawable.tv_islock_left_press);
		ib_islock_right.setImageResource(R.drawable.tv_islock_right_unpress);
		lv_show_apps.setVisibility(View.VISIBLE);
		lv_show_lockapps.setVisibility(View.GONE);
	}

	public void showlocked(View view) {
		ib_islock_left.setImageResource(R.drawable.tv_islock_left_unpress);
		ib_islock_right.setImageResource(R.drawable.tv_islock_right_press);
		lv_show_apps.setVisibility(View.GONE);
		tv_app_count.setVisibility(View.GONE);
		lv_show_lockapps.setVisibility(View.VISIBLE);
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
