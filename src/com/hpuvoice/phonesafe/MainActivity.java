package com.hpuvoice.phonesafe;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;
import com.hpuvoice.phonesafe.adapter.GridViewAdapter;
import com.hpuvoice.phonesafe.adapter.MainAdapterItem;
import com.hpuvoice.phonesafe.airplane.FeiJi_Wel;
import com.hpuvoice.phonesafe.bean.MainItem;
import com.hpuvoice.phonesafe.utils.MD5Encoder;

@SuppressLint({ "SdCardPath", "HandlerLeak" })
@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements OnItemClickListener {

	protected static final int STOP = 100;
	private ImageView iv_cache = null;
	private ImageView iv_cache_bak = null;
	private GridView content = null;
	private SharedPreferences sp = null;
	private ListView lv_main_item = null;
	private LinearLayout ll = null;
	private SlidingDrawer sd = null;
	private ViewGroup mContainer;
	private FrameLayout rv = null;
	private GridViewAdapter adapter = null;
	private AlertDialog dialog;
	LinearInterpolator lin;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ll = (LinearLayout) findViewById(R.id.ll);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		content = (GridView) findViewById(R.id.content);
		iv_cache = (ImageView) findViewById(R.id.iv_cache);
		iv_cache_bak = (ImageView) findViewById(R.id.iv_cache_bak);
		// -----------------��ҳ��ת����-------------------------
		RotateAnimation animation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(2000);
		animation.setRepeatCount(Animation.INFINITE);
		iv_cache.setAnimation(animation);

		RotateAnimation animationbak = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animationbak.setDuration(300);
		animationbak.setRepeatCount(Animation.INFINITE);
		iv_cache_bak.setAnimation(animationbak);
		// -----------------��ҳ��ת����-------------------------
		lv_main_item = (ListView) findViewById(R.id.lv_main_item);
		sd = (SlidingDrawer) findViewById(R.id.sd);
		rv = (FrameLayout) findViewById(R.id.rv);
		mContainer = (ViewGroup) findViewById(R.id.container);

		mContainer
				.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			public void onDrawerOpened() {
				ll.setVisibility(View.GONE);
				lv_main_item.setVisibility(View.GONE);
				rv.setVisibility(View.GONE);
				mContainer.setVisibility(View.GONE);
			}
		});
		sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				ll.setVisibility(View.VISIBLE);
				lv_main_item.setVisibility(View.VISIBLE);
				rv.setVisibility(View.VISIBLE);
				mContainer.setVisibility(View.VISIBLE);
			}
		});

		MainAdapterItem myadapter = new MainAdapterItem(getItem(),
				getApplicationContext());
		lv_main_item.setAdapter(myadapter);

		/**
		 * ���listView��Ŀ���õ��¼�
		 */
		lv_main_item.setOnItemClickListener(new MyOnItemClickListener());

		adapter = new GridViewAdapter(this);
		content.setAdapter(adapter);
		content.setOnItemClickListener(this);
	}

	class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				Intent aboutIntent = new Intent(MainActivity.this,
						FeiJi_Wel.class);
				startActivity(aboutIntent);
				break;
			case 1:
				Intent intentnumberbak = new Intent(MainActivity.this,
						CommunicationSafeActivity.class);
				startActivity(intentnumberbak);
				break;
			case 2:
				Intent intentrafficbak = new Intent(MainActivity.this,
						TrafficMangerActivity.class);
				startActivity(intentrafficbak);
				break;
			case 3:
				Intent intentantivirusbak = new Intent(MainActivity.this,
						AntiVirusActivity.class);
				startActivity(intentantivirusbak);
				break;
			default:
				break;
			}

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:// ��ʾ�ֻ�����
			boolean registed = sp.getBoolean("registed", false);
			if (registed) {
				showEnterDialog();
			} else {
				showRegistDialog();
			}

			break;
		case 1:
			Intent intentnumber = new Intent(MainActivity.this,
					CommunicationSafeActivity.class);
			startActivity(intentnumber);
			break;
		case 2:
			Intent intentapp = new Intent(MainActivity.this,
					AppMangerActivity.class);
			startActivity(intentapp);
			break;
		case 3:
			Intent intenttask = new Intent(MainActivity.this,
					TaskMangerActivity.class);
			startActivity(intenttask);
			break;
		case 4:
			Intent intentraffic = new Intent(MainActivity.this,
					TrafficMangerActivity.class);
			startActivity(intentraffic);
			break;
		case 5:
			Intent intentantivirus = new Intent(MainActivity.this,
					AntiVirusActivity.class);
			startActivity(intentantivirus);
			break;
		case 6:
			Intent intentclear = new Intent(MainActivity.this,
					CacheClearActivity.class);
			startActivity(intentclear);
			break;
		case 7:
			Intent intent7 = new Intent(MainActivity.this,
					HeightToolsActivity.class);
			startActivity(intent7);
			break;
		case 8:
			Intent intent8 = new Intent(MainActivity.this,
					SetCenterActivity.class);
			startActivity(intent8);
			break;
		default:
			break;
		}

	}

	/**
	 * ========����������==================
	 */

	private List<MainItem> getItem() {
		List<MainItem> listItem = new ArrayList<MainItem>();
		MainItem mainItem1 = new MainItem("�ɻ���ս", "��һ�֣�����ͣ������...",
				getResources().getDrawable(R.drawable.antivrus_icon_pressed));
		listItem.add(mainItem1);
		MainItem mainItem2 = new MainItem("ɧ������", "ȫ�������������ź�ɧ�ŵ绰",
				getResources().getDrawable(R.drawable.block_icon_pressed));
		listItem.add(mainItem2);
		MainItem mainItem3 = new MainItem("�������", "ʱʱ�������.����������ʧ",
				getResources().getDrawable(R.drawable.traffic_icon_pressed));
		listItem.add(mainItem3);
		MainItem mainItem4 = new MainItem("������ɱ", "ȫ���ɱľ��.�������", getResources()
				.getDrawable(R.drawable.progress_icon_pressed));
		listItem.add(mainItem4);
		return listItem;
	}

	/**
	 * ===============����safe�Ի���=========
	 */

	int count = 0;

	protected void showEnterDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.login_safe,
				null);
		// ��ʼ���ؼ�
		final EditText et_password = (EditText) view
				.findViewById(R.id.et_password);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		ImageView iv_show_password = (ImageView) view
				.findViewById(R.id.iv_show_password);
		iv_show_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (count % 2 == 0) {
					et_password.setInputType(1);// ��ʾ����

				} else {
					et_password.setInputType(129);// ��������
				}
				count++;
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
				if (!TextUtils.isEmpty(password)) {
					String sp_password = sp.getString("password", "");
					if (MD5Encoder.disgest(password).equals(sp_password)) {
						// һ��
						Toast.makeText(getApplicationContext(), "������һ������",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(getApplicationContext(),
								SafeActivity.class);
						startActivity(intent);
						finish();
						dialog.dismiss();
					} else {
						Toast.makeText(getApplicationContext(), "�����������",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "�������벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// builder.setView(view);

		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * ===============ע��Ի���=========
	 */
	private void showRegistDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.regist_safe,
				null);
		final EditText et_password = (EditText) view
				.findViewById(R.id.et_password);
		final EditText et_password_confirm = (EditText) view
				.findViewById(R.id.et_password_confirm);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
				String password_confirm = et_password_confirm.getText()
						.toString().trim();
				if (!TextUtils.isEmpty(password)
						&& !TextUtils.isEmpty(password_confirm)) {
					if (password.equals(password_confirm)) {
						// ��������ɹ�
						Editor edit = sp.edit();
						edit.putString("password", MD5Encoder.disgest(password));
						edit.putBoolean("registed", true);
						edit.commit();
						// edit.apply();
						dialog.dismiss();
						Intent intent = new Intent(getApplicationContext(),
								SafeActivity.class);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "�������벻һ��",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "��������벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// builder.setView(view);

		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	public void kill_simple(View view) {
		Intent killIntent = new Intent(this, AntiVirusActivity.class);
		killIntent.putExtra("kill_simple", "true");
		startActivity(killIntent);
		finish();
	}

}
