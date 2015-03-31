package com.hpuvoice.phonesafe.dao;

import java.util.ArrayList;
import java.util.List;

import com.hpuvoice.phonesafe.db.WatchDogOpenhelper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class WatchDogDao {

	private Context context;
	private WatchDogOpenhelper helper;

	public WatchDogDao(Context context) {
		this.context = context;
		helper = new WatchDogOpenhelper(context);
	}

	public List<String> queryAll() {
		List<String> allApps = new ArrayList<String>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("lockapp", new String[] {"packagename"}, null, null, null, null, null);
		while (cursor.moveToNext()) {
			String packagename = cursor.getString(0);
			allApps.add(packagename);
		}
		db.close();
		cursor.close();
		return allApps;

	}
	
	public void addLockApp(String packagename) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("packagename", packagename);
		db.insert("lockapp", null, values);
		db.close();
		
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://comhpuvoice.phonesafe.db");
		resolver.notifyChange(uri , null);
	}

	public boolean isLock(String packagename) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("lockapp", null, "packagename=?",
				new String[] { packagename }, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			db.close();
			return true;
		} else {
			cursor.close();
			db.close();
			return false;
		}
	}

	public void deleteLockApp(String packagename) {
		SQLiteDatabase sql = helper.getWritableDatabase();
		sql.delete("lockapp", "packagename=?", new String[] { packagename });
		sql.close();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://comhpuvoice.phonesafe.db");
		resolver.notifyChange(uri , null);
	}

}
