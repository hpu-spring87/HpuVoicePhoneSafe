package com.hpuvoice.phonesafe.dao;

import java.util.ArrayList;
import java.util.List;

import com.hpuvoice.phonesafe.bean.BlackNumInfo;
import com.hpuvoice.phonesafe.db.BlackNumOpenhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackNumDao {

	public static final int DingCan = 1;
	public static final int GongGong = 2;
	public static final int YunYingShang = 3;
	public static final int KuaiDi = 4;
	public static final int JiPiao = 5;
	public static final int YinHang = 6;
	public static final int BaoXian = 7;
	public static final int PinPai = 8;
	@SuppressWarnings("unused")
	private Context context;
	private BlackNumOpenhelper helper;

	public BlackNumDao(Context context) {
		this.context = context;
		helper = new BlackNumOpenhelper(context);
	}

	public void addBlackNum(BlackNumInfo blackNumInfo) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("name", blackNumInfo.getName());
		values.put("number", blackNumInfo.getNumber());
		values.put("title_id", blackNumInfo.getTitle_id());
		db.insert("commonnum", null, values);
		db.close();
	}

	public void updateBlackNum(BlackNumInfo blackNumInfo) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("name", blackNumInfo.getName());
		values.put("number", blackNumInfo.getNumber());
		values.put("title_id", blackNumInfo.getTitle_id());
		db.update("commonnum", values, "name=?",
				new String[] { blackNumInfo.getName() });
		db.close();
	}

	public int queryBlackNumTitle(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int title_id = 2;
		Cursor cursor = db.query("commonnum", new String[] { "title_id" },
				"number=?", new String[] { number }, null, null, null);
		while (cursor.moveToNext()) {
			title_id = cursor.getInt(0);
		}
		db.close();
		cursor.close();
		return title_id;
	}
	
	public boolean isNull(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("commonnum", new String[]{"title_id"},
				"number=?", new String[] { number }, null, null, null);
		if (cursor.moveToNext()) {
			return false;
		}else{
			return true;
		}
	}

	public List<BlackNumInfo> queryAll() {
		List<BlackNumInfo> blackNumInfos = new ArrayList<BlackNumInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("commonnum", new String[] { "name", "number",
				"title_id" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			String name = cursor.getString(0);
			String number = cursor.getString(1);
			int title_id = cursor.getInt(2);
			BlackNumInfo bNumInfo = new BlackNumInfo(name, number, title_id);
			blackNumInfos.add(bNumInfo);
		}
		db.close();
		cursor.close();
		return blackNumInfos;

	}

	public List<BlackNumInfo> queryPart(int unit, int startindex) {
		List<BlackNumInfo> blackNumInfos = new ArrayList<BlackNumInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select name,number,title_id from  commonnum order by id desc limit ? offset ?;",
						new String[] { unit + "", startindex + "" });
		while (cursor.moveToNext()) {
			String name = cursor.getString(0);
			String number = cursor.getString(1);
			int title_id = cursor.getInt(2);
			BlackNumInfo bNumInfo = new BlackNumInfo(name, number, title_id);
			blackNumInfos.add(bNumInfo);
		}
		db.close();
		cursor.close();
		return blackNumInfos;

	}

	public void deleteBlaknum(String number) {
		SQLiteDatabase sql = helper.getWritableDatabase();
		sql.delete("commonnum", "number=?", new String[] { number });
		sql.close();
	}

}
