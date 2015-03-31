package com.hpuvoice.phonesafe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.hpuvoice.phonesafe.bean.CacheAppInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PhoneClearFragment extends Fragment {

	List<CacheAppInfo> cacheAppInfos;
	CleanAdapter adapter;
	long totalCache = 0;
	String totalCacheStr;
	TextView tv_appclean_tips;
	ProgressBar pb_show_appclean;
	ListView lv_show_appclean;
	PackageManager mpm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_phoneclear, container,
				false);
		tv_appclean_tips = (TextView) view.findViewById(R.id.tv_appclean_tips);
		pb_show_appclean = (ProgressBar) view
				.findViewById(R.id.pb_show_appclean);
		lv_show_appclean = (ListView) view.findViewById(R.id.lv_show_appclean);

		// ------来自父窗体的View对象点击事件---监听button按钮点击--------------
		Button bt = (Button) getActivity().findViewById(R.id.bt_app_clearnow);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Class<?> loadClass;
				try {
					loadClass = PhoneClearFragment.class.getClassLoader()
							.loadClass("android.content.pm.PackageManager");
					Method declaredMethod = loadClass.getDeclaredMethod(
							"freeStorageAndNotify", Long.TYPE,
							IPackageDataObserver.class);
					declaredMethod.invoke(mpm, Long.MAX_VALUE,
							new IPackageDataObserver.Stub() {

								@Override
								public void onRemoveCompleted(
										String packageName, boolean succeeded)
										throws RemoteException {
									getActivity().runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(getActivity(),
													"缓存清理完毕！",
													Toast.LENGTH_SHORT).show();
											tv_appclean_tips.setText("缓存清理干净喽！");
											cacheAppInfos.clear();
											adapter.notifyDataSetChanged();
											lv_show_appclean
													.setVisibility(View.INVISIBLE);
										}
									});

								}
							});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return view;
	}

	/**
	 * 初始进入扫描------
	 */
	public void findAllCache() {
		if (getActivity() != null) {
			mpm = getActivity().getPackageManager();
			tv_appclean_tips.setText("正在初始化16核引擎...");
			new Thread() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					List<PackageInfo> installedPackages = mpm
							.getInstalledPackages(0);
					pb_show_appclean.setMax(installedPackages.size());

					int count = 0;
					for (PackageInfo packageInfo : installedPackages) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						String packString = packageInfo.packageName;
						final String label = packageInfo.applicationInfo
								.loadLabel(mpm).toString();
						Class<?> loadClass;
						try {
							loadClass = PhoneClearFragment.class
									.getClassLoader()
									.loadClass(
											"android.content.pm.PackageManager");
							Method declaredMethod = loadClass
									.getDeclaredMethod("getPackageSizeInfo",
											String.class,
											IPackageStatsObserver.class);
							declaredMethod.invoke(mpm, packString,
									mStatsObserver);

						} catch (Exception e) {
							e.printStackTrace();
						}

						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (tv_appclean_tips != null) {

									tv_appclean_tips.setText("正在扫描:" + label);
								}
							}
						});

						count++;
						pb_show_appclean.setProgress(count);
					}
					final String totalCacheStr = Formatter.formatFileSize(
							getActivity(), totalCache);
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (cacheAppInfos.size() < 1) {
								tv_appclean_tips.setText("手机很干净，没有缓存吆！");
							} else {
								tv_appclean_tips.setText("手机有缓存吆-"
										+ totalCacheStr + "赶快清理吧.");

								adapter = new CleanAdapter();
								lv_show_appclean.setAdapter(adapter);

								lv_show_appclean
										.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												CacheAppInfo cInfoclick = cacheAppInfos
														.get(position);
												String clickpkg = cInfoclick
														.getPackagename();
												Intent intent = new Intent();
												intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
												intent.setData(Uri
														.parse("package:"
																+ clickpkg));
												startActivity(intent);
											}
										});

								lv_show_appclean
										.setOnScrollListener(new OnScrollListener() {

											@Override
											public void onScrollStateChanged(
													AbsListView view,
													int scrollState) {

											}

											@Override
											public void onScroll(
													AbsListView view,
													int firstVisibleItem,
													int visibleItemCount,
													int totalItemCount) {
											}
										});
							}
						}
					});

				};
			}.start();
		}

	}

	IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long appcache = pStats.cacheSize;
			String pkgname = pStats.packageName;
			String AppCache = Formatter.formatFileSize(getActivity(), appcache);

			if (appcache > 0) {
				totalCache += appcache;
				CacheAppInfo cacheitem = new CacheAppInfo();
				cacheitem.setPackagename(pkgname);
				cacheitem.setCache(AppCache);
				cacheAppInfos.add(cacheitem);
			}
		}

	};

	public class CleanAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return cacheAppInfos.size();
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
			CacheAppInfo cInfo = cacheAppInfos.get(position);
			View view;
			ViewHolder vHolder;
			if (convertView != null) {
				view = convertView;
				vHolder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getActivity(), R.layout.show_appclean_item,
						null);
				vHolder = new ViewHolder();
				vHolder.iv_appclean_icon = (ImageView) view
						.findViewById(R.id.iv_appclean_icon);
				vHolder.tv_appclean_name = (TextView) view
						.findViewById(R.id.tv_appclean_name);
				vHolder.tv_appclean_count = (TextView) view
						.findViewById(R.id.tv_appclean_count);
				view.setTag(vHolder);
			}
			String pkgnameitem = cInfo.getPackagename();
			String cacheString = cInfo.getCache();
			String nameitem = null;
			try {
				nameitem = mpm.getApplicationInfo(pkgnameitem, 0)
						.loadLabel(mpm).toString();
				Drawable itemicon = mpm.getApplicationInfo(pkgnameitem, 0)
						.loadIcon(mpm);
				vHolder.iv_appclean_icon.setImageDrawable(itemicon);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			vHolder.tv_appclean_name.setText(nameitem);
			vHolder.tv_appclean_count.setText(cacheString);

			return view;
		}
	}

	static class ViewHolder {
		ImageView iv_appclean_icon;
		TextView tv_appclean_name;
		TextView tv_appclean_count;
	}

	/**
	 * ---立即清理--
	 */
	public void cleannow(View view) {
		// Toast.makeText(getActivity(), "just do..", 0).show();
		System.out.println("in----------------000099999----------");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		cacheAppInfos = new ArrayList<CacheAppInfo>();
		findAllCache();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
