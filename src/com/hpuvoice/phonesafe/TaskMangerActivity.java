package com.hpuvoice.phonesafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hpuvoice.phonesafe.bean.TaskInfo;
import com.hpuvoice.phonesafe.engine.TaskInfoService;
import com.hpuvoice.phonesafe.utils.TaskUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class TaskMangerActivity extends Activity {

	@ViewInject(R.id.lv_show_tasks)
	private ListView lv_show_tasks;
	@ViewInject(R.id.tv_task_count)
	private TextView tv_task_count;
	@ViewInject(R.id.tv_task_ram)
	private TextView tv_task_ram;
	@ViewInject(R.id.tv_task_running)
	private TextView tv_task_running;
	@ViewInject(R.id.bt_selectall)
	private Button bt_selectall;
	@ViewInject(R.id.bt_cancel)
	private Button bt_cancel;
	@ViewInject(R.id.bt_clean)
	private Button bt_clean;
	@ViewInject(R.id.bt_setting)
	private Button bt_setting;
	// List list = Collections.synchronizedList(new ArrayList(...));
	List<TaskInfo> taskInfos = Collections
			.synchronizedList(new ArrayList<TaskInfo>());
	List<TaskInfo> otherappInfos = Collections
			.synchronizedList(new ArrayList<TaskInfo>());
	List<TaskInfo> sysappInfos = Collections
			.synchronizedList(new ArrayList<TaskInfo>());

	TaskAdapter adapter;
	int runningProcessCount;
	long availableRam;
	long total;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskmanger);

		ViewUtils.inject(this);
		runningProcessCount = TaskUtils
				.getRunningCount(getApplicationContext());
		availableRam = TaskUtils.avaliableRAM(this);
		total = TaskUtils.totalRAM(this);
		tv_task_running.setText("Running Task:" + runningProcessCount);
		tv_task_ram.setText(Formatter.formatFileSize(this, availableRam) + "/"
				+ Formatter.formatFileSize(this, total));
		// tv_task_count.setText("----Current Task----");
		filldata();
		// -------------滚动监听------------------------
		lv_show_tasks.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (otherappInfos != null && sysappInfos != null) {
					if (firstVisibleItem >= otherappInfos.size() + 1) {
						tv_task_count.setText("系统进程----(" + sysappInfos.size()
								+ ")个");
					} else {
						tv_task_count.setText("用户进程----("
								+ otherappInfos.size() + ")个");
					}
				}
			}
		});
		// ----------点击监听------------------------------
		lv_show_tasks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return;
				}
				if (position == otherappInfos.size() + 1) {
					return;
				}
				ViewHolder vh = (ViewHolder) view.getTag();
				TaskInfo infoitemclick;
				if (position <= otherappInfos.size()) {
					infoitemclick = otherappInfos.get(position - 1);
				} else {
					infoitemclick = sysappInfos.get(position - 2
							- otherappInfos.size());
				}
				if (infoitemclick.getPackagename().equals(getPackageName())) {
					return;
				}
				if (infoitemclick.isChenked()) {
					infoitemclick.setChenked(false);
					vh.cb_task.setChecked(false);
				} else {
					infoitemclick.setChenked(true);
					vh.cb_task.setChecked(true);
				}
			}
		});
	}

	private void filldata() {
		new AsyncTask<Void, Void, Void>() {

			@SuppressWarnings("static-access")
			@Override
			protected Void doInBackground(Void... params) {
				TaskInfoService appInfoService = new TaskInfoService();
				taskInfos = appInfoService.geTaskInfos(getApplicationContext());
				for (TaskInfo iteminfo : taskInfos) {
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
					adapter = new TaskAdapter();
					lv_show_tasks.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
			}
		}.execute();
	}

	boolean ishowSystem = true;
	// --------------------------适配器------------------------------------------
	private class TaskAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if(ishowSystem){
				return otherappInfos.size() + sysappInfos.size() + 2;
			}else{
				return otherappInfos.size()+1;
			}
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
					TextView showmode1 = new TextView(TaskMangerActivity.this);
					showmode1.setText("用户进程----(" + otherappInfos.size() + ")个");
					return showmode1;
				}
				if (position == otherappInfos.size() + 1) {
					TextView showmode2 = new TextView(TaskMangerActivity.this);
					showmode2.setTextSize(17);
					showmode2.setTextColor(Color.WHITE);
					showmode2.setBackgroundColor(Color.GRAY);
					showmode2.setText("系统进程----(" + sysappInfos.size() + ")个");
					return showmode2;
				}
				ViewHolder viewHolder;
				View appView;
				TaskInfo taskitem;
				if (convertView != null && convertView instanceof RelativeLayout) {
					appView = convertView;
					viewHolder = (ViewHolder) appView.getTag();
				} else {
					viewHolder = new ViewHolder();
					appView = View.inflate(TaskMangerActivity.this,
							R.layout.show_task_item, null);
					viewHolder.iv_task_icon = (ImageView) appView
							.findViewById(R.id.iv_task_icon);
					viewHolder.tv_task_name = (TextView) appView
							.findViewById(R.id.tv_task_name);
					viewHolder.tv_task_position = (TextView) appView
							.findViewById(R.id.tv_task_position);
					viewHolder.tv_task_version = (TextView) appView
							.findViewById(R.id.tv_task_version);
					viewHolder.cb_task = (CheckBox) appView
							.findViewById(R.id.cb_task);
					appView.setTag(viewHolder);
				}

				if (position <= otherappInfos.size()) {
					taskitem = otherappInfos.get(position - 1);

				} else {
					taskitem = sysappInfos.get(position - 2 - otherappInfos.size());
				}
				if (taskitem.getIcon() == null) {
					viewHolder.iv_task_icon
							.setImageResource(R.drawable.ic_launcher);
				} else {
					viewHolder.iv_task_icon.setImageDrawable(taskitem.getIcon());
				}
				viewHolder.tv_task_name.setText(taskitem.getAppname());
				if (taskitem.isSystemApp()) {
					viewHolder.tv_task_position.setText("系统进程");
				} else {
					viewHolder.tv_task_position.setText("用户进程");
				}
				if (taskitem.getPackagename().equals(getPackageName())) {
					viewHolder.cb_task.setVisibility(View.INVISIBLE);
				} else {
					viewHolder.cb_task.setVisibility(View.VISIBLE);
				}
				// -----version用来显示内存大小-----------
				viewHolder.tv_task_version.setText(Formatter.formatFileSize(
						getApplicationContext(), taskitem.getMemory()));
				if (taskitem.isChenked()) {
					viewHolder.cb_task.setChecked(true);
				} else {
					viewHolder.cb_task.setChecked(false);
				}
				return appView;
			
			
		}

	}

	static class ViewHolder {
		ImageView iv_task_icon;
		TextView tv_task_name;
		TextView tv_task_position;
		TextView tv_task_version;
		CheckBox cb_task;
	}

	// --------------------------适配器------------------------------------------
	/**
	 * --------全选
	 * 
	 * @param view
	 */
	public void selectall(View view) {
		for (TaskInfo iterable_element : taskInfos) {
			if (iterable_element.getPackagename().equals(getPackageName())) {
				continue;
			}
			iterable_element.setChenked(true);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * --------取消
	 * 
	 * @param view
	 */
	public void cancel(View view) {
		for (TaskInfo iterable_element : taskInfos) {
			iterable_element.setChenked(false);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * --------清理运行进程
	 * 
	 * @param view
	 */
	public void clean(View view) {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<TaskInfo> deleteInfos = new ArrayList<TaskInfo>();
		long freeMemory = 0;
		for (TaskInfo info : otherappInfos) {
			if (info.isChenked()) {
				deleteInfos.add(info);
				freeMemory += info.getMemory();
			}
		}
		for (TaskInfo info : sysappInfos) {
			if (info.isChenked()) {
				deleteInfos.add(info);
				freeMemory += info.getMemory();
			}
		}
		Toast.makeText(
				getApplicationContext(),
				"清理了" + deleteInfos.size() + "个进程,释放了"
						+ Formatter.formatFileSize(this, freeMemory) + "内存",
				Toast.LENGTH_SHORT).show();

		runningProcessCount -= deleteInfos.size();
		tv_task_running.setText("Running Task:" + runningProcessCount);
		availableRam += freeMemory;
		tv_task_ram.setText(Formatter.formatFileSize(this, availableRam) + "/"
				+ Formatter.formatFileSize(this, total));

		for (TaskInfo info : deleteInfos) {
			if (info.isSystemApp()) {
				sysappInfos.remove(info);

			} else {
				otherappInfos.remove(info);
			}
			am.killBackgroundProcesses(info.getPackagename());
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * --------全选
	 * 
	 * @param view
	 */
	public void setting(View view) {
		if(ishowSystem){
			ishowSystem=false;
		}else{
			ishowSystem=true;
		}
		adapter.notifyDataSetChanged();
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
