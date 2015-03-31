package com.hpuvoice.phonesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSForService extends Service {

	private LocationManager lManager;
	private SharedPreferences sp;
	String provider;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		lManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAltitudeRequired(true); // 请求高度定位
		criteria.setPowerRequirement(Criteria.POWER_HIGH);//允许使用高电量
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = lManager.getBestProvider(criteria , true);
		lManager.requestLocationUpdates(provider , 0, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
			
			@Override
			public void onProviderEnabled(String provider) {
			}
			
			@Override
			public void onProviderDisabled(String provider) {
			}
			
			@Override
			public void onLocationChanged(Location location) {
				//=======获取经纬度，精确度=====================
				String longitude = "longitude"+location.getLongitude()+"\n";
				String latitude = "longitude"+location.getLatitude()+"\n";
				String accuracy = "longitude"+location.getAccuracy()+"\n";
				
				Editor editor = sp.edit();
				editor.putString("lastlocation", longitude+latitude+accuracy);
				editor.commit();
			}
		});
	}
	
	@Override
	public void onDestroy() {
		lManager.removeUpdates((LocationListener) this);
		lManager.setTestProviderEnabled(provider, false);
		super.onDestroy();
	}
}
