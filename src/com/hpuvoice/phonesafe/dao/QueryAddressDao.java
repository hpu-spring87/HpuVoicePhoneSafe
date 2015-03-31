package com.hpuvoice.phonesafe.dao;

import java.io.File;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QueryAddressDao {	private Context context;
public QueryAddressDao(Context context){
	this.context = context;
}

public String queryAddress(String phonenum){
	String Address = null;
	File file = new File(context.getFilesDir(),"address.db");
	SQLiteDatabase sd = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
	if(phonenum.matches("^1[34578]\\d{9}$")){
		//String sql = "select location from data2  where id=(select outkey from data1  where id=?);";
		//String[] selectionArgs = new String[]{phonenum.substring(0, 7)};
		//Cursor cursor = sd.rawQuery(sql, selectionArgs);
		Cursor cursor = sd.rawQuery("select location from data2  where id=(select outkey from data1  where id=?);",
				new String[] { phonenum.substring(0, 7) });
		if(cursor.moveToNext()){
			Address =  cursor.getString(0);
		}
		cursor.close();
	}else if(phonenum.startsWith("0")){
		if (phonenum.length() >= 10 && phonenum.startsWith("0")) {
			Cursor c = sd.rawQuery(
					"select location from data2 where area=?;",
					new String[] { phonenum.substring(1, 3) });   
			if (c.moveToNext()) {
				String string = c.getString(0);
				Address = string.substring(0, string.length() - 2);
			} else {
				c = sd.rawQuery(
						"select location from data2 where area=?;",
						new String[] { phonenum.substring(1, 4) }); //区号4位
				if (c.moveToNext()) {
					String string = c.getString(0);
					Address = string.substring(0, string.length() - 2);
				}
			}
			c.close();
		}
	}else{
		Address ="数居库没有收录";
	}
	sd.close();
	return Address;
}}
