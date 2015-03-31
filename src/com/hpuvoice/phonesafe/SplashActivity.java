package com.hpuvoice.phonesafe;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import android.view.animation.AlphaAnimation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.hpuvoice.phonesafe.bean.UpdataInfo;
import com.hpuvoice.phonesafe.engine.UpdataInfoService;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;


public class SplashActivity extends Activity {

	private String version = null;
	private UpdataInfo info = null;
	private TextView tv_splash_process;
	private TextView tv_version;
	private SharedPreferences sp;
	private LinearLayout ll_splash;
	private String savepath ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_process = (TextView) findViewById(R.id.tv_splash_process);
        tv_version = (TextView) findViewById(R.id.tv_version);
        ll_splash = (LinearLayout) findViewById(R.id.ll_splash);
        version = gerVersion();
        tv_version.setText("Version . "+version);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean isupdate =  sp.getBoolean("update", true);
        initDb();
        //======================================================
        // �ж�updata �����Ƿ�ִ��isupdate(),ȱ���ж�============
           if(isupdate){}
           showShortcut();
        //======================================================
        	new Thread(){
				public void run() {
					try {
						Thread.sleep(2000);   
						runOnUiThread(new Runnable() {  
							
							@Override
							public void run() {
								mainUI();
							}
						});
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
			}.start();
       
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		ll_splash.startAnimation(aa);
		//��ɴ����ȫ����ʾ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
     
    /**
     * �������ͼ��
     */
    private void showShortcut() {
    	if(sp.getBoolean("isfirstlauncher", true)){
    		Intent shortcutIntent =  new Intent();
    		shortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    		/*
    		Intent valueintent = new Intent();
    		valueintent.setAction("com.hpuvoice.phonesafe.main");
    		//valueintent.setAction("android.intent.action.MAIN");
    		valueintent.addCategory(Intent.CATEGORY_DEFAULT);
    		//valueintent.addCategory(Intent.CATEGORY_LAUNCHER);
    		 * */
    		
    		//PackageManager  manager=getPackageManager();
    		// ���Ի�ȡ��ָ��������Ӧ�ó����������intent���� 
    		//Intent launchIntentForPackage = manager.getLaunchIntentForPackage(getPackageName());
    		
    		//�������ظ����  
    		shortcutIntent.putExtra("duplicate", false); 
    		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "��Ƶ��ʿ");
    		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
    		//-------��Ҫ����ʽ��ͼ��
    		//shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntentForPackage );
    		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent()  
             .setAction(Intent.ACTION_MAIN)  
              .addCategory(Intent.CATEGORY_LAUNCHER)  
              .setClass(this, SplashActivity.class));  
    		sendBroadcast(shortcutIntent); 
    		Editor editshort = sp.edit();
    		editshort.putBoolean("isfirstlauncher", false);
    		editshort.commit();
    	}
    	
	}

	/**
     * ===============����Ƿ���Ҫ����========
     */
    public void isNeedUpdate(){
    	UpdataInfoService updataservice = new UpdataInfoService(this);
    	try {
			info = updataservice.getUpdataInfo(R.string.apkurl);
			if(version.equals(info.getVersion())){
				mainUI();
			}else{
				showUpdataDialog();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			mainUI();
		}
    }
    /**
     * =========�����°汾apk==================
     */
    protected void downloadNewApk() {
		HttpUtils httpUtils = new HttpUtils();
		savepath = Environment.getExternalStorageDirectory().getPath()+"/phonesafe2.0.apk";
		httpUtils.download(R.string.apkurl+"",savepath ,
				new RequestCallBack<File>() {
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						Toast.makeText(getApplicationContext(), "���سɹ�", Toast.LENGTH_SHORT)
								.show();
						installAPk();
					}
					@Override
					public void onFailure(HttpException e, String arg1) {
						Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
						tv_splash_process.setVisibility(View.VISIBLE);// ��ʾ�ؼ�
						tv_splash_process.setText("���ؽ��� . "+current + "/" + total);
					}
				});
	}
    
    /**
     * ============ installAPk ()==============
     */
    
    public void installAPk(){
    	Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(
				Uri.fromFile(new File(savepath)),
				"application/vnd.android.package-archive");
		//startActivity(intent);
		startActivityForResult(intent, 0); // ���������activity �˳���ʱ��  ��ص���ǰactivity��onActivityResult
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mainUI();
	}
    
    /**
     * =========��ʾ�����Ի���=================
     */
    private void showUpdataDialog() {
    	AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("���°汾:" + info.getVersion());
		builder.setMessage(info.getDescription());
		builder.setCancelable(false);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton("��������",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// �����°汾
						downloadNewApk();
					}
				});
		builder.setNegativeButton("�ݲ�����",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// ������һ������
						mainUI();
					}
				});
		builder.show();
	}

	/**
     * ==============��ȡ�汾��===============
     */
    private String gerVersion(){
    	try {
    		PackageManager pm = getPackageManager();
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	return null;
    }
    /**
     *==============����������================   
     */
    private void mainUI(){
    	Intent intetn = new Intent(this,MainActivity.class);
		startActivity(intetn);
		finish();
    }
    /**
     * ==============��ʼ�����ӿ�================ 
     */
    private void initDb(){
    	File file = new File(getFilesDir(),"address.db");
    	if(!file.exists()&& file.length()==0){
    		InputStream iStream = null;
    		FileOutputStream fos = null;
    		AssetManager am = getAssets();
    		try {
				iStream = am.open("address.db");
				fos = new FileOutputStream(file);
				int len  = -1;
				byte[] bys = new byte[1024*4];
				while((len = iStream.read(bys))!=-1){
					fos.write(bys, 0, len);
				}
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(iStream != null){
					try {
						iStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
    	}
    }
}
